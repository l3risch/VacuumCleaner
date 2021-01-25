package Algorithms;

/**
 * Class implementing the ZigZag pattern
 */
import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import Algorithms.Basic.CellState;
import Listener.StartAlgorithm;
import Objects.Robot;
import Performance.Performance;
import Physics.Coordinates2D;
import Threads.Thread1;
import main.TestSeries;
import main.MainFrame;


public class ZigZag extends CPPAlgorithm implements ActionListener {


	private int _uTurn = 0;
	private boolean right = true;
	
	public ZigZag(MainFrame frame, int iteration)
	{
		_cpp = "ZigZag";
		_frame = frame;
		_iteration = iteration;
		_secondsMap = new HashMap<Integer,Double>();
		_perf = new Performance(_frame, _iteration, "ZigZag", _secondsMap);
		_revisitedCells = 0;

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
				
		determineRoute(_actualRow, _actualCol, _encircledArea, _mentalMap);
		_frame.repaint();

	}	
	
	private void determineRoute(int actualRow, int actualCol, Coordinates2D[] encircledArea, Map<String, CellState> mentalMap) {
		
		_nn = NearestNeighbour.getNearestNeighbour(actualRow, actualCol);		

		if(actualRow >= 0 && actualCol >= 0)
		{
			updateMap(actualRow, actualCol, mentalMap);
		}

		
		if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT) && _uTurn == 0 && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(right && !_pathCalculated && partiallyFreeDirection(encircledArea, ScanDirection.RIGHT))
		{
			performUTurnRight(encircledArea);
			
		} else if(!right && !_pathCalculated && partiallyFreeDirection(encircledArea, ScanDirection.LEFT)) 
		{
			performUTurnLeft(encircledArea);
			
		} else if(totallyCovered(encircledArea))
		{
			backtrack(actualRow, actualCol);

		} else {
			if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
			{
				performUTurnRight(encircledArea);
			} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
			{
				performUTurnLeft(encircledArea);
			} else {
				//Robot.getMovement().moveForward();
				_uTurn = 0;
			}
			_pathCalculated = false;
			_movesToNN = 0;
		}
		
		if(reachedStoppingCriteria())
		{
			Timer timer = Thread1.getTimer();
			stopNevaluate("ZigZag", timer, _perf);
		}		
		
		long passedSeconds;
		
		if(TestSeries._series == true)
		{
			passedSeconds = System.currentTimeMillis() - Thread1._start;
		} else {
			passedSeconds = System.currentTimeMillis() - StartAlgorithm._start;
		}
		_secondsMap= updatePathCoverage(_secondsMap, passedSeconds, _perf);
	}


	private void performUTurnRight(Coordinates2D[] encircledArea) {
		if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT))
		{
			_uTurn += 1;
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.LEFT))
		{
			_uTurn += 1;
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} 
		else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT))
		{
			Robot.getMovement().moveForward();
		} 	else
		{
			_uTurn += 1;
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		}
		
		if(_uTurn > 1)
		{
			right = false;
			_uTurn = 0;
		}
	}
	
	private void performUTurnLeft(Coordinates2D[] encircledArea) {
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT))
		{
			_uTurn += 1;
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT))
		{
			_uTurn += 1;
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} 
		else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT))
		{
			Robot.getMovement().moveForward();
		} 	else
		{
			_uTurn += 1;
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		}
		
		if(_uTurn > 1)
		{
			right = true;
			_uTurn = 0;
		}
	}

	
}

