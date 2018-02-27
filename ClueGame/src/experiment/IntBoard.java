package experiment;

import java.awt.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	private Map<BoardCell, Set<BoardCell>> adjMap;

	private void calcAdjacencies(){
		int maxr = 4;
		int maxc = 4;
		for(int i = 0; i < maxr; i++){
			for(int j = 0; j < maxc; j++){
				BoardCell origin = new BoardCell(i,j);
				Set<BoardCell> adjs = null;
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
					adjs.add(new BoardCell(i,j+1));
				}
				adjMap.put(origin, adjs);
			}
		}
		return;
	}
	
	private ArrayList<BoardCell> getAdjList(BoardCell startCell){
		ArrayList<BoardCell> ourList = new ArrayList<BoardCell>();
		for(BoardCell temp : adjMap.get(startCell)){
			ourList.add(temp);
		}
		return ourList;

	}
	
	private Set<BoardCell> calcTargets(BoardCell startCell, int pathLength){
		Set<BoardCell> visited = null;
		Set<BoardCell> targets = null;
		BoardCell[][] grid;
		int numSteps = pathLength;
		for(int i = 0; i < getAdjList(startCell).size(); i++){
			if(!visited.contains(getAdjList(startCell).get(i))){
				visited.add(getAdjList(startCell).get(i));
				if(numSteps == 1){
					targets.add(getAdjList(startCell).get(i));
				}
				else{
					calcTargets(getAdjList(startCell).get(i), numSteps - 1);
				}
			}
		}
	}




}
