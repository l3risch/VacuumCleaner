package Algorithms;

import java.util.HashMap;
import java.util.Map;

import Algorithms.Basic.CellState;
import Objects.Robot;
import Physics.Coordinates2D;

public class PathDeterminer extends Basic{

	private Map<String, CellState> _mentalMap = new HashMap<String, CellState>();
	
	public PathDeterminer()
	{
		_mentalMap = getMentalMap();
	}
	
	public Coordinates2D getNearestNeighbour(int row, int col)
	{
		Coordinates2D[][] robotCoordinates = Robot.getCoordinates(row, col);
		
		//The cell that is covered by the robot in the 2nd row and 2nd col is considered as the middle of the robot
		Coordinates2D robotPos = robotCoordinates[1][1];
		
		//Determine map with free cells
		int counter = 0;		
		Map<String, Double> distanceMap = new HashMap<String, Double>();
		
		for(String key : _mentalMap.keySet())
 		{
 			if(_mentalMap.get(key).equals(CellState.FREE))
 			{
 				int cellRow = Integer.parseInt(key.substring(0, 2));
 				int cellCol = Integer.parseInt(key.substring(2, 4));
 				
 				Coordinates2D freeCell = new Coordinates2D(cellRow, cellCol);
 				
 				double distance = calcDistance(robotPos, freeCell);
 				distanceMap.put(key, distance);
 				counter++;
 			}		
 		}
		
		//Determine nearest Cell
		double upperBound = 999999;
		int nearestCellRow = 0;
		int nearestCellCol = 0;
		
		for(String key : distanceMap.keySet())
 		{
			if(distanceMap.get(key) < upperBound)
			{
				upperBound = distanceMap.get(key);
				nearestCellRow = Integer.parseInt(key.substring(0, 2));
				nearestCellCol = Integer.parseInt(key.substring(2, 4));
			}
 		}
		
		Coordinates2D nearestCell = new Coordinates2D(nearestCellRow, nearestCellCol);
		
		return nearestCell;
	}

	private double calcDistance(Coordinates2D robotPos, Coordinates2D freeCell) 
	{
		double dist = Math.sqrt((robotPos.getRow() - freeCell.getRow()) * (robotPos.getRow() - freeCell.getRow()) + (robotPos.getCol() - freeCell.getCol()) * (robotPos.getCol() - freeCell.getCol()));
		return dist;
	}
}
