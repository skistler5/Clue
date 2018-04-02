/**
 * Author: Shannon Bride and Stephen Kistler
 * 
 * BoardCell class stores row and cell number
 */
package clueGame;


/**
 * 
 * @author Shannon Bride
 * @author Stephen Kistler
 *
 */
public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection;
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

	
	
	/**
	 * figure out if it's a walkway
	 * @return
	 */
	public boolean isWalkway(){
		Character c = new Character(getInitial());
		return c.equals('W');
	}
	
	/**
	 * figure out if it's a room
	 * @return
	 */
	public boolean isRoom(){
		Character c = new Character(getInitial());
		return (!c.equals('W') && !door);
	}
	
	/**
	 * figure out if it's a doorway
	 * @return
	 */
	public boolean isDoorway(){
		return door;
	}

	
	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return doorDirection;
	}
	
	public void setDoorWay(boolean b){
		door = b;
	}
	
	public void setDoorDirection(char c){
		if(c == 'U'){
			doorDirection = DoorDirection.UP;
		}
		else if(c == 'D'){
			doorDirection = DoorDirection.DOWN;
		}
		else if(c == 'R'){
			doorDirection = DoorDirection.RIGHT;
		}
		else if(c == 'L'){
			doorDirection = DoorDirection.LEFT;
		}
		else{
			doorDirection = DoorDirection.NONE;
		}
	}
	public char getInitial() {
		return initial;
	}
	
	public void setInitial(char c){
		this.initial = c;
	}
}
