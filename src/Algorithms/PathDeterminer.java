package Algorithms;

import java.util.HashMap;
import enums.Direction;

import java.util.Map;

import Objects.Robot;
import Physics.Coordinates2D;
import Physics.Movement;
import enums.Direction;

public class PathDeterminer extends Basic{

	private Map<String, CellState> _mentalMap = new HashMap<String, CellState>();
	private Coordinates2D _robotPos;
	private double _distance;
	
	public PathDeterminer()
	{
		_mentalMap = getMentalMap();
	}
	
	public Coordinates2D getNearestNeighbour(int row, int col)
	{
		Coordinates2D[][] robotCoordinates = Robot.getCoordinates(row, col);
		
		//The cell that is covered by the robot in the 2nd row and 2nd col is considered as the middle of the robot
		_robotPos = robotCoordinates[1][1];
		
		//Determine map with free cells
		Map<String, Double> distanceMap = new HashMap<String, Double>();
		
		for(String key : _mentalMap.keySet())
 		{
 			if(_mentalMap.get(key).equals(CellState.FREE))
 			{
 				int cellRow = Integer.parseInt(key.substring(0, 2));
 				int cellCol = Integer.parseInt(key.substring(2, 4));
 				
 				Coordinates2D freeCell = new Coordinates2D(cellRow, cellCol);
 				
 				_distance = calcDistance(_robotPos, freeCell);
 				distanceMap.put(key, _distance);
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
	
	public void moveToNearestNeighbour(Coordinates2D nearestNeighbour)
	{
		double hypotenuse = _distance;
		double gegenkathete = nearestNeighbour.getRow() - _robotPos.getRow();
		double radian = Math.asin(gegenkathete/hypotenuse);
		double angle = radian/Math.PI * 180;
		
		Direction dir = Movement._dir;
		switch(dir) {
		case RIGHT:
		break;
		case DOWN: angle = angle + 270;
		break;
		case LEFT: angle = angle + 180;
		break;
		case UP: angle = angle + 90;
		}
		System.out.println(angle);
		Movement.setAng(Movement.getAng() + angle);
	}


	private double calcDistance(Coordinates2D robotPos, Coordinates2D freeCell) 
	{
		double dist = Math.sqrt((robotPos.getRow() - freeCell.getRow()) * (robotPos.getRow() - freeCell.getRow()) + (robotPos.getCol() - freeCell.getCol()) * (robotPos.getCol() - freeCell.getCol()));
		return dist;
	}
	

//	private double turnByDegrees(double angle, Coordinates2D nearestNeighbour) 
//	{
//		Direction dir = Movement._dir;
//		switch(dir) {
//		case RIGHT: 
//			if(_robotPos.getCol() - nearestNeighbour.getCol() < 0)
//			{
//				return angle;
//			}
//			else if(_robotPos.getCol() - nearestNeighbour.getCol() > 0)
//			{
//				return angle + 180;
//			}
//		}
//	}
	
}
