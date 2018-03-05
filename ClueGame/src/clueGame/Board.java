package clueGame;

public class Board {
	private static final int MAX_BOARD_SIZE = 50;
	
	//variable used for singleton pattern
	private static Board theInstance = new Board();
	
	//constructor is private to ensure only one is created
	private Board(){
	}
	
	//returns the only board	
	public static Board getInstance(){
		return theInstance;
	}
}
