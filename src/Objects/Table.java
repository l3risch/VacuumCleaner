package Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Objects.Obstacle.Shape;
import Physics.Movement;
import Physics.Sweeper;
import Rendering.Renderer1;

public class Table {

	public static int _rows = 64;
	public static int _cols = 64;
	public static List<Obstacle> _listObs = null;
	
	public static boolean[][] _markedPath;
	public static boolean[][] _markedObstacles;
	
	public static int _numberObs;

		
	public Table()
	{
		/**
		 * Set configuration for Type1
		 */
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
	}


	public static void setType(String type)
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
		case "7": setType7();
		break;
		}
	}

	private static void setType1() {
		Robot.setStartingPos(15, 53);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = null;
	}

	
	private static void setType2() {
		Robot.setStartingPos(56, 5);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(230, 260, 300, 100, Shape.RECTANGLE);
		
		Obstacle obs2 = new Obstacle(140, 200, 50, 50, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(320, 250, 50, 50, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(390, 120, 80, 20, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(460, 540, 120, 70, Shape.RECTANGLE);

		Obstacle obs6 = new Obstacle(250, 130, 30, 50, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(320, 120, 60, 50, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(100, 570, 210, 50, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(460, 420, 90, 50, Shape.RECTANGLE);
		Obstacle obs10 = new Obstacle(480, 650, 30, 60, Shape.RECTANGLE);
		Obstacle ob11 = new Obstacle(560, 570, 90, 100, Shape.RECTANGLE);
		
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
		_listObs.add(obs10);
		_listObs.add(ob11);
		
		markObstacles();

	}

	private static void setType3() {
		Robot.setStartingPos(27, 27);
		Robot.getMovement().setAng(270);
		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		_listObs = new ArrayList<Obstacle>();
		
		Obstacle obs1 = new Obstacle(420, 310, 170, 150, Shape.RECTANGLE);	
		Obstacle obs2 = new Obstacle(700, 330, 40, 70, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(140, 140, 70, 150, Shape.RECTANGLE);	
		Obstacle obs4 = new Obstacle(100, 630, 120, 70, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(210, 240, 80, 50, Shape.RECTANGLE);	
		Obstacle obs6 = new Obstacle(580, 360, 60, 70, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(140, 640, 50, 40, Shape.RECTANGLE);	
		Obstacle obs8 = new Obstacle(480, 260, 110, 50, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(100, 410, 230, 20, Shape.RECTANGLE);	
		
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
	

	private static void setType4() {
		Robot.setStartingPos(14, 20);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(150, 340, 50, 40, Shape.RECTANGLE);
		
		Obstacle obs2 = new Obstacle(530, 360, 140, 90, Shape.RECTANGLE);
		
		Obstacle obs3 = new Obstacle(280, 540, 140, 140, Shape.RECTANGLE);
		
		_listObs = new ArrayList<Obstacle>();
		
		_listObs.add(obs1);
		_listObs.add(obs2);
		_listObs.add(obs3);

		
		markObstacles();
	}
	
	
	private static void setType5() {
		Robot.setStartingPos(17, 25);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(540, 750, 40, 30, Shape.RECTANGLE);
		Obstacle obs2 = new Obstacle(170, 270, 110, 10, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(440, 220, 100, 150, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(400, 350, 90, 110, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(660, 640, 10, 40, Shape.RECTANGLE);
		Obstacle obs6 = new Obstacle(280, 550, 80, 10, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(270, 420, 90, 140, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(490, 660, 10, 30, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(460, 560, 40, 150, Shape.RECTANGLE);
		Obstacle obs10 = new Obstacle(230, 140, 120, 80, Shape.RECTANGLE);
		Obstacle obs11 = new Obstacle(660, 280, 20, 100, Shape.RECTANGLE);
		Obstacle obs12 = new Obstacle(250, 320, 100, 140, Shape.RECTANGLE);
		Obstacle obs13 = new Obstacle(470, 360, 80, 80, Shape.RECTANGLE);
		Obstacle obs14 = new Obstacle(220, 760, 60, 10, Shape.RECTANGLE);
		Obstacle obs15 = new Obstacle(660, 650, 20, 50, Shape.RECTANGLE);
		
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
		_listObs.add(obs10);
		_listObs.add(obs11);
		_listObs.add(obs12);
		_listObs.add(obs13);
		_listObs.add(obs14);
		_listObs.add(obs15);
		
		markObstacles();
	}
	
	private static void setType6() {
		Robot.setStartingPos(45, 30);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(660, 420, 30, 150, Shape.RECTANGLE);
		Obstacle obs2 = new Obstacle(190, 640, 80, 10, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(520, 410, 80, 150, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(370, 270, 150, 40, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(570, 740, 110, 40, Shape.RECTANGLE);
		Obstacle obs6 = new Obstacle(270, 520, 40, 140, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(520, 260, 140, 10, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(460, 420, 120, 10, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(170, 190, 130, 90, Shape.RECTANGLE);
		Obstacle obs10 = new Obstacle(570, 720, 110, 60, Shape.RECTANGLE);
		
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
		_listObs.add(obs10);
	
		markObstacles();
	}
	
	private static void setType7() {
		Robot.setStartingPos(25, 57);

		_rows = 64;
		_cols = 64;
		_markedPath = new boolean[_rows][_cols];
		_markedObstacles = new boolean[_rows][_cols];
		
		Obstacle obs1 = new Obstacle(560, 500, 100, 20, Shape.RECTANGLE);
		Obstacle obs2 = new Obstacle(470, 320, 130, 130, Shape.RECTANGLE);
		Obstacle obs3 = new Obstacle(200, 280, 140, 50, Shape.RECTANGLE);
		Obstacle obs4 = new Obstacle(660, 560, 40, 50, Shape.RECTANGLE);
		Obstacle obs5 = new Obstacle(330, 600, 50, 70, Shape.RECTANGLE);
		Obstacle obs6 = new Obstacle(140, 670, 60, 20, Shape.RECTANGLE);
		Obstacle obs7 = new Obstacle(150, 600, 80, 130, Shape.RECTANGLE);
		Obstacle obs8 = new Obstacle(440, 310, 130, 130, Shape.RECTANGLE);
		Obstacle obs9 = new Obstacle(380, 290, 140, 120, Shape.RECTANGLE);
		Obstacle obs10 = new Obstacle(310, 410, 90, 110, Shape.RECTANGLE);
		Obstacle obs11 = new Obstacle(290, 300, 110, 120, Shape.RECTANGLE);
		Obstacle obs12 = new Obstacle(450, 560, 140, 70, Shape.RECTANGLE);


		
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
		_listObs.add(obs10);
		_listObs.add(obs11);
		_listObs.add(obs12);
		
		markObstacles();
	}
	
	public static void setRandomMap(List<Obstacle> listObs)
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
		_numberObs = listObs.size();
		 
	}
	public static void markPath(int row, int col)
	{
		_markedPath[row][col] = true;
	}
	
	public static boolean getPath(int row, int col)
	{
		return _markedPath[row][col];
	}
	
	
	public List<Obstacle> getObstacles()
	{
		return _listObs;
	}
	

	private static void markObstacles() {

		for(Obstacle obs: _listObs)
		{
			checkCoordinates(obs);
		}
	}
	
	private static void checkCoordinates(Obstacle obs) {
		
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
	
	public static boolean getMarkedObstacles(int row, int col)
	{
		return _markedObstacles[row][col];
	}
	
	public static void clearMarkedPath()
	{
		 _markedPath = new boolean[_rows][_cols];
	}

}

