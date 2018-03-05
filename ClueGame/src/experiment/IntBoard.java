/**
 * Author: Shannon Bride and Stephen Kistler
 * 
 * IntBoard Class
 * fixed code
 */
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
	private BoardCell[][] grid;



	public IntBoard() {
		grid = new BoardCell[maxr][];
		for(int i = 0; i < maxr; i++){
			grid[i] = new BoardCell[maxc];
			for(int j = 0; j < maxc; j++){
				grid[i][j] = new BoardCell(i,j);
			}
		}
		visited.clear();
		targets.clear();
		calcAdjacencies();
	}


	/**
	 * Creates adjacency map
	 */
	private void calcAdjacencies(){
		for(int i = 0; i < maxr; i++){
			for(int j = 0; j < maxc; j++){
				HashSet<BoardCell> adjs = new HashSet<BoardCell>();
				if (i - 1 >= 0){
					adjs.add(grid[i-1][j]);
				}
				if(i + 1 < maxr){
					adjs.add(grid[i+1][j]);
				}
				if(j-1 >= 0){
					adjs.add(grid[i][j-1]);
				}
				if(j+1 < maxc){
					adjs.add(grid[i][j+1]);
				}
				adjMap.put(grid[i][j], adjs);
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
		return adjMap.get(startCell);
	}

	/**
	 * Finds all possible target cells from the start cell
	 * @param startCell
	 * @param pathLength
	 * @return
	 */
	public void calcTargets(BoardCell startCell, int pathLength){
		if(!visited.contains(startCell)){
			visited.add(startCell);
		}
		for(BoardCell b : getAdjList(startCell)){
			if(visited.contains(b)){
				continue;
			}
			visited.add(b);
			if(pathLength == 1){
				targets.add(b);
			}
			else{
				calcTargets(b, pathLength - 1);
			}
			visited.remove(b);
		}
	}

	public Set<BoardCell> getTargets(){
		return targets;
	}

	public BoardCell getCell(int row, int col){
		return grid[row][col];
	}
<<<<<<< HEAD
}
=======
}
>>>>>>> 11e074dfd88b341d05d8be7fb29db1740659b868
