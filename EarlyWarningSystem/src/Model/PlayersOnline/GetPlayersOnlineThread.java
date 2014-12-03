package Model.PlayersOnline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Server;

public class GetPlayersOnlineThread extends Thread {
	private Server serverFrom;
	private List<TimeStampPlayersOnline> listTpo = new ArrayList<TimeStampPlayersOnline>();

	public GetPlayersOnlineThread(Server server) {
		this.serverFrom = server;
	}

	@Override
	public void run() {
			URL url;
			BufferedReader br;
			
			
			try {
				url = new URL("http://" + Server.getName(serverFrom)+ ".wurmonline.com/mrtg/wurm.log");
				br = new BufferedReader(new InputStreamReader(url.openStream()));
				String temp = "";
				int counter = 0;
				while(null != (temp = br.readLine())){
					if(counter == 0){
						//skip the first one
						counter++;
					}else if(counter < 8){
						String[] response = temp.split(" ");
						listTpo.add(new TimeStampPlayersOnline(Integer.parseInt(response[2]), Long.parseLong(response[0])));
						counter++;
					}else{
						break;
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public List<TimeStampPlayersOnline> getListTimeStampPlayersOnline(){
		return this.listTpo;
	}
}
