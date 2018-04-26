package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.SplashScreen;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGame extends JFrame{
	private Board board;
	private JTextField turn;
	private JTextField dieRoll;
	private JTextField guess;
	private JTextField guessResult;
	private JPanel controlPanel;
	JMenuBar menuBar = new JMenuBar();
	JFrame splash = new JFrame();
	private static ControlGame gui;

	public ControlGame(){
		board = Board.getInstance();
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");
		board.setCardFiles("players.txt", "weapons.txt");
		board.initialize();
		board.setPlayersOptions();
		board.dealCards();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());

		createSplashScreen();

		add(board, BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
		add(createCardPanel(), BorderLayout.EAST);
		turn(board.getCurrentPlayer());  

	}
	
	public void update(){
		updateDiePanel();
		updateTurnPanel(board.getCurrentPlayer().getPlayerName());
		updateGuessPanel(board.getPlayersGuess());
		updateGuessResultPanel(board.getCardShown());  //TODO: seems to like returning patricia
	}
	
	public static void errorMessage(){
		JOptionPane.showMessageDialog(gui, "Invalid Move", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void createSplashScreen(){
		String message = "You are " + board.getCurrentPlayer().getPlayerName() + ", press next Player to begin play";
		JFrame splashScreen = new JFrame("Welcome to Clue");
		splashScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JOptionPane.showMessageDialog(splashScreen, message, "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		setSize(900, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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


	public JPanel createCardPanel(){
		ArrayList<Card> playersHand = findPlayersCards();
		JPanel cardPanel = new JPanel();

		JTextArea myCard = new JTextArea(2,1);
		myCard.setEditable(false);
		JTextArea myWeapon = new JTextArea(2,1);
		myWeapon.setEditable(false);
		JTextArea myRoom = new JTextArea(2,1);
		myRoom.setEditable(false);

		for(Card c: playersHand){
			if(c.getCardType().equals(CardType.PERSON)){
				myCard.append(c.getCardName() + "\n");
			}
			else if(c.getCardType().equals(CardType.WEAPON)){
				myWeapon.append(c.getCardName() + "\n");
			}
			else{
				myRoom.append(c.getCardName() + "\n");
			}
		}


		JLabel myCardLabel = new JLabel("Players:");
		JLabel myWeaponLabel = new JLabel("Weapons:");
		JLabel myRoomLabel = new JLabel("Rooms:");

		cardPanel.setLayout(new GridLayout(6,1));
		cardPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		cardPanel.setPreferredSize(new Dimension(200, 0));

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
	
	private JPanel createDiePanel() {
		JPanel dicePanel = new JPanel();

		JLabel dieRollLabel = new JLabel("Die Roll:");
		dieRoll = new JTextField("   ");
		//want to be able to change the number whenever we roll for each turn
		dieRoll.setEditable(false);

		dicePanel.add(dieRollLabel);
		dicePanel.add(dieRoll);
		dicePanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));

		return dicePanel;
	}
	
	private void updateDiePanel(){
		String s = "";
		String roll = board.getDieRoll();
		dieRoll.setText(s + roll);
	}

	private JPanel createTurnPanel() {
		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new GridLayout(2,0));
		
		JLabel turnLabel = new JLabel("Whose turn?", JLabel.CENTER);
		turn = new JTextField();

		turnPanel.add(turnLabel);
		turnPanel.add(turn);
		turn.setEditable(false);
		return turnPanel;
	}
	
	private void updateTurnPanel(String s){
		turn.setText(s);
	}
	
	private JPanel createGuessResultPanel() {
		JPanel guessResultPanel = new JPanel();

		JLabel responseLabel = new JLabel("Response");
		guessResult = new JTextField(8);
		guessResult.setEditable(false);

		guessResultPanel.add(responseLabel);
		guessResultPanel.add(guessResult);
		guessResultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Result"));

		return guessResultPanel;
	}
	
	public void updateGuessResultPanel(Card c){
		if(c != null){
			guessResult.setText(c.getCardName());
		}
		else{
			guessResult.setText("");
		}
		
	}
	
	private JPanel createGuessPanel() {
		guess = new JTextField(5);
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(2, 1));
		JLabel guessLabel = new JLabel("Guess");
		guess.setEditable(false);
		Font font = new Font("Verdana", Font.PLAIN, 10);
		guess.setFont(font);

		guessPanel.add(guessLabel);
		guessPanel.add(guess);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

		return guessPanel;
	}
	
	public void updateGuessPanel(Solution s){
		guess.setText(s.getPerson() + " in the " + s.getRoom() + " with the " + s.getWeapon());
	}
	
	public JPanel createControlPanel(){
		JPanel controlPanel = new JPanel();

		controlPanel.setLayout(new GridLayout(3,2));

		controlPanel.add(createTurnPanel(), BorderLayout.EAST);

		controlPanel.add(createDiePanel(), BorderLayout.EAST);
		
		controlPanel.add(createGuessPanel(), BorderLayout.EAST);

		controlPanel.add(createGuessResultPanel(), BorderLayout.EAST);

		JButton nextPlayer = new JButton("Next Player");
		nextPlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(board.getHumanSelection()){
					Player p = board.getNextPlayer();
					board.playerTurn(p);
					update();
				}
				else{
					//error message
					JOptionPane.showMessageDialog(gui, "Human player has not chosen a target", "Error", JOptionPane.ERROR_MESSAGE);
				}
				//setVisible(false);
			}
		});
		controlPanel.add(nextPlayer, BorderLayout.EAST);


		JButton makeAccusation = new JButton("Make an Accusation");
		controlPanel.add(makeAccusation, BorderLayout.EAST);
		controlPanel.add(nextPlayer, BorderLayout.WEST);

		return controlPanel;

	}
	
	public void turn(Player p){
		board.playerTurn(p);
		board.repaint();
		menuBar.repaint();
		board.repaint();
		update();
	}


	public static void main(String[] args){
		ControlGame gui = new ControlGame();
		gui.setVisible(true);
	}
}
