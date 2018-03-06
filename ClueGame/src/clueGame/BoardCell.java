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
		return false;
	}
	
	public boolean isRoom(){
		return false;
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
