package Model;

import java.util.Observable;
import java.util.Observer;

import Model.PlayersOnline.PlayersOnlineModel;

public class ModelManager extends Observable implements Observer{
	private static RefreshThread refreshThread;
	private static ModelManager modelManager;

	public static ModelManager getModelManager() {
		if (null == modelManager) {
			modelManager = new ModelManager();
		}

		return modelManager;
	}

	private ModelManager() {
		PlayersOnlineModel.getPlayersOnlineModel().addObserver(this);
		refreshThread = new RefreshThread();
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
