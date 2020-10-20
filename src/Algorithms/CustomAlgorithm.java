package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import main.MainFrame;


public class CustomAlgorithm extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();
	Coordinates2D _nearestNeighbour = new Coordinates2D(0, 0);
	private boolean _bypass = false;
	private String _key;
	private int _movesToNN = 1;
	private List<Node> _shortestPath;
	private boolean _pathCalculated = false;

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
				
		determineRoute(_actualRow, _actualCol, _encircledArea, _mentalMap);
		_frame.repaint();

	}	
	
	private void determineRoute(int actualRow, int actualCol, Coordinates2D[] encircledArea, Map<String, CellState> mentalMap) {
		System.out.println("\nactual row: " +  actualRow +"\nactual col: " + actualCol + "\n");
		
		if(actualRow >= 0 && actualCol >= 0)
		{
			updateMap(actualRow, actualCol, mentalMap);
		}
		
		_nearestNeighbour = _pathDeterminer.getNearestNeighbour(actualRow, actualCol);
		
		int x = Robot.getX();
		int y = Robot.getY();
		double ang = Movement.getAng();
		
		
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().moveForward();
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
		} else if(totallyCovered(encircledArea))
		{
			if(!_pathCalculated)
			{
				//Calculate shortest route to nearest neighbour
				_shortestPath = ShortestPath.computePath(actualRow, actualCol, _nearestNeighbour);
				_pathCalculated = true;
			}

			else
			{			
			if(_shortestPath != null)
			{
				if(!ShortestPath.nnReached(actualRow, actualCol))
				{
					Node currentNode = _shortestPath.get(_movesToNN);
					
					if(_movesToNN < _shortestPath.size() - 1)
					{
						Node nextNode = _shortestPath.get(_movesToNN+1);
	//					System.out.println("Current Node: " + _shortestPath.get(_movesToNN ).x + ", " +_shortestPath.get(_movesToNN).y);
						Movement.setX(100 + 10 * currentNode.y);
						Movement.setY(110 + 10 * currentNode.x);
						
						if(_movesToNN < _shortestPath.size()-1)
						{
							//Determine angle 
							if(currentNode.x == nextNode.x - 1)
							{
								Movement._ang = 90;
							} else if(currentNode.y == nextNode.y - 1)
							{
								Movement._ang = 0;
							} else if(currentNode.x == nextNode.x + 1)
							{
								Movement._ang = 270;
							} else if(currentNode.y == nextNode.y + 1)
							{
								Movement._ang = 180;
							} 
							
						}
					} else {
						Movement.setX(100 + 10 * currentNode.y);
						Movement.setY(110 + 10 * currentNode.x);
					}
					
					_movesToNN++;

				} else {
					_movesToNN = 0;
					_pathCalculated = false;
				}
			} else {
				_pathCalculated = false;
				_movesToNN = 0;
			}
		}
		} else {
			Robot.getMovement().moveForward();
			_pathCalculated = false;
			_movesToNN = 0;
		}
		
				
		
		
		
//		if(reachedStoppingCriteria(x, y, ang))
//		{
//			StartAlgorithm._timer.stop();
//		}	
		
		
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

    
	
//	private boolean reachedStoppingCriteria(int x, int y, double ang) 
//	{
//
//		if((System.currentTimeMillis() / 1000l) - StartAlgorithm._start > 180)
//		{
//			return true;
//		} else {
//			
//			if(Robot.getX()== x && Robot.getY() == y && ang == Movement.getAng())
//			{
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
	
	public static PathDeterminer getPathDeterminer()
	{
		return _pathDeterminer;
	}
	
	private void bypassObstacle(Coordinates2D[] encircledArea, int row, int col, boolean countMoves, ScanDirection dir) {
		
		
		if(super.isFrontAccesable(row, col))
		{
			if(freeDirection(encircledArea, ScanDirection.RIGHT) && _bypass == true)
			{
				if(dir == ScanDirection.LEFT)
				{
					Robot.getMovement().turnRight();
				} else if(dir == ScanDirection.RIGHT)
				{
					Robot.getMovement().turnLeft();
				}
				_bypass = false;
				_nnVisited = true;
			}
			Robot.getMovement().moveForward();
		} else 
		{
			if(dir == ScanDirection.LEFT)
			{
				bypassLeft();
			} else if(dir == ScanDirection.RIGHT)
			{
				bypassRight();
			}
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
	
	private ScanDirection calcRoute()
	{
		int actualRow = _actualRow;
		int actualCol = _actualCol;
		Coordinates2D[] encircledArea = _encircledArea;
		Map<String, CellState> mentalMapRight = new HashMap<String, CellState>();
		Map<String, CellState> mentalMapLeft = new HashMap<String, CellState>();

		mentalMapRight.putAll(_mentalMap);
		mentalMapLeft.putAll(_mentalMap);
 		
		int rightCount = calcDistance(mentalMapRight, actualRow, actualCol, encircledArea, ScanDirection.RIGHT);
		int leftCount = calcDistance(mentalMapLeft, actualRow, actualCol, encircledArea, ScanDirection.LEFT);
		
		//System.out.println("Left: "+leftCount+"\nRight:"+rightCount);
		if(leftCount <= rightCount)
		{
			return ScanDirection.LEFT;
		} else {
			return ScanDirection.RIGHT;
		}
	}
	
	//Count distance in left and right direction
	private int calcDistance(Map<String, CellState> mentalMap, int row, int col, Coordinates2D[] encircledArea, ScanDirection dir)
	{
		int counter = 0;
		int x = Robot.getX();
		int y = Robot.getY();
		double ang = Movement.getAng();

		//Set initial direction
		if(dir == ScanDirection.LEFT)
		{
			Robot.getMovement().turnLeft();
			//System.out.println(ang);
		} else {
			Robot.getMovement().turnRight();
		}
		
		
		while(!mentalMap.get(_key).equals(CellState.VISITED) && counter < 500)
		{
			col = Robot.getXasCol();
			row = Robot.getYasRow();
			encircledArea = getEncircledScannedArea(row, col);
			//determineRoute(row, col, encircledArea, mentalMap, dir, true);	
			_pathDeterminer.turnToNearestNeighbour(_nearestNeighbour);
			ang = Movement.getAng();

//			if(totallyCovered(_encircledArea))
//			{
//				if(super.isFrontAccesable(row, col))
//				{
//					Robot.getMovement().moveForward();
//				} else {
//					if(Movement.getAng()%90 != 0)
//					{
//						Movement.setAng(Movement.getAng() - (Movement.getAng()%90));
//					} else 
//					{
//						Robot.getMovement().turnLeft();
//					}
//					Robot.getMovement().moveForward();
//				}
//				System.out.println(Robot.getYasRow());
//				System.out.println(Robot.getXasCol());
//			
//			}

			if(freeDirection(encircledArea, ScanDirection.RIGHT))
			{
				Robot.getMovement().turnRight();
			} else {
				
				if(isFrontAccesable(row, col))
				{
					Robot.getMovement().moveForward();
				} else {
					Robot.getMovement().turnLeft();
				}
			}
		}
		
		counter++;
		
		Movement.setX(x);
		Movement.setY(y);
		Movement.setAng(ang);
		
		return counter;
	}
	
		
  }
