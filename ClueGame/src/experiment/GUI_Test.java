package experiment;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clueGame.Board;

public class GUI_Test extends JPanel{
	private JTextField turn;
	private JTextField dieRoll;
	private JTextField guess;
	private JTextField guessResult;
	
	public GUI_Test(){
		setLayout(new GridLayout(6,5));
		createLayout();
		
	}
	
	public void createLayout(){
		JLabel turnLabel = new JLabel("Whose Turn:");
		turn = new JTextField(5);
		turn.setEditable(false);
		add(turnLabel, BorderLayout.EAST);
		add(turn, BorderLayout.EAST);
		
		JLabel dieRollLabel = new JLabel("Die Roll:");
		dieRoll = new JTextField(5);
		dieRoll.setEditable(false);
		add(dieRollLabel, BorderLayout.EAST);
		add(dieRoll, BorderLayout.EAST);
		
		JLabel guessLabel = new JLabel("Guess:");
		guess = new JTextField(5);
		add(guessLabel, BorderLayout.EAST);
		add(guess, BorderLayout.EAST);
		
		JLabel guessResultLabel = new JLabel("Guess Result:");
		guessResult = new JTextField(5);
		guess.setEditable(false);
		add(guessResultLabel, BorderLayout.EAST);
		add(guessResult, BorderLayout.EAST);
		
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make an Accusation");
		add(makeAccusation, BorderLayout.EAST);
		add(nextPlayer, BorderLayout.WEST);
		
	}
	
	public static void main(String[] args){
		clueGame.Board board = Board.getInstance();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(500, 200);
		
		JFrame frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setTitle("Board Example");
		frame2.setSize(1000, 1000);
		frame2.add(board, BorderLayout.CENTER);
		
		GUI_Test gui = new GUI_Test();
		frame.add(gui, BorderLayout.CENTER);
		
		frame.setVisible(true);
		
		frame2.setVisible(true);
	}
}
