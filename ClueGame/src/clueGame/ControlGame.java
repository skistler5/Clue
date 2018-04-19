package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	JMenuBar menuBar = new JMenuBar();
	JFrame splash = new JFrame();

	public ControlGame(){
		board = Board.getInstance();
		board.setConfigFiles("ClueBoardLayout.csv", "roomLegend.txt");
		board.setCardFiles("players.txt", "weapons.txt");
		board.initialize();
		board.dealCards();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());

		createSplashScreen();

		add(board, BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
		add(createCardPanel(), BorderLayout.EAST);

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

	public JPanel createControlPanel(){
		JPanel controlPanel = new JPanel();

		controlPanel.setLayout(new GridLayout(5,6));

		JLabel turnLabel = new JLabel("Whose Turn:");
		turn = new JTextField(5);
		turn.setText(board.getCurrentPlayer().getPlayerName());
		turn.setEditable(false);
		controlPanel.add(turnLabel, BorderLayout.EAST);
		controlPanel.add(turn, BorderLayout.EAST);

		JLabel dieRollLabel = new JLabel("Die Roll:");
		dieRoll = new JTextField(5);
		dieRoll.setText(board.getDieRoll());
		dieRoll.setEditable(false);
		controlPanel.add(dieRollLabel, BorderLayout.EAST);
		controlPanel.add(dieRoll, BorderLayout.EAST);

		JLabel guessLabel = new JLabel("Guess:");
		guess = new JTextField(5);
		Solution accu = board.createAccusation(board.getCurrentPlayer());
		guess.setText(accu.person + ", " + accu.room + ", " + accu.weapon);
		guess.setEditable(false);
		controlPanel.add(guessLabel, BorderLayout.EAST);
		controlPanel.add(guess, BorderLayout.EAST);

		JLabel guessResultLabel = new JLabel("Guess Result:");

		guessResult = new JTextField(5);
		guessResult.setText(board.handleSuggestion(board.getCurrentPlayer(), accu).getCardName());
		guess.setEditable(false);
		controlPanel.add(guessResultLabel, BorderLayout.EAST);
		controlPanel.add(guessResult, BorderLayout.EAST);

		JButton nextPlayer = new JButton("Next Player");
		//		class ButtonListener implements ActionListener{
		//		public void actionPerformed(ActionEvent e){
		//			JDialog dialog = new JDialog();
		//			dialog.setVisible(true);
		//		}
		//	}
		nextPlayer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(board.isTargetSelected()){
					board.playerTurn(board.getNextPlayer());
				}
				else{
					//error message
				}
				//setVisible(false);
			}
		});
		//add(nextPlayer);


		JButton makeAccusation = new JButton("Make an Accusation");
		controlPanel.add(makeAccusation, BorderLayout.EAST);
		controlPanel.add(nextPlayer, BorderLayout.WEST);

		return controlPanel;

	}


	public static void main(String[] args){
		ControlGame gui = new ControlGame();
		gui.setVisible(true);
	}
}
