package clueGame;

import java.awt.Color;

public class Player {

	private String playerName;
	private int row;
	private int col;
	private Color color;
	private boolean isComp;
	
	public Player(String name, int row, int col, int r, int g, int b, String comp){
		playerName = name;
		this.row = row;
		this.col = col;
		this.color = new Color(r,g,b);
		
		if(comp.equals("c")){
			isComp = true;
		}
		else{
			isComp = false;
		}
	}
	
	public Card disproveSuggestion(Solution suggestion){
		
	}
	
	
	
	
	
	
	
	
	
	
	//Getters and Setters
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isComputer() {
		return isComp;
	}
	
}
