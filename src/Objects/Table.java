package Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Objects.Obstacle.Shape;
import Physics.Sweeper;
import Rendering.Renderer1;

public class Table {

	public int _rows = 64;
	public int _cols = 64;
	public List<Obstacle> _listObs = null;
	
	public boolean[][] _markedPath;
	public boolean[][] _markedObstacles;

		
	public Table(int rows, int cols, String type)
	{
		/**
		 * Set configuration for Type1
		 */
		_rows = rows;
		_cols = cols;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		setType(type);
	}


	public void setType(String type)
	{	
		switch(type) {
		case "1": setType1();
		break;
		case "2": setType2();
		break;
		case "3": setType3();
		break;
		case "4": setType4();
		break;
		case "5": setType5();
		break;
		case "6": setType6();
		break;
		}
	}

	private void setType1() {
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = null;
	}

	private void setType2() {
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		Obstacle obs1 = new Obstacle(230, 260, 200, 200, Shape.OVAL);
		
		Obstacle obs2 = new Obstacle(285, 460, 50, 50, Shape.OVAL);
		Obstacle obs3 = new Obstacle(370, 450, 50, 50, Shape.OVAL);
		Obstacle obs4 = new Obstacle(410, 410, 50, 50, Shape.OVAL);
		Obstacle obs5 = new Obstacle(300, 200, 50, 50, Shape.OVAL);
		Obstacle obs6 = new Obstacle(500, 730, 200, 50, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(100, 530, 60, 60, Shape.RECTANGLE);

		_listObs.add(obs1);
		_listObs.add(obs2);
		_listObs.add(obs3);
		_listObs.add(obs4);
		_listObs.add(obs5);
		_listObs.add(obs6);
		_listObs.add(obs7);

		markObstacles();
	}
	
	private void setType3() {
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(230, 260, 300, 100, Shape.RECTANGLE);
		
		Obstacle obs2 = new Obstacle(250, 200, 50, 50, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(320, 200, 50, 50, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(390, 200, 50, 50, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(460, 200, 50, 50, Shape.RECTANGLE);

		Obstacle obs6 = new Obstacle(250, 370, 50, 50, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(320, 370, 50, 50, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(390, 370, 50, 50, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(460, 370, 50, 50, Shape.RECTANGLE);
		
		_listObs = new ArrayList<Obstacle>();
		
		_listObs.add(obs1);
		_listObs.add(obs2);
		_listObs.add(obs3);
		_listObs.add(obs4);
		_listObs.add(obs5);
		_listObs.add(obs6);
		_listObs.add(obs7);
		_listObs.add(obs8);
		_listObs.add(obs9);
		
		markObstacles();

	}
	
	private void setType4() {
		_rows = 64;
		_cols = 52;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		Obstacle obs1 = new Obstacle(100, 530, 60, 60, Shape.RECTANGLE);
		Obstacle obs2 = new Obstacle(340, 540, 50, 50, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(350, 730, 100, 50, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(590, 510, 30, 50, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(140, 230, 100, 50, Shape.RECTANGLE);
		Obstacle obs6 = new Obstacle(400, 200, 70, 70, Shape.OVAL);
		Obstacle obs7 = new Obstacle(320, 320, 100, 100, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(360, 360, 25, 125, Shape.RECTANGLE);

		_listObs.add(obs1);
		_listObs.add(obs2);
		_listObs.add(obs3);
		_listObs.add(obs4);
		_listObs.add(obs5);
		_listObs.add(obs6);
		_listObs.add(obs7);
		_listObs.add(obs8);
		
		markObstacles();
	}
	

	private void setType5() {
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		Obstacle obs1 = new Obstacle(220, 260, 400, 400, Shape.RECTANGLE);	
		
		_listObs.add(obs1);
		
		markObstacles();
	}
	
	private void setType6() {
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		Obstacle obs1 = new Obstacle(220, 260, 400, 400, Shape.OVAL);	
		
		_listObs.add(obs1);
		
		markObstacles();
	}
	
	public void setRandomMap(List<Obstacle> listObs)
	{
		 _rows = 64;
		 _cols = 64;
		 _markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		for(Obstacle obs: listObs)
		{
			_listObs.add(obs);
		}
		
		markObstacles();
		 
	}
	public void markPath(int row, int col)
	{
		for(int i = row; i > row - 4; i--)
		{
			for(int j = col; j < col + 4; j++)
			{
				_markedPath[i][j] = true;
			}
		}
	}
	
	public boolean getPath(int row, int col)
	{
		return _markedPath[row][col];
	}
	
	
	public List<Obstacle> getObstacles()
	{
		return _listObs;
	}
	

	private void markObstacles() {

		for(Obstacle obs: _listObs)
		{
			checkCoordinates(obs);
		}
	}
	
	private void checkCoordinates(Obstacle obs) {
		
		int x;
		int y;
		
		for(int i = 0; i < _rows; i++)
		{
			for(int j = 0; j < _cols; j++)
			{
				y = i*10 + 140;
				x = j*10 + 100;

				if((x >= obs._x && x < obs._x + obs._width) && (y >= obs._y && y < obs._y + obs._height))
				{
					_markedObstacles[i][j] = true;
				}
			}
		}
	}
	
	public boolean getMarkedObstacles(int row, int col)
	{
		return _markedObstacles[row][col];
	}
	
	public int getRows()
	{
		return _rows;
	}
	
	public int getCols()
	{
		return _cols;
	}
	
	public boolean[][] getMarkedPath()
	{
		return _markedPath;
	}

	public boolean[][] getMarkedObstacles()
	{
		return _markedObstacles;
	}
}

