package main;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.Timer;

import Algorithms.CustomAlgorithm;
import Algorithms.RandomWalk;
import Algorithms.Spiral;
import Algorithms.ZigZag;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Performance.Performance;
import Rendering.Renderer1;

public class TestSeries {

	public String _algorithm;
	public int _iterations;
	
	public static Renderer1 _render;
	private static Container _contentPane;
	public static Timer _timer;
	
	public static long _start;

			
	public static JFrame _frame;
	
	public static boolean _series = false;
	
	public static void main(String[] args) 
	{
		try {
			_series = true;
			int iterations = 1;
			_frame = new MainFrame();
			_contentPane = _frame.getContentPane();
			for(int i = 0; i < iterations; i++)
			{
				setupRandomMap(i);
				startAlgorithm(i);
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
	
	
	private static void startAlgorithm(int iteration) throws InterruptedException 
	{
		_start = System.currentTimeMillis() / 1000l;
		 ZigZag zigzag = new ZigZag((MainFrame) _frame, iteration);
		_timer = new Timer(50, zigzag);
        _timer.start();
        _timer.setRepeats(true);
             
        Thread.sleep(180000);
        clearAlgorithm();
        
		_start = System.currentTimeMillis() / 1000l;
        RandomWalk random = new RandomWalk((MainFrame) _frame, iteration);
			_timer = new Timer(50, random);
	        _timer.start();
	        _timer.setRepeats(true);
	        
	    clearAlgorithm();

		_start = System.currentTimeMillis() / 1000l;
		CustomAlgorithm custom = new CustomAlgorithm((MainFrame) _frame, iteration);
		_timer = new Timer(50, custom);
        _timer.start();
        _timer.setRepeats(true);
        
        clearAlgorithm();

		_start = System.currentTimeMillis() / 1000l;
        Spiral spiral = new Spiral((MainFrame) _frame, iteration);
		_timer = new Timer(50, zigzag);
        _timer.start();
        _timer.setRepeats(true);
        
        clearAlgorithm();
					
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
}
