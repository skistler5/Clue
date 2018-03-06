/*
 * Authors: Stephen Kistler and Shannon Bride
 */

package clueGame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private int maxr = MAX_BOARD_SIZE;
	private int maxc = MAX_BOARD_SIZE;
	private Map<BoardCell, HashSet<BoardCell>> adjMap = new HashMap<BoardCell, HashSet<BoardCell>>();
	private BoardCell[][] board;
	private Map<Character, String> legend = new HashMap<Character, String>();
	private HashSet<BoardCell> targets = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
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
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(BadConfigFormatException e){
			System.out.println(e.getMessage());
		}
		
		try{
			loadBoardConfig();
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(BadConfigFormatException e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(roomConfigFile);
		Scanner input = new Scanner(reader);
		
		while(input.hasNextLine()){
			String line = input.nextLine();
			String[] words = line.split(", ");
			if(words[0].length() > 1){throw new BadConfigFormatException("Room abbreviation is in the wrong format");}
			if(words.length < 3){throw new BadConfigFormatException("NO ROOM TYPE");}
			if(words[2] != "Card" && words[2] != "Other"){throw new BadConfigFormatException("This is not a valid option");}
			legend.put(words[0].charAt(0), words[1]);
		}
		
		input.close();
	}
	
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader reader = new FileReader(boardConfigFile);
		Scanner input = new Scanner(reader);
		int rowCount = 0;
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		
		
		while(input.hasNextLine()){
			String line = input.nextLine();
			String[] words = line.split("\\s*,\\s*");
			
			for(int i = 0; i < words.length; i++){
				board[rowCount][i] = new BoardCell(rowCount,i);
				board[rowCount][i].setInitial(words[i].charAt(0));
				if(words[i].length() > 1){
					board[rowCount][i].setDoorDirection(words[i].charAt(1));
					board[rowCount][i].setDoorWay(true);
					if(words[i].charAt(1) != 'U' && words[i].charAt(1) != 'D' && words[i].charAt(1) != 'R' && words[i].charAt(1) != 'L'){
						throw new BadConfigFormatException("Invalid Door Direction");
					}
				}
				else{
					board[rowCount][i].setDoorDirection('N');
					board[rowCount][i].setDoorWay(false);
				}
			}
			
			if(rowCount == 0){
				maxc = words.length;
			}
			else if(words.length != maxc){
				throw new BadConfigFormatException("Number of columns is inconsistent");
			}
			
			rowCount++;
		}
		
		input.close();
		
		maxr = rowCount;
	}
	
	public void calcAdjacencies(){
		for(int i = 0; i < maxr; i++){
			for(int j = 0; j < maxc; j++){
				HashSet<BoardCell> adjs = new HashSet<BoardCell>();
				if (i - 1 >= 0){
					adjs.add(board[i-1][j]);
				}
				if(i + 1 < maxr){
					adjs.add(board[i+1][j]);
				}
				if(j-1 >= 0){
					adjs.add(board[i][j-1]);
				}
				if(j+1 < maxc){
					adjs.add(board[i][j+1]);
				}
				adjMap.put(board[i][j], adjs);
			}
		}
		return;
	}
	
	public void calcTargets(BoardCell cell, int pathLength){
		if(!visited.contains(cell)){
			visited.add(cell);
		}
		for(BoardCell b : getAdjList(cell)){
			if(visited.contains(b)){
				continue;
			}
			visited.add(b);
			if(pathLength == 1){
				targets.add(b);
			}
			else{
				calcTargets(b, pathLength - 1);
			}
			visited.remove(b);
		}
	}
	
	public HashSet<BoardCell> getAdjList(BoardCell startCell){
		return adjMap.get(startCell);
	}

	public void setConfigFiles(String board, String room) {
		// TODO Auto-generated method stub
		boardConfigFile = board;
		roomConfigFile = room;
		return;
		
	}

	public Map<Character, String> getLegend() {
		initialize();
		return legend;	
		// TODO Auto-generated method stub
		
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return maxr;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return maxc;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
}
