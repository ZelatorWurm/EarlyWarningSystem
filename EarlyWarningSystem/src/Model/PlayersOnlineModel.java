package Model;

import java.util.ArrayList;

public class PlayersOnlineModel {
	private ArrayList<TimeStampPlayersOnline> playersOnlineElevation;
	private ArrayList<TimeStampPlayersOnline> playersOnlineAffliction;
	private ArrayList<TimeStampPlayersOnline> playersOnlineDesertion;
	private ArrayList<TimeStampPlayersOnline> playersOnlineSerenity;

	private static PlayersOnlineModel model;
	
	private PlayersOnlineModel(){
		playersOnlineElevation = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineAffliction = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineDesertion = new ArrayList<TimeStampPlayersOnline>();
		playersOnlineSerenity = new ArrayList<TimeStampPlayersOnline>();
		
		init();
	}

	public static PlayersOnlineModel getPlayersOnlineModel() {
		if (model == null) {
			model = new PlayersOnlineModel();
		}
		return model;
	}
	
	private void init(){
		loadPlayersOnline();
	}
	
	public void loadPlayersOnline(){
		for (Server server : Server.values()) {
			GetPlayerOnline gpo = new GetPlayerOnline(server);
			gpo.start();

			try {
				gpo.join();
				
				switch (server) {
				case Elevation: playersOnlineElevation.add(gpo.getTimeStampPlayersOnline()); break;
				case Desertion: playersOnlineAffliction.add(gpo.getTimeStampPlayersOnline()); break;
				case Affliction: playersOnlineDesertion.add(gpo.getTimeStampPlayersOnline()); break;
				case Serenity: playersOnlineSerenity.add(gpo.getTimeStampPlayersOnline()); break;
				}
			} catch (Exception e) {
			}
		}
		
		System.out.println("elevation " + playersOnlineElevation.get(0).getPlayersOnline());
		System.out.println("affliction " + playersOnlineAffliction.get(0).getPlayersOnline());
		System.out.println("desertion " + playersOnlineDesertion.get(0).getPlayersOnline());
		System.out.println("serenity " + playersOnlineSerenity.get(0).getPlayersOnline());
	}
}
