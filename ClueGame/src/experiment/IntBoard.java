package experiment;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class IntBoard {
	private Map<BoardCell, HashSet<BoardCell>> adjMap = new HashMap<BoardCell, HashSet<BoardCell>>();
	private HashSet<BoardCell> targets = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private final int maxr = 4;
	private final int maxc = 4;
	private BoardCell[][] grid = new BoardCell[maxr][maxc];
	
	
	
	public IntBoard() {
		this.calcAdjacencies();
	}


	/**
	 * Creates adjacency map
	 */
	private void calcAdjacencies(){
		for(int i = 0; i < maxr; i++){
			for(int j = 0; j < maxc; j++){
				BoardCell origin = new BoardCell(i,j);
				HashSet<BoardCell> adjs = new HashSet<BoardCell>();
				if (i - 1 >= 0){
					adjs.add(new BoardCell(i-1, j));
				}
				if(i + 1 < maxr){
					adjs.add(new BoardCell(i+1, j));
				}
				if(j-1 >= 0){
					adjs.add(new BoardCell(i, j-1));
				}
				if(j+1 < maxc){
					adjs.add(new BoardCell(i, j+1));
				}
				adjMap.put(origin, adjs);
				grid[i][j] = origin;
			}
		}
		return;
	}
	
	
	/**
	 * Returns a list of adjacent cells to the startCell
	 * @param startCell
	 * @return
	 */
	public HashSet<BoardCell> getAdjList(BoardCell startCell){
		HashSet<BoardCell> ourList = new HashSet<BoardCell>();
		ourList = adjMap.get(startCell);
		return ourList;

	}
	
	/**
	 * Finds all possible target cells from the start cell
	 * @param startCell
	 * @param pathLength
	 * @return
	 */
	public void calcTargets(BoardCell startCell, int pathLength){
		int numSteps = pathLength;
		HashSet<BoardCell> s = getAdjList(startCell);
		for(BoardCell b : s){
			b = getCell(b.getRow(), b.getCol());
			if(!visited.contains(b)){
				visited.add(b);
				if(numSteps == 1){
					targets.add(b);
				}
				else{
					calcTargets(b, numSteps - 1);
				}
				visited.remove(visited.size() - 1);
			}
		}
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public BoardCell getCell(int row, int col){
		BoardCell b1 = null;
		for(int i = 0; i < maxr; i++){
			for(int j = 0; j < maxc; j++){
				if(row == i){
					if(col == j){
						b1 = grid[i][j];
					}
				}
			}
		}
		return b1;
	}
	

}
