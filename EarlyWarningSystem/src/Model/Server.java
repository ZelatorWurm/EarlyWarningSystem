package Model;

public enum Server {
	Elevation, Serenity, Affliction, Desertion;
	
	public static String getName(Server server){
		switch (server) {
		case Elevation:
			return "elevation";
		case Desertion:
			return "desertion";
		case Affliction:
			return "affliction";
		case Serenity:
			return "serenity";
		}
		return null;
	}
}
