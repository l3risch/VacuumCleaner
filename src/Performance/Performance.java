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

public class Performance extends Observable{

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
		_numberOfTurns = Robot.getMovement()._numberOfTurns;
		_totalDistance = Robot.getMovement()._totalDistance;
		_totalMovements= _totalDistance + _numberOfTurns;
		_iteration = iteration;
		_algorithm = algorithm;
		_obstacles = MapGenerator._numberObs;
		_secondsMap = secondsMap;
	}

	public void evaluate() 
	{
		computeStats();
		
		archive();
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
		int i = TestSeries._iteration ++;
		if(i < TestSeries._iterations) 
		{
			_t1 = new Thread1(i, _frame);
			_t1.clearAlgorithm();					
			_t1.setupRandomMap(i);
			_t1.startSpiral();
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

	private void archive() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/" + _algorithm + _iteration + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);

		    writeToFile("Local Time:_" + java.time.LocalDateTime.now() + "_____________________\n\n", osw);
		    writeToFile("Algorithmus: " + _frame.getAlgorithm() + "\n\n", osw);

			writeToFile("\nDistance: " + _totalDistance, osw);
			writeToFile("\nNumber of turns: " + _numberOfTurns, osw);
			writeToFile("\nTotal Movements: " + _totalMovements, osw);
			writeToFile("\nNumber of Obstacles: " + _obstacles, osw);

			
			writeToFile("\n\nFree Cells: " + _freeCells, osw);
			writeToFile("\nVisited Cells: " + _visitedCells, osw);
			writeToFile("\nObstacle Cells: " + _obstacleCells, osw);
			writeToFile("\nAccessable Cells: " + _accessableCells, osw);
			writeToFile("\n\nCoverage: " +  _coverage + "%", osw);
			
			writeToFile("\n\nDuration: " +  CPPAlgorithm._duration + "s", osw);
			
			writeToFile("\n\n________________________________________\nAchieved overage at specific execution time in seconds\n", osw);
			
			for(int key : _secondsMap.keySet())
			{
				writeToFile(key + " : " + _secondsMap.get(key) + "%\n", osw);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeToFile(String message, OutputStreamWriter osw){
	    try {
	        osw.write(message);
	        osw.flush();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	private void computeStats() 
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();
//		System.out.println("\n");
//		for(int k = 0; k < 64; k++)
//		{
//			StringBuilder sb = new StringBuilder();
//			for(int l = 0; l < 64; l++)
//			{
//				sb.append(pathMatrix[k][l] +  " ");
//			}
//			System.out.println(sb);
//		}
//		System.out.println("\n");


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
		_coverage = ((double)_visitedCells/(double)_accessableCells) * 100;

		System.out.println("Coverage: " +  _coverage + "%");
	}

	public Thread1 getNewThread()
	{
		return _t1;
	}
	
	public static double computeCoverage()
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
