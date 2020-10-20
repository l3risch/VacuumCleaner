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
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;

public class ShortestPath extends Basic{

	private static Coordinates2D[][] _robotPos = new Coordinates2D[4][4];
	private static Coordinates2D _sourceCell;
	static List<Node> _nodeList = new LinkedList<Node>();
	private static char[][] _matrix;
	private static Coordinates2D _nn;
	
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
		 
		 _robotPos = Robot.getCoordinates(robotRow, robotCol);
		 
		 //Set position of robot as source
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				int row = _robotPos[i][j].getRow();
				int col = _robotPos[i][j].getCol();
				_matrix[row][col] = 'S';
			}
		}
		
		//Choose starting position 
		_sourceCell = _robotPos[3][0];
		 
		 //Set position of nearest neighbour as destination
		if(_matrix[_nn.getRow()][_nn.getCol()] == 'S')
		{
			return null;
		} else {
			_matrix[_nn.getRow()][_nn.getCol()] = 'D';
		} 
		
       boolean exists = pathExists(_matrix);		
		
		if(exists)
	    {
		  List<Node> dijkstraPath = computeDijkstraPath();
		  List<Node> adjustedPath = adjustDijkstraPath(dijkstraPath);
		  return adjustedPath;
	    } else {
	    	return null;
	    }
    }
	 


	private static boolean pathExists(char[][] matrix) {
		
		Node source = new Node(_sourceCell.getRow(), _sourceCell.getCol());
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


	private static List<Node> addNeighbours(Node poped, char[][] matrix) {
		
		List<Node> list = new LinkedList<Node>();
		
		if((poped.x-1 > 0 && poped.x-1 < matrix.length) && matrix[poped.x-1][poped.y] != '0') {
			list.add(new Node(poped.x-1, poped.y));
		}
		if((poped.x+1 > 0 && poped.x+1 < matrix.length) && matrix[poped.x+1][poped.y] != '0') {
			list.add(new Node(poped.x+1, poped.y));
		}
		if((poped.y-1 > 0 && poped.y-1 < matrix.length) && matrix[poped.x][poped.y-1] != '0') {
			list.add(new Node(poped.x, poped.y-1));
		}
		if((poped.y+1 > 0 && poped.y+1 < matrix.length) && matrix[poped.x][poped.y+1] != '0') {
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
		
		 _robotPos = Robot.getCoordinates(robotRow, robotCol);

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
	 * Since robot consists of 16 cells instead of 1, the Disjkastra algorithm has to be adjusted
	 */
	private static List<Node> adjustDijkstraPath(List<Node> nodeList)
	{
		boolean abort = false;
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
					abort = true;
					
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

		if(!abort)
		{
			adjustedPath.add(nodeList.get(nodeList.size()-1));
		}
		else {
			List<Node> restPath = calcRestOfPath(nextRobotPosition, adjustedPath.get(adjustedPath.size()-1));
			for(Node node : restPath)
			{
				adjustedPath.add(node);
			}
			abort = false;
		}

			
//		System.out.println("Adj Path: ");
//		for(Node node : adjustedPath)
//		{
//			System.out.println(node.x + ", " + node.y);
//		}
//		System.out.println("Nearest Neighbour: " + _nn.getRow() + ", " + _nn.getCol());
//		System.out.println("_________________________");
		return adjustedPath;
	}
	

	private static List<Node> calcRestOfPath(Coordinates2D[][] nextRobotPosition, Node currentNode) 
	{
		Coordinates2D nextPosition = nextRobotPosition[3][0];
		//Determine direction of nearest neighbour
		int[] dir = {nextPosition.getRow() - currentNode.x, nextPosition.getCol() - currentNode.y};
		String direction = null;

		if(dir[0]==-1 || dir[0]==1) {
			if(_nn.getCol() - currentNode.y <= 3)
			{
				direction = "LEFT";
			} else {
				direction = "RIGHT";
			}
		
		} else if(dir[1]==-1 || dir[1]==1) {
			if(_nn.getRow() - currentNode.x < -3)
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
//	private static boolean isTopHittingObstacle(Coordinates2D[][] nextRobotPosition) 
//	{
//
//		for(int row = 0; row < 3; row++)
//		{
//			for(int col = 0; col < 3; col++)
//			{
//				int robotCellRow = nextRobotPosition[row][col].getRow();
//				int robotCellCol = nextRobotPosition[row][col].getCol();	
//				if(Table.getMarkedObstacles(robotCellRow, robotCellCol) ||  robotCellRow < 0 || robotCellRow >= 64 || robotCellCol < 0 || robotCellCol >= 64)
//				{
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//	
//	
	private static boolean isRightHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{

		for(int row = 1; row < 4; row++)
		{
			for(int col = 1; col < 4; col++)
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

class Node {
    int x;
    int y;
    List<Node> shortestPath = new LinkedList<>();
    
    Integer distance = Integer.MAX_VALUE;
    
    List<Node> adjacentNodes = new LinkedList<>();
 
    public void addDestination(Node destination) {
        adjacentNodes.add(destination);
    }
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
     
    }
    
    public void setShortestPath(List<Node> shortestPath)
    {
    	this.shortestPath = shortestPath;
    }
    
    public List<Node> getShortestPath()
    {
    	return this.shortestPath;
    }
    
    public void setDistance(Integer distance)
    {
    	this.distance = distance;
    }
    
    public Integer getDistance()
    {
    	return distance;
    }
    
    public void setAdjacentNodes(List<Node> adjacentNodes)
    {
    	this.adjacentNodes = adjacentNodes;
    }
    
    public List<Node> getAdjacentNodes()
    {
    	return this.adjacentNodes;
    }
}

class Graph {
	 
    Set<Node> nodes = new HashSet<>();
    
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }
 
    public Set<Node> getNodes()
    {
    	return this.nodes;
    }
}
