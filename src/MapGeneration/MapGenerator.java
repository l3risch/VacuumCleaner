package MapGeneration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import Objects.Obstacle;
import Objects.Table;
import Objects.Obstacle.Shape;
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
		int numberObs = (int) (Math.random() * 10) ;
		
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
		
		return obs;

	}

	
}
