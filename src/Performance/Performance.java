package Performance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import Algorithms.Basic;
import Algorithms.CPPAlgorithm;
import Algorithms.NearestNeighbour;
import Objects.Robot;
import Objects.Table;
import main.MainFrame;

public class Performance extends Basic{

	private int _numberOfTurns;
	private int _totalDistance;
	private int _totalMovements;
	
	private int _freeCells = 0;
	private int _visitedCells = 0;
	private int _obstacleCells = 0;
	private int _accessableCells = 0;
	private double _coverage = 0;
	
	private MainFrame _frame;
	private int _iteration;
	private String _algorithm;
	
	public Performance(MainFrame frame, int iteration, String algorithm)
	{
		_frame = frame;
		_numberOfTurns = Robot.getMovement()._numberOfTurns;
		_totalDistance = Robot.getMovement()._totalDistance;
		_totalMovements= _totalDistance + _numberOfTurns;
		_iteration = iteration;
		_algorithm = algorithm;
	}

	public void evaluate() 
	{
		System.out.println("Distance: " + _totalDistance);
		System.out.println("Number of turns: " + _numberOfTurns);
		System.out.println("Total Movements: " + _totalMovements);
		
		calculateCoverage();
		
		archive();
		_frame.saveImage();
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
			
			writeToFile("\n\nFree Cells: " + _freeCells, osw);
			writeToFile("\nVisited Cells: " + _visitedCells, osw);
			writeToFile("\nObstacle Cells: " + _obstacleCells, osw);
			writeToFile("\nAccessable Cells: " + _accessableCells, osw);
			writeToFile("\n\nCoverage: " +  _coverage + "%", osw);
			
			writeToFile("\n\nDuration: " +  CPPAlgorithm._duration + "s", osw);

			
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

	private void calculateCoverage() 
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();
		System.out.println("\n");
		for(int k = 0; k < 64; k++)
		{
			StringBuilder sb = new StringBuilder();
			for(int l = 0; l < 64; l++)
			{
				sb.append(pathMatrix[k][l] +  " ");
			}
			System.out.println(sb);
		}
		System.out.println("\n");


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
		
		System.out.println("\nFree Cells: " + _freeCells);
		System.out.println("Visited Cells: " + _visitedCells);
		System.out.println("Obstacle Cells: " + _obstacleCells);
		System.out.println("Accessable Cells: " + _accessableCells);
		System.out.println("Coverage: " +  _coverage + "%");
	}
}
