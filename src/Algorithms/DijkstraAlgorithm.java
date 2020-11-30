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

public class DijkstraAlgorithm extends Basic{

	static List<Node> _nodeList = new LinkedList<Node>();
	private static char[][] _matrix;
	private static Coordinates2D _nn;
	public static List<Node> adjustedPath = new LinkedList<Node>();

	 public static List<Node> computePath(int robotRow, int robotCol, Coordinates2D nn)
	 {
		 _nodeList.clear();
		 _nn = nn;
		 _matrix = updateMatrix();
		 
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
		if(_nn.getRow() < 61 && _nn.getCol() > 2)
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
			
//			 for(int i = 0; i < 64; i++)
//			 {
//				 StringBuilder sb = new StringBuilder();
//				 for(int j = 0; j < 64; j++)
//				 {
//					 sb.append(_matrix[i][j]);
//				 }
//				 System.out.println(sb);
//			 }
//			System.out.println("\n\n");
		} else {
			_matrix[_nn.getRow()][_nn.getCol()] = 'D';
		}

		
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
		return NearestNeighbour.getNearestNeighbour(row, col);
	}

}



