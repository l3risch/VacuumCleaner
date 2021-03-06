package Algorithms;

/**
 * Parent Class of the CPPAlgorithm class providing the algorithms with methods for scanning of cells and updating the mental map with information about the robot's environment
 */
import java.util.HashMap;
import java.util.Map;

import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;


public class Basic {
	
	public enum ScanDirection{FRONT, LEFT, RIGHT};
	
	public enum CellState{FREE, VISITED, OCCUPIED};
	
	public static Map<String, CellState> _mentalMap = new HashMap<String, CellState>();
	
	protected static char[][] _matrix = new char[64][64];
	
	public static int _revisitedCells;

	protected static int _dijkstraExecutions = 0;
	
	/**
	 * Checks if cells in front of the robot are accesable
	 * @param row
	 * @param col
	 * @return
	 */
	static boolean isFrontAccesable(int row, int col)
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

	/**
	 * Getting the scanned cells by one of the sensors
	 * @param row 	row of current robot position
	 * @param col	col of current robot position
	 * @param dir 	direction of the sensor
	 * @return		scanned cells
	 */
	static Coordinates2D[] getScannedArea(int row, int col, ScanDirection dir) {
		
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
				
				int x = (int) Math.round(Math.cos(Math.toRadians(Robot.getMovement().getAng())));
				int y = (int) Math.round(Math.sin(Math.toRadians(Robot.getMovement().getAng())));

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
			
	
	/**
	 * Getting the scanned cells of all sensors
	 * @param row 	row of current robot position
	 * @param col	col of current robot position
	 * @return		scanned cells
	 */
	public static Coordinates2D[] getEncircledScannedArea(int row, int col) 
	{
		Coordinates2D[] circledArea = new Coordinates2D[12];
		
		Coordinates2D[] front = getScannedArea(row, col, ScanDirection.FRONT);
		Coordinates2D[] right = getScannedArea(row, col, ScanDirection.RIGHT);
		Coordinates2D[] left = getScannedArea(row, col, ScanDirection.LEFT);

		for(int i=0; i<4; i++)
		{
			circledArea[i] = front[i];
			circledArea[i+4] = left[i];
			circledArea[i+8] = right[i];

		}
		
		return circledArea;
		
	}
	
	/**
	 * Getting the scanned cells of a specific direction
	 * @param scannedArea 	encircled scanned area
	 * @param direction 	direction
	 * @return
	 */
	static Coordinates2D[] determineScanDirections(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] front = new Coordinates2D[4];
		Coordinates2D[] left = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];

		for(int i = 0; i < 4; i++)
		{
			front[i] = scannedArea[i];
			left[i] = scannedArea[i+4];
			right[i] = scannedArea[i+8];
		}
		
		Coordinates2D[] scanArea = null;
		
		switch(direction) {
		case FRONT: 
			scanArea = front;
			break;
		case LEFT:
			scanArea = left;
			break;
		case RIGHT:
			scanArea = right;
			break;
		}	
		
		return scanArea;
		
	}
	
	
	/**
	 * Direction is partially free, which means no obstacles, walls and the scanned area is partially covered in this direction
	 * @param scannedArea 	encircled scanned area
	 * @param direction 	direction
	 * @return
	 */
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
	
	/**
	 * Direction is partially covered, which means no obstacles, walls but the scanned area has been partially covered by the robot
	 * @param scannedArea 	encircled scanned area
	 * @param direction 	direction
	 * @return
	 */
	boolean partiallyCoveredDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
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
		
		if(pixels > 3 || pixels <= 0)
		{
			return false;
		} else {
			return true;
		}
		
	}

	
	/**
	 * Robot is completely surrounded by Obstacles and visited Cells
	 * @param scannedArea	encircled scanned area
	 * @return
	 */
	boolean totallyCovered(Coordinates2D[] scannedArea) {
		
		Coordinates2D[] up = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];
		Coordinates2D[] left = new Coordinates2D[4];

		boolean upBlocked = false;
		boolean rightBlocked = false;
		boolean leftBlocked = false;

		int upFields = 0;
		int rightFields = 0;
		int leftFields = 0;

		
		for(int i = 0; i < 4; i++)
		{
			up[i] = scannedArea[i];
			right[i] = scannedArea[i+4];
			left[i] = scannedArea[i+8];

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
		
		if(upBlocked && leftBlocked && rightBlocked)
		{
			return true;
		} else {
			return false;
		}
		
	}
	
	
	/**
	 * Direction is totally free, which means no obstacles, walls and also no visited cells by the robot in this direction
	 * @param scannedArea 	encircled scanned area
	 * @param direction 	direction
	 * @return
	 */
	
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
	
	/**
	 * Direction is free, which means no obstacles or walls in this scanning direction
	 * @param scannedArea 	encircled scanned area
	 * @param direction 	direction
	 * @return
	 */
	static boolean freeDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
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
	
	/**
	 * Updates mental map
	 * @param row 	row of current robot position
	 * @param col	col of current robot position
	 * @param mentalMap
	 */
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
		
		int freeCells = 0;
		
		//Update already visited cells
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{

				String mapKey = generateKey(robotCoordinates[i][j].getRow(), robotCoordinates[i][j].getCol());
				if(mentalMap.get(mapKey)== CellState.FREE)
				{
					freeCells++;
				}
				mentalMap.put(mapKey, CellState.VISITED);

			}

		}
		_revisitedCells += (4-freeCells);
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

	static Map<String, CellState> getMentalMap()
	{
		return _mentalMap;
	}
	
	
	
	protected static char[][] updateMatrix()
	{
		_dijkstraExecutions++;
		 //Transform Hash Map to char matrix
		 for(String key : _mentalMap.keySet())
		 {
			 int row = Integer.parseInt(key.substring(0, 2));
			 int col = Integer.parseInt(key.substring(2, 4));
			 if(row >= 0 && row < 64 && col >= 0 && col < 64)
			 { 
				 if(_mentalMap.get(key).equals(CellState.FREE)) 
				 {
					 _matrix[row][col] = '1';
				 }
				 if(_mentalMap.get(key).equals(CellState.VISITED))
				 {
					 _matrix[row][col] = '1';
				 }
				 if(_mentalMap.get(key).equals(CellState.OCCUPIED))
				 {
					 _matrix[row][col] = '0';
		 		 }
			 }
		 }
		 
		 //Set all unknown cells as obstacles '0'
		 for(int i = 0; i < 64; i++)
		 {
			 for(int j = 0; j < 64; j++)
			 {
				 if(_matrix[i][j] == 0)
				 {
					 _matrix[i][j] = '0';
				 }
			 }		
		 }
		 
		 
		 return _matrix;
	}
	
	private static boolean isElementContainedInArray(Coordinates2D coordinates2d, Coordinates2D[][] oldArrayPosition) 
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

}
