package Algorithms;

import java.util.HashMap;
import enums.Direction;

import java.util.Map;

import Objects.Robot;
import Physics.Coordinates2D;
import Physics.Movement;

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
		double gegenkathete;
		double radian;
		double angle = 0;
		
		double currentAngle = Movement.getAng();
		double normedAngle = currentAngle % 360;

		if(normedAngle < 180)
		{
			gegenkathete = nearestNeighbour.getRow() - _robotPos.getRow();

			//If target cell is right
			if(nearestNeighbour.getCol() - _robotPos.getCol() > 0)
			{
				radian = Math.asin(gegenkathete/hypotenuse);
				angle = radian/Math.PI * 180;
				
			//If target cell is left
			} else 
			{
				radian = Math.asin(gegenkathete/hypotenuse);
				angle = (radian/Math.PI * 180) + 180;
			}	
		} else if(normedAngle >= 180)
		{
			gegenkathete = _robotPos.getRow() - nearestNeighbour.getRow();

			//If target cell is right
			if(nearestNeighbour.getCol() - _robotPos.getCol() > 0)
			{
				radian = Math.asin(gegenkathete/hypotenuse);
				angle = radian/Math.PI * 180;
				
			//If target cell is left
			} else
			{
				radian = Math.asin(gegenkathete/hypotenuse);
				angle = radian/Math.PI * 180;
			}
		}
		
		Movement.setAng(Movement.getAng() + angle);

		
//		//If target cell below and left
//		if(nearestNeighbour.getCol() - _robotPos.getCol() < 0 && nearestNeighbour.getRow() - _robotPos.getRow() > 0)
//		{
//			Direction dir = Movement._dir;
//			
//			switch(dir) {
//			case RIGHT: angle = angle * (-1) + 180;
//			break;
//			case DOWN: angle = angle * (-1) + 90;
//			break;
//			case LEFT: angle = angle * (-1);
//			break;
//			case UP: angle = angle * (-1) + 270;
//			}
//			
//			//If target cell below and right
//		} else if(nearestNeighbour.getCol() - _robotPos.getCol() > 0 && nearestNeighbour.getRow() - _robotPos.getRow() > 0)
//		{
//			Direction dir = Movement._dir;
//			
//			switch(dir) {
//			case RIGHT:
//			break;
//			case DOWN: angle = angle + 270;
//			break;
//			case LEFT: angle = angle + 180;
//			break;
//			case UP: angle = angle + 90;
//			}
//		
//			//If target cell above and right
//		} else if(nearestNeighbour.getCol() - _robotPos.getCol() > 0 && nearestNeighbour.getRow() - _robotPos.getRow() < 0)
//		{
//			Direction dir = Movement._dir;
//			
//			switch(dir) {
//			case RIGHT: angle = angle * (-1);
//			break;
//			case DOWN: angle = angle * (-1) + 270;
//			break;
//			case LEFT: angle = angle * (-1) + 180;
//			break;
//			case UP: angle = angle * (-1) + 90;
//			}
//			
//			//If target cell above and left
//		} else if(nearestNeighbour.getCol() - _robotPos.getCol() < 0 && nearestNeighbour.getRow() - _robotPos.getRow() < 0)
//		{
//			Direction dir = Movement._dir;
//			
//			switch(dir) {
//			case RIGHT: angle = angle + 180;
//			break;
//			case DOWN: angle = angle + 90;
//			break;
//			case LEFT: 
//			break;
//			case UP: angle = angle + 270;
//			}
//			
//		}
//		System.out.println(angle);
//		Movement.setAng(Movement.getAng() + angle);
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
