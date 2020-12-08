package MapGeneration;

import java.util.ArrayList;
import java.util.List;

import Objects.Obstacle;
import Objects.Table;
import Physics.Coordinates2D;
import Objects.Obstacle.Shape;
import Objects.Robot;

public class MapGenerator{

	public static List<Obstacle> _listObs = new ArrayList<Obstacle>();

	public static int _numberObs;
	
	public MapGenerator(int numberObs) {
		createRandomObstacles(numberObs);
	}

	public void createRandomObstacles(int numberObs) 
	{		
		Table._markedPath = new boolean[64][64];
		Table._markedObstacles = new boolean[64][64];
		
		//Randomize number of obstacles for this map from one to ten
		//int _numberObs = 1 + (int) (Math.random() * 20) ;
		
		
		if(_listObs != null)
		{
			_listObs.clear();
		}
		while(_listObs.size() < numberObs)
		{
			Obstacle randomObstacle = createRandomObstacle();
			if((randomObstacle.getWidth() + randomObstacle.getHeight()) > 0)
			{
				_listObs.add(randomObstacle);
			}
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
			//Make sure height of an obstacle is not > 150 px
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
		
		//If random obstacle is overlapping with robot set obstacle to 0
		for(int i = 0; i < robotWidth; i++)
		{
			for(int j = 0; j < robotLength; j++)
			{
				if(robotPos[i][j].getCol() >= obs.getX() && robotPos[i][j].getCol() <= (obs.getX() + obs.getWidth()))
				{
					if(robotPos[i][j].getRow() <= obs.getY() + obs.getHeight() && robotPos[i][j].getRow() >= obs.getY())
					{
						obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
					}				
				}
			}
			
		}	
		
		//If random obstacle blocks some part of the room set obstacle to 0
		return checkIfBlock(obs);		
		

	}

	private Obstacle checkIfBlock(Obstacle obs) {
		
		if(obs.getX() > 100 && obs.getX() < 140)
		{
			obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
		}
		
		if(obs.getY() > 140 && obs.getY() < 180)
		{
			obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
		}		
		
		if(obs.getX() + obs.getWidth() > 700 && obs.getX() + obs.getWidth() < 740)
		{
			obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
		}
		
		if(obs.getY() + obs.getHeight() > 740 && obs.getY() + obs.getHeight() < 780)
		{
			obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
		}
		
		for(Obstacle obstacle : _listObs)
		{
			if(obs.getX() > obstacle.getX() + obstacle.getWidth() && obs.getX() < obstacle.getX() + obstacle.getWidth() + 40)
			{
				obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
			}
			
			if(obs.getX() + obs.getWidth() < obstacle.getX() && obs.getX() + obs.getWidth() > obstacle.getX() - 40)
			{
				obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
			}		
			
			if(obs.getY() > obstacle.getY() + obstacle.getHeight() && obs.getY() < obstacle.getY() + obstacle.getHeight() + 40)
			{
				obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
			}
			
			if(obs.getY() + obs.getHeight() < obstacle.getY() && obs.getY() + obs.getHeight() > obstacle.getY() - 40)
			{
				obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
			}	
			
			if(obs.getX() >= obstacle.getX() && (obs.getX()+obs.getHeight()) <= (obstacle.getX()+obstacle.getHeight()) &&
					obs.getY() >= obstacle.getY() && (obs.getY()+obs.getWidth()) <= (obstacle.getY()+obstacle.getWidth()))
			{
				obs = new Obstacle(0, 0, 0, 0, Shape.RECTANGLE);
			}
			
		}
		
		return obs;
	}

	
}
