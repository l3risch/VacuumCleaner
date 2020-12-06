package main;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.Timer;

import Algorithms.CPPAlgorithm;
import Algorithms.CustomAlgorithm;
import Algorithms.RandomWalk;
import Algorithms.Spiral;
import Algorithms.ZigZag;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Rendering.Renderer1;
import Threads.Thread1;

public class TestSeries {

	public String _algorithm;
	public int _iterations;
	
	private static Container _contentPane;
	public static Timer _timer;
	
	public static long _start;

	private static Renderer1 _render;
			
	public static JFrame _frame;
	
	public static boolean _series = false;
	
	public static void main(String[] args) 
	{
		try {
			_series = true;
			int iterations = 1;
			_frame = new MainFrame();
			_contentPane = _frame.getContentPane();
			_render = ((MainFrame) _frame).getRenderer();
			for(int i = 0; i < iterations; i++)
			{
				setupRandomMap(i);
		    	
				_start = System.currentTimeMillis() / 1000l;
				Thread1 t1 = new Thread1(i, _frame);
				t1.start();
				t1.startZigZag();
				
				clearAlgorithm();
				t1.startRandom();
				t1.join();
				clearAlgorithm();

//				_start = System.currentTimeMillis() / 1000l;
//				ThreadRandom random = new ThreadRandom("Random", i, _frame);
//				random.start();
//				random.join();
			      
				//startAlgorithm(i);
				clearMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static void setupRandomMap(int iteration) 
	{
		if(Table._listObs != null)
		{
			Table._listObs.clear();
		}
		
		Robot.setRandomStartingPos();
		if(iteration > 0)
		{
			_render.clearMarks();
		}
		new MapGenerator();

		_contentPane.repaint();
	}				
	


	private static void clearAlgorithm()
	{
		Table.clearMarkedPath();
		_render.clearMarks();
		Robot.setStartingPos(Robot._startingPos.getRow(), Robot._startingPos.getCol());
		_contentPane.repaint();
	}
	
	private static void clearMap()
	{
		_render.clearMarks();

		_contentPane.repaint();
	}
	
	public static void setTimer(Timer timer)
	{
		_timer = timer;
	}
	
	public static Timer getTimer()
	{
		return _timer;
	}
}
