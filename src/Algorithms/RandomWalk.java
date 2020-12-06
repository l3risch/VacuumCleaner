package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.Timer;

import Listener.StartAlgorithm;
import Objects.Robot;
import Threads.Thread1;
import main.TestSeries;
import main.MainFrame;

public class RandomWalk extends CPPAlgorithm implements ActionListener{

	public boolean _distanceExceeded = false;
	
	private Timer _timer;

	
	public RandomWalk(MainFrame frame, int iteration)
	{
		_frame = frame;
		_iteration = iteration;
		_timer = Thread1.getTimer();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		//Not acutally needed but used for measuring coverage
		_nn = NearestNeighbour.getNearestNeighbour(_actualRow, _actualCol);		

		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol, _mentalMap);
		}
		
		boolean accesableField;

		accesableField = isFrontAccesable(_actualRow, _actualCol);

		if(!accesableField)
		{
			if(TestSeries._series == true)
			{
				_timer.stop();
				Robot.getMovement().turnByDegrees((int)(Math.random()*360));
				_timer.start();
			} else {
				
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnByDegrees((int)(Math.random()*360));
				StartAlgorithm._timer.start();
			}
		} else {
			Robot.getMovement().moveForward();
		}

		if(reachedStoppingCriteria())
		{
			stopNevaluate("Random", _timer);
		}			
		
		_frame.repaint();		
		
		
		
	}

}
