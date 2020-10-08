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
import Physics.Coordinates2D;

public class ShortestPath extends Basic{

	private static Coordinates2D[][] _robotPos = new Coordinates2D[4][4];
	private static Coordinates2D _sourceCell;
	static List<Node> _nodeList = new LinkedList<Node>();
	
	 public static int calcPath(int robotRow, int robotCol, Coordinates2D nn)
	 {
		 char[][] matrix = new char[64][64];
		 
		 //Transform Hash Map to char matrix
		 for(String key : _mentalMap.keySet())
		 {
			 int row = Integer.parseInt(key.substring(0, 2));
			 int col = Integer.parseInt(key.substring(2, 4));
			 if(_mentalMap.get(key).equals(CellState.FREE) || _mentalMap.get(key).equals(CellState.VISITED))
 			 {
				 matrix[row][col] = '1';
 			 }
			 if(_mentalMap.get(key).equals(CellState.OCCUPIED))
 			 {
				 matrix[row][col] = '0';
 	 		 }
		 }
		 
		 //Set all unknown cells as obstacles '0'
		 for(int i = 0; i < 64; i++)
		 {
			 for(int j = 0; j < 64; j++)
			 {
				 if(matrix[i][j] == 0)
				 {
					 matrix[i][j] = '0';
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
				matrix[row][col] = 'S';
			}
		}
		
		//Choose starting position 
		_sourceCell = _robotPos[3][0];
		 
		 //Set position of nearest neighbour as destination
		if(matrix[nn.getRow()][nn.getCol()] == 'S')
		{
			return -1;
		} else {
			matrix[nn.getRow()][nn.getCol()] = 'D';
		} 
		
		 
       boolean exists = pathExists(matrix);
       
		if(exists)
	    {
		  long start = System.currentTimeMillis();
          computeDijkstra();
          System.out.println(System.currentTimeMillis()-start);
	    }
       
       return 1;
    }
	 


	private static boolean pathExists(char[][] matrix) {
		
		Node source = new Node(_sourceCell.getRow(), _sourceCell.getCol());
		Queue<Node> queue = new LinkedList<Node>();
		
		queue.add(source);

		while(!queue.isEmpty()) {
			Node currentNode = queue.poll();
			List<Node> neighbourList = addNeighbours(currentNode, matrix);
			queue.addAll(neighbourList);
			
			if(matrix[currentNode.x][currentNode.y] == 'D' ) {
				_nodeList.add(currentNode);

				return true;
			}
			else {
				if(matrix[currentNode.x][currentNode.y]!='0')
				{
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


	private static void computeDijkstra() 
	{
		Graph graph = new Graph();
		for(Node node : _nodeList)
		{
			graph.addNode(node);
		}
		System.out.println("Graph Size: " +  _nodeList.size());
		
		Node dest = _nodeList.get(_nodeList.size()-1);
		dest.getShortestPath().add(dest);
		System.out.println(dest.x + ", "+ dest.y);
		Node source = _nodeList.get(0);
		graph = calculateShortestPathFromSource(graph, source);
		
		
		System.out.println("Shortest Path: ");
		for(Node node: dest.getShortestPath())
		{
			System.out.println(node.x + ", " + node.y);
		}
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
            for(Node node : unsettledNodes)
            {
            	if(node.x == 19 && node.y == 15)
            	{
            		System.out.println("nn gefunden");
            	}
            }
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
