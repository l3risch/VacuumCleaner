package Algorithms;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Listener.StartAlgorithm;
import Objects.Node;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import main.MainFrame;


public class Spiral extends Basic implements ActionListener {

	private MainFrame _frame; 
	private int _actualRow;
	private int _actualCol;
	private Coordinates2D[] _encircledArea = new Coordinates2D[16];
	
	Coordinates2D _nn = new Coordinates2D(0, 0);
	
	private int _movesToNN = 1;
	private List<Node> _shortestPath;
	private boolean _pathCalculated = false;

	boolean _nnVisited = true;

	public Spiral(MainFrame frame)
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
		
		_nn = NearestNeighbour.getNearestNeighbour(actualRow, actualCol);
		
		
		if(totallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(totallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.LEFT) && !_pathCalculated)
		{
			Robot.getMovement().turnLeft();
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.FRONT) && !_pathCalculated)
		{
			Robot.getMovement().moveForward();
			
		} else if(partiallyFreeDirection(encircledArea, ScanDirection.RIGHT) && !_pathCalculated)
		{
			Robot.getMovement().turnRight();
			Robot.getMovement().moveForward();
			
		} else if(totallyCovered(encircledArea))
		{
			if(!_pathCalculated)
			{
				//Calculate shortest route to nearest neighbour
				_shortestPath = DijsktraAlgorithm.computePath(actualRow, actualCol, _nn);
				_pathCalculated = true;
			}

			else
			{			
			if(_shortestPath != null)
			{
				if(!DijsktraAlgorithm.nnReached(actualRow, actualCol))
				{
					Node currentNode = _shortestPath.get(_movesToNN);
					
					if(_movesToNN < _shortestPath.size() - 1)
					{
						Node nextNode = _shortestPath.get(_movesToNN+1);
	//					System.out.println("Current Node: " + _shortestPath.get(_movesToNN ).x + ", " +_shortestPath.get(_movesToNN).y);
						Robot.getMovement().setX(100 + 10 * currentNode.y);
						Robot.getMovement().setY(110 + 10 * currentNode.x);
						
						if(_movesToNN < _shortestPath.size()-1)
						{
							//Determine angle 
							if(currentNode.x == nextNode.x - 1)
							{
								Robot.getMovement()._ang = 90;
							} else if(currentNode.y == nextNode.y - 1)
							{
								Robot.getMovement()._ang = 0;
							} else if(currentNode.x == nextNode.x + 1)
							{
								Robot.getMovement()._ang = 270;
							} else if(currentNode.y == nextNode.y + 1)
							{
								Robot.getMovement()._ang = 180;
							} 
							
						}
					} else {
						Robot.getMovement().setX(100 + 10 * currentNode.y);
						Robot.getMovement().setY(110 + 10 * currentNode.x);
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

 
    
	
//	private boolean reachedStoppingCriteria(int x, int y, double ang) 
//	{
//
//		if((System.currentTimeMillis() / 1000l) - StartAlgorithm._start > 180)
//		{
//			return true;
//		} else {
//			
//			if(Robot.getX()== x && Robot.getY() == y && ang == Robot.getMovement().getAng())
//			{
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
	
		
  }
