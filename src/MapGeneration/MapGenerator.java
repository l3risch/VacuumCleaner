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
	private Table _table;
	
	public MapGenerator(Table table) {
		_table = table;
		createRandomObstacles();
	}

	public void createRandomObstacles() 
	{				
		//Randomize number of obstacles for this map from one to ten
		int numberObs = (int) (Math.random() * 10) ;
		
		for(int i = 0; i < numberObs; i++)
		{
			_listObs.add(createRandomObstacle());
		}
		
		_table.setRandomMap(_listObs);
	}

	private Obstacle createRandomObstacle() {

		int minX = 100;
		int maxX = 740;
		
		int minY = 140;
		int maxY = 780;
		
		int x = minX + 10 * (int) (Math.random() * 63); //100 min; 740 max
		System.out.println(x);
		int y = minY + 10 * (int) (Math.random() * 63); //140 min; 780 max
		System.out.println(y);

		int width;
		
		if(Math.abs(maxX - x) < 150)
		{
			int dif = Math.abs(maxX - x);
			width = 10 + 10 * (int) (Math.random() * (dif/10));
		} else {
			//Make sure width of an obstacle is not > 150 px
			width = 10 + 10 * (int) (Math.random() * 15);
		}
		System.out.println(width);
		
		int height;
		if(Math.abs(maxY - y) < 150)
		{
			int dif = Math.abs(maxY - y);
			height = 10 + 10 * (int) (Math.random() * (dif/10));
		} else {
			//Make sure width of an obstacle is not > 150 px
			height = 10 + 10 * (int) (Math.random() * 15);
		}
		System.out.println(height);
		
		Obstacle obs = new Obstacle(x, y, width, height, Shape.RECTANGLE);
		
		return obs;

	}

	
}
