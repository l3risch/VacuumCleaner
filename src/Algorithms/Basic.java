package Algorithms;

import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;

public class Basic {
	
	boolean getSensorData(int row, int col) {

		Coordinates2D[] scannedArea = getScannedArea(row, col);

		boolean unaccesable = false;
		
		for(int i = 0; i < scannedArea.length; i++)
		try {

			if(Table.getMarkedObstacles(scannedArea[i].getRow(), scannedArea[i].getCol()))
			{
				unaccesable = true;
			} 
			
		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			unaccesable = true;
		}
		
		return unaccesable;
	}

	Coordinates2D[] getScannedArea(int row, int col) {
		
		Coordinates2D[][] oldArrayPosition = new Coordinates2D[4][4];
		oldArrayPosition = Robot.getCoordinates(row, col);

		Coordinates2D[][] newArrayPosition = new Coordinates2D[4][4];
								
		//Set new ArrayPosition of Robot based on Angle
		for(int i = 0; i <4; i++)
		{
			for(int j = 0; j <4; j++)
			{
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
}
