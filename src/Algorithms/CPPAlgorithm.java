package Algorithms;

import java.util.List;

import Algorithms.Basic.CellState;
import Listener.StartAlgorithm;
import Objects.Node;
import Objects.Robot;
import Physics.Coordinates2D;
import main.MainFrame;

public class CPPAlgorithm extends Basic{
	
	protected MainFrame _frame; 
	protected int _actualRow;
	protected int _actualCol;
	protected Coordinates2D[] _encircledArea = new Coordinates2D[16];

	
	protected Coordinates2D _nn = new Coordinates2D(0, 0);
	
	protected int _movesToNN = 1;
	protected List<Node> _shortestPath;
	protected boolean _pathCalculated = false;
	
	protected boolean _nnVisited = true;

	protected void backtrack(int actualRow, int actualCol) {
		_nn = NearestNeighbour.getNearestNeighbour(actualRow, actualCol);		

		if(!_pathCalculated)
		{
			//Calculate shortest route to nearest neighbour
			_shortestPath = DijkstraAlgorithm.computePath(actualRow, actualCol, _nn);
			_pathCalculated = true;
		}

		else
		{			
		if(_shortestPath != null)
		{
			if(!DijkstraAlgorithm.nnReached(actualRow, actualCol))
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
	}

	protected boolean reachedStoppingCriteria() 
	{
		if((System.currentTimeMillis() / 1000l) - StartAlgorithm._start > 5)
		{
			return true;
		} else if(Robot.getMovement()._totalDistance > 4000)
		{
			return true;
		}  
		
		for(String key : _mentalMap.keySet())
		{
			if(_mentalMap.get(key).equals(CellState.FREE))
			{
				return false;
			}
		}
		
		return true;
	}
}
