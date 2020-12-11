package Performance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Observable;

import Algorithms.Basic;
import Algorithms.CPPAlgorithm;
import Algorithms.NearestNeighbour;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Threads.Thread1;
import main.MainFrame;
import main.TestSeries;

public class Performance extends Basic{

	private int _numberOfTurns;
	private int _totalDistance;
	private int _totalMovements;
	
	private int _freeCells = 0;
	private static int _visitedCells = 0;
	private int _obstacleCells = 0;
	private int _accessableCells = 0;
	private double _coverage = 0;
	private int _obstacles = 0;
	
	private MainFrame _frame;
	private int _iteration;
	private String _algorithm;
	
	private Map<Integer, Double> _secondsMap;
	
	public Thread1 _t1;
	
	
	public Performance(MainFrame frame, int iteration, String algorithm, Map<Integer, Double> secondsMap)
	{
		_frame = frame;
		_iteration = iteration;
		_algorithm = algorithm;
		_obstacles = Table._numberObs;
		_secondsMap = secondsMap;
	}

	public void evaluate(int timeLimit) 
	{
		computeStats();
		
		try {
			archive(timeLimit);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_frame.saveImage(_algorithm, _iteration);
		
		if(Thread1._cpp == "Random")
		{
			nextIteration();
		} else {
			nextAlgorithm();
		}
	}



	private void nextIteration() 
	{
		TestSeries._iteration++;
		int i = TestSeries._iteration;
		if(i < TestSeries._iterations) 
		{
			_t1 = new Thread1(i, _frame);
			_t1.clearAlgorithm();	
			TestSeries._obstacles++;
			_t1.startIteration(i, TestSeries._obstacles);
		} else {
		    System.exit(0);
		}
	}

	private void nextAlgorithm() 
	{
		if(TestSeries._series == true)
		{
			switch(Thread1._cpp)
			{
				case "Spiral" : 
					_t1 = new Thread1(_iteration, _frame);
					_t1.clearAlgorithm();
					_t1.startZigZag();
				break;
	
				case "ZigZag" :
				_t1 = new Thread1(_iteration, _frame);
				_t1.clearAlgorithm();
				_t1.startCustom();
				break;
				
				case "Custom" :
				_t1 = new Thread1(_iteration, _frame);
				_t1.clearAlgorithm();
				_t1.startRandom();
				_t1.clearMap();
				break;
			}
		}		
	}

	private void archive(int timeLimit) throws IOException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/" + _algorithm + _iteration + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);

		    writeToFile("Local Time:_" + java.time.LocalDateTime.now() + "_____________________\n\n", osw);
		    writeToFile("Algorithm: " + _frame.getAlgorithm() + " with time limit: " + timeLimit + "\n\n", osw);

			writeToFile("\nDistance: " + _totalDistance, osw);
			writeToFile("\nNumber of turns: " + _numberOfTurns, osw);
			writeToFile("\nTotal Movements: " + _totalMovements, osw);
			writeToFile("\nNumber of Obstacles: " + _obstacles, osw);

			
			writeToFile("\n\nFree Cells: " + _freeCells, osw);
			writeToFile("\nVisited Cells: " + _visitedCells, osw);
			
			//Substract 4 from the set of revisited Cells due to initialization error
			writeToFile("\nRevisited Cells: " + (_revisitedCells-4), osw);
			writeToFile("\nObstacle Cells: " + _obstacleCells, osw);
			writeToFile("\nAccessable Cells: " + _accessableCells, osw);
			writeToFile("\n\nCoverage: " +  _coverage, osw);
			
			writeToFile("\n\nDuration: " +  CPPAlgorithm._duration , osw);
			writeToFile("\nTimes Dijkstra executed: " +  _dijkstraExecutions, osw);
			
			writeToFile("\n\n________________________________________\nAchieved coverage at specific execution time in seconds\n", osw);

			for(int key : _secondsMap.keySet())
			{
				writeToFile(key + " : " + _secondsMap.get(key) + "\n", osw);
			}
			
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		_visitedCells = 0;
		_dijkstraExecutions = 0;
	}
	
	private static void writeToFile(String message, OutputStreamWriter osw){
	    try {
	        osw.write(message);
	        osw.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void computeStats() 
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();

		_numberOfTurns = Robot.getMovement()._numberOfTurns;
		_totalDistance = Robot.getMovement()._totalDistance;
		_totalMovements= _totalDistance + _numberOfTurns;

		for(int i = 0; i<64; i++)
		{
			for(int j = 0; j<64; j++)
			{
				if(pathMatrix[i][j]=='2' || pathMatrix[i][j]=='D')
				{
					_freeCells++;
				}
				if(pathMatrix[i][j]=='1' || pathMatrix[i][j]=='S')
				{
					_visitedCells++;
				}
				if(Table._markedObstacles[i][j] == true)
				{
					_obstacleCells++;
				}
			}
		}
		
		_accessableCells = (64*64) - _obstacleCells;
		_coverage = ((double)_visitedCells/(double)_accessableCells) ;
		if(_coverage > 1)
		{
			_coverage = 1;
		}
		System.out.println("Coverage: " +  _coverage);
	}

	public Thread1 getNewThread()
	{
		return _t1;
	}
	
	public double computeCoverage()
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();
		int visitedCells = 0;
		int obstacleCells = 0;
		int accessableCells = 0;


		for(int i = 0; i<64; i++)
		{
			for(int j = 0; j<64; j++)
			{
				if(pathMatrix[i][j]=='1' || pathMatrix[i][j]=='S')
				{
					visitedCells++;
				}
				if(Table._markedObstacles[i][j] == true)
				{
					obstacleCells++;
				}
			}
		}
		
		accessableCells = (64*64) - obstacleCells;
		
		double coverage = ((double)visitedCells/(double)accessableCells) * 100;
		
		return coverage;
	}
}
