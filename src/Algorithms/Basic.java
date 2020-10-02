package Algorithms;

import java.util.HashMap;

import java.util.Map;

import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import enums.Direction;

public class Basic {
	
	public enum ScanDirection{FRONT, LEFT, BACK, RIGHT};
	
	public enum CellState{FREE, VISITED, OCCUPIED};
	
	public static Map<String, CellState> _mentalMap = new HashMap<String, CellState>();
	
	boolean isFrontAccesable(int row, int col)
	{
		Coordinates2D[] scannedArea = getScannedArea(row, col, ScanDirection.FRONT);

		boolean accesable = true;
		
		for(int i = 0; i < scannedArea.length; i++)
		try {

			if(Table.getMarkedObstacles(scannedArea[i].getRow(), scannedArea[i].getCol()))
			{
				accesable = false;
			}

		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			accesable = false;
		}
		
		return accesable;
	}

	
	Coordinates2D[] getScannedArea(int row, int col, ScanDirection dir) {
		
		Coordinates2D[][] oldArrayPosition = new Coordinates2D[4][4];
		oldArrayPosition = Robot.getCoordinates(row, col);

		Coordinates2D[][] newArrayPosition = new Coordinates2D[4][4];
								
		//Set new ArrayPosition of Robot based on Angle
		for(int i = 0; i <4; i++)
		{
			for(int j = 0; j <4; j++)
			{
				//Set visited path
				int robotRow = Robot.getCoordinates(row, col)[i][j].getRow();
				int robotCol = Robot.getCoordinates(row, col)[i][j].getCol();

				if(robotRow >= 0 && robotRow < 64 && robotCol >= 0 && robotCol < 64)
				{
					Table.markPath(robotRow, robotCol);
				}
				//Copy old array into new array
				newArrayPosition[i][j] = Coordinates2D.copyOf(oldArrayPosition[i][j]);
				
				int x = (int) Math.round(Math.cos(Math.toRadians(Movement._ang)));
				int y = (int) Math.round(Math.sin(Math.toRadians(Movement._ang)));

				if(dir == ScanDirection.FRONT)
				{
					newArrayPosition[i][j].setRow(newArrayPosition[i][j].getRow() + y);
					newArrayPosition[i][j].setCol(newArrayPosition[i][j].getCol() + x);
				} else if (dir == ScanDirection.RIGHT)
				{
					newArrayPosition[i][j].setRow(newArrayPosition[i][j].getRow() + x);
					newArrayPosition[i][j].setCol(newArrayPosition[i][j].getCol() - y);
				} else if (dir == ScanDirection.LEFT)
				{
					newArrayPosition[i][j].setRow(newArrayPosition[i][j].getRow() - x);
					newArrayPosition[i][j].setCol(newArrayPosition[i][j].getCol() + y);
				} else if (dir == ScanDirection.BACK)
				{
					newArrayPosition[i][j].setRow(newArrayPosition[i][j].getRow() - y);
					newArrayPosition[i][j].setCol(newArrayPosition[i][j].getCol() - x);
				}
			}
		}
			
		//Set simply size of the Area Array to be scanned (var: scannedArea)
		int size = 0;
		for(int i = 0; i <4; i++)
		{
			for(int j = 0; j <4; j++)
			{
				if(!isElementContainedInArray(newArrayPosition[i][j], oldArrayPosition))
				{
					size += 1; 
				}
			}
		}	

		Coordinates2D[] scannedArea = new Coordinates2D[size];
		
		//Determine the area in front of the robot to be scanned
		int position = 0;
		for(int i = 0; i <4; i++)
		{
			for(int j = 0; j <4; j++)
			{				
				if(!isElementContainedInArray(newArrayPosition[i][j], oldArrayPosition))
				{
					scannedArea[position] = newArrayPosition[i][j];
					position += 1;
				}
			}
		}

			
			return scannedArea;
	}
			
		

	
	private boolean isElementContainedInArray(Coordinates2D coordinates2d, Coordinates2D[][] oldArrayPosition) 
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if((coordinates2d.getRow() == oldArrayPosition[i][j].getRow()) && (coordinates2d.getCol() == oldArrayPosition[i][j].getCol()))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Coordinates2D[] getEncircledScannedArea(int row, int col) 
	{
		Coordinates2D[] circledArea = new Coordinates2D[16];
		
		Coordinates2D[] front = getScannedArea(row, col, ScanDirection.FRONT);
		Coordinates2D[] right = getScannedArea(row, col, ScanDirection.RIGHT);
		Coordinates2D[] back = getScannedArea(row, col, ScanDirection.BACK);
		Coordinates2D[] left = getScannedArea(row, col, ScanDirection.LEFT);

		for(int i=0; i<4; i++)
		{
			circledArea[i] = front[i];
			circledArea[i+4] = left[i];
			circledArea[i+8] = back[i];
			circledArea[i+12] = right[i];

		}
		
		return circledArea;
		
	}
	
	
	Coordinates2D[] determineScanDirections(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] front = new Coordinates2D[4];
		Coordinates2D[] left = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];
		Coordinates2D[] back = new Coordinates2D[4];

		for(int i = 0; i < 4; i++)
		{
			front[i] = scannedArea[i];
			left[i] = scannedArea[i+4];
			back[i] = scannedArea[i+8];
			right[i] = scannedArea[i+12];
		}
		
		Coordinates2D[] scanArea = null;
		
		switch(direction) {
		case FRONT: 
			scanArea = front;
			break;
		case LEFT:
			scanArea = left;
			break;
		case BACK:
			scanArea = back;
			break;
		case RIGHT:
			scanArea = right;
			break;
		}	
		
		return scanArea;
		
	}
	
	
	//Direction is covered, which means no obstacles or walls but parts of the scanned area are covered in this direction
	boolean partiallyCoveredPathInDirection(Coordinates2D[] scannedArea, ScanDirection direction)
	{
		Coordinates2D[] scanArea = determineScanDirections(scannedArea, direction);

		boolean pathCovered = false;
		
		for(int i = 0; i < scanArea.length; i++)
		{
			try {
				if(Table.getMarkedObstacles(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				} else if(Table.getPath(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					pathCovered = true;
				}
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
				pathCovered = false;
			}
		}
		return pathCovered;
			
	}
	
	
	//Direction is partially free, which means no obstacles, walls and the scanned area is partially covered in this direction
	boolean partiallyFreeDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] scanArea = determineScanDirections(scannedArea, direction);
		
		int pixels = 0;

		for(int i = 0; i < scanArea.length; i++)
		{
			try {
				if(Table.getMarkedObstacles(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				}
				
				if(Table.getPath(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					pixels += 1;
				}
				
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
				return false;
			}
		}
		
		if(pixels > 3)
		{
			return false;
		} else {
			return true;
		}
		
	}

	
	//Robot is completely surrounded by Obstacles and visited Cells
	boolean totallyCovered(Coordinates2D[] scannedArea) {
		
		Coordinates2D[] up = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];
		Coordinates2D[] down = new Coordinates2D[4];
		Coordinates2D[] left = new Coordinates2D[4];

		boolean upBlocked = false;
		boolean rightBlocked = false;
		boolean belowBlocked = false;
		boolean leftBlocked = false;

		int upFields = 0;
		int rightFields = 0;
		int belowFields = 0;
		int leftFields = 0;

		
		for(int i = 0; i < 4; i++)
		{
			up[i] = scannedArea[i];
			right[i] = scannedArea[i+4];
			down[i] = scannedArea[i+8];
			left[i] = scannedArea[i+12];

			if(up[i].getRow() > 63 || up[i].getCol() > 63 || up[i].getRow() < 0 || up[i].getCol() < 0)
			{
				upBlocked = true;
			} else if(Table.getMarkedObstacles(up[i].getRow(), up[i].getCol()))
			{
				upBlocked = true;
			} else if(Table.getPath(up[i].getRow(), up[i].getCol()))
			{
				upFields += 1;
			}
			if(upFields > 3)
			{
				upBlocked = true;
			}
			
			
			if(right[i].getRow() > 63 || right[i].getCol() > 63 || right[i].getRow() < 0 || right[i].getCol() < 0)
			{
				rightBlocked = true;
			} else if(Table.getMarkedObstacles(right[i].getRow(), right[i].getCol()))
			{
				rightBlocked = true;
			} else if(Table.getPath(right[i].getRow(), right[i].getCol()))
			{
				rightFields += 1;
			}
			if(rightFields > 3)
			{
				rightBlocked = true;
			}
			
			
			if(down[i].getRow() > 63 || down[i].getCol() > 63 || down[i].getRow() < 0 || down[i].getCol() < 0)
			{
				belowBlocked = true;
			} else if(Table.getMarkedObstacles(down[i].getRow(), down[i].getCol()))
			{
				belowBlocked = true;
			} else if(Table.getPath(down[i].getRow(), down[i].getCol()))
			{
				belowFields += 1;
			}
			if(belowFields > 3)
			{
				belowBlocked = true;
			}
			
			
			if(left[i].getRow() > 63 || left[i].getCol() > 63 || left[i].getRow() < 0 || left[i].getCol() < 0)
			{
				leftBlocked = true;
			} else if(Table.getMarkedObstacles(left[i].getRow(), left[i].getCol()))
			{
				leftBlocked = true;
			} else if(Table.getPath(left[i].getRow(), left[i].getCol()))
			{
				leftFields += 1;
			}
			if(leftFields > 3)
			{
				leftBlocked = true;
			}
		}
		
		if(upBlocked && leftBlocked && rightBlocked && belowBlocked)
		{
			return true;
		} else {
			return false;
		}
		
	}
	
	
	//Direction is totaly free, which means no obstacles, walls and also no coverage of the scanned area in this direction
	boolean totallyFreeDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] scanArea = determineScanDirections(scannedArea, direction);
		
		for(int i = 0; i < scanArea.length; i++)
		{
			try {
				if(Table.getMarkedObstacles(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				}
				
				if(Table.getPath(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				}
				
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
				return false;
			}
		}
		return true;
	}
	
	//Direction is free, which means no obstacles or walls in this scanning direction
	boolean freeDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] scanArea = determineScanDirections(scannedArea, direction);
		
		for(int i = 0; i < scanArea.length; i++)
		{
			try {
				if(Table.getMarkedObstacles(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				}
				
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
				return false;
			}
		}
		return true;
	}
	
	//Updates mental map
	void updateMap(int row, int col, Map<String, CellState> mentalMap)
	{
		Coordinates2D[][] robotCoordinates = Robot.getCoordinates(row, col);
		
		//Update free and occupied cells
		Coordinates2D[] encircledArea = getEncircledScannedArea(row, col);
		for(int i = 0; i < encircledArea.length; i++)
		{
			String mapKey = generateKey(encircledArea[i].getRow(), encircledArea[i].getCol());

			try {
				if(Table.getMarkedObstacles(encircledArea[i].getRow(), encircledArea[i].getCol()))
				{
					mentalMap.put(mapKey, CellState.OCCUPIED);
				} else if(Table.getPath(encircledArea[i].getRow(), encircledArea[i].getCol()))
				{
					mentalMap.put(mapKey, CellState.VISITED);
				} else {
					mentalMap.put(mapKey, CellState.FREE);
				}
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
			}
		}
		
		//Update already visited cells
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				
				String mapKey = generateKey(robotCoordinates[i][j].getRow(), robotCoordinates[i][j].getCol());
				mentalMap.put(mapKey, CellState.VISITED);
	
			}
		}
		
		int free = 0;
		int occupied = 0;
		int visited = 0;
		
 		for(String key : mentalMap.keySet())
 		{
 			if(mentalMap.get(key).equals(CellState.FREE))
 			{
 				free++;
 			}
 			if(mentalMap.get(key).equals(CellState.OCCUPIED))
 			{
 				occupied++;
 			}
 			if(mentalMap.get(key).equals(CellState.VISITED))
 			{
 				visited++;
 			}
 		}
//		System.out.println("Free cells: " + free);
//		System.out.println("Occ cells: " + occupied);
//		System.out.println("Visited cells: " + visited);



	}

	
	protected static String generateKey(int row, int col) 
	{
		String rowStr;
		if(row >= 0 && row < 10)
		{
			rowStr = "0" + (row);
		} else {
			rowStr = "" + (row);
		}
		
		String colStr;
		if(col >= 0 && col < 10)
		{
			colStr = "0" + (col);
		} else {
			colStr = "" + (col);
		}
		
		String key = rowStr + colStr;
		return key;
	}

	Map<String, CellState> getMentalMap()
	{
		return _mentalMap;
	}

}
