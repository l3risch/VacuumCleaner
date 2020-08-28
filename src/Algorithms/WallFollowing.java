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
	private boolean _flag = false;
	
	public WallFollowing(MainFrame frame)
	{
		_frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		_scannedArea = getEncircledScannedArea(_actualRow, _actualCol);

		
		//TODO: Build Map to memorize visited cells
		
		if (freeDirection(_scannedArea, ScanDirection.LEFT) && _flag == true) 
		{
			StartAlgorithm._timer.stop();
			Robot.getMovement().turnLeft();
			StartAlgorithm._timer.start();	
			
			_flag = false;
		} 
		
		if (!freeDirection(_scannedArea, ScanDirection.LEFT)) 
		{
			_flag = true;
		} 
		
		if(partiallyFreeDirection(_scannedArea, ScanDirection.FRONT))
		{
			Robot.getMovement().moveForward();	

		} else if (freeDirection(_scannedArea, ScanDirection.RIGHT)) 
		{
			StartAlgorithm._timer.stop();
			Robot.getMovement().turnRight();
			StartAlgorithm._timer.start();	
						
		}  

		_frame.repaint();		
	}
		
}


