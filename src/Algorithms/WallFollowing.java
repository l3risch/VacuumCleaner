package Algorithms;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import Algorithms.Basic.ScanDirection;
import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import main.MainFrame;

public class WallFollowing extends Basic implements ActionListener{

	private MainFrame _frame; 
	public boolean _distanceExceeded = false;
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _scannedArea = new Coordinates2D[4];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();
	Coordinates2D _nearestNeighbour = new Coordinates2D(0, 0);
	
	public WallFollowing(MainFrame frame)
	{
		_frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		_scannedArea = getEncircledScannedArea(_actualRow, _actualCol);

		
//		//TODO: Build Map to memorize visited cells
//		
//		if (freeDirection(_scannedArea, ScanDirection.LEFT) && _flag == true) 
//		{
//			StartAlgorithm._timer.stop();
//			Robot.getMovement().turnLeft();
//			StartAlgorithm._timer.start();	
//			
//			_flag = false;
//		} 
//		
//		if (!freeDirection(_scannedArea, ScanDirection.LEFT)) 
//		{
//			_flag = true;
//		} 
//		
//		if(partiallyFreeDirection(_scannedArea, ScanDirection.FRONT))
//		{
//			Robot.getMovement().moveForward();	
//
//		} else if (freeDirection(_scannedArea, ScanDirection.RIGHT)) 
//		{
//			StartAlgorithm._timer.stop();
//			Robot.getMovement().turnRight();
//			StartAlgorithm._timer.start();	
//						
//		}  
		double ang = Movement.getAng();
		_nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
		_pathDeterminer.turnToNearestNeighbour(_nearestNeighbour);

		if(isFrontAccesable(_actualRow, _actualCol))
		{
			Robot.getMovement().moveForward();
		} else {
			
			Movement.setAng(ang);
			if(freeDirection(_scannedArea, ScanDirection.RIGHT))
			{
				Robot.getMovement().turnRight();
				Robot.getMovement().moveForward();
			} else {
				
				if(isFrontAccesable(_actualRow, _actualCol))
				{
					Robot.getMovement().moveForward();
				} else {
					Robot.getMovement().turnLeft();
				}
			}
		}
		
		_frame.repaint();		
	}
		
}


