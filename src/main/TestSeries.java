package main;

/**
 * Class providing a second main method to run the simulation in a series of tests. Main method should be executed via command line.
 * @author Christian Brzeski
 */
import java.io.FileNotFoundException;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;

import Algorithms.CPPAlgorithm;
import Performance.Performance;
import Threads.Thread1;

public class TestSeries {

	public String _algorithm;
	
	public static Timer _timer;
	
	public static long _start;
			
	public static JFrame _frame;
	
	public static boolean _series = false;
	
	public static int _iteration = 0;
	public static int _iterations = 10;

	
	public static int _obstacles = 10;
	
	private static Object[][] _spiral;
	private static Object[][] _zigzag;
	private static Object[][] _random;

	private static Object[][] _spiralCumulative;
	private static Object[][] _zigzagCumulative;
	private static Object[][] _randomCumulative;
	
	
	/**
	 * 
	 * @param args Number of iterations the test should run; Number of obstacles that should be randomly placed into the environment
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
	
		System.out.println("Iterations: " + args[0] +", Obstacles: " + args[1]);

		_iterations = Integer.parseInt(args[0]);
		_obstacles = Integer.parseInt(args[1]);

		_spiral = new Object[13][_iterations+1];
		_zigzag = new Object[13][_iterations+1];
		_random = new Object[13][_iterations+1];

		_spiralCumulative = new Object[CPPAlgorithm._timeLimit+1][_iterations+1];
		_zigzagCumulative = new Object[CPPAlgorithm._timeLimit+1][_iterations+1];
		_randomCumulative = new Object[CPPAlgorithm._timeLimit+1][_iterations+1];
		
		Performance.initExcel(_spiral, _zigzag, _random, _spiralCumulative, _zigzagCumulative, _randomCumulative);

		try {
			_series = true;
			_frame = new MainFrame();

			Thread1 t1 = new Thread1(_iteration, _frame);

			t1.startIteration(_iteration, _obstacles);
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


}
