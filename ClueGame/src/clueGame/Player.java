package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Player {

	private String playerName;
	private ArrayList<Card> playerHand = new ArrayList<Card>();
	private ArrayList<String> roomOptions = new ArrayList<String>();
	private ArrayList<String> weaponOptions = new ArrayList<String>();
	private ArrayList<String> playerOptions = new ArrayList<String>();


	private int row;
	private int col;
	private Color color;
	private boolean isComp;
	private char lastVisitedRoom;
	private char roomInitial;
	boolean readyToAccuse = false;
	private Solution accusation = new Solution("","","");
	
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
	
	public void draw(Graphics g){
		g.setColor(color);
		g.fillOval(col*BoardCell.CELL_SIZE, row*BoardCell.CELL_SIZE, BoardCell.CELL_SIZE - 1, BoardCell.CELL_SIZE - 1);
	}
	
	public void addToHand(Card c){
		playerHand.add(c);
		if(c.getCardType().equals(CardType.PERSON)){
			playerOptions.remove(c.getCardName());
		}
		else if(c.getCardType().equals(CardType.WEAPON)){
			weaponOptions.remove(c.getCardName());
		}
		else{
			roomOptions.remove(c.getCardName());
		}
	}
	
	public void addToRoomsSeen(Card c){
		roomOptions.remove(c.getCardName());
	}
	
	public void addToRoomsSeen(String r){
		roomOptions.remove(r);
	}
	
	public void addToPlayersSeen(Card c){
		playerOptions.remove(c.getCardName());
		
	}
	
	public void addToPlayersSeen(Player p){
		playerOptions.remove(p.getPlayerName());
	}
	
	public void addToWeaponsSeen(Card c){
		weaponOptions.remove(c.getCardName());

	}
	
	public void addToWeaponsSeen(String w){
		weaponOptions.remove(w);
	}
	
	public Solution createSuggestion(String currentRoom) {
		if(playerOptions.contains(this)){
			playerOptions.remove(this);
		}


		//randomly choose a card from weaponOptions and playerOptions to be part of your suggestion
		Random rand = new Random();
		int pRand = rand.nextInt(playerOptions.size());
		int wRand = rand.nextInt(weaponOptions.size());

		String player = playerOptions.get(pRand);
		String weapon = weaponOptions.get(wRand);

		return new Solution(player, currentRoom, weapon);
	}
	
	public Solution createAccusation() {
		if(playerOptions.contains(this)){
			playerOptions.remove(this);
		}
		//randomly choose a card from unseen cards to be part of your accusation
		Random rand = new Random();
		int wRand = rand.nextInt(weaponOptions.size());
		int pRand = rand.nextInt(playerOptions.size());
		int rRand = rand.nextInt(roomOptions.size());

		String player = playerOptions.get(pRand);
		String weapon = weaponOptions.get(wRand);
		String room = roomOptions.get(rRand);

		return new Solution(player, room, weapon);
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
	
	public void setWeaponOptions(ArrayList<String> w){
		weaponOptions = w;
	}
	
	public void setPlayerOptions(ArrayList<Player> p){
		for(Player player: p){
			playerOptions.add(player.getPlayerName());
		}
	}
	
	public void setRoomOptions(ArrayList<String> r){
		roomOptions = r;
	}
	
	public ArrayList<String> getWeaponOptions() {
		return weaponOptions;
	}

	public ArrayList<String> getPlayerOptions() {
		return playerOptions;
	}

	public ArrayList<String> getRoomOptions() {
		return roomOptions;
	}
	
	
	public void setReadyToAccuse(boolean b){
		readyToAccuse = b;
	}
	
	public boolean getReadyToAccuse(){
		return readyToAccuse;
	}
	
	public void setAccusation(Solution s){
		accusation = s;
	}
	
	public Solution getAccusation(){
		return accusation;
	}
	
}
