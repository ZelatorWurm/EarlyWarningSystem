package Model.PlayersOnline;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import Model.Server;

public class PlayersOnlineModel extends Observable{
	private static PlayersOnlineModel model;
	
	private PlayersOnlineModel(){
		playersOnlineElevation = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineAffliction = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineDesertion = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineSerenity = new ArrayList<TimeStampPlayersOnline>();
	}

	public static PlayersOnlineModel getPlayersOnlineModel() {
		if (model == null) {
			model = new PlayersOnlineModel();
		}
		return model;
	}	
	
	private ArrayList<TimeStampPlayersOnline> playersOnlineElevation;
	private ArrayList<TimeStampPlayersOnline> playersOnlineAffliction;
	private ArrayList<TimeStampPlayersOnline> playersOnlineDesertion;
	private ArrayList<TimeStampPlayersOnline> playersOnlineSerenity;
	
	public ArrayList<TimeStampPlayersOnline> getPlayersOnlineElevation() {
		return playersOnlineElevation;
	}

	public ArrayList<TimeStampPlayersOnline> getPlayersOnlineAffliction() {
		return playersOnlineAffliction;
	}

	public ArrayList<TimeStampPlayersOnline> getPlayersOnlineDesertion() {
		return playersOnlineDesertion;
	}

	public ArrayList<TimeStampPlayersOnline> getPlayersOnlineSerenity() {
		return playersOnlineSerenity;
	}
	
	public TimeStampPlayersOnline getCurrentPlayersOnlineFromServer(Server server){
		switch (server) {
		case Elevation:
			return playersOnlineElevation.get(0);
		case Desertion:
			return playersOnlineDesertion.get(0);
		case Affliction:
			return playersOnlineAffliction.get(0);
		case Serenity:
			return playersOnlineSerenity.get(0);
		}
		
		return null;
	}
	
	public void loadPlayersOnline(){
		playersOnlineElevation.clear();
		playersOnlineAffliction.clear();
		playersOnlineDesertion.clear();
		playersOnlineSerenity.clear();
		
		for (Server server : Server.values()) {
			GetPlayersOnlineThread gpo = new GetPlayersOnlineThread(server);
			gpo.start();

			try {
				gpo.join();
				for (TimeStampPlayersOnline timeStampPlayersOnline : gpo.getListTimeStampPlayersOnline()) {

					switch (server) {
					case Elevation:
							playersOnlineElevation.add(timeStampPlayersOnline);
						break;
					case Desertion:
							playersOnlineDesertion.add(timeStampPlayersOnline);
						break;
					case Affliction:
							playersOnlineAffliction.add(timeStampPlayersOnline);
						break;
					case Serenity:
							playersOnlineSerenity.add(timeStampPlayersOnline);
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
