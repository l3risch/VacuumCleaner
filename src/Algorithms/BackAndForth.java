package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import Objects.Node;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import main.MainFrame;


public class BackAndForth extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	private static PathDeterminer _pathDeterminer = new PathDeterminer();
	Coordinates2D _nearestNeighbour = new Coordinates2D(0, 0);
	private int _movesToNN = 1;
	private List<Node> _shortestPath;
	private boolean _pathCalculated = false;

	boolean _nnVisited = true;

	public BackAndForth(MainFrame frame)
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
		
		if(actualRow >= 0 && actualCol >= 0)
		{
			updateMap(actualRow, actualCol, mentalMap);
		}
		
		_nearestNeighbour = _pathDeterminer.getNearestNeighbour(actualRow, actualCol);		
		
		
		if(totallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				Robot.getMovement().moveForward();
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
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
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
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			if(Movement.getAng()%90 != 0)
			{
				roundAngle(Movement.getAng());
			}
			Robot.getMovement().turnLeft();
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

	
  }
