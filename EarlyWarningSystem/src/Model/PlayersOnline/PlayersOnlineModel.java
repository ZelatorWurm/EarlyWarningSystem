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
		for (Server server : Server.values()) {
			GetPlayersOnlineThread gpo = new GetPlayersOnlineThread(server);
			gpo.start();

			try {
				gpo.join();
				
				switch (server) {
					case Elevation: playersOnlineElevation.add(gpo.getTimeStampPlayersOnline()); break;
					case Desertion: playersOnlineDesertion.add(gpo.getTimeStampPlayersOnline()); break;
					case Affliction: playersOnlineAffliction.add(gpo.getTimeStampPlayersOnline()); break;
					case Serenity: playersOnlineSerenity.add(gpo.getTimeStampPlayersOnline()); break;
				}
			} catch (Exception e) {
			}
		}
		
		setChanged();
		notifyObservers();
//		System.out.println("elevation: " + playersOnlineElevation.get(0).getPlayersOnline() + " at: " + playersOnlineElevation.get(0).getEpoch());
//		System.out.println("affliction: " + playersOnlineAffliction.get(0).getPlayersOnline() + " at: " + playersOnlineAffliction.get(0).getEpoch());
//		System.out.println("desertion: " + playersOnlineDesertion.get(0).getPlayersOnline() + " at: " + playersOnlineDesertion.get(0).getEpoch());
//		System.out.println("serenity: " + playersOnlineSerenity.get(0).getPlayersOnline() + " at: " + playersOnlineSerenity.get(0).getEpoch());
	}
}
