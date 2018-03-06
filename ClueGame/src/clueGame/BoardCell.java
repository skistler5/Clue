/**
 * Author: Shannon Bride and Stephen Kistler
 * 
 * BoardCell class stores row and cell number
 */
package clueGame;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}

	
	//stubs
	public boolean isWalkway(){
		return false;
	}
	
	public boolean isRoom(){
		return false;
	}
	
	public boolean isDoorway(){
		return false;
	}

	
	public Object getDoorDirection() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public char getInitial() {
		return initial;
	}
}
