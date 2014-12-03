package Model;

import Model.PlayerNamesOnline.PlayerNamesOnlineModel;
import Model.PlayersOnline.PlayersOnlineModel;

public class RefreshThread extends Thread {
	public static final int SLEEP_TIMER = 30; //IN SECONDS
	private static boolean continueToRefesh = true;
	
	@Override
	public void run() {
		do {
			refreshAllDataModels();
			
			try {
				ModelManager.getModelManager().setProgress(0);
				do{
					sleep(1000);
					ModelManager.getModelManager().setProgress(ModelManager.getModelManager().getProgress() + 1);
				}while(ModelManager.getModelManager().getProgress() <= SLEEP_TIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (continueToRefesh);
	}

	private void refreshAllDataModels() {
		PlayersOnlineModel.getPlayersOnlineModel().loadPlayersOnline();
		PlayerNamesOnlineModel.getPlayerNamesOnlineModel().loadPlayerNamesOnline();
	}
	
	public void stopRefreshing(){
		continueToRefesh = false;
	}
}
