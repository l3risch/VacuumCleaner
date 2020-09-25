package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import Algorithms.Basic.CellState;
import Algorithms.Basic.ScanDirection;
import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import Physics.Sweeper;
import Rendering.Renderer1;
import main.MainFrame;
import enums.Direction;


public class CustomAlgorithm extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();
	private Coordinates2D _oldNearestNeighbour = new Coordinates2D(0, 0);



	public CustomAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
		
		Coordinates2D nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
		
		
		if(reachedStoppingCriteria())
		{
			StartAlgorithm._timer.stop();
		}

		if(totallyFreeDirection(_encircledArea, ScanDirection.LEFT))
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(_encircledArea, ScanDirection.FRONT))
		{
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
			
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.LEFT))
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.FRONT))
		{
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} else if(totallyCovered(_actualRow, _actualCol, _encircledArea))
		{
			if(super.isFrontAccesable(_actualRow, _actualCol))
			{
				System.out.println("NN: " + nearestNeighbour.getRow() +", " + nearestNeighbour.getCol() +"\nOld: " + _oldNearestNeighbour.getRow() + ", " + _oldNearestNeighbour.getCol() + "\n\n");
				//Check if nearest neighbour has changed
				if(nearestNeighbour.getRow() != _oldNearestNeighbour.getRow() && nearestNeighbour.getCol() != _oldNearestNeighbour.getCol())
				{
					_pathDeterminer.turnToNearestNeighbour(nearestNeighbour);
					System.out.println(Movement.getAng());
					_oldNearestNeighbour = nearestNeighbour;
				}
					Robot.getMovement().moveForward();
			} else 
			{
				if(Movement.getAng()%90 != 0)
				{
					Movement.setAng(Movement.getAng() + (90 - (Movement.getAng()%90)));
				} else 
				{
					Robot.getMovement().turnRight();
				}
			}
	
		}
	
		
		_frame.repaint();
		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol);
		}

	}	
	
	@Override
	boolean isFrontAccesable(int row, int col)
	{
		Coordinates2D[] scannedArea = getEncircledScannedArea(row, col);
		Coordinates2D[] frontOfRobot = determineScanDirections(scannedArea, ScanDirection.FRONT);

		boolean accesable = true;
		
		for(int i = 0; i < frontOfRobot.length; i++)
		try {

			if(Table.getMarkedObstacles(frontOfRobot[i].getRow(), frontOfRobot[i].getCol()))
			{
				accesable = false;
			}
			
			if(Table._markedPath[frontOfRobot[i].getRow()][frontOfRobot[i].getCol()])
			{
				accesable = false;
			}
			
		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			accesable = false;
		}
		
		return accesable;
	}

    
	
	private boolean reachedStoppingCriteria() 
	{
		for(int i = 0; i < Table._rows; i++)
		{
			for(int j = 0; j < Table._cols; j++)
			{
				if(Table.getPath(i, j) == true)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static PathDeterminer getPathDeterminer()
	{
		return _pathDeterminer;
	}
}
