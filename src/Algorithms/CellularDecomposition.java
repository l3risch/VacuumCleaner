package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import Physics.Sweeper;
import Rendering.Renderer1;
import main.MainFrame;
import enums.Direction;


public class CellularDecomposition extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _scannedArea;

	public CellularDecomposition(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		Coordinates2D[] scannedCircleArea = getScannedArea(_actualCol, _actualRow);
		if(hitWall(scannedCircleArea)) {
			followWall(scannedCircleArea);
		}
		//TODO: Quitting Criteria
		reachedStoppingCriteria();

		_frame.repaint();		

	}
	

	@Override
	Coordinates2D[] getScannedArea(int row, int col) 
	{
		//Get scanned area in front of the robot
		_scannedArea = super.getScannedArea(row, col);
		
		Coordinates2D[] circledArea = new Coordinates2D[16];
		
		//Determine the scanned area encirceling the robot
		double ang = Movement.getAng();
		if(ang % 90 == 0)
		{
			switch(Movement._dir) {
			case DOWN: circledArea = getAreaIfDown(_scannedArea);
			System.out.println("DOWN: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfDown(_scannedArea)[i].getRow()+", " +getAreaIfDown(_scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case LEFT: circledArea = getAreaIfLeft(_scannedArea);
			System.out.println("Left: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfLeft(_scannedArea)[i].getRow()+", " +getAreaIfLeft(_scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case RIGHT: circledArea = getAreaIfRight(_scannedArea);
			System.out.println("Right: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfRight(_scannedArea)[i].getRow()+", " +getAreaIfRight(_scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case UP: circledArea = getAreaIfUp(_scannedArea);
			System.out.println("UP: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfUp(_scannedArea)[i].getRow()+", " +getAreaIfUp(_scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			default:
				break;
			}
		}
		return circledArea;
		
	}
	
	private Coordinates2D[] getAreaIfUp(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = _scannedArea[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] getAreaIfRight(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = _scannedArea[i];
		}
		
		return circledArea;
	}

	private Coordinates2D[] getAreaIfDown(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = leftSensor(_scannedArea, dir)[i];
			circledArea[i+8] = _scannedArea[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] getAreaIfLeft(Coordinates2D[] _scannedArea)
	{
		Direction dir = Movement._dir;
		Coordinates2D[] circledArea = new Coordinates2D[16];
		for(int i = 0; i < 4; i++)
		{
			circledArea[i] = upperSensor(_scannedArea, dir)[i];
			circledArea[i+4] = _scannedArea[i];
			circledArea[i+8] = lowerSensor(_scannedArea, dir)[i];
			circledArea[i+12] = rightSensor(_scannedArea, dir)[i];
		}

		return circledArea;
	}
	
	private Coordinates2D[] upperSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] upperSide = new Coordinates2D[4];
		int maxRow = 0;
		
		if(dir.equals(Direction.RIGHT))
		{
			maxRow = _scannedArea[3].getRow() - 4;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() - i - 1);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxRow = _scannedArea[3].getRow() - 4;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() + i + 1);
			}
		} else if (dir.equals(Direction.DOWN)) {
			maxRow = _scannedArea[3].getRow() - 5;
			for(int i = 0; i < 4; i++)
			{
				upperSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol());
			
			}
		}

		return upperSide;			
	}
	
	private Coordinates2D[] lowerSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] lowerSide = new Coordinates2D[4];
		int maxRow = 0;
		
		if(dir.equals(Direction.RIGHT))
		{
			maxRow = _scannedArea[3].getRow() + 1;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() - i - 1);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxRow = _scannedArea[3].getRow() + 1;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol() + i + 1);
			}
		} else if (dir.equals(Direction.UP)) {
			maxRow = _scannedArea[3].getRow() + 5;
			for(int i = 0; i < 4; i++)
			{
				lowerSide[i] = new Coordinates2D(maxRow, _scannedArea[i].getCol());
			}
		}
		
		return lowerSide;			
	}

	private Coordinates2D[] rightSensor(Coordinates2D[] _scannedArea, Direction dir) 
	{
		Coordinates2D[] rightSide = new Coordinates2D[4];
		int maxCol = 0;
		
		if(dir.equals(Direction.UP))
		{
			maxCol = _scannedArea[3].getCol() + 1;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow() + i + 1, maxCol);
			}
		} else if (dir.equals(Direction.DOWN)){
			maxCol = _scannedArea[3].getCol() + 1;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow() - i - 1, maxCol);
			}
		} else if (dir.equals(Direction.LEFT)) {
			maxCol = _scannedArea[0].getCol() + 5;
			for(int i = 0; i < 4; i++)
			{
				rightSide[i] = new Coordinates2D(_scannedArea[i].getRow(), maxCol);
			}
		}
		
		return rightSide;		
	}

	private Coordinates2D[] leftSensor(Coordinates2D[] _scannedArea, Direction dir) 
    {
		Coordinates2D[] leftSide = new Coordinates2D[4];
		int minCol = 0;
		
		if(dir.equals(Direction.UP))
		{
			minCol = _scannedArea[0].getCol() - 1;
			for(int i = 0; i < 4; i++)
			{
				leftSide[i] = new Coordinates2D(_scannedArea[i].getRow() + i + 1, minCol);
			}
		} else if (dir.equals(Direction.DOWN)){
				minCol = _scannedArea[0].getCol() - 1;
				for(int i = 0; i < 4; i++)
				{
					leftSide[i] = new Coordinates2D(_scannedArea[i].getRow() - i - 1, minCol);
			}
		} else if (dir.equals(Direction.RIGHT)) {
			minCol = _scannedArea[0].getCol() - 5;
			for(int i = 0; i < 4; i++)
			{
				leftSide[i] = new Coordinates2D(_scannedArea[i].getRow(), minCol);
			}
		}

		return leftSide;
    }
	
	private boolean hitWall(Coordinates2D[] scannedCircleArea) 
	{
		for(int i = 0; i < scannedCircleArea.length; i++)
		{
			if(checkIfObstacleOrWallIsAround(scannedCircleArea[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean checkIfObstacleOrWallIsAround(Coordinates2D scannedUnit)
	{
		if(Table.getMarkedObstacles(scannedUnit.getRow(), scannedUnit.getCol())
				|| scannedUnit.getRow() < 0 || scannedUnit.getRow() >= Table._rows
				|| scannedUnit.getCol() < 0 || scannedUnit.getCol() >= Table._cols)
		{
			return true;
		}
		
		return false;
	}
	
	private void followWall(Coordinates2D[] scannedCircleArea) 
	{
		Coordinates2D[] upside = new Coordinates2D[4];
		Coordinates2D[] leftside = new Coordinates2D[4];
		Coordinates2D[] bottomside = new Coordinates2D[4];
		Coordinates2D[] rightside = new Coordinates2D[4];

		for(int i = 0; i < 4; i++)
		{
			upside[i] = scannedCircleArea[i];
			leftside[i] = scannedCircleArea[i+4];
			bottomside[i] = scannedCircleArea[i+8];
			rightside[i] = scannedCircleArea[i+12];
		}
		
		//Move along the wall if there is a wall scanned left or right from the robot
		for(int i = 0; i < leftside.length; i++)
		{
			if(Table.getMarkedObstacles(leftside[i].getRow(), leftside[i].getCol()) || Table.getMarkedObstacles(rightside[i].getRow(), rightside[i].getCol()))
			{		
				switch(Movement._dir) {
				case DOWN: Robot.getMovement().moveForward();
					break;
				case UP: Robot.getMovement().moveForward();
					break;
				case LEFT: Robot.getMovement().turnRight();
					break;
				case RIGHT: Robot.getMovement().turnRight();
					break;
				default:
					break;		
				}
			}
		}
	}
    
	
	private boolean reachedStoppingCriteria() {
		return false;
	}
}
