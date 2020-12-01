package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Listener.StartAlgorithm;
import Objects.Node;
import Objects.Robot;
import Objects.Table;
import Performance.Performance;
import Physics.Coordinates2D;
import Physics.Movement;
import main.MainFrame;


public class Spiral extends CPPAlgorithm implements ActionListener {

	public Spiral(MainFrame frame)
	{
		_frame = frame;
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
		
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
		} else if(totallyCovered(encircledArea))
		{
			backtrack(_actualRow, _actualCol);

		} else {
			Robot.getMovement().moveForward();
			_pathCalculated = false;
			_movesToNN = 0;
		}
		
		if(reachedStoppingCriteria())
		{
			StartAlgorithm._timer.stop();
			Performance perf = new Performance(_frame);
			perf.evaluate();
		}				
	}

	
		
  }
