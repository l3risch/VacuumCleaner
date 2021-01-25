package Objects;

/**
 * Class to define nodes as a component of a graph
 */
import java.util.LinkedList;
import java.util.List;

public class Node {
    public int x;
    public int y;
    List<Node> shortestPath = new LinkedList<>();
    
    Integer distance = Integer.MAX_VALUE;
    
    List<Node> adjacentNodes = new LinkedList<>();
 
    public void addDestination(Node destination) {
        adjacentNodes.add(destination);
    }
    
    public Node(int x, int y) {
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