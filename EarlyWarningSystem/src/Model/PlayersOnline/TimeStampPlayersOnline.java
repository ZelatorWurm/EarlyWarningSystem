package Model.PlayersOnline;

public class TimeStampPlayersOnline {
	private int playersOnline;
	private long epoch;
	
	public TimeStampPlayersOnline(int playersOnline, long epoch){
		this.playersOnline = playersOnline;
		this.epoch = epoch ;
	}
	
	public int getPlayersOnline() {
		return playersOnline;
	}
	
	public long getEpoch() {
		return epoch;
	}
}
