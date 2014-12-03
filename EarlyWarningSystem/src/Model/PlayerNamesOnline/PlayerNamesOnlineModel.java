package Model.PlayerNamesOnline;

import java.util.ArrayList;
import java.util.Observable;

import Model.Kingdom;

public class PlayerNamesOnlineModel extends Observable{
	private static PlayerNamesOnlineModel model;
	
	private PlayerNamesOnlineModel(){
		playerNamesOnlineHots = new ArrayList<PlayerName>();
		playerNamesOnlineMr = new ArrayList<PlayerName>();
		playerNamesOnlineJk = new ArrayList<PlayerName>();
	}

	public static PlayerNamesOnlineModel getPlayerNamesOnlineModel() {
		if (model == null) {
			model = new PlayerNamesOnlineModel();
		}
		return model;
	}	
	
	private ArrayList<PlayerName> playerNamesOnlineHots;
	private ArrayList<PlayerName> playerNamesOnlineMr;
	private ArrayList<PlayerName> playerNamesOnlineJk;
	
	public ArrayList<PlayerName> getPlayersOnlineHots() {
		return playerNamesOnlineHots;
	}

	public ArrayList<PlayerName> getPlayersOnlineMr() {
		return playerNamesOnlineMr;
	}

	public ArrayList<PlayerName> getPlayersOnlineJk() {
		return playerNamesOnlineJk;
	}
	
	public void loadPlayerNamesOnline(){
		playerNamesOnlineHots.clear();
		playerNamesOnlineMr.clear();
		playerNamesOnlineJk.clear();
		
		for (Kingdom kingdom : Kingdom.values()) {
			GetPlayerNamesOnlineThread gpo = new GetPlayerNamesOnlineThread(Kingdom.getName(kingdom));
			gpo.start();

			try {
				gpo.join();
				for (PlayerName playername : gpo.getListPlayerNames()) {

					switch (kingdom) {
					case Jk:
						playerNamesOnlineJk.add(playername);
						break;
					case Hots:
						playerNamesOnlineHots.add(playername);
						break;
					case Mr:
						playerNamesOnlineMr.add(playername);
						break;
					}
				}
			} catch (Exception e) {
			}
		}
		
		setChanged();
		notifyObservers();
	}
}
