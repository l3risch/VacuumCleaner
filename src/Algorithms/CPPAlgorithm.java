package Algorithms;

/**
 * Parent Class of the Spiral and ZigZag algorithm. This class combines the backtracking with the motion pattern
 */
import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import Listener.StartAlgorithm;
import Objects.Node;
import Objects.Robot;
import Performance.Performance;
import Physics.Coordinates2D;
import Threads.Thread1;
import main.TestSeries;
import main.MainFrame;

public class CPPAlgorithm extends Basic{
	
	protected MainFrame _frame; 
	protected int _actualRow;
	protected int _actualCol;
	protected Coordinates2D[] _encircledArea = new Coordinates2D[16];
	protected int _iteration = 0;

	public static long _duration;
	protected Coordinates2D _nn = new Coordinates2D(0, 0);
	
	protected int _movesToNN = 1;
	protected List<Node> _shortestPath;
	protected boolean _pathCalculated = false;
	
	protected boolean _nnVisited = true;
	
	public Map<Integer, Double> _secondsMap;
	protected Performance _perf;
	public static String _cpp;
	
	public static int _timeLimit = 180;
	
	protected void backtrack(int actualRow, int actualCol) {

		if(!_pathCalculated)
		{
			//Calculate shortest route to nearest neighbour
			_shortestPath = DijkstraAlgorithm.computePath(actualRow, actualCol, _nn);
			_pathCalculated = true;
		}

		else
		{			
		if(_shortestPath != null)
		{
			if(!DijkstraAlgorithm.nnReached(actualRow, actualCol))
			{
				Node currentNode = _shortestPath.get(_movesToNN);
				if(_movesToNN < _shortestPath.size() - 1)
				{
					Node nextNode = _shortestPath.get(_movesToNN+1);

					//Move robot on GUI
					Robot.getMovement().setX(100 + 10 * currentNode.y);
					Robot.getMovement().setY(110 + 10 * currentNode.x);
					
					if(_movesToNN < _shortestPath.size()-1)
					{
						//Determine angle of robot
						if(currentNode.x == nextNode.x - 1)
						{
							Robot.getMovement()._ang = 90;
						} else if(currentNode.y == nextNode.y - 1)
						{
							Robot.getMovement()._ang = 0;
						} else if(currentNode.x == nextNode.x + 1)
						{
							Robot.getMovement()._ang = 270;
						} else if(currentNode.y == nextNode.y + 1)
						{
							Robot.getMovement()._ang = 180;
						} 
						
					}
				} else {
					Robot.getMovement().setX(100 + 10 * currentNode.y);
					Robot.getMovement().setY(110 + 10 * currentNode.x);
				}
				
				_movesToNN++;

			} else {
				_movesToNN = 0;
				_pathCalculated = false;
			}
		} else {
			_pathCalculated = false;
			_movesToNN = 0;
		}
	}		
	}

	/**
	 * Check if stopping criteria is reached
	 * @return	Stopping criteria is reached if all free cells are covered or a specific amount of time has passed
	 */
	protected boolean reachedStoppingCriteria() 
	{
		if(TestSeries._series == true)
		{
			_duration = (System.currentTimeMillis() / 1000l) - (Thread1._start/ 1000l);
			if(_duration > _timeLimit)
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
	
	/**
	 * Stops the current algorithm and starts the evaluation
	 * @param algorithm		current algorithm
	 * @param timer 	Timer rendering the robot
	 * @param perf		Performance instance performing evaluation
	 */
	protected void stopNevaluate(String algorithm, Timer timer, Performance perf) 
	{
		try 
		{
			if(TestSeries._series == true)
			{
				timer.stop();
				perf.evaluateSeries(_timeLimit, _duration);
			} else {
				StartAlgorithm._timer.stop();
				perf.evaluateSolo(_timeLimit);
			}
		}  catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	/**
	 * Providing coverage cumulative coverage at each second.
	 * @param secondsMap	Map saving the second and corresponding coverage
	 * @param passedSeconds		seconds passed by
	 * @param perf	Performance instance performing evaluation
	 * @return	Map saving the second and corresponding coverage
	 */
	protected Map<Integer, Double> updatePathCoverage(Map<Integer, Double> secondsMap, long passedSeconds, Performance perf)
	{
		double coverage;
		
		for(int sec = 0; sec < _timeLimit; sec++)
		{
			double milSec = sec*1000;
			double nextMilSec = (sec + 1) * 1000;
			if(passedSeconds > milSec && passedSeconds < nextMilSec)
			{
				if(!secondsMap.containsKey(sec))
				{
					coverage = perf.computeCoverage();
					secondsMap.put(sec, coverage);
				}
			}
		}

		return secondsMap;
	}
}
