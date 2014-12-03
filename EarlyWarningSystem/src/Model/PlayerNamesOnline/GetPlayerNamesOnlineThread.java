package Model.PlayerNamesOnline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.ws.Response;

import Model.Server;

public class GetPlayerNamesOnlineThread extends Thread {
	private String kingdom;
	private List<PlayerName> listPlayernames = new ArrayList<PlayerName>();
	
	private final String jk = "http://wurmfeed.rotabland.eu/log";
	private final String jkGL = "http://wurmfeed.rotabland.eu/loggl";
	private final String hots = "http://wurmfeed.rotabland.eu/bllog";
	private final String hotsGL = "http://wurmfeed.rotabland.eu/blloggl";
	private final String mr = "http://wurmfeed.rotabland.eu/mrlog";
	private final String mrGL = "http://wurmfeed.rotabland.eu/mrloggl";

	public GetPlayerNamesOnlineThread(String kingdom) {
		this.kingdom = kingdom;
	}

	@Override
	public void run() {
		if(this.kingdom.equals("jk")){
			getData(jk, false);
			getData(jkGL, true);
		}else if(this.kingdom.equals("hots")){
			getData(hots, false);
			getData(hotsGL, true);
		}else if(this.kingdom.equals("mr")){
			getData(mr, false);
			getData(mrGL, true);
		}		
	}
	
	public void getData(String urlString, boolean isGL){
		URL url;
		BufferedReader br;
		try {
			url = new URL(urlString);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			String temp = "";
			int counter = 0;
			
			Calendar calendar = Calendar.getInstance();
			int currentDay = calendar.get(Calendar.DAY_OF_MONTH); 
			int currentMonth = calendar.get(Calendar.MONTH) + 1; //wtf, why does it start on zero
			int currentYear = calendar.get(Calendar.YEAR);
			
			int notedYear = 0;
			int notedMonth = 0;
			int notedDay = 0;
			
			while(null != (temp = br.readLine())){
				if(temp.substring(0, 1).equals("L")){ //Logging started, parse date time
					notedYear = Integer.parseInt(temp.substring(16, 20));
					notedMonth = Integer.parseInt(temp.substring(21, 23));
					notedDay = Integer.parseInt(temp.substring(24, 26));
				}else{
					if(notedYear == currentYear && notedMonth == currentMonth && notedDay == currentDay){
						Pattern patternName = Pattern.compile("<(.+?)>");
						Matcher matcherName = patternName.matcher(temp);
						
						if(matcherName.find()){
							String name = matcherName.group(1);
							
							Pattern patternTime = Pattern.compile("\\[([^\\]]+)]");
							Matcher matcherTime = patternTime.matcher(temp);
							
							if(matcherTime.find()){
								String time = matcherTime.group(1);
								
								int notedHour = Integer.parseInt(time.substring(0, 2)) + 2;//fix timezone?
								int notedMinute = Integer.parseInt(time.substring(3, 5));
								int notedSecond = Integer.parseInt(time.substring(6, 8));
								
								Calendar c = new GregorianCalendar(TimeZone.getTimeZone("Europe/Copenhagen"));//location of rotabland
								c.set(notedYear, notedMonth, notedDay, notedHour, notedMinute, notedSecond);
								
								//convert to local time
								Calendar localtime = new GregorianCalendar(TimeZone.getDefault());
								localtime.setTimeInMillis(c.getTimeInMillis());
								
								PlayerName pn = new PlayerName(name, isGL, localtime.getTimeInMillis());
								
								listPlayernames.add(pn);
							}
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collections.sort(listPlayernames, new Comparator<PlayerName>() {
			@Override
			public int compare(PlayerName one, PlayerName two) {
				if(one.getEpoch() < two.getEpoch()){
					return 1;
				}else if(one.getEpoch() > two.getEpoch()){
					return -1;
				}
				return 0;
			}
		});
		
		//obtain not uniques
		ArrayList<PlayerName> removeables = new ArrayList<PlayerName>();
		
		for (PlayerName playerNameone : listPlayernames) {
			boolean ignoreFirstUnique = true;
			for (PlayerName playerNametwo : listPlayernames) {
				if(playerNameone.getName().equals(playerNametwo.getName())){
					if(ignoreFirstUnique == false){
						removeables.add(playerNametwo);
					}else{
						ignoreFirstUnique = false;
					}
				}
			}
		}
		
		//remove not uniques
		for (PlayerName playerName : removeables) {
			for (int i = 0; i < listPlayernames.size(); i++) {
				if(listPlayernames.get(i).getName().equals(playerName.getName()) 
						&& listPlayernames.get(i).getEpoch() == playerName.getEpoch()
						&& listPlayernames.get(i).getIsGL() == playerName.getIsGL()){
					listPlayernames.remove(i);
				}
			}
		}
	}
	
	public List<PlayerName> getListPlayerNames(){
		return this.listPlayernames;
	}
}
