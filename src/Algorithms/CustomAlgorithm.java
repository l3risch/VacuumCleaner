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
				_pathDeterminer.turnToNearestNeighbour(nearestNeighbour);
			}
			
			if(super.isFrontAccesable(_actualRow, _actualCol))
			{
				Robot.getMovement().moveForward();
			} else 
			{
				while(!super.isFrontAccesable(_actualRow, _actualCol))
				{
					if(Movement.getAng()%90 != 0)
					{
						Movement.setAng(Movement.getAng() + (90 - (Movement.getAng()%90)));
						System.out.println(Movement.getAng());
					} else 
					{
						Robot.getMovement().turnRight();
					}
				}
				Robot.getMovement().moveForward();
			}
	
		}
	
		
		_frame.repaint();
		
		if(_actualRow >= 0 && _actualCol >= 0)
		{
			updateMap(_actualRow, _actualCol);
		}
		
//		for(int i = 0; i < 4; i++)
//		{
//			for(int j = 0; j < 4; j++)
//			{
//				System.out.println(Robot.getCoordinates(_actualRow, _actualCol)[i][j].getRow() +", "+Robot.getCoordinates(_actualRow, _actualCol)[i][j].getCol());
//			}
//		}
		
//		Coordinates2D[] front = getScannedArea(_actualRow, _actualCol, ScanDirection.FRONT);
//		for(int i = 0; i <4; i++)
//		{
//			System.out.println("front area("+i+"): "+front[i].getRow()+", "+ front[i].getCol());
//		}
//		Coordinates2D[] left = getScannedArea(_actualRow, _actualCol, ScanDirection.LEFT);
//		for(int i = 0; i <4; i++)
//		{
//			System.out.println("left area("+i+"): "+left[i].getRow()+", "+ left[i].getCol());
//		}
//		Coordinates2D[] right = getScannedArea(_actualRow, _actualCol, ScanDirection.RIGHT);
//		for(int i = 0; i <4; i++)
//		{
//			System.out.println("right area("+i+"): "+right[i].getRow()+", "+ right[i].getCol());
//		}
//		Coordinates2D[] back = getScannedArea(_actualRow, _actualCol, ScanDirection.BACK);
//		for(int i = 0; i <4; i++)
//		{
//			System.out.println("back area("+i+"): "+back[i].getRow()+", "+ back[i].getCol());
//		}
	}	
	
	private void roundAngle(double ang) 
	{
		System.out.println("ang: "+ang);
		double rest = (ang%90) / 90;
		System.out.println("rest: " + rest);
		double roundAngle;
		if(rest >= 0.5)
		{
			roundAngle = ang  + (90 - (ang%90));
		} else {
			roundAngle = ang - (ang%90);
		}
		System.out.println("round: "+roundAngle);
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
