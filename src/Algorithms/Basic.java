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
		Coordinates2D[] scannedArea = getScannedFrontArea(row, col);

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

	Coordinates2D[] getScannedFrontArea(int row, int col) {
				
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

				newArrayPosition[i][j].setRow(newArrayPosition[i][j].getRow() + y);
				
				newArrayPosition[i][j].setCol(newArrayPosition[i][j].getCol() + x);
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
		//Get scanned area in front of the robot
		Coordinates2D[] scannedArea = getScannedFrontArea(row, col);
		
		Coordinates2D[] circledArea = new Coordinates2D[16];
		
		//Determine the scanned area encirceling the robot
//		double ang = Movement.getAng();
//		if(ang % 90 == 0)
//		{
			switch(Movement._dir) {
			case DOWN: circledArea = getAreaIfDown(scannedArea);
				break;
			case LEFT: circledArea = getAreaIfLeft(scannedArea);
				break;
			case RIGHT: circledArea = getAreaIfRight(scannedArea);
				break;
			case UP: circledArea = getAreaIfUp(scannedArea);
				break;
			}
//		}
		return circledArea;
		
	}
	
	private Coordinates2D[] getAreaIfUp(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = _scannedArea[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] getAreaIfRight(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = _scannedArea[i];
		}
		
		return circledArea;
	}

	private Coordinates2D[] getAreaIfDown(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = _scannedArea[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] getAreaIfLeft(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = _scannedArea[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] upperSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] upperSide = new Coordinates2D[4];
		int maxRow = 0;
		
		if(dir.equals(Direction.RIGHT))
		{
			maxRow = _scannedArea[3].getRow() - 4;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() - i - 1);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxRow = _scannedArea[3].getRow() - 4;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() + i + 1);
			}
		} else if (dir.equals(Direction.DOWN)) {
			maxRow = _scannedArea[3].getRow() - 5;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol());
			
			}
		}

		return upperSide;			
	}
	
	private Coordinates2D[] lowerSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] lowerSide = new Coordinates2D[4];
		int maxRow = 0;
		
		if(dir.equals(Direction.RIGHT))
		{
			maxRow = _scannedArea[3].getRow() + 1;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() - i - 1);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxRow = _scannedArea[3].getRow() + 1;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() + i + 1);
			}
		} else if (dir.equals(Direction.UP)) {
			maxRow = _scannedArea[3].getRow() + 5;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol());
			}
		}
		
		return lowerSide;			
	}

	private Coordinates2D[] rightSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] rightSide = new Coordinates2D[4];
		int maxCol = 0;
		
		if(dir.equals(Direction.UP))
		{
			maxCol = _scannedArea[3].getCol() + 1;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow() + i + 1, maxCol);
			}
		} else if (dir.equals(Direction.DOWN)){
			maxCol = _scannedArea[3].getCol() + 1;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow() - i - 1, maxCol);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxCol = _scannedArea[0].getCol() + 5;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow(), maxCol);
			}
		}
		
		return rightSide;		
	}

	private Coordinates2D[] leftSensor(Coordinates2D[] _scannedArea, Direction dir) 
    {
		Coordinates2D[] leftSide = new Coordinates2D[4];
		int minCol = 0;
		
		if(dir.equals(Direction.UP))
		{
			minCol = _scannedArea[0].getCol() - 1;
			for(int i = 0; i < 4; i++)
			{
				leftSide[i] = new Coordinates2D(_scannedArea[i].getRow() + i + 1, minCol);
			}
		} else if (dir.equals(Direction.DOWN)){
				minCol = _scannedArea[0].getCol() - 1;
				for(int i = 0; i < 4; i++)
				{
					leftSide[i] = new Coordinates2D(_scannedArea[i].getRow() - i - 1, minCol);
			}
		} else if (dir.equals(Direction.RIGHT)) {
			minCol = _scannedArea[0].getCol() - 5;
			for(int i = 0; i < 4; i++)
			{
				leftSide[i] = new Coordinates2D(_scannedArea[i].getRow(), minCol);
			}
		}

		return leftSide;
    }

	
	Coordinates2D[] determineScanDirections(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] front = new Coordinates2D[4];
		Coordinates2D[] left = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];
		Coordinates2D[] back = new Coordinates2D[4];

		
		switch(Movement._dir) {
		case UP: 
			for(int i = 0; i < 4; i++)
			{
				front[i] = scannedArea[i];
				left[i] = scannedArea[i+4];
				back[i] = scannedArea[i+8];
				right[i] = scannedArea[i+12];
			}
			break;
		case LEFT:
			for(int i = 0; i < 4; i++)
			{
				front[i] = scannedArea[i+4];
				left[i] = scannedArea[i+8];
				back[i] = scannedArea[i+12];
				right[i] = scannedArea[i];
			}
			break;
		case DOWN:
			for(int i = 0; i < 4; i++)
			{
				front[i] = scannedArea[i+8];
				left[i] = scannedArea[i+12];
				back[i] = scannedArea[i];
				right[i] = scannedArea[i+4];
			}
			break;
		case RIGHT:
			for(int i = 0; i < 4; i++)
			{
				front[i] = scannedArea[i+12];
				left[i] = scannedArea[i];
				back[i] = scannedArea[i+4];
				right[i] = scannedArea[i+8];
			}
			break;
		}
		
		Coordinates2D[] scanArea = new Coordinates2D[4];
		switch(direction)
		{
		case LEFT: scanArea = left;
			break;
		case BACK: scanArea = back;
			break;
		case RIGHT: scanArea = right;
			break;
		case FRONT: scanArea = front;
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
	boolean totallyCovered(int row, int col, Coordinates2D[] scannedArea) {
		
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

//		for(int i = 0; i < 4; i++)
//		{
//			for(int j = 0; j < 4; j++)
//			{
//				System.out.println(Robot.getCoordinates(row, col)[i][j].getRow() +", "+Robot.getCoordinates(row, col)[i][j].getCol());
//			}
//		}
		
		for(int i = 0; i < 4; i++)
		{
			up[i] = scannedArea[i];
			right[i] = scannedArea[i+4];
			down[i] = scannedArea[i+8];
			left[i] = scannedArea[i+12];

			//System.out.println(up[i].getRow()+", "+ up[i].getCol()+" | " +right[i].getRow()+", "+ right[i].getCol()+" | " +down[i].getRow()+", "+ down[i].getCol()+" | " +left[i].getRow()+", "+ left[i].getCol()+"\n");
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
	void updateMap(int row, int col)
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
					_mentalMap.put(mapKey, CellState.OCCUPIED);
				} else if(Table.getPath(encircledArea[i].getRow(), encircledArea[i].getCol()))
				{
					_mentalMap.put(mapKey, CellState.VISITED);
				} else {
					_mentalMap.put(mapKey, CellState.FREE);
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
				_mentalMap.put(mapKey, CellState.VISITED);
	
			}
		}
		
		int free = 0;
		int occupied = 0;
		int visited = 0;
		
 		for(String key : _mentalMap.keySet())
 		{
 			if(_mentalMap.get(key).equals(CellState.FREE))
 			{
 				free++;
 			}
 			if(_mentalMap.get(key).equals(CellState.OCCUPIED))
 			{
 				occupied++;
 			}
 			if(_mentalMap.get(key).equals(CellState.VISITED))
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
