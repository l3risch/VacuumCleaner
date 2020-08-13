package Physics;

import java.util.ArrayList;
import java.util.List;

import Objects.Table;

/**
 * Boustropedeon Cellular Decomposition is defined in 2 Events: IN and OUT.
 * Based on this 2 Events the area is divided into cells.
 *
 */
public class Sweeper {

	private int _rows;
	private int _cols;
	private int[][] cells;
	int _cellNumber = 0;
	
	private List<Coordinates2D[]> _obstacleList = new ArrayList<Coordinates2D[]>();

	public Sweeper()
	{
		setRows(Table._rows);
		setCols(Table._cols);
		startSweeping();
	}

//	private  void getVertices() {
//		int cellNumber = 0;
//		for(int col = 0; col < _cols; col++)
//		{
//			boolean exceeded = false;
//			for(int row = 0; row < _rows; row++)
//			{
//				if(Table.getMarkedObstacles(row, col) && !exceeded)
//					{
//						createCell(col, cellNumber);
//						exceeded = true;
//						cellNumber +=1;
//					}
//			}
//		}
//	}
//
//	private  void createCell(int col, int cellNumber) {
//		for(int j = 0; j < col; j++)
//		{
//			for(int i = 0; i < _rows; i++)
//			{
//				if(!Table.getMarkedObstacles(i, j))
//				{
//					cells[i][j] = cellNumber;
//				}
//			}
//		}
//	}
//	
	public  int[][] getCells()
	{
		cells = new int[_rows][_cols];
		return cells;
	}
	
	public  void setRows(int rows)
	{
		_rows = rows;
	}
	
	public  void setCols(int cols)
	{
		_cols = cols;
	}
	
	
	
	
	
	/**
	 * Initiates a Sweeping Line by analysing all rows within one column. This Sweeping Line cheks if
	 * there is an IN or OUT Event.
	 */
	public void startSweeping()
	{
		for(int col = 0; col < _cols; col++)
		{
			//Check length of obstacle for every row
			int obstacleLength = checkObstacleLength(col);

			Coordinates2D[] obstacle = new Coordinates2D[obstacleLength];
			
			for(int row = 0; row < _rows; row++)
			{
				int counter = 0;
				if(Table.getMarkedObstacles(row, col))
				{
					obstacle[counter] = new Coordinates2D(row, col);
				}
			}
			
			_obstacleList.add(obstacle);
			
			if(inEvent(obstacle))
			{
				splitCells(obstacle, col);
				_cellNumber += 3;
			} 
			else if (outEvent(obstacle))
			{
				mergeCells(obstacle, col);
			} 
			else
			{
				for(int row = 0; row < _rows; row++)
				{
				cells[row][col] = _cellNumber;
				}
			}
		}	
	}
	

	/**
	 * @return returns true if the current col defines an IN Event. An IN Event occurs if one cell is 
	 * splitted into two.
	 */
	private boolean inEvent(Coordinates2D[] obstacle)
	{
		if(obstacle != null)
		{
			for(int i = 0; i < obstacle.length; i++)
			{
				if(obstacle[i].getRow() < 4 && obstacle[i].getRow() > Table._rows - 4)
				{
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return returns true if the current col defines an OUT Event. An OUT Event occurs two cells are 
	 * merged into one.
	 */
	private boolean outEvent(Coordinates2D[] obstacle)
	{
		
		return false;

	}
	
	private void splitCells(Coordinates2D[] obstacle, int col) 
	{
		int beginOfObstacle = obstacle[0].getRow();
		int endOfObstacle = obstacle[obstacle.length].getRow();

		for(int row = 0; row < beginOfObstacle; row++)
		{
			cells[row][col] = _cellNumber + 1;
		}
		
		for(int row = endOfObstacle; row < Table._rows; row++)
		{
			cells[row][col] = _cellNumber + 2;
		}
	}
	

	private void mergeCells(Coordinates2D[] obstacle, int col) 
	{

	}


	private int checkObstacleLength(int col) {
	
		int counter = 0;
		
		for(int row = 0; row < _rows; row++)
		{
			if(Table.getMarkedObstacles(row, col))
			{
				counter += 1;
			}
		}
		
		return counter;
	}

}
