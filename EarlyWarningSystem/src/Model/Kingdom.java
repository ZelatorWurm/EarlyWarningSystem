package Model;

public enum Kingdom {
	Hots, Jk, Mr;
	
	public static String getName(Kingdom kingdom){
		switch (kingdom) {
		case Hots:
			return "hots";
		case Jk:
			return "jk";
		case Mr:
			return "mr";
		}
		
		return null;
	}
}
