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


public class Spiral2 extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();



	public Spiral2(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
		

		boolean accesableField;

		Coordinates2D nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
		
		if(Movement.getAng()%90 != 0)
		{
			accesableField = super.isFrontAccesable(_actualRow, _actualCol);
			if(!accesableField)
			{
				StartAlgorithm._timer.stop();
				Movement.setAng(Movement.getAng() + Movement.getAng()%90);
				StartAlgorithm._timer.start();	
				
			}
		}
		
		accesableField = isFrontAccesable(_actualRow, _actualCol);

		if(!accesableField)
		{
			
			//Check if dead end
			if(!totallyFreeDirection(_encircledArea, ScanDirection.LEFT)
					&& !totallyFreeDirection(_encircledArea, ScanDirection.BACK)
					&& !totallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
			{
				
				//Check if front of robot is covered path
				if(partiallyCoveredPathInDirection(_encircledArea, ScanDirection.FRONT))
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
							System.out.println("Searching for Nearest Neighbour...");
							_pathDeterminer.turnToNearestNeighbour(nearestNeighbour);
							StartAlgorithm._timer.start();	
							StartAlgorithm._timer.setDelay(100);	
							Robot.getMovement().moveForward();
							StartAlgorithm._timer.setDelay(50);	
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
			updateMap(_actualRow, _actualCol, _mentalMap);
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