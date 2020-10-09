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
	
	public void turnToNearestNeighbour(Coordinates2D nearestNeighbour)
	{
		double hypotenuse = _distance;
		double gegenkathete;
		double radian;
		double angle = Movement.getAng();
		
		double currentAngle = Movement.getAng();
		double normedAngle = getNormedAngle(currentAngle);
		int rowDistance;
		int colDistance;

		
		rowDistance = _robotPos.getRow() - nearestNeighbour.getRow();
		colDistance = nearestNeighbour.getCol() - _robotPos.getCol();

		//If target cell is in 90 degree area above
		if(rowDistance >= 0 && Math.abs(rowDistance) > Math.abs(colDistance))
		{
			gegenkathete = nearestNeighbour.getCol() - _robotPos.getCol();

			radian = Math.asin(gegenkathete/hypotenuse);
			angle = 270 + radian/Math.PI * 180;
			
		//If target cell is in 90 degree area right
		} else if(colDistance >= 0 && Math.abs(colDistance) > Math.abs(rowDistance))
		{
			gegenkathete =  nearestNeighbour.getRow() - _robotPos.getRow();

			radian = Math.asin(gegenkathete/hypotenuse);
			angle = radian/Math.PI * 180 ;
				
		//If target cell is in 90 degree area below	
		} else if(rowDistance < 0 && Math.abs(rowDistance) > Math.abs(colDistance))
		{
			gegenkathete =  _robotPos.getCol() - nearestNeighbour.getCol();

			radian = Math.asin(gegenkathete/hypotenuse);
			angle = 90 + radian/Math.PI * 180;
			
		//If target cell 90 is indegree area left	
		} else if(colDistance < 0 && Math.abs(colDistance) > Math.abs(rowDistance))
		{
			gegenkathete =  _robotPos.getRow() - nearestNeighbour.getRow();

			radian = Math.asin(gegenkathete/hypotenuse);
			angle = 180 + radian/Math.PI * 180 ;
				
		}
		
	
		Movement.setAng(angle);
	}


	private double getNormedAngle(double currentAngle) 
	{		
		if(currentAngle < 0)
		{
			currentAngle = Math.abs(currentAngle);
			currentAngle += 180;
		}
			
		return currentAngle % 360;
	}

	private double calcDistance(Coordinates2D robotPos, Coordinates2D freeCell) 
	{
		double dist = Math.sqrt((robotPos.getRow() - freeCell.getRow()) * (robotPos.getRow() - freeCell.getRow()) + (robotPos.getCol() - freeCell.getCol()) * (robotPos.getCol() - freeCell.getCol()));
		return dist;
	}
	

	
}
