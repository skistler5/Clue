package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

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
		
		//setting player loc for entering room not just visited
		Player testPlayer2 = new ComputerPlayer("Test Player 2", 21, 3, 100, 100, 100, "c");
		board.calcTargets(21, 3, 1, testPlayer2);
		BoardCell cell1 = new BoardCell(0,0);
		
		testPlayer2.setLastVisitedRoom(cell1);
		
		//tests if player is in room
		assertEquals(22, testPlayer2.getRow());
		assertEquals(3, testPlayer2.getCol());
		
		//test if player selects randomly when by room that was just visited
		testPlayer.setRow(21);
		testPlayer.setCol(3);
		BoardCell cell = new BoardCell(22,3);
		testPlayer.setLastVisitedRoom(cell);
		testRow = testPlayer.getRow();
		testCol = testPlayer.getCol();
		isThere = false;
		
		for(BoardCell b: possibleTargets){
			if(b.getRow() == testRow && b.getCol() == testCol){
				isThere = true;
			}
		}
		

	}
	
	@Test
	//testing checking an accusation
	public void checkAccusation(){
		
	}
	
	@Test
	//testing creating a suggestion
	public void createSuggestion(){
		
	}
	@Test
	//testing disproviing a suggestion
	public void disproveSuggestion(){
		Solution suggestion = new Solution("Shannon", "Bowling Alley", "Toothpick");
		
		
		//test if player has only one matching card it should be returned
		Player testPlayer = new ComputerPlayer("Test Player", 5, 7, 100, 100, 100, "c");
		ArrayList<Card> playCards = new ArrayList<Card>();
		playCards.add(new Card("Shannon", CardType.PERSON));
		playCards.add(new Card("Rope", CardType.WEAPON));
		playCards.add(new Card("Kitchen", CardType.ROOM));
		playCards.add(new Card("Icicle", CardType.WEAPON));
		
		Card testCard = testPlayer.disproveSuggestion(suggestion);
		assertEquals(testCard, new Card("Shannon", CardType.PERSON));
		
		//test if players has >1 matching card, returned card should be chosen randomly
		Player testPlayer1 = new ComputerPlayer("Test Player 1", 20, 17, 100, 100, 100, "c");
		ArrayList<Card> playCards1 = new ArrayList<Card>();

		playCards1.add(new Card("Shannon", CardType.PERSON));
		playCards1.add(new Card("Toothpick", CardType.WEAPON));
		playCards1.add(new Card("Bowling Alley", CardType.ROOM));
		playCards1.add(new Card("Icicle", CardType.WEAPON));
		
		Card testCard1 = testPlayer1.disproveSuggestion(suggestion);
		boolean isEqual = false;
		for(int i = 0; i < playCards1.size(); i++){
			if(testCard1.equals(playCards1.get(i))){
				isEqual = true;
			}
		}
		assertTrue(isEqual);

		
		//test if player has no matching cards, null is returned
		Player testPlayer2 = new ComputerPlayer("Test Player 2", 21, 3, 100, 100, 100, "c");
		ArrayList<Card> playCards2 = new ArrayList<Card>();

		playCards2.add(new Card("Stephen", CardType.PERSON));
		playCards2.add(new Card("Kitchen", CardType.WEAPON));
		playCards2.add(new Card("Icicle", CardType.WEAPON));
		
		Card testCard2 = testPlayer2.disproveSuggestion(suggestion);
		assertEquals(testCard2, null);
	}
	
	@Test
	//testing handing a suggestion
	public void handleSuggestion(){
		
	}

}
