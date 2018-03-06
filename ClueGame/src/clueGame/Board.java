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
import java.util.Set;

import experiment.BoardCell;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private final int maxr = 23;
	private final int maxc = 14;
	private clueGame.BoardCell[][] board;
	private Map<Character, String> legend = new HashMap<Character, String>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
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
		board = new clueGame.BoardCell[maxr][];
		for(int i = 0; i < maxr; i++){
			board[i] = new clueGame.BoardCell[maxc];
			for(int j = 0; j < maxc; j++){
				board[i][j] = new clueGame.BoardCell(i,j);
			}
		}
	}
	
	public void loadRoomConfig(){
		
	}
	
	public void loadBoardConfig(){
		
		
	}
	
	public void calcAdjacencies(){
		
	}
	
	public void calcTargets(BoardCell cell, int pathLength){
		
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	public Map<Character, String> getLegend() {
		String line;
		try(
				InputStream fr = new FileInputStream("CTest_ClueLegend.txt");
				InputStreamReader is = new InputStreamReader(fr);
				BufferedReader br = new BufferedReader(is);
		){
		
			while((line = br.readLine()) != null){
				String[] words = line.split(", ");
				char c = words[0].charAt(0);
				String temp = words[1];
				legend.put(c, temp);
			}	
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return legend;	
		// TODO Auto-generated method stub
		
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public clueGame.BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
}
