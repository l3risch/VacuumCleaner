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
		 
//		 char[][] matrix = {
//		            {'S', '0', '1', '1', '1', '1', '1', '1', '0', '1'},
//		            {'1', '1', '1', '0', '1', '1', '1', '1', '0', '1'},
//		            {'0', '1', '1', '0', '1', '1', '1', '1', '0', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '0', '1'},
//		            {'1', '0', '1', '1', '1', '1', '1', '1', '0', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '1', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '1', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '1', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '1', '1'},
//		            {'1', '0', '1', '0', '1', '1', '1', '1', '1', 'D'},
//
//	        };
		 
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
		  //long start = System.currentTimeMillis();
		  List<Node> dijkstraPath = computeDijkstraPath();
		  List<Node> adjustedPath = adjustDijkstraPath(dijkstraPath);
		  return adjustedPath;
          //System.out.println(System.currentTimeMillis()-start);
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
		//System.out.println("Graph Size: " +  _nodeList.size());
		
		Node dest = _nodeList.get(_nodeList.size()-1);
		dest.getShortestPath().add(dest);
		//System.out.println(dest.x + ", "+ dest.y);
		Node source = _nodeList.get(0);
		graph = calculateShortestPathFromSource(graph, source);
		
		List<Node> shortestPath = dest.getShortestPath();

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

		 //Set position of robot
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				int row = _robotPos[i][j].getRow();
				int col = _robotPos[i][j].getCol();
				_matrix[row][col] = 'S';
		        //System.out.print("(" + row + ", " + col + ") ");
			}
		}
		
		//System.out.println("Nearest Neighbour: " + _nn.getRow() + ", " + _nn.getCol());
		 //Set position of nearest neighbour as destination
		if(_matrix[_nn.getRow()][_nn.getCol()] == 'S')
		{
			return true;
		} else {
			return false;
		} 
	}
	
	/*
	 * Since robot consists of 16 cells instead of 1, the Disjkastra algorithm has to be adjusted
	 */
	private static List<Node> adjustDijkstraPath(List<Node> nodeList)
	{
		int x = 0;
		int y = 0;
		for(int i = 0; i < nodeList.size()-1; i++)
		{
			Node currentNode = nodeList.get(i);
			Node nextNode = nodeList.get(i+1);
			
			Coordinates2D[][] nextRobotPosition = Robot.getCoordinates(nextNode.x + x, nextNode.y + y);
			if(x != 0 || y != 0)
			{
				nextRobotPosition = Robot.getCoordinates(currentNode.x + x, currentNode.y + y);
			}
			x = 0;
			y = 0;
			System.out.println("Current: "+ currentNode.x + "; " + currentNode.y);
			System.out.println("Next: " + nextNode.x + ", " +nextNode.y);

			if(isRobotHittingObstacle(nextRobotPosition))
			{
//				for(Node node : nodeList)
//				{
//					System.out.println(node.x + ", " + node.y);
//				}		
//				System.out.println("__________");
//				
				
				if(currentNode.y + 1 == nextNode.y)
				{
				    nodeList.add(i+1, new Node(currentNode.x + 1, currentNode.y));
				    y = 1;
				} else if(currentNode.y -1 == nextNode.y)
				{
					nodeList.add(i+1, new Node(currentNode.x + 1, currentNode.y));
				    y = -1;
				} else if(currentNode.x + 1 == nextNode.x)
				{
				    nodeList.add(i+1, new Node(currentNode.x, currentNode.y + 1));
				    x = 1;
				} else if(currentNode.x - 1 == nextNode.x)
				{
					x = -1;
				}
			}
		}
			
		
		for(Node node : nodeList)
		{
			System.out.println(node.x + ", " + node.y);
		}
		return nodeList;
	}
	



	private static boolean isRobotHittingObstacle(Coordinates2D[][] nextRobotPosition) 
	{
//		for(int row = 0; row < 4; row++)
//		{
//			StringBuilder sb = new StringBuilder();
//			for(int col = 0; col < 4; col++)
//			{
//				sb.append("("+nextRobotPosition[row][col].getRow()+","+nextRobotPosition[row][col].getCol()+") ");
//			}
//			System.out.println(sb);
//		}
//			
		for(int row = 0; row < 4; row++)
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
