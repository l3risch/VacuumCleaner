package Performance;

import Objects.Robot;

public class Performance {

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
	}
}
