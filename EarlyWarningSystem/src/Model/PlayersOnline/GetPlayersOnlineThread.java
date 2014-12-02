package Model.PlayersOnline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import Model.Server;

public class GetPlayersOnlineThread extends Thread {
	private Server serverFrom;
	private TimeStampPlayersOnline tpo;

	public GetPlayersOnlineThread(Server server) {
		this.serverFrom = server;
	}

	@Override
	public void run() {
			URL url;
			BufferedReader br;
			String[] response = null;
			
			try {
				url = new URL("http://" + Server.getName(serverFrom)+ ".wurmonline.com/mrtg/wurm.log");
				br = new BufferedReader(new InputStreamReader(url.openStream()));
				String temp = "";
				
				while(null != (temp = br.readLine())){
					break;
				}
				
				response = temp.split(" ");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.tpo = new TimeStampPlayersOnline(Integer.parseInt(response[response.length-1]), Long.parseLong(response[0]));
	}
	
	public TimeStampPlayersOnline getTimeStampPlayersOnline(){
		return this.tpo;
	}
}
