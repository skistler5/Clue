package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;


public class gameSetupTests {
	private static Board board;

	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");
		board.setCardFiles("players.txt", "weapons.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}

	@Test
	public void people(){
		ArrayList<Player> players;
		players = board.getPlayers();
		//first person in array list should be computer named shannon at 0,2
		Color test = new Color(244,173,66);
		assertEquals(players.get(0).getPlayerName(),"Shannon");
		assertEquals(players.get(0).getRow(),0);
		assertEquals(players.get(0).getCol(),2);
		assertEquals(players.get(0).getColor(),test);
		assertTrue(players.get(0).isComputer());

		//third person in array list should be human player named Gary at 10,0
		Color test2 = new Color(224,20,37);
		assertEquals(players.get(2).getPlayerName(),"Gary");
		assertEquals(players.get(2).getRow(),10);
		assertEquals(players.get(2).getCol(),0);
		assertEquals(players.get(2).getColor(),test2);
		assertFalse(players.get(2).isComputer());

		//last person in array list should be computer player named Ellie at 11,13
		Color test3 = new Color(30,188,80);
		assertEquals(players.get(5).getPlayerName(),"Ellie");
		assertEquals(players.get(5).getRow(),11);
		assertEquals(players.get(5).getCol(),13);
		assertEquals(players.get(5).getColor(),test3);
		assertTrue(players.get(5).isComputer());
	}

	@Test
	public void loadCards(){
		ArrayList<Card> deck;
		deck = board.getDeck();

		//test that deck has 21 cards
		assertEquals(deck.size(), 21);

		//test that deck has 6 players, 6 weapons, and 9 rooms
		int p = 0;
		int w = 0;
		int r = 0;
		for(int i = 0; i < deck.size(); i++){
			if(deck.get(i).getCardType() == CardType.PERSON){
				p++;
			}
			else if(deck.get(i).getCardType() == CardType.ROOM){
				r++;
			}
			else{
				w++;
			}
		}
		assertEquals(p, 6);
		assertEquals(r, 9);
		assertEquals(w, 6);

		//test that Shannon, the rope, and the library are in the deck
		for(int i = 0; i < deck.size(); i++){
			if(deck.get(i).getCardType() == CardType.PERSON){
				if(deck.get(i).getCardName() == "Shannon"){
					assertEquals(deck.get(i).getCardName(), "Shannon");
				}
			}
			else if(deck.get(i).getCardType() == CardType.ROOM){
				if(deck.get(i).getCardName() == "Library"){
					assertEquals(deck.get(i).getCardName(), "Library");
				}
			}
			else{
				if(deck.get(i).getCardName() == "Rope"){
					assertEquals(deck.get(i).getCardName(), "Rope");
				}
			}
		}

	}

	@Test
	public void dealingCards(){
		boolean numCardTest = true;
		boolean sameCardTest = true;
		ArrayList<Card> deck = new ArrayList<Card>();
		Map<Player, ArrayList<Card>> playerCards = new HashMap<Player, ArrayList<Card>>();
		ArrayList<Player> players = board.getPlayers();
		board.dealCards();
		playerCards = board.getPlayerCards();
		deck = board.getDeck();
		//tests that all cards have been dealt
		assertEquals(deck.size(), 0);

		//number of cards each player should be within one of
		int numCards = playerCards.get(players.get(0)).size();
		for(Player p: players){
			if(playerCards.get(p).size() != numCards && playerCards.get(p).size() != (numCards - 1)){
				numCardTest = false;
			}
		}
		//tests if cardTest is still true after comparing all players number of cards
		assertTrue(numCardTest);

		for(Player p: players){
			for(int i = 0; i < playerCards.get(p).size(); i++){
				for(Player p2: players){
					for(int j = 0; j < playerCards.get(p2).size(); j++){
						if(p != p2){
							if(playerCards.get(p).get(i) == playerCards.get(p2).get(j)){
								sameCardTest = false;
							}
						}
					}
				}
			}
		}
		//tests that no players have the same card
		assertTrue(sameCardTest);


	}


}
