package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
		_secondsMap = new HashMap<Integer,Double>();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		if(_actualRow >= 0 && _actualRow <64 && _actualCol >= 0 && _actualCol < 64)
		{
			NearestNeighbour.updatePathMatrix(_actualRow, _actualCol);
		}
		
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
			stopNevaluate("Random", _timer, _secondsMap);
		}		
		
		long passedSeconds = System.currentTimeMillis() - Thread1._start;
		_secondsMap= updatePathCoverage(_secondsMap, passedSeconds);
		
		_frame.repaint();		
		
	}

}
