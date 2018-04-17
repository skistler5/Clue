/**
 * Author: Shannon Bride and Stephen Kistler
 * 
 * BoardCell class stores row and cell number
 */
package clueGame;

import java.awt.Color;
import java.awt.Font;
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
		drawRow = CELL_SIZE*row;
		drawCol = CELL_SIZE*col;
		if(isDoorway()){
			g.setColor(Color.BLUE);
			if(doorDirection == DoorDirection.LEFT){
				g.drawLine(drawCol, drawRow, drawCol, drawRow + CELL_SIZE - 1);
				g.drawLine(drawCol + 1, drawRow, drawCol + 1, drawRow + CELL_SIZE - 1);
				g.drawLine(drawCol + 2, drawRow, drawCol + 2, drawRow + CELL_SIZE - 1);
			}
			if(doorDirection == DoorDirection.DOWN){
				g.drawLine(drawCol, drawRow + CELL_SIZE - 1, drawCol + CELL_SIZE - 1, drawRow + CELL_SIZE - 1);
				g.drawLine(drawCol, drawRow + CELL_SIZE - 2, drawCol + CELL_SIZE - 1, drawRow + CELL_SIZE - 2);
				g.drawLine(drawCol, drawRow + CELL_SIZE - 3, drawCol + CELL_SIZE - 1, drawRow + CELL_SIZE - 3);
			}
			if(doorDirection == DoorDirection.UP){
				g.drawLine(drawCol, drawRow, drawCol + CELL_SIZE - 1, drawRow);
				g.drawLine(drawCol, drawRow + 1, drawCol + CELL_SIZE - 1, drawRow + 1);
				g.drawLine(drawCol, drawRow + 2, drawCol + CELL_SIZE - 1, drawRow + 2);
			}
			if(doorDirection == DoorDirection.RIGHT){
				g.drawLine(drawCol + CELL_SIZE - 1, drawRow, drawCol + CELL_SIZE - 1, drawRow + CELL_SIZE - 1);
				g.drawLine(drawCol + CELL_SIZE - 2, drawRow, drawCol + CELL_SIZE - 2, drawRow + CELL_SIZE - 1);
				g.drawLine(drawCol + CELL_SIZE - 3, drawRow, drawCol + CELL_SIZE - 3, drawRow + CELL_SIZE - 1);
			}
		}
		else if(isWalkway()){
			g.setColor(Color.BLACK);
			g.drawRect(drawCol, drawRow, CELL_SIZE, CELL_SIZE);
			g.setColor(Color.YELLOW);
			g.fillRect(drawCol - 1, drawRow - 1, CELL_SIZE - 2, CELL_SIZE - 2);
		}
		else if(isRoom()){
			g.setColor(Color.GRAY);
			g.fillRect(drawCol, drawRow, CELL_SIZE, CELL_SIZE);
		}
		else{
			g.setColor(Color.BLUE);
			g.drawRect(drawCol, drawRow, CELL_SIZE, CELL_SIZE);
		}

	}
	
	public void drawTargets(Graphics g){
		g.setColor(Color.BLACK);
		g.drawRect(drawCol, drawRow, CELL_SIZE, CELL_SIZE);
		g.setColor(Color.CYAN);
		g.fillRect(drawCol - 1, drawRow - 1, CELL_SIZE - 2, CELL_SIZE - 2);
	}

	public void drawName(Graphics g, Board board) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
//		if (board.centers.contains(this)) { //If this RoomCell is a center label			
//			g.drawString(board.getRooms().get(initial).toUpperCase(), col * CELL_SIZE, row * CELL_SIZE);
//		}
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
