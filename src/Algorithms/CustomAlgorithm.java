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
	Coordinates2D _nearestNeighbour = new Coordinates2D(0, 0);

	boolean _nnVisited = true;

	public CustomAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
				
		if(reachedStoppingCriteria())
		{
			StartAlgorithm._timer.stop();
		}
		
		if(totallyFreeDirection(_encircledArea, ScanDirection.LEFT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(_encircledArea, ScanDirection.FRONT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
			
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.LEFT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.FRONT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(_encircledArea, ScanDirection.RIGHT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} else if(totallyCovered(_encircledArea))
		{
			if(super.isFrontAccesable(_actualRow, _actualCol))
			{
				String key = generateKey(_nearestNeighbour.getRow(), _nearestNeighbour.getCol());

				if( _mentalMap.get(key).equals(CellState.VISITED))
				{
					System.out.println(_nearestNeighbour.getRow() +", " +_nearestNeighbour.getCol() + " visited");
					_nnVisited = true;
					_nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
					System.out.println("NEW: "+_nearestNeighbour.getRow() +", " +_nearestNeighbour.getCol());
				}
				if(_nnVisited)
				{
					_pathDeterminer.turnToNearestNeighbour(_nearestNeighbour);
					System.out.println("Ang: "+Movement.getAng());
					_nnVisited = false;
				}
			}
			
			if(super.isFrontAccesable(_actualRow, _actualCol))
			{
				Robot.getMovement().moveForward();
			} else 
			{
				//TODO: Wall Following
				//TODO: After Obstacle is bypassed and nn is not visited, new Path has to be calcuated
				
				while(!super.isFrontAccesable(_actualRow, _actualCol))
				{
					if(Movement.getAng()%90 != 0)
					{
						Movement.setAng(Movement.getAng() + (90 - (Movement.getAng()%90)));
					} else 
					{
						Robot.getMovement().turnRight();
					}
				}
				Robot.getMovement().moveForward();
			}
	
		}
	
		
		_frame.repaint();
		
		_nearestNeighbour = _pathDeterminer.getNearestNeighbour(_actualRow, _actualCol);
		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol);
		}
		
	}	
	
	private void roundAngle(double ang) 
	{
		double rest = (ang%90) / 90;
		double roundAngle;
		if(rest >= 0.5)
		{
			roundAngle = ang  + (90 - (ang%90));
		} else {
			roundAngle = ang - (ang%90);
		}
		Movement.setAng(roundAngle);
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
