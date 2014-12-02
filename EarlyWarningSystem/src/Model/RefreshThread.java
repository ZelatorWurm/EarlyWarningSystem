package Model;

import Model.PlayersOnline.PlayersOnlineModel;

public class RefreshThread extends Thread {
	private static final int SLEEP_TIMER = 5; //IN SECONDS
	private static boolean continueToRefesh = true;
	
	@Override
	public void run() {
		do {
			refreshAllDataModels();
			
			try {
				int progresscounter = 0;
				System.out.print("progress till refreshing: ");
				do{
					System.out.print("..." + 100/SLEEP_TIMER*progresscounter + "%");
					sleep(1000);
					progresscounter ++;
				}while(progresscounter <= SLEEP_TIMER);
				System.out.println();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (continueToRefesh);
	}

	private void refreshAllDataModels() {
		PlayersOnlineModel.getPlayersOnlineModel().loadPlayersOnline();
	}
	
	public void stopRefreshing(){
		continueToRefesh = false;
	}
}
