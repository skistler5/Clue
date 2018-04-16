/**
 * Author: Shannon Bride and Stephen Kistler
 * 
 * BoardCell class stores row and cell number
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 * 
 * @author Shannon Bride
 * @author Stephen Kistler
 *
 */
public class BoardCell {
	private int row;
	private int col;
	private int drawRow;
	private int drawCol;
	private char initial;
	public static final int CELL_SIZE = 25;
	private DoorDirection doorDirection;
	boolean door;

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
	
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		drawRow = CELL_SIZE*row;
		drawCol = CELL_SIZE*col;
		if(isDoorway()){
			g2.setColor(Color.BLUE);
			g2.fillRect(drawRow, drawCol, CELL_SIZE, CELL_SIZE);
		}
		else if(isWalkway()){
			g2.setColor(Color.YELLOW);
			g2.drawRect(drawRow, drawCol, CELL_SIZE, CELL_SIZE);
			g2.setColor(Color.BLACK);
			g2.fillRect(drawRow, drawCol, CELL_SIZE - 1, CELL_SIZE - 1);
		}
		else if(isRoom()){
			g2.setColor(Color.GRAY);
			g2.fillRect(drawRow, drawCol, CELL_SIZE, CELL_SIZE);
		}
		else{
			g2.setColor(Color.blue);
			g2.drawRect(drawRow, drawCol, CELL_SIZE, CELL_SIZE);
		}
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
