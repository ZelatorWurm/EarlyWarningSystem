package Model;

import java.util.Observable;
import java.util.Observer;

import Model.PlayerNamesOnline.PlayerNamesOnlineModel;
import Model.PlayersOnline.PlayersOnlineModel;

public class ModelManager extends Observable implements Observer{
	private static RefreshThread refreshThread;
	private static ModelManager modelManager;
	private int progress;

	public static ModelManager getModelManager() {
		if (null == modelManager) {
			modelManager = new ModelManager();
		}

		return modelManager;
	}

	private ModelManager() {
		PlayersOnlineModel.getPlayersOnlineModel().addObserver(this);
		PlayerNamesOnlineModel.getPlayerNamesOnlineModel().addObserver(this);
		refreshThread = new RefreshThread();
	}
	
	public void setProgress(int progress){
		this.progress = progress;
		setChanged();
		notifyObservers("updateProgress");
	}
	
	public int getProgress(){
		return this.progress;
	}
	
	public void startRefreshingDataModels(){
		refreshThread.start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}
}
