package MapGeneration;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import Objects.Obstacle;
import Objects.Table;
import Physics.Coordinates2D;
import Objects.Obstacle.Shape;
import Objects.Robot;
import main.MainFrame;

public class MapGenerator{

	public static List<Obstacle> _listObs = new ArrayList<Obstacle>();

	
	public MapGenerator() {
		createRandomObstacles();
	}

	public void createRandomObstacles() 
	{		
		Table._markedPath = new boolean[64][64];
		Table._markedObstacles = new boolean[64][64];
		
		//Randomize number of obstacles for this map from one to ten
		int numberObs = (int) (Math.random() * 12) ;
		
		if(_listObs != null)
		{
			_listObs.clear();
		}
		for(int i = 0; i < numberObs; i++)
		{
			_listObs.add(createRandomObstacle());
		}
		
		Table.setRandomMap(_listObs);
	}

	private Obstacle createRandomObstacle() {

		int minX = 100;
		int maxX = 740;
		
		int minY = 140;
		int maxY = 780;
		
		int x = minX + 10 * (int) (Math.random() * 63); //100 min; 740 max
		int y = minY + 10 * (int) (Math.random() * 63); //140 min; 780 max

		int width;		
		
		
		if(Math.abs(maxX - x) < 150)
		{
			int dif = Math.abs(maxX - x);
			width = 10 + 10 * (int) (Math.random() * (dif/10));
		} else {
			//Make sure width of an obstacle is not > 150 px
			width = 10 + 10 * (int) (Math.random() * 15);
		}
		
		int height;
		if(Math.abs(maxY - y) < 150)
		{
			int dif = Math.abs(maxY - y);
			height = 10 + 10 * (int) (Math.random() * (dif/10));
		} else {
			//Make sure width of an obstacle is not > 150 px
			height = 10 + 10 * (int) (Math.random() * 15);
		}
		
		Obstacle obs = new Obstacle(x, y, width, height, Shape.RECTANGLE);
		
		
		//Determine Robot Pos
		int robotWidth = 4;
		int robotLength = 4;
		Coordinates2D[][] robotPos = new Coordinates2D[4][4];
		for(int i = 0; i < robotWidth; i++)
		{
			for(int j = 0; j < robotLength; j++)
			{
				robotPos[i][j] = new Coordinates2D((Robot.getY() + j*10), (Robot.getX() + j*10));
			}
		}
		
//		System.out.println("Min X: " + obs.getX() + ", Min Y: " +  obs.getY() + ", Max X: " + (obs.getX() + obs.getWidth()) + ", Max Y: " + (obs.getY()+ obs.getHeight()));
//		System.out.println("\nRobot: " + Robot.getY() +", " +Robot.getX() + " bzw. " + Robot.getYasRow() + ", " +Robot.getXasCol() );
//		System.out.println((Robot.getYasRow()*10 +140) + ", "+ (Robot.getXasCol()*10 +100));
//		System.out.println("X: " + Robot.getX() + ", Y:" + Robot.getY());
		
		//If random obstacle is overlapping with robot set obstacle to 0
		for(int i = 0; i < robotWidth; i++)
		{
			for(int j = 0; j < robotLength; j++)
			{
				if(robotPos[i][j].getCol() >= obs.getX() && robotPos[i][j].getCol() <= (obs.getX() + obs.getWidth()))
				{
					if(robotPos[i][j].getRow() <= obs.getY() + obs.getHeight() && robotPos[i][j].getRow() >= obs.getY())
					{
						System.out.println("Set 0");
						obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
					}				
				}

			}
			
		}

		
		return obs;

	}

	
}
