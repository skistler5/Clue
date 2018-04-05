package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.ComputerPlayer;
import clueGame.Player;

public class gameActionTests {
	private static Board board;
	
	@Before
	public void setUp(){
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");
		board.setCardFiles("players.txt", "weapons.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}
	
	@Test
	//testing selecting a target location
	public void targetLocation(){
		
		
		//Test: if no rooms in list, select randomly
		// [5,2] - 2 steps
		Player testPlayer = new ComputerPlayer("Test PLayer", 5, 2, 100, 100, 100, "c");
		board.calcTargets(5,2,2,testPlayer);
		Set<BoardCell> possibleTargets = new HashSet<BoardCell>();
		possibleTargets = board.getTargets();
		
		int testRow = testPlayer.getRow();
		int testCol = testPlayer.getCol();
		boolean isThere = false;
		
		for(BoardCell b: possibleTargets){
			if(b.getRow() == testRow && b.getCol() == testCol){
				isThere = true;
			}
		}
		//actual test
		assertTrue(isThere);
		

	}
	
	@Test
	//testing checking an accusation
	public void checkAccusation(){
		
	}
	
	@Test
	//testing disproviing a suggestion
	public void disproveSuggestion(){
		
	}
	
	@Test
	//testing handing a suggestion
	public void handleSuggestion(){
		
	}
	
	@Test
	//testing creating a suggestion
	public void createSuggestion(){
		
	}
}
