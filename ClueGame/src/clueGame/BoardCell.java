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
	private DoorDirection dd;
	boolean door;
	boolean room;
	
	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public BoardCell(int row, int col) {
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
		Character c = new Character(getInitial());
		return c.equals('W');
	}
	
	public boolean isRoom(){
		Character c = new Character(getInitial());
		return (!c.equals('W') && !door);
	}
	
	public boolean isDoorway(){
		return door;
	}

	
	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return dd;
	}
	
	public void setDoorWay(boolean b){
		door = b;
	}
	
	public void setDoorDirection(char c){
		if(c == 'U'){
			dd = DoorDirection.UP;
		}
		else if(c == 'D'){
			dd = DoorDirection.DOWN;
		}
		else if(c == 'R'){
			dd = DoorDirection.RIGHT;
		}
		else if(c == 'L'){
			dd = DoorDirection.LEFT;
		}
		else{
			dd = DoorDirection.NONE;
		}
	}
	public char getInitial() {
		return initial;
	}
	
	public void setInitial(char c){
		this.initial = c;
	}
}
