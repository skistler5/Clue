/*
 * Authors: Stephen Kistler and Shannon Bride
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGame.BoardCell;

/**
 * 
 * @author Shannon Bride
 * @author Stephen Kistler
 * 
 * 
 */

public class Board extends JPanel{
	public static final int MAX_BOARD_SIZE = 50;
	private int row; 
	private int col;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Player currentPlayer = null;
	private ArrayList<String> weapons = new ArrayList<String>();
	private ArrayList<String> rooms = new ArrayList<String>();
	private Map<BoardCell, HashSet<BoardCell>> adjMap = new HashMap<BoardCell, HashSet<BoardCell>>(); //contains sets of adjacencies for each cell
	private Map<Player, ArrayList<Card>> playerCards = new HashMap<Player, ArrayList<Card>>();
	private BoardCell[][] board;
	public Map<Character, String> legend = new HashMap<Character, String>(); //stores room names and short hand for them
	private Set<BoardCell> targets = new HashSet<BoardCell>(); //places player can move to with given steps
	private Set<BoardCell> visited = new HashSet<BoardCell>(); //already visited cells 
	private String boardConfigFile = new String();
	private String roomConfigFile = new String();
	private String peopleFile = new String();
	private String weaponFile = new String();
	private ArrayList<Card> deck = new ArrayList<Card>();
	public ArrayList<BoardCell> centers = new ArrayList<BoardCell>();
	private int dieRoll = 0;
	private boolean targetSelected = true;
	private Solution currentAccusation = new Solution(" ", " ", " ");
	private Card cardShown = new Card(" ", CardType.PERSON);
	private String room = " ";
	private ClickListener cl;


	//variable used for singleton pattern
	private static Board theInstance = new Board();

	//constructor is private to ensure only one is created
	private Board(){
	}

	//returns the only board	
	public static Board getInstance(){
		return theInstance;
	}


	/**
	 * calls room and board config functions
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public void initialize() {
		clearPlayers();
		
		try{
			loadRoomConfig();
			loadBoardConfig();
			loadPeople();
			loadWeapons();

		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(BadConfigFormatException e){
			System.out.println(e.getMessage());
		}
		for(Player p : players){
			if(!p.isComputer()){
				currentPlayer = p;
			}
		}
		calcAdjacencies();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, BoardCell.CELL_SIZE*col, BoardCell.CELL_SIZE*row);
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				(board[i][j]).draw(g);
			}	
		}
		for(Player p: players){
			p.draw(g);
			if(!p.isComputer() && !currentPlayer.isComputer()){
				calcTargets(p.getRow(), p.getCol(), dieRoll);
				for(BoardCell b: targets){
					b.drawTargets(g);
				}
			}
		}
		for(BoardCell b: centers){
			b.drawName(g, this);
		}
	}
	
	public void playerTurn(Player p){
		targetSelected = false;
		int r = p.getRow();
		int c = p.getCol();
		Random rand = new Random();
		dieRoll = rand.nextInt(6) + 1;
		calcTargets(r,c,dieRoll);
		repaint();
		if(p.isComputer()){
			chooseTarget(p);
			Solution guess = createAccusation(p);
			cardShown = handleSuggestion(p, guess);
			if(cardShown.getCardType().equals(CardType.PERSON)){
				p.addToPlayersSeen(cardShown.getCardName());
			}
			else if(cardShown.getCardType().equals(CardType.WEAPON)){
				p.addToWeaponsSeen(cardShown.getCardName());
			}
			else{
				p.addToRoomsSeen(cardShown.getCardName());
			}
		}
		else{
			targetSelected = true;
			Solution guess = createAccusation(p);
			cardShown = handleSuggestion(p, guess);
		}
		repaint();
		if(getNextPlayer().isComputer()){
			playerTurn(currentPlayer);
		}

	}
	
	private class ClickListener implements MouseListener{
		public void mouseReleased(MouseEvent event){}
		public void mouseEntered(MouseEvent event){}
		public void mouseExited(MouseEvent event){}
		public void mousePressed(MouseEvent event) {}
		public void mouseClicked(MouseEvent event){
			BoardCell clicked = null;
			boolean isValid = false;
			int x = event.getX();
			int y = event.getY();
			clicked = board[y][x];
			for(BoardCell c: targets){
				if(clicked.equals(c)){
					isValid = true;
				}
			}
			if(isValid){
				players.get(2).setRow(y);
				players.get(2).setCol(x);
			}
			else{
				ControlGame.errorMessage();
			}
			
		}
	}
	public class mouses implements MouseListener{
		mouses(){
			
		}
		BoardCell clicked = null;
		public void mousePressed(MouseEvent event){

		}
		public void mouseReleased(MouseEvent event){}
		public void mouseEntered(MouseEvent event){}
		public void mouseExited(MouseEvent event){}
		public void mouseClicked(MouseEvent event){

		}
	}
	
	private void addMouseListener(MouseAdapter mouseAdapter) {
		// TODO Auto-generated method stub
		
	}

	public boolean containsClick(int mouseX, int mouseY, BoardCell cell){
		Rectangle rect = new Rectangle(cell.getDrawRow(), cell.getDrawCol(), BoardCell.CELL_SIZE, BoardCell.CELL_SIZE);
		if(rect.contains(new Point(mouseX, mouseY))){
			return true;
		}
		else{
			return false;
		}
	}
	public Player getNextPlayer(){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).equals(currentPlayer)){
				if(i == players.size() - 1){
					currentPlayer = players.get(1);
					return currentPlayer;
				}
				else{
					currentPlayer = players.get((i+1));
					return currentPlayer;
				}
			}
		}
		return currentPlayer;
	}

	
	public Solution createAccusation(Player p){
		Random rand = new Random();
		ArrayList<String> possibleWeapons = new ArrayList<String>();
		ArrayList<String> possiblePlayers = new ArrayList<String>();
		room = legend.get(p.getRoom());
		possibleWeapons = weapons;
		for(int i = 0; i < players.size(); i++){
			possiblePlayers.add(players.get(i).getPlayerName());
		}
		
		for(int i = 0; i < possibleWeapons.size(); i++){
			if(p.getWeaponsSeen().contains(possibleWeapons.get(i))){
				possibleWeapons.remove(i);
				i--;
			}
		}
		for(int i = 0; i < possiblePlayers.size(); i++){
			if(p.getPlayersSeen().contains(possiblePlayers.get(i))){
				possiblePlayers.remove(i);
				i--;
			}
		}
		
		int weaponNum = possibleWeapons.size();
		int playerNum = possiblePlayers.size();
		int wit = rand.nextInt(weaponNum);
		int pit = rand.nextInt(playerNum);
		
		currentAccusation = new Solution(possiblePlayers.get(pit), room, possibleWeapons.get(wit));
		return currentAccusation;

	}

	public Card handleSuggestion(Player p, Solution suggestion){
		Card shown = null;
		int iter = 0;
		for(int i = 0; i < players.size(); i++){
			if(p.equals(players.get(i))){
				iter = i;
			}
		}
	
		for(int i = iter; i < (players.size() + iter); i++){
			Card temp = players.get(i%players.size()).disproveSuggestion(suggestion);
			if(temp != null){
				shown = temp;
				return shown;
			}
		}
		return shown;

	}


	/**
	 * Deals the cards to the players and clears the deck so that it is empty
	 */
	public void dealCards(){
		Collections.shuffle(deck);
		
		int numPlayers = players.size();
		int count = 0;
		int i = 0;
		
		for(Card c: deck){
			players.get(i).addToHand(c);
			if(c.getCardType().equals(CardType.PERSON)){
				players.get(i).addToPlayersSeen(c.getCardName());
			}
			else if(c.getCardType().equals(CardType.ROOM)){
				players.get(i).addToRoomsSeen(c.getCardName());
			}
			else{
				players.get(i).addToWeaponsSeen(c.getCardName());
			}
			count++;
			i = count % numPlayers;
		}
		deck.clear();
	}

	/**
	 * loads from the weapons file into the deck
	 * @throws FileNotFoundException
	 */
	public void loadWeapons() throws FileNotFoundException{
		FileReader reader = new FileReader(weaponFile);
		Scanner input = new Scanner(reader);

		while(input.hasNextLine()){
			String line = input.nextLine();
			Card temp = new Card(line, CardType.WEAPON);
			deck.add(temp);
			weapons.add(temp.getCardName());
		}
		input.close();
	}

	/**
	 * loads from the people file into the deck
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 */
	public void loadPeople() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(peopleFile); 
		Scanner input = new Scanner(reader);

		//reads in room names, symbols, and type
		while(input.hasNextLine()){
			String line = input.nextLine();
			String[] words = line.split(",");
			String[] loc = words[1].split("-");
			int row = Integer.parseInt(loc[0]);
			int col = Integer.parseInt(loc[1]);
			int r = Integer.parseInt(words[2]);
			int g = Integer.parseInt(words[3]);
			int b = Integer.parseInt(words[4]);
			Player temp = new Player(words[0], row, col, r, g, b, words[5]);
			players.add(temp);
			if(!temp.isComputer()){
				currentPlayer = temp;
			}
			Card temp1 = new Card(words[0], CardType.PERSON);
			deck.add(temp1);
		}
		input.close();
	}

	/**
	 * 
	 * loads room info from document
	 * 
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 */
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(roomConfigFile); 
		Scanner input1 = new Scanner(reader);

		//reads in room names, symbols, and type
		while(input1.hasNextLine()){
			String line = input1.nextLine();
			String[] words = line.split(", ");
			if(words[0].length() > 1){throw new BadConfigFormatException("Room abbreviation is in the wrong format");}
			if(words.length < 3){throw new BadConfigFormatException("NO ROOM TYPE");}
			if(!words[2].equals("Card") && !words[2].equals("Other")){throw new BadConfigFormatException("This is not a valid option");}
			legend.put(words[0].charAt(0), words[1]);
			if(words[2].equals("Card")){
				Card temp = new Card(words[1], CardType.ROOM);
				deck.add(temp);
				rooms.add(temp.getCardName());
			}
		}
		
		input1.close();
	}

	/**
	 * loads board info from csv
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 */
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(boardConfigFile);
		Scanner input2 = new Scanner(reader);
		int rowCount = 0;
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];

		//reads csv line by line and figures out if it's a doorway
		while(input2.hasNextLine()){
			String line = input2.nextLine();
			String[] words = line.split("\\s*,\\s*");

			for(int i = 0; i < words.length; i++){
				board[rowCount][i] = new BoardCell(rowCount,i);
				BoardCell currentCell = board[rowCount][i]; //holds board[rowCount][i] for less typing
				currentCell.setInitial(words[i].charAt(0));
				
				if(words[i].length() > 1){
					Character c = new Character(words[i].charAt(1)); //need this to compare to other constants
					if(c.equals('S')){
						currentCell.setDoorWay(false);
						currentCell.setDoorDirection('N');
						centers.add(currentCell);
					}
					else{
						currentCell.setDoorDirection(c);
						currentCell.setDoorWay(true);


						if(c.equals('N')){
							currentCell.setDoorWay(false);
							currentCell.setDoorDirection('N');
						}
						if(!c.equals('U') && !c.equals('D') && !c.equals('R') && !c.equals('L') && !c.equals('N') && !c.equals('S')){
							throw new BadConfigFormatException("Invalid Door Direction");
						}
					}
				}
				else{
					currentCell.setDoorDirection('N');
					currentCell.setDoorWay(false);
				}
			}

			if(rowCount == 0){
				col = words.length;
			}
			else if(words.length != col){
				throw new BadConfigFormatException("Number of columns is inconsistent");
			}
			rowCount++;
		}
		row = rowCount;
		input2.close();
	}


	/**
	 * finds valid adjacencies 
	 */
	public void calcAdjacencies(){
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				HashSet<BoardCell> adjs = new HashSet<BoardCell>(); //temp set to input into map
				//doorways only have one adjacency
				if(board[i][j].isDoorway()){
					if(board[i][j].getDoorDirection().equals(DoorDirection.UP)){
						adjs.add(board[i-1][j]);
					}
					else if(board[i][j].getDoorDirection().equals(DoorDirection.DOWN)){
						adjs.add(board[i+1][j]);
					}
					else if(board[i][j].getDoorDirection().equals(DoorDirection.RIGHT)){
						adjs.add(board[i][j+1]);
					}
					else{
						adjs.add(board[i][j-1]);
					}
				}
				//calculates walkway adjacencies
				else if(!board[i][j].isRoom()){
					if (i - 1 >= 0){
						Character c = new Character(board[i-1][j].getInitial());
						if(c.equals('W')){
							adjs.add(board[i-1][j]);
						}
						else if(board[i-1][j].isDoorway()){
							if(board[i-1][j].getDoorDirection().equals(DoorDirection.DOWN)){
								adjs.add(board[i-1][j]);
							}
						}

					}
					if(i + 1 < row){
						Character c = new Character(board[i+1][j].getInitial());
						if(c.equals('W')){
							adjs.add(board[i+1][j]);
						}
						else if(board[i+1][j].isDoorway()){
							if(board[i+1][j].getDoorDirection().equals(DoorDirection.UP)){
								adjs.add(board[i+1][j]);
							}
						}
					}
					if(j-1 >= 0){
						Character c = new Character(board[i][j-1].getInitial());
						if(c.equals('W')){
							adjs.add(board[i][j-1]);
						}
						else if(board[i][j-1].isDoorway()){
							if(board[i][j-1].getDoorDirection().equals(DoorDirection.RIGHT)){
								adjs.add(board[i][j-1]);
							}
						}
					}
					if(j+1 < col){
						Character c = new Character(board[i][j+1].getInitial());
						if(c.equals('W')){
							adjs.add(board[i][j+1]);
						}
						else if(board[i][j+1].isDoorway()){
							if(board[i][j+1].getDoorDirection().equals(DoorDirection.LEFT)){
								adjs.add(board[i][j+1]);
							}
						}
					}
				}
				adjMap.put(board[i][j], adjs);
			}
		}
		return;
	}


	/**
	 * calls findTargets
	 * @param row
	 * @param col
	 * @param pathLength
	 */
	public void calcTargets(int row, int col, int pathLength){
		visited.clear();
		targets.clear();

		findTargets(row, col, pathLength);
	}
	
	public void calcTargets(int row, int col, int pathLength, Player p){
		visited.clear();
		targets.clear();

		findTargets(row, col, pathLength);
		chooseTarget(p);
	}

	public void chooseTarget(Player player){
		if(player.isComputer()){
			for(BoardCell cell: targets){
				if(cell.isDoorway() && cell.getInitial() != player.getLastVisitedRoom()){
					player.setRow(cell.getRow());
					player.setCol(cell.getCol());
					return;
				}
			}
			int size = targets.size();
			Random rand = new Random();
			int  n = rand.nextInt(size);
			int count = 0;
			for(BoardCell cell: targets){
				if(count == n){
					player.setRow(cell.getRow());
					player.setCol(cell.getCol());
					targetSelected = true;
					return;
				}
				count++;
			}
			player.setRoom(board[player.getRow()][player.getCol()].getInitial());
			targetSelected = true;
		}
		else{
			//idk do something
			
		}
	}
	



	

	/**
	 * uses algorithm given to calculate targets
	 * from a certain cell with a certain num of steps
	 * 
	 * @param row
	 * @param col
	 * @param pathLength
	 */
	public void findTargets(int row, int col, int pathLength){
		BoardCell cell = board[row][col];
		if(!visited.contains(cell)){
			visited.add(cell);
		}
		for(BoardCell b : getAdjList(row, col)){
			if(visited.contains(b)){
				continue;
			}
			visited.add(b);

			if(pathLength == 1 || b.isDoorway()){
				targets.add(b);
			}
			else{
				findTargets(b.getRow(), b.getCol(), pathLength - 1);
			}
			visited.remove(b);
		}
	}

	public void setCardFiles(String people, String weapons){
		peopleFile = people;
		weaponFile = weapons;
		return;
	}


	public void setConfigFiles(String board, String room) {
		// TODO Auto-generated method stub
		boardConfigFile = board;
		roomConfigFile = room;
		return;

	}

	public Map<Character, String> getLegend() {
		//initialize();
		return legend;	
		// TODO Auto-generated method stub

	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return row;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return col;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		// TODO Auto-generated method stub
		return adjMap.get(board[i][j]);
	}

	public Set<BoardCell> getTargets() {
		// TODO Auto-generated method stub
		return targets;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getDeck(){
		return deck;
	}
	public Map<Player, ArrayList<Card>> getPlayerCards() {
		return playerCards;
	}

	public BoardCell[][] getBoard() {
		return board;
	}
	public void clearPlayers(){
		players.clear();
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public ArrayList<String> getWeapons(){
		return weapons;
	}
	
	public ArrayList<String> getRooms(){
		return rooms;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public String getDieRoll(){
		String num = new String();
		Random rand = new Random();
		dieRoll = rand.nextInt(6) + 1;
		num = Integer.toString(dieRoll);		
		return num;
	}
	public boolean isTargetSelected() {
		return targetSelected;
	}

	public Card getCardShown() {
		return cardShown;
	}

	public Solution getCurrentAccusation() {
		return currentAccusation;
	}
	
	
}
