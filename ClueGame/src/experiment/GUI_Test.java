package experiment;
import java.awt.BorderLayout;
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
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(500, 200);
		
		GUI_Test gui = new GUI_Test();
		frame.add(gui, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}
}
