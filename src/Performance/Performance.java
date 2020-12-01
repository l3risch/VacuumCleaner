package Performance;

import Algorithms.Basic;
import Algorithms.NearestNeighbour;
import Objects.Robot;
import Objects.Table;

public class Performance extends Basic{

	private int _numberOfTurns;
	private int _totalDistance;
	private int _totalMovements;
	
	public Performance()
	{
		_numberOfTurns = Robot.getMovement()._numberOfTurns;
		_totalDistance = Robot.getMovement()._totalDistance;
		_totalMovements= _totalDistance + _numberOfTurns;
	}

	public void evaluate() 
	{
		System.out.println("Distance: " + _totalDistance);
		System.out.println("Number of turns: " + _numberOfTurns);
		System.out.println("Total Movements: " + _totalMovements);
		
		calculateCoverage();
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

		
		int freeCells = 0;
		int visitedCells = 0;
		int obstacleCells = 0;
		int accessableCells = 0;
		double coverage = 0;

		for(int i = 0; i<64; i++)
		{
			for(int j = 0; j<64; j++)
			{
				if(pathMatrix[i][j]=='2' || pathMatrix[i][j]=='D')
				{
					freeCells++;
				}
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
		coverage = ((double)visitedCells/(double)accessableCells) * 100;
		
		System.out.println("\nFree Cells: " + freeCells);
		System.out.println("Visited Cells: " + visitedCells);
		System.out.println("Obstacle Cells: " + obstacleCells);
		System.out.println("Accessable Cells: " + accessableCells);
		System.out.println("Coverage: " +  coverage + "%");
	}
}
