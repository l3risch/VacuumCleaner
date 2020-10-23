package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import Algorithms.Basic.CellState;
import Algorithms.Basic.ScanDirection;
import Objects.Graph;
import Objects.Node;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;

public class ShortestPath extends Basic{

	static List<Node> _nodeList = new LinkedList<Node>();
	private static char[][] _matrix;
	private static Coordinates2D _nn;
	public static List<Node> adjustedPath = new LinkedList<Node>();

	 public static List<Node> computePath(int robotRow, int robotCol, Coordinates2D nn)
	 {
		 _nodeList.clear();
		 _nn = nn;
		 _matrix = new char[64][64];
		 
		 //Transform Hash Map to char matrix
		 for(String key : _mentalMap.keySet())
		 {
			 int row = Integer.parseInt(key.substring(0, 2));
			 int col = Integer.parseInt(key.substring(2, 4));
			 if(_mentalMap.get(key).equals(CellState.FREE) || _mentalMap.get(key).equals(CellState.VISITED))
 			 {
				 _matrix[row][col] = '1';
 			 }
			 if(_mentalMap.get(key).equals(CellState.OCCUPIED))
 			 {
				 _matrix[row][col] = '0';
 	 		 }
		 }
		 
		 //Set all unknown cells as obstacles '0'
		 for(int i = 0; i < 64; i++)
		 {
			 for(int j = 0; j < 64; j++)
			 {
				 if(_matrix[i][j] == 0)
				 {
					 _matrix[i][j] = '0';
				 }
			 }		
		 }

		 //Adjust obstacles to match with robot size by extending each obstacle by 3 cells in width and height
		 List<Coordinates2D> rowList = new ArrayList<Coordinates2D>();
		 List<Coordinates2D> colList = new ArrayList<Coordinates2D>();

		 for(int i = 0; i < 61; i++)
		 {
			 for(int j = 3; j < 64; j++)
			 {
				 if(_matrix[i][j] == '0')
				 {
					 for(int k = 1; k < 4; k++)
					 {
						 rowList.add(new Coordinates2D(i+k,j));
						 colList.add(new Coordinates2D(i,j-k));
					 }
				 }
			 }		
		 }
		 
		 for(Coordinates2D coor : rowList)
		 {
			 for(int i = 0; i < 4; i++)
			 {
				 _matrix[coor.getRow()][coor.getCol()-i] = '0';
			 }
		 }
		 
		 for(Coordinates2D coor : colList)
		 {
			 for(int i = 0; i < 4; i++)
			 {
				 _matrix[coor.getRow()+i][coor.getCol()] = '0';
			 }
		 }
		 		 
		 //Set position of robot as source
		 _matrix[robotRow][robotCol] = 'S';
		 
		 //Set position of nearest neighbour as destination
		if(_nn.getRow() < 61 && _nn.getCol() >= 0)
		{
			if(_matrix[_nn.getRow()+3][_nn.getCol()-3] != '0')
			{
				_matrix[_nn.getRow()+3][_nn.getCol()-3] = 'D';
				
			} else if(_matrix[_nn.getRow()+3][_nn.getCol()] != '0')
			{
				_matrix[_nn.getRow()+3][_nn.getCol()] = 'D';
				
			} else if(_matrix[_nn.getRow()][_nn.getCol()-3] != '0')
			{
				_matrix[_nn.getRow()][_nn.getCol()-3] = 'D';
				
			} else {
				_matrix[_nn.getRow()][_nn.getCol()] = 'D';
			}
		} else {
			_matrix[_nn.getRow()][_nn.getCol()] = 'D';
		}
		
		
//		 for(int i = 0; i < 64; i++)
//		 {
//			 StringBuilder sb = new StringBuilder();
//			 for(int j = 0; j < 64; j++)
//			 {
//				 sb.append(_matrix[i][j]);
//			 }
//			 System.out.println(sb);
//		 }
//		System.out.println("\n\n");
       boolean exists = pathExists(_matrix, robotRow, robotCol);		
		
		if(exists)
	    {
		  List<Node> dijkstraPath = computeDijkstraPath();
		  //adjustedPath = adjustDijkstraPath(dijkstraPath);
//		  System.out.println("Disjktra");
//		  for(Node node: dijkstraPath)
//		  {
//			  System.out.println(node.x + ", "+ node.y);
//		  }
		  
		  return dijkstraPath;
	    } else {
	    	return null;
	    }
    }
	 


	private static boolean pathExists(char[][] matrix, int row, int col) {
		
		Node source = new Node(row, col);
		Queue<Node> queue = new LinkedList<Node>();
		
		queue.add(source);

		while(!queue.isEmpty()) {
			Node currentNode = queue.poll();
			
			if(matrix[currentNode.x][currentNode.y]!='0')
			{
				List<Node> neighbourList = addNeighbours(currentNode, matrix);
				queue.addAll(neighbourList);
			
				if(matrix[currentNode.x][currentNode.y] == 'D' ) {
					_nodeList.add(currentNode);
					return true;
				}
				else {
			
					matrix[currentNode.x][currentNode.y]='0';
													
					for(Node node : neighbourList)
					{
						currentNode.addDestination(node);
					}
	            	
					_nodeList.add(currentNode);

				}
			}	
		}
		return false;
	}


public static Coordinates2D getNearestNeighbour(int row, int col) {
		
		Node source = new Node(row, col);
		Queue<Node> queue = new LinkedList<Node>();
		
		queue.add(source);

		while(!queue.isEmpty()) {
			Node currentNode = queue.poll();
			if(_matrix[currentNode.x][currentNode.y]!='0')
			{
				List<Node> neighbourList = addNeighbours(currentNode, _matrix);
				queue.addAll(neighbourList);
			
				String key = generateKey(currentNode.x, currentNode.y);
				if(_mentalMap.get(key) == CellState.FREE ) {
					return new Coordinates2D(currentNode.x, currentNode.y);
				}
				else {
			
					_matrix[currentNode.x][currentNode.y]='0';
													
					for(Node node : neighbourList)
					{
						currentNode.addDestination(node);
					}
	            	
				}
			}	
		}
		return PathDeterminer.getNearestNeighbour(row, col);
	}

	private static List<Node> addNeighbours(Node poped, char[][] matrix) {
		
		List<Node> list = new LinkedList<Node>();
		
		if((poped.x-1 >= 0 && poped.x-1 < matrix.length) && matrix[poped.x-1][poped.y] != '0') {
			list.add(new Node(poped.x-1, poped.y));
		}
		if((poped.x+1 >= 0 && poped.x+1 < matrix.length) && matrix[poped.x+1][poped.y] != '0') {
			list.add(new Node(poped.x+1, poped.y));
		}
		if((poped.y-1 >= 0 && poped.y-1 < matrix.length) && matrix[poped.x][poped.y-1] != '0') {
			list.add(new Node(poped.x, poped.y-1));
		}
		if((poped.y+1 >= 0 && poped.y+1 < matrix.length) && matrix[poped.x][poped.y+1] != '0') {
			list.add(new Node(poped.x, poped.y+1));
		}		
		return list;
	}


	private static List<Node> computeDijkstraPath() 
	{
		Graph graph = new Graph();
		for(Node node : _nodeList)
		{
			graph.addNode(node);
		}
		

		Node dest = _nodeList.get(_nodeList.size()-1);
		dest.getShortestPath().add(dest);
		Node source = _nodeList.get(0);
		graph = calculateShortestPathFromSource(graph, source);
		
		List<Node> shortestPath = dest.getShortestPath();
		shortestPath.add(new Node(_nn.getRow(), _nn.getCol()));
//		System.out.println("Old Path");
//		for(Node node : shortestPath)
//		{
//			System.out.println(node.x + ", " + node.y);
//		}
		return shortestPath;
	}
	
	public static Graph calculateShortestPathFromSource(Graph graph, Node source) 
	{
	    source.setDistance(0);
	 
	    Set<Node> settledNodes = new HashSet<>();
	    Set<Node> unsettledNodes = new HashSet<>();
	 
	    unsettledNodes.add(source);
	 
	    while (unsettledNodes.size() != 0) {
	        Node currentNode = getLowestDistanceNode(unsettledNodes);
	        unsettledNodes.remove(currentNode);
	        for (Node adjacentNode : currentNode.getAdjacentNodes()) {

	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }
	        }
	        settledNodes.add(currentNode);
	    }
	    return graph;
	}
	
	private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.getDistance();
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }

	    return lowestDistanceNode;
	}

	private static void calculateMinimumDistance(Node evaluationNode, Node sourceNode) {
	    Integer sourceDistance = sourceNode.getDistance();
	    if (sourceDistance + 1 < evaluationNode.getDistance()) {
	        evaluationNode.setDistance(sourceDistance + 1);
	        LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
	        shortestPath.add(sourceNode);
	        evaluationNode.setShortestPath(shortestPath);
	    }
	}
	
	public static boolean nnReached(int robotRow, int robotCol)
	{
		
		 Coordinates2D[][] _robotPos = Robot.getCoordinates(robotRow, robotCol);

		 //Set position of nearest neighbour as destination
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(_robotPos[i][j].getRow()==_nn.getRow() && _robotPos[i][j].getCol()==_nn.getCol())
				{
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	/*
	 * Since robot consists of 16 cells instead of 1, the Dijkstra algorithm has to be adjusted
	 */
	private static List<Node> adjustDijkstraPath(List<Node> nodeList)
	{
		//Consider only visited fields by the Dijkstra path until obstacle is hit
		
		boolean pathIsCut = false;
		List<Node> adjustedPath = new LinkedList<Node>();
		adjustedPath.add(nodeList.get(0));
		Coordinates2D[][] nextRobotPosition = new Coordinates2D[4][4];
		
		for(int i = 1; i < nodeList.size()-1; i++)
		{
			Node previousNode = nodeList.get(i-1);
			Node currentNode = nodeList.get(i);
			Node nextNode = nodeList.get(i+1);
			
			int[] lastMove = {currentNode.x - previousNode.x, currentNode.y - previousNode.y};
			nextRobotPosition = Robot.getCoordinates(nextNode.x, nextNode.y);
			if(isHittingObstacle(nextRobotPosition))
			{
				Node adjNode = new Node(currentNode.x  + lastMove[0], currentNode.y + lastMove[1]);
				nextRobotPosition = Robot.getCoordinates(adjNode.x, adjNode.y);
				if(isHittingObstacle(nextRobotPosition))
				{
					for(int j = i; j < nodeList.size(); j++)
					{
						nodeList.remove(j);
					}
					adjustedPath.add(i, currentNode);
					i = nodeList.size()-1;
					pathIsCut = true;
					
				} else {
				
					for(int j = i+1; j < nodeList.size(); j++)
					{
						nodeList.set(j, new Node(nodeList.get(j).x + lastMove[0], nodeList.get(j).y + lastMove[1]));
					}
					
					if(adjustedPath.get(adjustedPath.size()-1).x != currentNode.x || adjustedPath.get(adjustedPath.size()-1).y != currentNode.y)
						adjustedPath.add(currentNode);
						
					adjustedPath.add(adjNode);
					nodeList.add(i+1, adjNode);
				}

				
			} else {
				if(adjustedPath.get(adjustedPath.size()-1).x != currentNode.x || adjustedPath.get(adjustedPath.size()-1).y != currentNode.y)
					adjustedPath.add(currentNode);
			}
			
		}

		if(!pathIsCut)
		{
			adjustedPath.add(nodeList.get(nodeList.size()-1));
		} else {
//			List<Node> restPath = calcRestOfPath(nextRobotPosition, adjustedPath.get(adjustedPath.size()-1));
//			List<Node> restPath = followWall(nextRobotPosition, adjustedPath.get(adjustedPath.size()-1));
			List<Node> restPath = bypassRight(adjustedPath.get(adjustedPath.size()-1));
			for(Node node : restPath)
			{
				adjustedPath.add(node);
			}
			
			System.out.println("Restlicher Pfad:");
			for(Node node : adjustedPath)
			{
				System.out.println(node.x + ", "+ node.y);		
			}
			pathIsCut = false;
		}
		
		
		return adjustedPath;
	}
	
	private static List<Node> followWall(Coordinates2D[][] nextRobotPosition, Node lastNode) 
	{

		if(isTopHittingObstacle(nextRobotPosition))
		{
			bypassRight(lastNode);
		} else if(isRightHittingObstacle(nextRobotPosition))
		{
			
		} else if(isBottomHittingObstacle(nextRobotPosition))
		{
			
		} else if(isLeftHittingObstacle(nextRobotPosition))
		{
			
		}
		return null;
	}

	

	private static List<Node> bypassRight(Node lastNode) 
	{
		List<Node> wallFollowPath = new LinkedList<Node>();
		Coordinates2D[] encircledArea = getEncircledScannedArea(Robot.getYasRow(), Robot.getXasCol());
		double ang = Robot.getMovement().getAng();

		int x = Robot.getX();
		int y = Robot.getY();
				
		System.out.println(x + ", " + y);
		
		Node nextNode = lastNode;
				
		System.out.println("NN:" + _nn.getRow() + ", " + _nn.getCol());
		System.out.println("Last Node: " + lastNode.x + ","  + lastNode.y);
		
		while(!isFrontAccesable(Robot.getYasRow(), Robot.getXasCol()))
		{
			Robot.getMovement().turnRight(); 
		}
		do
		{
			if(freeDirection(encircledArea, ScanDirection.FRONT))
			{
				if(freeDirection(encircledArea, ScanDirection.LEFT))
				{
					Robot.getMovement().turnLeft();
				}
				Robot.getMovement().moveForward();

			} else {
				if(freeDirection(encircledArea, ScanDirection.LEFT))
				{
					Robot.getMovement().turnLeft();
				} else if(freeDirection(encircledArea, ScanDirection.RIGHT))
				{
					Robot.getMovement().turnRight();
				} else 
				{
					Robot.getMovement().turnLeft();
					Robot.getMovement().turnLeft();
				}
			}
			
			nextNode = new Node(Robot.getYasRow(), Robot.getXasCol());
			
			System.out.println(Robot.getYasRow() + ", " + Robot.getXasCol());
			
			wallFollowPath.add(nextNode);
			encircledArea = getEncircledScannedArea(Robot.getYasRow(), Robot.getXasCol());
		} while(!nnReached(nextNode.x, nextNode.y) && !(lastNode.x == nextNode.x && lastNode.y == nextNode.y));
			
		Robot.getMovement().setAng(ang);
		Robot.getMovement().setX(x);
		Robot.getMovement().setY(y);
		return wallFollowPath;
	}



	private static List<Node> calcRestOfPath(Coordinates2D[][] nextRobotPosition, Node currentNode) 
	{
		Coordinates2D nextPosition = nextRobotPosition[3][0];
		//Determine direction of nearest neighbour
		int[] dir = {nextPosition.getRow() - currentNode.x, nextPosition.getCol() - currentNode.y};
		String direction = null;

		//Make sure robot moves into direction of nearest neighbour
		if(dir[0]==-1 || dir[0]==1) {
			if(_nn.getCol() - currentNode.y <= 2)
			{
				direction = "LEFT";
			} else {
				direction = "RIGHT";
			}
		
		} else if(dir[1]==-1 || dir[1]==1) {
			if(_nn.getRow() - currentNode.x < -2)
			{
				direction = "TOP";
			} else {
				direction = "BOTTOM";
			}
	
		}

		List<Node> listNodes = new LinkedList<Node>();
		
		int i = 1;
		while(isHittingObstacle(nextRobotPosition))
		{
			if(direction == "LEFT")
			{
				Node nextNode = new Node(currentNode.x, currentNode.y - i);
				listNodes.add(nextNode);
				nextRobotPosition = Robot.getCoordinates(nextPosition.getRow(), nextPosition.getCol()-i);
			} else if(direction == "RIGHT")
			{
				Node nextNode = new Node(currentNode.x, currentNode.y + i);
				listNodes.add(nextNode);
				nextRobotPosition = Robot.getCoordinates(nextPosition.getRow(), nextPosition.getCol()+i);
			} else if(direction == "TOP")
			{
				Node nextNode = new Node(currentNode.x - i, currentNode.y);
				listNodes.add(nextNode);
				nextRobotPosition = Robot.getCoordinates(nextPosition.getRow() - i, nextPosition.getCol());
			} else if(direction == "BOTTOM")
			{
				Node nextNode = new Node(currentNode.x + i, currentNode.y);
				listNodes.add(nextNode);
				nextRobotPosition = Robot.getCoordinates(nextPosition.getRow() + i, nextPosition.getCol());
			} 
			
			i++;
		}
		
		System.out.println("Avoid obstacle:");
		for(Node node : listNodes)
		{
			System.out.println(node.x + ", " + node.y);
		}
		
		
		nextPosition = nextRobotPosition[3][0];
		listNodes.add(new Node(nextPosition.getRow(), nextPosition.getCol()));
		
		if(!nnReached(listNodes.get(listNodes.size()-1).x, listNodes.get(listNodes.size()-1).y))
		{
			List<Node> listNodesDijkstra = computePath(listNodes.get(listNodes.size()-1).x, listNodes.get(listNodes.size()-1).y, _nn);
			
			if(listNodesDijkstra!=null)
			{
				for(Node node : listNodesDijkstra)
				{
					listNodes.add(node);
				}
			}
		}
		
		System.out.println("Rest of Path:");
		for(Node node : listNodes)
		{
			System.out.println(node.x + ", " + node.y);
		}
		
		System.out.println("Nearest Neighbour: " + _nn.getRow() + ", " + _nn.getCol());

		return listNodes;
	}



	private static boolean isHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				int robotCellRow = nextRobotPosition[row][col].getRow();
				int robotCellCol = nextRobotPosition[row][col].getCol();	
				if(robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
				{
					return true;
				}
				if(Table.getMarkedObstacles(robotCellRow, robotCellCol))
				{
					return true;
				}
			}
		}
		return false;
	}

//	private static boolean turnInPath(int[] lastMove, int[]nextMove)
//	{
//		if(lastMove[0] == nextMove[0] && lastMove[1] == nextMove[1])
//		{
//			return false;
//		} else {
//			return true;
//		}
//	}
//
	private static boolean isTopHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 0; row < 1; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				int robotCellRow = nextRobotPosition[row][col].getRow();
				int robotCellCol = nextRobotPosition[row][col].getCol();	
				if(Table.getMarkedObstacles(robotCellRow, robotCellCol) ||  robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isBottomHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 3; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				int robotCellRow = nextRobotPosition[row][col].getRow();
				int robotCellCol = nextRobotPosition[row][col].getCol();	
				if(Table.getMarkedObstacles(robotCellRow, robotCellCol) ||  robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	private static boolean isRightHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 0; row < 4; row++)
		{
			for(int col = 3; col < 4; col++)
			{
				int robotCellRow = nextRobotPosition[row][col].getRow();
				int robotCellCol = nextRobotPosition[row][col].getCol();	
				if(Table.getMarkedObstacles(robotCellRow, robotCellCol) ||  robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isLeftHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 1; col++)
			{
				int robotCellRow = nextRobotPosition[row][col].getRow();
				int robotCellCol = nextRobotPosition[row][col].getCol();	
				if(Table.getMarkedObstacles(robotCellRow, robotCellCol) ||  robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
				{
					return true;
				}
			}
		}
		return false;
	}


}



