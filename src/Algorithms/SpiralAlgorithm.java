package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import Algorithms.Basic.ScanDirection;
import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import Physics.Sweeper;
import Rendering.Renderer1;
import main.MainFrame;
import enums.Direction;


public class SpiralAlgorithm extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();



	public SpiralAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
		

		boolean accesableField;

		accesableField = isFrontAccesable(_actualRow, _actualCol);
		Coordinates2D nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
		System.out.println(nearestNeighbour.getRow() + ", " +  nearestNeighbour.getCol());
		
		if(!accesableField)
		{
			
			//Check if dead end
			if(!totallyFreeDirection(_encircledArea, ScanDirection.LEFT)
					&& !totallyFreeDirection(_encircledArea, ScanDirection.BACK)
					&& !totallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
			{
				
				//Check if front of robot is covered path
				if(partiallyCoveredPathInDircetion(_encircledArea, ScanDirection.FRONT))
				{
					Robot.getMovement().moveForward();
					
					//Check if area left of robot is partially covered and free to access
					if(partiallyFreeDirection(_encircledArea, ScanDirection.LEFT))
					{
						StartAlgorithm._timer.stop();
						Robot.getMovement().turnLeft();
						StartAlgorithm._timer.start();	
						Robot.getMovement().moveForward();
					}
					
					//Check if area right of robot is partially covered and free to access
					if(partiallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
					{
						StartAlgorithm._timer.stop();
						Robot.getMovement().turnRight();
						StartAlgorithm._timer.start();	
						Robot.getMovement().moveForward();
					}
					
					//If every direction around the robot is covered got to nearest uncovered Cell
					if(totallyCovered(_encircledArea))
					{
							StartAlgorithm._timer.stop();
							_pathDeterminer.moveToNearestNeighbour(nearestNeighbour);
							StartAlgorithm._timer.start();	
						
							Robot.getMovement().moveForward();

					}
					
					
				} else {
					//Turn around
					StartAlgorithm._timer.stop();
					Robot.getMovement().turnRight();
					Robot.getMovement().turnRight();
					StartAlgorithm._timer.start();		
				}
				
			} else {
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnRight();
				StartAlgorithm._timer.start();
			}
		} else {
			if(totallyFreeDirection(_encircledArea, ScanDirection.LEFT))
			{
				Robot.getMovement().turnLeft();
			}
			Robot.getMovement().moveForward();
		}
		
		_frame.repaint();
		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol);
		}
	}	
	
	@Override
	boolean isFrontAccesable(int row, int col)
	{
		Coordinates2D[] scannedArea = getEncircledScannedArea(row, col);
		Coordinates2D[] frontOfRobot = determineScanDirections(scannedArea, ScanDirection.FRONT);

		boolean accesable = true;
		
		for(int i = 0; i < frontOfRobot.length; i++)
		try {

			if(Table.getMarkedObstacles(frontOfRobot[i].getRow(), frontOfRobot[i].getCol()))
			{
				accesable = false;
			}
			
			if(Table._markedPath[frontOfRobot[i].getRow()][frontOfRobot[i].getCol()])
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

    
	
	private boolean reachedStoppingCriteria() {
		return false;
	}
	
	public static PathDeterminer getPathDeterminer()
	{
		return _pathDeterminer;
	}
}
