package clueGame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlGame extends JFrame{
	private Board board;
	private JTextField turn;
	private JTextField dieRoll;
	private JTextField guess;
	private JTextField guessResult;
	
	public ControlGame(){
		JMenuBar menuBar = new JMenuBar();
		board = Board.getInstance();
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");
		board.setCardFiles("players.txt", "weapons.txt");
		board.initialize();
		board.dealCards();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());

	}
	
	public JMenu createFileMenu(){
		JMenu menu = new JMenu("File");
		menu.add(createFileExitItem());
		menu.add(createDetectiveNotesItem());
		return menu;
	}
	
	public JMenuItem createFileExitItem(){
		JMenuItem item = new JMenuItem("Exit");
		class ExitItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		}
		item.addActionListener(new ExitItemListener());
		return item;
	}
	
	public JMenuItem createDetectiveNotesItem(){
		JMenuItem detectiveNotes = new JMenuItem("Notes");
		final JDialog notes = new DetectiveDialog(board.getPlayers(), board.getWeapons(), board.getRooms());
		class DetectiveItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				notes.setVisible(true);
			}
		}
		detectiveNotes.addActionListener(new DetectiveItemListener());
		return detectiveNotes;
	}
	
	public JPanel createBoardPanel(){
		JPanel boardPanel = new JPanel();
		boardPanel.add(board, BorderLayout.CENTER);
		return boardPanel;
	}
	
	public JPanel createCardPanel(){
		ArrayList<Card> playersHand = findPlayersCards();
		JPanel cardPanel = new JPanel();
		JTextField myCard = new JTextField(playersHand.get(2).getCardName());
		myCard.setEditable(false);
		JLabel myCardLabel = new JLabel("Person:");
		JTextField myWeapon = new JTextField(playersHand.get(1).getCardName());
		myWeapon.setEditable(false);
		JLabel myWeaponLabel = new JLabel("Weapon:");
		JTextField myRoom = new JTextField(playersHand.get(0).getCardName());
		myRoom.setEditable(false);
		JLabel myRoomLabel = new JLabel("Room:");
		
		cardPanel.setLayout(new GridLayout(6,1));
		
		cardPanel.add(myCardLabel, BorderLayout.CENTER);
		cardPanel.add(myCard, BorderLayout.CENTER);
		cardPanel.add(myWeaponLabel, BorderLayout.CENTER);
		cardPanel.add(myWeapon, BorderLayout.CENTER);
		cardPanel.add(myRoomLabel, BorderLayout.CENTER);
		cardPanel.add(myRoom, BorderLayout.CENTER);
		
		return cardPanel;
	}
	
	public ArrayList<Card> findPlayersCards(){
		ArrayList<Card> playersCards = new ArrayList<Card>();
		for(Player p: board.getPlayers()){
			if(!p.isComputer()){
				playersCards = p.getPlayerHand();
			}
		}
		
		return playersCards;
	}
	
	public JPanel createControlPanel(){
		JPanel controlPanel = new JPanel();
		
		controlPanel.setLayout(new GridLayout(5,6));
		
		JLabel turnLabel = new JLabel("Whose Turn:");
		turn = new JTextField(5);
		turn.setEditable(false);
		controlPanel.add(turnLabel, BorderLayout.EAST);
		controlPanel.add(turn, BorderLayout.EAST);
		
		JLabel dieRollLabel = new JLabel("Die Roll:");
		dieRoll = new JTextField(5);
		dieRoll.setEditable(false);
		controlPanel.add(dieRollLabel, BorderLayout.EAST);
		controlPanel.add(dieRoll, BorderLayout.EAST);
		
		JLabel guessLabel = new JLabel("Guess:");
		guess = new JTextField(5);
		guess.setEditable(false);
		controlPanel.add(guessLabel, BorderLayout.EAST);
		controlPanel.add(guess, BorderLayout.EAST);
		
		JLabel guessResultLabel = new JLabel("Guess Result:");
		guessResult = new JTextField(5);
		guess.setEditable(false);
		controlPanel.add(guessResultLabel, BorderLayout.EAST);
		controlPanel.add(guessResult, BorderLayout.EAST);
		
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make an Accusation");
		controlPanel.add(makeAccusation, BorderLayout.EAST);
		controlPanel.add(nextPlayer, BorderLayout.WEST);
		
		return controlPanel;
		
	}
	
	public static void main(String[] args){
//		ControlGame game = new ControlGame();
		ControlGame gui = new ControlGame();
		
		gui.setSize(900, 900);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gui.add(gui.createBoardPanel(), BorderLayout.CENTER);
		gui.add(gui.createControlPanel(), BorderLayout.SOUTH);
		gui.add(gui.createCardPanel(), BorderLayout.EAST);
		
		gui.setVisible(true);
	}
}
