package Physics;

/**
 * Class implementing the physique of the robot moving in a 2D environment
 */

import java.util.ArrayList;

import java.util.List;

import enums.Direction;

public class Movement {
	
	public int _i = 0;
	public int _x = 0;
	public int _y = 0;
	public int _totalDistance = 0;
	public int _numberOfTurns = 0;

		public double _ang;
	
	public Direction _dir;
	

	
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
		_totalDistance++;
	}
	
	
	public void turnByDegrees(int ang) 
	{
		_ang = _ang + ang;

		_numberOfTurns++;
		
	}
	
	public void turnRight()
	{
		_ang += 90;
				
		_numberOfTurns++;
	}
	
	public void turnLeft()
	{
		_ang -= 90;
		
		_numberOfTurns++;
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
		_x = 120 + 10 * (int) (Math.random() * 56);
		_y = 160 + 10 * (int) (Math.random() * 56);
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
