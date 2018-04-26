package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
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
	public void makeAccusation(){
		Solution accusation1 = new Solution("Shannon", "Bowling Alley", "Toothpick");
		Solution accusation2 = new Solution("Stephen", "Bowling Alley", "Toothpick");
		Solution accusation3 = new Solution("Shannon", "Chapter Room", "Toothpick");
		Solution accusation4 = new Solution("Shannon", "Bowling ALley", "Shotgun");
		Solution guess = new Solution("Shannon", "Bowling Alley", "Toothpick");
		//tests if the solution is the same
		assertTrue(accusation1.equals(guess));
		//tests for wrong person
		assertFalse(accusation2.equals(guess));
		//tests for wrong room
		assertFalse(accusation3.equals(guess));
		//tests for wrong weapon
		assertFalse(accusation4.equals(guess));
		
	}
	
	@Test
	//testing creating a suggestion
	public void createSuggestion(){
		board.clearPlayers();
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		Player testPlayer = new Player("Test Player", 2, 7, 100, 100, 100, "c");
		tempPlayers.add(testPlayer);
		Solution suggestion = new Solution("Gary", "Library", "Dog Bone");
		Player test1 = new Player("Gary", 3, 8, 100, 100, 100, "c");
		Player test2 = new Player("Shannon", 4, 8, 100, 100, 100, "c");
		Player test3 = new Player("Stephen", 5, 8, 100, 100, 100, "c");
		Player test4 = new Player("Bob", 3, 9, 100, 100, 100, "c");
		tempPlayers.add(test1);
		tempPlayers.add(test2);
		tempPlayers.add(test3);
		tempPlayers.add(test4);
		board.setPlayers(tempPlayers);
		board.setPlayersOptions();
//		for(String s: board.getWeapons()){
//			System.out.print(s + " ");
//		}
//		System.out.println("hi");
//		for(String s : testPlayer.getWeaponOptions()){
//			System.out.print(s + " ");
//		}
		testPlayer.addToPlayersSeen(test1);
		testPlayer.addToPlayersSeen(test2);
		testPlayer.addToWeaponsSeen("Dog Bone");
		testPlayer.addToWeaponsSeen("Rope");
		testPlayer.addToWeaponsSeen("Shotgun");
		testPlayer.addToWeaponsSeen("Knife");
		testPlayer.setRoom('L');
		Solution temp = testPlayer.createSuggestion(board.getPlayersRoom(testPlayer));
		boolean test = false;
		
		//tests if suggestion has the same room as the player
		assertEquals(suggestion.room, temp.room);
		
		//if multiple weapons not seen, one is randomly selected
		if(temp.weapon.equals("Toothpick") || temp.weapon.equals("Icicle")){
			test = true;
		}

		//System.out.println(suggestion.weapon);
		assertTrue(test);
		
		//if multiple people not seen, one is selected randomly
		test = false;
		if(temp.person.equals("Stephen") || temp.person.equals("Bob")){
			test = true;
		}
		
		assertTrue(test);
		
		testPlayer.addToWeaponsSeen("Toothpick");
		testPlayer.addToPlayersSeen(test3);
		temp = testPlayer.createAccusation();
		
		//if only one weapon not seen it is selected
		assertTrue(temp.weapon.equals("Icicle"));
		//if only one person not seen it is selected
		assertTrue(temp.person.equals("Bob"));
		
		
		
	}
	@Test
	//testing disproving a suggestion
	public void disproveSuggestion(){
		Solution suggestion = new Solution("Shannon", "Bowling Alley", "Toothpick");
		
		
		//test if player has only one matching card it should be returned
		Player testPlayer = new ComputerPlayer("Test Player", 5, 7, 100, 100, 100, "c");
		//testPlayer.clearHand();
		testPlayer.addToHand(new Card("Shannon", CardType.PERSON));
		testPlayer.addToHand(new Card("Rope", CardType.WEAPON));
		testPlayer.addToHand(new Card("Kitchen", CardType.ROOM));
		testPlayer.addToHand(new Card("Icicle", CardType.WEAPON));
		
		Card testCard = testPlayer.disproveSuggestion(suggestion);
		assertTrue(testCard.equals(new Card("Shannon", CardType.PERSON)));
		
		//test if players has >1 matching card, returned card should be chosen randomly
		Player testPlayer1 = new ComputerPlayer("Test Player 1", 20, 17, 100, 100, 100, "c");
		//testPlayer1.clearHand();
		testPlayer1.addToHand(new Card("Shannon", CardType.PERSON));
		testPlayer1.addToHand(new Card("Toothpick", CardType.WEAPON));
		testPlayer1.addToHand(new Card("Bowling Alley", CardType.ROOM));
		testPlayer1.addToHand(new Card("Icicle", CardType.WEAPON));
		
		Card testCard1 = testPlayer1.disproveSuggestion(suggestion);
		boolean isEqual = false;
		for(int i = 0; i < testPlayer1.getPlayerHand().size(); i++){
			if(testCard1.equals(testPlayer1.getPlayerHand().get(i))){
				isEqual = true;
			}
		}
		assertTrue(isEqual);

		
		//test if player has no matching cards, null is returned
		Player testPlayer2 = new ComputerPlayer("Test Player 2", 21, 3, 100, 100, 100, "c");
		//testPlayer2.clearHand();
		testPlayer2.addToHand(new Card("Stephen", CardType.PERSON));
		testPlayer2.addToHand(new Card("Kitchen", CardType.WEAPON));
		testPlayer2.addToHand(new Card("Icicle", CardType.WEAPON));
		
		Card testCard2 = testPlayer2.disproveSuggestion(suggestion);
		assertEquals(testCard2, null);
	}
	
	@Test
	//testing handing a suggestion
	public void handleSuggestion(){
		Solution suggestion = new Solution("Shannon", "Bowling Alley", "Toothpick");
		board.clearPlayers();
		
		
		//test that suggestion no one can disprove returns null
		Player tplayer0 =  new ComputerPlayer("tp0", 15, 12, 100, 100, 100, "c");
		Player tplayer = new ComputerPlayer("tp", 15, 12, 100, 100, 100, "c");
		Player tplayer1 = new ComputerPlayer("tp1", 15, 12, 100, 100, 100, "c");
		Player tplayer2 = new HumanPlayer("tp2", 15, 12, 100, 100, 100, "h");
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		tempPlayers.add(tplayer0);
		tempPlayers.add(tplayer);
		tempPlayers.add(tplayer1);
		tempPlayers.add(tplayer2);
		board.setPlayers(tempPlayers);

		board.setPlayersOptions();
		
		tplayer.addToHand(new Card("Stephen", CardType.PERSON));
		tplayer.addToHand(new Card("Gary", CardType.PERSON));
		tplayer.addToHand(new Card("Shotgun", CardType.WEAPON));
		tplayer1.addToHand(new Card("Bob", CardType.PERSON));
		tplayer1.addToHand(new Card("Storage", CardType.ROOM));
		tplayer1.addToHand(new Card("Bathroom", CardType.ROOM));
		tplayer2.addToHand(new Card("Chapter Room", CardType.ROOM));
		tplayer2.addToHand(new Card("Library", CardType.ROOM));
		tplayer2.addToHand(new Card("Icicle", CardType.WEAPON));
		
		
		
		//test that suggestion only accusing player can disprove returns null
		Card testCard = board.handleSuggestion(tplayer0, suggestion);
		assertEquals(testCard, null);
		
		//test that suggestion only human can disprove returns answer
		Solution suggestion1 = new Solution("Stephen", "Bowling Alley", "Toothpick");
		Card testCard1 = board.handleSuggestion(tplayer0, suggestion1);
		assertTrue(testCard1.equals(new Card("Stephen", CardType.PERSON)));
		
		//test that suggestion only human can disprove, but human is accuser returns null
		Solution suggestion2 = new Solution("Shannon", "Library", "Toothpick");
		Card testCard2 = board.handleSuggestion(tplayer2, suggestion2);
		assertTrue(testCard2.equals(new Card("Library", CardType.ROOM)));
		
		
		// test that suggestion that two players can disprove , correct player (based on starting with next
		//player in list returns answer
		Solution suggestion3 = new Solution("Bob", "Chapter Room", "Toothpick");
		Card testCard3 = board.handleSuggestion(tplayer0, suggestion3);
		assertTrue(testCard3.equals(new Card("Bob", CardType.PERSON)));
		
		//suggestion that human and another player can disprove, other player is next
		// in list, ensure other player returns answer
		Solution suggestion4 = new Solution("Bob", "Library", "Toothpick");
		Card testCard4 = board.handleSuggestion(tplayer0, suggestion4);
		assertTrue(testCard4.equals(new Card("Bob", CardType.PERSON)));
		
	}

}
