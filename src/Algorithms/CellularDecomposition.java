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
	private enum ScanDirection{LEFT, BACK, RIGHT};

	public CellularDecomposition(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		
		boolean unaccesableField;

		unaccesableField = getSensorData(_actualRow, _actualCol);
		
		if(unaccesableField)
		{
			if(!freeDirection(getScannedArea(_actualRow, _actualCol), ScanDirection.LEFT)
					&& !freeDirection(getScannedArea(_actualRow, _actualCol), ScanDirection.BACK)
					&& !freeDirection(getScannedArea(_actualRow, _actualCol), ScanDirection.RIGHT))
			{
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnRight();
				Robot.getMovement().turnRight();
				StartAlgorithm._timer.start();				

				while(!isObstacleOrEndOfMap(_actualRow, _actualCol))
				{
					Robot.getMovement().moveForward();
					_frame.repaint();
				}
			} else {
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnRight();
				StartAlgorithm._timer.start();
			}
		} else {
			if(freeDirection(getScannedArea(_actualRow, _actualCol), ScanDirection.LEFT))
			{
				Robot.getMovement().turnLeft();
			}
			Robot.getMovement().moveForward();
		}

		_frame.repaint();
	}	
	
	
	
	
	private boolean freeDirection(Coordinates2D[] scannedArea, ScanDirection direction) {
		
		Coordinates2D[] left = new Coordinates2D[4];
		Coordinates2D[] right = new Coordinates2D[4];
		Coordinates2D[] back = new Coordinates2D[4];

		
		switch(Movement._dir) {
		case UP: 
			for(int i = 0; i < 4; i++)
			{
				left[i] = scannedArea[i+4];
				back[i] = scannedArea[i+8];
				right[i] = scannedArea[i+12];
			}
			break;
		case LEFT:
			for(int i = 0; i < 4; i++)
			{
				left[i] = scannedArea[i+8];
				back[i] = scannedArea[i+12];
				right[i] = scannedArea[i];
			}
			break;
		case DOWN:
			for(int i = 0; i < 4; i++)
			{
				left[i] = scannedArea[i+12];
				back[i] = scannedArea[i];
				right[i] = scannedArea[i+4];
			}
			break;
		case RIGHT:
			for(int i = 0; i < 4; i++)
			{
				left[i] = scannedArea[i];
				back[i] = scannedArea[i+4];
				right[i] = scannedArea[i+8];
			}
			break;
		}
		
		Coordinates2D[] scanArea = new Coordinates2D[4];
		switch(direction)
		{
		case LEFT: scanArea = left;
			break;
		case BACK: scanArea = back;
			break;
		case RIGHT: scanArea = right;
			break;
		}
		
		for(int i = 0; i < scanArea.length; i++)
		{
			try {
				if(Table.getMarkedObstacles(scanArea[i].getRow(), scanArea[i].getCol()))
				{
					return false;
				}
				
				if(Table._markedPath[scanArea[i].getRow()][scanArea[i].getCol()])
				{
					return false;
				}
				
			} catch(ArrayIndexOutOfBoundsException e)
			{
				e.getStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override 
	boolean getSensorData(int row, int col)
	{
		Coordinates2D[] scannedArea = getScannedArea(row, col);
		Coordinates2D[] frontOfRobot = getFrontOfRobot(scannedArea);

		boolean unaccesable = false;
		
		for(int i = 0; i < frontOfRobot.length; i++)
		try {

			if(Table.getMarkedObstacles(frontOfRobot[i].getRow(), frontOfRobot[i].getCol()))
			{
				unaccesable = true;
			}
			
			if(Table._markedPath[frontOfRobot[i].getRow()][frontOfRobot[i].getCol()])
			{
				unaccesable = true;
			}
			
		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			unaccesable = true;
		}
		
		return unaccesable;
	}
	
	private boolean isObstacleOrEndOfMap(int row, int col)
	{
		Coordinates2D[] scannedArea = getScannedArea(row, col);
		Coordinates2D[] frontOfRobot = getFrontOfRobot(scannedArea);

		boolean unaccesable = false;
		
		for(int i = 0; i < frontOfRobot.length; i++)
		try {

			if(Table.getMarkedObstacles(frontOfRobot[i].getRow(), frontOfRobot[i].getCol()))
			{
				unaccesable = true;
			}
			
		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			unaccesable = true;
		}
		
		return unaccesable;
	}
	
	
	private Coordinates2D[] getFrontOfRobot(Coordinates2D[] scannedCircleArea) 
	{
		Coordinates2D[] frontOfRobot = new Coordinates2D[4];

		switch(Movement._dir) {
		case UP:
			for(int i = 0; i < 4; i++)
			{
				frontOfRobot[i] = scannedCircleArea[i];
			};
		break;
		case LEFT:
			for(int i = 0; i < 4; i++)
			{
				frontOfRobot[i] = scannedCircleArea[i+4];
			};
			break;
		case DOWN:
			for(int i = 0; i < 4; i++)
			{
				frontOfRobot[i] = scannedCircleArea[i+8];
			};
			break;
		case RIGHT:
			for(int i = 0; i < 4; i++)
			{
				frontOfRobot[i] = scannedCircleArea[i+12];
			};
			break;
		default:
			break;
		}
		
		return frontOfRobot;
	}
	
	@Override
	Coordinates2D[] getScannedArea(int row, int col) 
	{
		//Get scanned area in front of the robot
		Coordinates2D[] scannedArea = super.getScannedArea(row, col);
		
		Coordinates2D[] circledArea = new Coordinates2D[16];
		
		//Determine the scanned area encirceling the robot
		double ang = Movement.getAng();
		if(ang % 90 == 0)
		{
			switch(Movement._dir) {
			case DOWN: circledArea = getAreaIfDown(scannedArea);
			System.out.println("DOWN: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfDown(scannedArea)[i].getRow()+", " +getAreaIfDown(scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case LEFT: circledArea = getAreaIfLeft(scannedArea);
			System.out.println("Left: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfLeft(scannedArea)[i].getRow()+", " +getAreaIfLeft(scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case RIGHT: circledArea = getAreaIfRight(scannedArea);
			System.out.println("Right: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfRight(scannedArea)[i].getRow()+", " +getAreaIfRight(scannedArea)[i].getCol());
			}
			System.out.println("\n");
				break;
			case UP: circledArea = getAreaIfUp(scannedArea);
			System.out.println("UP: ");
			for(int i = 0; i < 16; i++) {
					System.out.println(getAreaIfUp(scannedArea)[i].getRow()+", " +getAreaIfUp(scannedArea)[i].getCol());
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

    
	
	private boolean reachedStoppingCriteria() {
		return false;
	}
}
