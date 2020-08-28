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

	public SpiralAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		
		boolean accesableField;

		accesableField = isFrontAccesable(_actualRow, _actualCol);
		
		if(!accesableField)
		{
			
			//Check if dead end
			if(!totallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.LEFT)
					&& !totallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.BACK)
					&& !totallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.RIGHT))
			{
				//Check if front of robot is covered path
				if(coveredPathInDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.FRONT))
				{
					Robot.getMovement().moveForward();
					
					//Check if area left of robot is partially covered and free to access
					if(partiallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.LEFT))
					{
						StartAlgorithm._timer.stop();
						Robot.getMovement().turnLeft();
						StartAlgorithm._timer.start();	
						Robot.getMovement().moveForward();
					}
					
					//Check if area left of robot is partially covered and free to access
					if(partiallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.RIGHT))
					{
						StartAlgorithm._timer.stop();
						Robot.getMovement().turnRight();
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
			if(totallyFreeDirection(getEncircledScannedArea(_actualRow, _actualCol), ScanDirection.LEFT))
			{
				Robot.getMovement().turnLeft();
			}
			Robot.getMovement().moveForward();
		}

		_frame.repaint();
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
}
