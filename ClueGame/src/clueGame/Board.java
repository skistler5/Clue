/*
 * Authors: Stephen Kistler and Shannon Bride
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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

import javax.swing.JPanel;

import clueGame.BoardCell;

/**
 * 
 * @author Shannon Bride
 * @author Stephen Kistler
 * 
 * 
 */

public class Board extends JPanel implements MouseListener{
	public static final int MAX_BOARD_SIZE = 50;
	private int row; 
	private int col;
	private ArrayList<Player> players = new ArrayList<Player>();
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
	private boolean targetSelected = false;
	private boolean humanSelection = false;
	private Card cardShown = null;
	private Player currentPlayer = null;


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
		addMouseListener(this);
		calcAdjacencies();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(humanSelection){
			ControlGame.errorMessage();
			return;
		}
		boolean isValid = false;
		BoardCell returnCell = null;
		int x = e.getX();
		int y = e.getY();
		
		for(BoardCell cell: targets){
			if(y<=(cell.getRow()*BoardCell.CELL_SIZE + 3*BoardCell.CELL_SIZE-1) && y >=(cell.getRow()*BoardCell.CELL_SIZE + 50)&&
					x<=(cell.getCol()*BoardCell.CELL_SIZE + BoardCell.CELL_SIZE-1) && x >=(cell.getCol()*BoardCell.CELL_SIZE)){
				returnCell = cell;
				isValid = true;
				//if it's a room cell, they need to make a guess
				
			}
		}
		if(isValid){
			players.get(2).setRow(returnCell.getRow());
			players.get(2).setCol(returnCell.getCol());
			humanSelection = true;
			repaint();
		}
		else{
			ControlGame.errorMessage();
		}

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
			if(!p.isComputer()){
				calcTargets(p.getRow(), p.getCol(), 3, p);
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
		humanSelection = false;
		Random rand = new Random();
		int numRoll = rand.nextInt(6) + 1;
		if(p.isComputer()){
			int r = p.getRow();
			int c = p.getCol();
			calcTargets(r,c,numRoll);
			chooseTarget(p);
			cardShown = handleSuggestion(p, createAccusation(p));
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
			Solution guess = createAccusation(p);
			cardShown = handleSuggestion(p, guess);
		}
		repaint();
		if(getNextPlayer().isComputer()){
			playerTurn(currentPlayer);
		}
	}


	public Player getNextPlayer(){
		for(int i = 0; i < players.size() + 1; i++){
			if(players.get(i).equals(currentPlayer)){
				if(i == players.size() - 1){
					currentPlayer = players.get(0);
					return currentPlayer;
				}
				currentPlayer = players.get(i+1);
				return currentPlayer;
			}
		}
		return currentPlayer;
	}

	
	public Solution createAccusation(Player p){
		Random rand = new Random();
		String room = new String();
		ArrayList<String> possibleWeapons = new ArrayList<String>();
		ArrayList<String> possiblePlayers = new ArrayList<String>();
		System.out.println(p.getRoom());
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
		
		Solution accusation = new Solution(possiblePlayers.get(pit), room, possibleWeapons.get(wit));
		
		return accusation;
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
			//			if(playerCards.containsKey(players.get(i))){
			//				playerCards.get(players.get(i)).add(c);
			//			}
			//			else{
			//				ArrayList<Card> temp = new ArrayList<Card>();
			//				temp.add(c);
			//				playerCards.put(players.get(i), temp);
			//			}
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
		Scanner input = new Scanner(reader);

		//reads in room names, symbols, and type
		while(input.hasNextLine()){
			String line = input.nextLine();
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
		input.close();
	}

	/**
	 * loads board info from csv
	 * @throws BadConfigFormatException
	 * @throws FileNotFoundException
	 */
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(boardConfigFile);
		Scanner input = new Scanner(reader);
		int rowCount = 0;
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];

		//reads csv line by line and figures out if it's a doorway
		while(input.hasNextLine()){
			String line = input.nextLine();
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
		input.close();
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
	public void calcTargets(int row, int col, int pathLength, Player player){
		visited.clear();
		targets.clear();

		findTargets(row, col, pathLength);
		chooseTarget(player);
	}

	public void calcTargets(int row, int col, int pathLength){
		visited.clear();
		targets.clear();

		findTargets(row, col, pathLength);
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
	
	public boolean getHumanSelection(){
		return humanSelection;
	}
	
}
