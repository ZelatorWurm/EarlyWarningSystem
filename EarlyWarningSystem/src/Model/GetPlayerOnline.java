package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPlayerOnline extends Thread {
	private Server serverFrom;
	private TimeStampPlayersOnline tpo;

	public GetPlayerOnline(Server server) {
		this.serverFrom = server;
	}

	@Override
	public void run() {
			URL url;
			BufferedReader br;
			String response = "-1";
			
			try {
				url = new URL("http://" + Server.getName(serverFrom)+ ".wurmonline.com/mrtg/wurm.log");
				br = new BufferedReader(new InputStreamReader(url.openStream()));
				
				response = br.readLine();
				String[] splitted = response.split(" ");
				response = splitted[splitted.length-1];
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.tpo = new TimeStampPlayersOnline(Integer.parseInt(response), System.currentTimeMillis());
	}
	
	public TimeStampPlayersOnline getTimeStampPlayersOnline(){
		return this.tpo;
	}
}
