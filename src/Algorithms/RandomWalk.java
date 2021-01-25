package Algorithms;

/**
 * Class implementing the Random Walk
 */
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.Timer;

import Listener.StartAlgorithm;
import Objects.Robot;
import Performance.Performance;
import Threads.Thread1;
import main.TestSeries;
import main.MainFrame;

public class RandomWalk extends CPPAlgorithm implements ActionListener{

	public boolean _distanceExceeded = false;
	
	private Timer _timer;
	private long _maxDuration;
	
	//Constructor to start the random walk via interface
	public RandomWalk(MainFrame frame, int iteration)
	{
		_frame = frame;
		_iteration = iteration;
		_timer = Thread1.getTimer();
		_secondsMap = new HashMap<Integer,Double>();
		_perf = new Performance(_frame, _iteration, "Random", _secondsMap);
		_revisitedCells = 0;
	}
	
	//Constructor to start the random walk in a test series
	public RandomWalk(MainFrame frame, int iteration, long duration)
	{
		_cpp = "Random";
		_frame = frame;
		_iteration = iteration;
		_timer = Thread1.getTimer();
		_secondsMap = new HashMap<Integer,Double>();
		_perf = new Performance(_frame, _iteration, "Random", _secondsMap);
		_revisitedCells = 0;
		_maxDuration = duration;
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
			stopNevaluate("Random", _timer, _perf);
		}		
		
		long passedSeconds;
		
		if(TestSeries._series == true)
		{
			passedSeconds = System.currentTimeMillis() - Thread1._start;
		} else {
			passedSeconds = System.currentTimeMillis() - StartAlgorithm._start;
		}
		_secondsMap= updatePathCoverage(_secondsMap, passedSeconds, _perf);
		
		_frame.repaint();		
		
	}

	@Override
	protected boolean reachedStoppingCriteria() 
	{
		if(TestSeries._series == true)
		{
			_duration = (System.currentTimeMillis() / 1000l) - (Thread1._start/ 1000l);
			if(_duration > _maxDuration)
			{
				return true;
			}
		} else {
			
			_duration = (System.currentTimeMillis() / 1000l) - (StartAlgorithm._start/ 1000l);
			if(_duration > _timeLimit)
			{
				return true;
			} 
		}
		
		for(String key : _mentalMap.keySet())
		{
			if(_mentalMap.get(key).equals(CellState.FREE))
			{
				return false;
			}
		}
		
		for(int i = 0; i < 64; i++)
		{
			for(int j = 0; j < 64; j++)
			{
				if(NearestNeighbour._pathMatrix[i][j]=='2')
				{
					return false;
				}
			}
		}
		
		return true;
	}

}
