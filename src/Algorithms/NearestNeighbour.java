package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import enums.Direction;

import java.util.Map;
import java.util.Queue;

import Algorithms.Basic.CellState;
import Objects.Node;
import Objects.Robot;
import Physics.Coordinates2D;
import Physics.Movement;

public class NearestNeighbour extends Basic{

	private static Map<String, CellState> _mentalMap = new HashMap<String, CellState>();
	private static Coordinates2D _robotPos;
	public static char[][] _pathMatrix = new char[64][64];
	public static int[][] _wavefrontMatrix;
	
	public static Coordinates2D getNearestNeighbour(int row, int col)
	{
		_mentalMap = getMentalMap();
		//Coordinates2D[][] robotCoordinates = Robot.getCoordinates(row, col);
		
		//The cell that is covered by the robot in the 2nd row and 2nd col is considered as the middle of the robot
		_robotPos = new Coordinates2D(row, col);
		

		Map<Coordinates2D, Integer> map = getFreeCells();

		Coordinates2D nearestCell = new Coordinates2D(0, 0);
		
		//Determine nearest Cell
		double upperBound = 999999;
		
		for(Coordinates2D key : map.keySet())
 		{
			if(map.get(key) < upperBound)
			{
				upperBound = map.get(key);
				nearestCell = key;
			}
 		}
		
		
		return nearestCell;
	}
	
	private static Map<Coordinates2D, Integer> getFreeCells()
	{
		Coordinates2D startPoint = _robotPos;
		int robotRow = _robotPos.getRow();
		int robotCol = _robotPos.getCol();
		
		_pathMatrix = updatePathMatrix(robotRow, robotCol);
		
		
		_wavefrontMatrix = new int[64][64];
		_wavefrontMatrix[startPoint.getRow()][startPoint.getCol()] = 1;
		
		Queue<Coordinates2D> queue = new LinkedList<Coordinates2D>();
		queue.add(startPoint);
		
		int i;

		while(!queue.isEmpty()) 
		{
			Coordinates2D currentCell = queue.poll();
			i = _wavefrontMatrix[currentCell.getRow()][currentCell.getCol()];
			List<Coordinates2D> neighbourList = getNeighbours(_wavefrontMatrix, _pathMatrix, currentCell, i);

			queue.addAll(neighbourList);
		}
		
//		for(int k = 0; k < 64; k++)
//		{
//			StringBuilder sb = new StringBuilder();
//			for(int l = 0; l < 64; l++)
//			{
//				if(_wavefrontMatrix[k][l] < 10 )
//				{
//					sb.append(" "+ _wavefrontMatrix[k][l] +  " ");
//
//				} else {
//					sb.append(_wavefrontMatrix[k][l] +  " ");
//				}
//			}
//			System.out.println(sb);
//		}
		
		Map<Coordinates2D, Integer> map = new HashMap<Coordinates2D, Integer>();
		
		for(int k = 0; k < 64; k++)
		{
			for(int l = 0; l < 64; l++)
			{
				if(_pathMatrix[k][l] == '2')
				{
					map.put(new Coordinates2D(k, l), _wavefrontMatrix[k][l]);
				}
			}
		}
				
		return map;
	}
	
	static char[][] updatePathMatrix(int robotRow, int robotCol) {
		 
		 //Transform Hash Map to char matrix
		 for(String key : _mentalMap.keySet())
		 {
			 int row = Integer.parseInt(key.substring(0, 2));
			 int col = Integer.parseInt(key.substring(2, 4));
			 if(row >= 0 && row < 64 && col >= 0 && col < 64)
			 { 
				 if(_mentalMap.get(key).equals(CellState.VISITED))
				 {
					 _pathMatrix[row][col] = '1';
					 
				 }
				 if(_mentalMap.get(key).equals(CellState.FREE))
			     {
					_pathMatrix[row][col] = '2';
				 }
				 if(_mentalMap.get(key).equals(CellState.OCCUPIED))
				 {
					 _pathMatrix[row][col] = '0';
		 		 }
			 }
		 }
		 
		 //Set all unknown cells as obstacles '0'
		 for(int i = 0; i < 64; i++)
		 {
			 for(int j = 0; j < 64; j++)
			 {
				 if(_pathMatrix[i][j] == 0)
				 {
					 _pathMatrix[i][j] = '0';
				 }
			 }		
		 }	
		 
		 //Set position of robot as source
		 _pathMatrix[robotRow][robotCol] = 'S';
		 
		 return _pathMatrix;
	}

	private static List<Coordinates2D> getNeighbours(int[][] wavefrontMatrix, char[][] pathMatrix, Coordinates2D cell, int i)
	{
		int row = cell.getRow();
		int col = cell.getCol();
		List<Coordinates2D> neighbourList = new LinkedList<Coordinates2D>();
		
		wavefrontMatrix[row][col] = i;	

//		for(int k = 0; k < 64; k++)
//		{
//			StringBuilder sb = new StringBuilder();
//			for(int l = 0; l < 64; l++)
//			{
//				sb.append(pathMatrix[k][l] +  " ");
//			}
//			System.out.println(sb);
//		}
		
		if(row-1 >= 0 && wavefrontMatrix[row - 1][col] == 0 && pathMatrix[row - 1][col] != '0')
		{
			wavefrontMatrix[row - 1][col] = i+1;
			neighbourList.add(new Coordinates2D(row -1, col));
		}
		if(row+1 < 64 && wavefrontMatrix[row + 1][col] == 0 && pathMatrix[row + 1][col] != '0')
		{
			wavefrontMatrix[row + 1][col] = i+1;
			neighbourList.add(new Coordinates2D(row + 1, col));
		}			
		if(col-1 >= 0 && wavefrontMatrix[row][col - 1] == 0 && pathMatrix[row][col - 1] != '0')
		{
			wavefrontMatrix[row][col - 1] = i+1;
			neighbourList.add(new Coordinates2D(row, col - 1));
		}
		if(col+1 < 64 && wavefrontMatrix[row][col + 1] == 0 && pathMatrix[row][col + 1] != '0')
		{
			wavefrontMatrix[row][col + 1] = i+1;
			neighbourList.add(new Coordinates2D(row, col + 1));
		}
				
		return neighbourList;
	}
	
	public static char[][] getPathMatrix()
	{
		return _pathMatrix;
	}
	
}