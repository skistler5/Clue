/*
 * Authors: Stephen Kistler and Shannon Bride
 */

package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;

/**
 * 
 * @author Shannon Bride
 * @author Stephen Kistler
 * 
 * 
 */

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private int row; 
	private int col;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Map<BoardCell, HashSet<BoardCell>> adjMap = new HashMap<BoardCell, HashSet<BoardCell>>(); //contains sets of adjacencies for each cell
	private Map<Player, ArrayList<Card>> playerCards = new HashMap<Player, ArrayList<Card>>();
	private BoardCell[][] board;
	private Map<Character, String> legend = new HashMap<Character, String>(); //stores room names and short hand for them
	private Set<BoardCell> targets = new HashSet<BoardCell>(); //places player can move to with given steps
	private Set<BoardCell> visited = new HashSet<BoardCell>(); //already visited cells 
	private String boardConfigFile = new String();
	private String roomConfigFile = new String();
	private String peopleFile = new String();
	private String weaponFile = new String();
	private ArrayList<Card> deck = new ArrayList<Card>();



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
		calcAdjacencies();
	}
	
	/**
	 * Deals the cards to the players and clears the deck so that it is empty
	 */
	public void dealCards(){
		int numPlayers = players.size();
		int count = 0;
		int i = 0;
		for(Card c: deck){
			if(playerCards.containsKey(players.get(i))){
				playerCards.get(players.get(i)).add(c);
			}
			else{
				ArrayList<Card> temp = new ArrayList<Card>();
				temp.add(c);
				playerCards.put(players.get(i), temp);
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
					currentCell.setDoorDirection(words[i].charAt(1));
					currentCell.setDoorWay(true);

					Character c = new Character(words[i].charAt(1)); //need this to compare to other constants
					if(c.equals('N')){
						currentCell.setDoorWay(false);
						currentCell.setDoorDirection('N');
					}
					if(!c.equals('U') && !c.equals('D') && !c.equals('R') && !c.equals('L') && !c.equals('N')){
						throw new BadConfigFormatException("Invalid Door Direction");
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
	
	public void chooseTarget(Player player){
		
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
	

}
