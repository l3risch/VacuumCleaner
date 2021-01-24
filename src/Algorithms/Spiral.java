package Algorithms;

import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import Listener.StartAlgorithm;
import Objects.Robot;
import Performance.Performance;
import Physics.Coordinates2D;
import Threads.Thread1;
import main.MainFrame;
import main.TestSeries;


public class Spiral extends CPPAlgorithm implements ActionListener {
	int i = 0;
	public Spiral(MainFrame frame, int iteration)
	{
		_cpp = "Spiral";
		_frame = frame;
		_iteration = iteration;
		_secondsMap = new HashMap<Integer,Double>();
		_perf = new Performance(_frame, _iteration, "Spiral", _secondsMap);
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
		
		if(i==0)
		{
			Robot.getMovement().moveForward();
		}
		i++;
		_nn = NearestNeighbour.getNearestNeighbour(actualRow, actualCol);		

		if(actualRow >= 0 && actualCol >= 0)
		{
			updateMap(actualRow, actualCol, mentalMap);
		}		
		
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
//			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
//			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
//			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
//			Robot.getMovement().moveForward();
			
		} else if(totallyCovered(encircledArea))
		{
			backtrack(_actualRow, _actualCol);

		} else {
			if(freeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
			{
				Robot.getMovement().turnLeft();
				Robot.getMovement().moveForward();
			}
			else if(freeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
			{
				Robot.getMovement().turnRight();
				Robot.getMovement().moveForward();
			} else if(freeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
			{
				Robot.getMovement().moveForward();
			}
			_pathCalculated = false;
			_movesToNN = 0;
		}
		
		if(reachedStoppingCriteria())
		{
			Timer timer = Thread1.getTimer();
			stopNevaluate("Spiral", timer, _perf);
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

	
		
  }
