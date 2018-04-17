package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveDialog extends JDialog {
	ArrayList<Player> players;
	ArrayList<String> weapons;
	ArrayList<String> rooms;
	
	public DetectiveDialog(ArrayList<Player> players, ArrayList<String> weapons, ArrayList<String> rooms) {
		this.players = players;
		this.weapons = weapons;
		this.rooms = rooms;
		setUpGui();
	}
	
	public void setUpGui() {
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLayout(new GridLayout(3, 2));
		add(createPeopleCheckboxes());
		add(createPersonGuess());
		add(createRoomCheckboxes());
		add(createRoomGuess());
		add(createWeaponCheckboxes());
		add(createWeaponGuess());
		
		pack();
	}
	
	public JPanel createPeopleCheckboxes() {
		JPanel people = new JPanel();
		
		people.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		people.setLayout(new GridLayout(3, 2));
		for (Player p : players) {
			people.add(new JCheckBox(p.getPlayerName()));
		}
		
		return people;
	}
	
	public JPanel createPersonGuess() {
		JPanel person = new JPanel();
		
		person.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
		
		JComboBox options = new JComboBox();
		
		for (Player p: players) {
			options.addItem(p.getPlayerName());
		}
		
		person.add(options);
		
		return person;
	}
	
	public JPanel createRoomCheckboxes() {
		JPanel room = new JPanel();
		
		room.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		room.setLayout(new GridLayout(0, 2));
		for (String r: rooms) {
			room.add(new JCheckBox(r));
		}
		
		return room;
	}
	
	public JPanel createRoomGuess() {
		JPanel roomsGuess = new JPanel();
		
		roomsGuess.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
		
		JComboBox roomGuesses = new JComboBox();
		
		for (String r: rooms) {
			roomGuesses.addItem(r);
		}
		
		roomsGuess.add(roomGuesses);
		
		return roomsGuess;
	}
	
	public JPanel createWeaponCheckboxes() {
		JPanel weapon = new JPanel();
		
		weapon.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		weapon.setLayout(new GridLayout(3, 2));
		for (String w: weapons) {
			weapon.add(new JCheckBox(w));
		}
		
		return weapon;
	}
	
	public JPanel createWeaponGuess() {
		JPanel weaponsGuess = new JPanel();

		weaponsGuess.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));

		JComboBox weaponGuesses = new JComboBox();

		for (String w: weapons) {
			weaponGuesses.addItem(w);
		}
		
		weaponsGuess.add(weaponGuesses);

		return weaponsGuess;
	}
	
}
