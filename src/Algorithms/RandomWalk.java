package Algorithms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Algorithms.Basic.CellState;
import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Performance.Performance;
import Physics.Coordinates2D;
import Physics.Movement;
import Physics.Movement.LastMove;
import main.MainFrame;

public class RandomWalk extends CPPAlgorithm implements ActionListener{

	public boolean _distanceExceeded = false;
	
	
	public RandomWalk(MainFrame frame) {
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		_nn = NearestNeighbour.getNearestNeighbour(_actualRow, _actualCol);		

		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol, _mentalMap);
		}
		
		boolean accesableField;

		accesableField = isFrontAccesable(_actualRow, _actualCol);

		if(!accesableField)
		{
			StartAlgorithm._timer.stop();
			Robot.getMovement().turnByDegrees((int)(Math.random()*360));
			StartAlgorithm._timer.start();
		} else {
			Robot.getMovement().moveForward();
		}

		if(reachedStoppingCriteria())
		{
			StartAlgorithm._timer.stop();
			Performance perf = new Performance();
			perf.evaluate();
		}			
		
		_frame.repaint();		
		
		
		
	}

}
