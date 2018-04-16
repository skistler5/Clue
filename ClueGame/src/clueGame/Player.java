package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Player {

	private String playerName;
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	private ArrayList<String> roomsSeen = new ArrayList<String>();
	private ArrayList<String> weaponsSeen = new ArrayList<String>();
	private ArrayList<String> playersSeen = new ArrayList<String>();


	private int row;
	private int col;
	private Color color;
	private boolean isComp;
	private char lastVisitedRoom;
	private char roomInitial;
	
	public boolean equals(Player p){
		if(p.getPlayerName().equals(playerName)){
			return true;
		}
		return false;
	}
	
	public Player(String name, int row, int col, int r, int g, int b, String comp){
		playerName = name;
		this.row = row;
		this.col = col;
		this.color = new Color(r,g,b);
		
		if(comp.equals("c")){
			isComp = true;
		}
		else{
			isComp = false;
		}
	}
	
	public void addToHand(Card c){
		playerHand.add(c);
	}
	
	public void addToPlayersSeen(String s){
		playersSeen.add(s);
	}
	
	public void addToWeaponsSeen(String s){
		weaponsSeen.add(s);
	}
	
	public Card disproveSuggestion(Solution suggestion){
		int numMatches = 0;
		Card shown = null;
		ArrayList<Card> correct = new ArrayList<Card>();
		for(Card c: playerHand){
			if(c.getCardName().equals(suggestion.person)){
				numMatches++;
				shown = c;
				correct.add(c);
				
			}
			if(c.getCardName().equals(suggestion.weapon)){
				numMatches++;
				shown = c;
				correct.add(c);
			}
			if(c.getCardName().equals(suggestion.room)){
				numMatches++;
				shown = c;
				correct.add(c);
			}
		}
		if(numMatches == 0){
			return null;
		}
		else if(numMatches == 1){
			return shown;
		}
		else{
			Random rand = new Random();
			int n = rand.nextInt(correct.size());
			return correct.get(n);
		}
	}
	
		
	//Getters and Setters
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isComputer() {
		return isComp;
	}

	public char getLastVisitedRoom() {
		return lastVisitedRoom;
	}
	
	public void setLastVisitedRoom(BoardCell cell){
		lastVisitedRoom = cell.getInitial();
	}
	public ArrayList<Card> getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(ArrayList<Card> playerHand) {
		this.playerHand = playerHand;
	}
	
	public void clearHand(){
		playerHand.clear();
	}
	
	public Character getRoom(){
		return roomInitial;
	}
	
	public void setRoom(Character c){
		roomInitial = c;
	}
	
	public ArrayList<String> getWeaponsSeen() {
		return weaponsSeen;
	}

	public ArrayList<String> getPlayersSeen() {
		return playersSeen;
	}

	
}
