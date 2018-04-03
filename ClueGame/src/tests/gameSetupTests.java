package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Player;


public class gameSetupTests {
	private static Board board;

	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}
	
	@Test
	public void people(){
		ArrayList<Player> players;
		players = board.getPlayers();
		//first person in array list should be computer named shannon at 0,2
		assertEquals(players.get(0).getPlayerName(),"Shannon");
		assertEquals(players.get(0).getRow(),0);
		assertEquals(players.get(0).getCol(),2);
		assertEquals(players.get(0).getColor(),Color.orange);
		assertTrue(players.get(0).isComputer());
		
		//third person in array list should be human player named Gary at 10,0
		assertEquals(players.get(2).getPlayerName(),"Gary");
		assertEquals(players.get(2).getRow(),10);
		assertEquals(players.get(2).getCol(),0);
		assertEquals(players.get(2).getColor(),Color.red);
		assertFalse(players.get(2).isComputer());
		
		//last person in array list should be computer player named Ellie at 11,13
		assertEquals(players.get(5).getPlayerName(),"Ellie");
		assertEquals(players.get(5).getRow(),11);
		assertEquals(players.get(5).getCol(),13);
		assertEquals(players.get(5).getColor(),Color.green);
		assertTrue(players.get(5).isComputer());
	}
}
