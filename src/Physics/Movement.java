package Physics;

import java.util.ArrayList;

import java.util.List;

import enums.Direction;

public class Movement {
	
	public int _i = 0;
	public int _x = 0;
	public int _y = 0;
	
	//Ausrichtung nach oben
	public double _ang;
	
	public Direction _dir;
	
	public enum LastMove{FORWARD, RIGHT, LEFT};
	
	public List<LastMove> _listOfMovements = new ArrayList<LastMove>();

	
	public Movement()
	{
		_dir = Direction.UP;
	}
	
	public void moveForward() {
		if(Math.cos(Math.toRadians(_ang)) < 0.01 && Math.cos(Math.toRadians(_ang)) > -0.01)
		{
			_x += 0;
		} else {
			_x += 10 * Math.cos(Math.toRadians(_ang));
		}
		
		if(Math.sin(Math.toRadians(_ang)) < 0.01 && Math.sin(Math.toRadians(_ang)) > -0.01)
		{
			_y += 0;

		} else {
			_y += 10 * Math.sin(Math.toRadians(_ang));	
		}
		
		_listOfMovements.add(LastMove.FORWARD);
	}
	
	public void turnByDegrees(int ang) 
	{
		_ang = _ang + ang;
		
	}
	
	public void turnRight()
	{
		_ang += 90;
		
		switch(_dir) {
			case UP: _dir = Direction.RIGHT;
		break;
			case RIGHT: _dir = Direction.DOWN;
		break;
			case DOWN: _dir = Direction.LEFT;
		break;
			case LEFT: _dir = Direction.UP;
		break;
		}
		
		_listOfMovements.add(LastMove.RIGHT);
	}
	
	public void turnLeft()
	{
		_ang -= 90;
		
		switch(_dir) {
			case UP: _dir = Direction.LEFT;
		break;
			case RIGHT: _dir = Direction.UP;
		break;
			case DOWN: _dir = Direction.RIGHT;
		break;
			case LEFT: _dir = Direction.DOWN;
		break;
		}
		
		_listOfMovements.add(LastMove.LEFT);
	}
	
	public void setX(int x)
	{
		_x = x;
	}
	
	public void setY(int y)
	{
		_y = y;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public void setAng(double ang)
	{
		_ang = ang;
	}
	
	public double getAng()
	{
		return _ang;
	}
	
	public void setRandomStartingPosition()
	{
		_x = 100 + 10 * (int) (Math.random() * 60);
		_y = 140 + 10 * (int) (Math.random() * 60);
		_ang = 270;
		//_ang = 90 * (int) (Math.random() * 4);
	}

	public void setStartingPosition(int rows, int cols)
	{
		_x = 100 + 10 * cols;
		_y = 100 + 10 * rows;
		_ang = 270;
	}

}
