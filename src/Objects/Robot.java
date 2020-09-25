package Objects;
import Physics.Coordinates2D;
import Physics.Movement;
import Rendering.Renderer1;

public class Robot {

	public static int _x;
	public static int _initX;
	public static Coordinates2D[][] _robotCoordinates = new Coordinates2D[4][4];
	public static int _initY;
	public static int _y;
	public static Movement _move;
	public static Coordinates2D _startingPos;
	

	
	public Robot() 
	{
		setRandomStartingPos();
	}

	public static void setStartingPos(int row, int col)
	{
		_move = new Movement();
		_move.setStartingPosition(row, col);
		_x = _move.getX();
		_initX = _move.getX();
		_y = _move.getY();
		_initY = _move.getY();

		_startingPos = new Coordinates2D(getYasRow(), getXasCol());
	}
	
	public static void setRandomStartingPos()
	{
		_move = new Movement();
		_move.setRandomStartingPosition();
		_x = _move.getX();
		_initX = _move.getX();
		_y = _move.getY();
		_initY = _move.getY();
		
		_startingPos = new Coordinates2D(getYasRow(), getXasCol());

	}
	public static Movement getMovement()
	{
		return _move;
	}
	
	
	public static int getX()
	{
		_x = _move.getX();
		return _x;
	}
	
	public static Coordinates2D[][] getCoordinates(int row, int col)
	{
		for(int i = 0; i > -4; i--)
		{
			for(int j = 0; j < 4; j++)
			{
				Coordinates2D coordinates = new Coordinates2D(row + i, col + j);
				_robotCoordinates[3 + i][j] = coordinates;		
			}
		}
		return _robotCoordinates;
	}
	
	public static int getXasCol()
	{
		float xFloat = (float)(getX()-100)/10;
		int xRound = Math.round(xFloat);
		return xRound;
	}
	
	public static int getY()
	{
		_y = _move.getY();
		return _y;
	}
	
	public static int getYasRow()
	{		
		float yFloat = (float)(getY()-110)/10;
		int yRound = Math.round(yFloat);
		return yRound;
	}
	
	public static int getInitX()
	{
		return _initX;
	}
	
	public static int getInitY()
	{
		return _initY;
	}
}
