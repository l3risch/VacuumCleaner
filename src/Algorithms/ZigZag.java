package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import Algorithms.Basic.CellState;
import Listener.StartAlgorithm;
import Objects.Node;
import Objects.Robot;
import Performance.Performance;
import Physics.Coordinates2D;
import main.MainFrame;


public class ZigZag extends CPPAlgorithm implements ActionListener {


	private int _uTurn = 0;
	private boolean right = true;
	
	
	public ZigZag(MainFrame frame)
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
			if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT))
			{
				performUTurnRight(encircledArea);
			} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT))
			{
				performUTurnLeft(encircledArea);
			} else {
				Robot.getMovement().moveForward();
			}
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

