package Model.PlayerNamesOnline;

public class PlayerName {
	private String name;
	private boolean isGL;
	private long epoch;

	public PlayerName(String name, boolean isGL,  long epoch) {
		super();
		this.name = name;
		this.isGL = isGL;
		this.epoch = epoch;
	}

	public String getName() {
		return name;
	}
	
	public boolean getIsGL(){
		return isGL;
	}

	public long getEpoch() {
		return epoch;
	}
}
