package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
	private boolean _bypass = false;
	private String _key;


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
				
		determineRoute(_actualRow, _actualCol, _encircledArea, _mentalMap, false);
		_frame.repaint();

	}	
	
	private void determineRoute(int actualRow, int actualCol, Coordinates2D[] encircledArea, Map<String, CellState> mentalMap, boolean countMoves) {
		if(reachedStoppingCriteria())
		{
			StartAlgorithm._timer.stop();
		}
		
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.FRONT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT))
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} else if(totallyCovered(encircledArea))
		{
			
			_key = generateKey(_nearestNeighbour.getRow(), _nearestNeighbour.getCol());

			if(mentalMap.get(_key).equals(CellState.VISITED))
			{
				_nnVisited = true;
				_nearestNeighbour = _pathDeterminer.getNearestNeighbour(actualRow, actualCol);
			}
			if(_nnVisited)
			{
				_pathDeterminer.turnToNearestNeighbour(_nearestNeighbour);
				_nnVisited = false;
			}

			bypassObstacle(encircledArea, actualRow, actualCol, countMoves);
	
		}
			
		_nearestNeighbour = _pathDeterminer.getNearestNeighbour(actualRow, actualCol);
		
		if(actualRow >= 0 && actualCol >= 0)
		{
			updateMap(actualRow, actualCol, mentalMap);
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
	
	private void bypassObstacle(Coordinates2D[] encircledArea, int row, int col, boolean countMoves) {
		
		
		if(super.isFrontAccesable(row, col))
		{
			if(freeDirection(encircledArea, ScanDirection.RIGHT) && _bypass == true)
			{
				Robot.getMovement().turnRight();
				_bypass = false;
				_nnVisited = true;
			}
			Robot.getMovement().moveForward();
		} else 
		{
			//TODO: Wall Following
			//TODO: After Obstacle is bypassed and nn is not visited, new Path has to be calcuated
			if(!countMoves)
			{
				calcRoute();
			}
			bypassLeft();
			_bypass = true;

		}		
	}
	
	private void bypassRight()
	{
		
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
		if(freeDirection(_encircledArea, ScanDirection.LEFT))
		{
			Robot.getMovement().turnLeft();
		} 
		
	}
	
	private void bypassLeft()
	{		

		while(!super.isFrontAccesable(_actualRow, _actualCol))
		{
			if(Movement.getAng()%90 != 0)
			{
				Movement.setAng(Movement.getAng() - (Movement.getAng()%90));
			} else 
			{
				Robot.getMovement().turnLeft();
			}
		}
		Robot.getMovement().moveForward();
		
		_encircledArea = getEncircledScannedArea(_actualRow, _actualCol);
		if(freeDirection(_encircledArea, ScanDirection.RIGHT))
		{
			Robot.getMovement().turnRight();
		} 
		
	}
	
	private int calcRoute()
	{
		int actualRow = _actualRow;
		int actualCol = _actualCol;
		Coordinates2D[] encircledArea = _encircledArea;
		Map<String, CellState> mentalMap = new HashMap<String, CellState>();
		
		mentalMap.putAll(_mentalMap);
 		
		int x = Robot.getX();
		int y = Robot.getY();
		double ang = Movement.getAng();
		
		int counter = 0;

		
		while(!mentalMap.get(_key).equals(CellState.VISITED) && counter < 500)
		{
			actualCol = Robot.getXasCol();
			actualRow = Robot.getYasRow();
			encircledArea = getEncircledScannedArea(actualRow, actualCol);
			
			determineRoute(actualRow, actualCol, encircledArea, mentalMap, true);	
			counter++;
		}
		
		Movement.setX(x);
		Movement.setY(y);
		Movement.setAng(ang);
		
		return counter;
	}
	
  }
