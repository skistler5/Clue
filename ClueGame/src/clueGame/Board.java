/*
 * Authors: Stephen Kistler and Shannon Bride
 */

package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private int row; 
	private int col;
	private Map<BoardCell, HashSet<BoardCell>> adjMap = new HashMap<BoardCell, HashSet<BoardCell>>(); //contains sets of adjacencies for each cell
	private BoardCell[][] board;
	private Map<Character, String> legend = new HashMap<Character, String>(); //stores room names and short hand for them
	private Set<BoardCell> targets = new HashSet<BoardCell>(); //places player can move to with given steps
	private Set<BoardCell> visited = new HashSet<BoardCell>(); //already visited cells 
	private String boardConfigFile = new String();
	private String roomConfigFile = new String();



	//variable used for singleton pattern
	private static Board theInstance = new Board();

	//constructor is private to ensure only one is created
	private Board(){
	}

	//returns the only board	
	public static Board getInstance(){
		return theInstance;
	}


	public void initialize(){
		try{
			loadRoomConfig();
			loadBoardConfig();
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(BadConfigFormatException e){
			System.out.println(e.getMessage());
		}
		calcAdjacencies();
	}

	
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
		}

		input.close();
	}

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
				board[rowCount][i].setInitial(words[i].charAt(0));
				if(words[i].length() > 1){
					board[rowCount][i].setDoorDirection(words[i].charAt(1));
					board[rowCount][i].setDoorWay(true);

					Character c = new Character(words[i].charAt(1));
					Character u = new Character('U');
					Character d = new Character('D');
					Character r = new Character('R');
					Character l = new Character('L');

					if(c.equals('N')){
						board[rowCount][i].setDoorWay(false);
						board[rowCount][i].setDoorDirection('N');
					}
					if(!c.equals(u) && !c.equals(d) && !c.equals(r) && !c.equals(l) && !c.equals('N')){
						throw new BadConfigFormatException("Invalid Door Direction");
					}
				}

				else{
					board[rowCount][i].setDoorDirection('N');
					board[rowCount][i].setDoorWay(false);
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
		input.close();
		row = rowCount;
	}

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


	public void calcTargets(int row, int col, int pathLength){
		visited.clear();
		targets.clear();
		
		findTargets(row, col, pathLength);
	}
	
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
}
