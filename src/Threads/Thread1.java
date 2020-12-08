package Threads;


import java.awt.Container;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import Algorithms.CustomAlgorithm;
import Algorithms.NearestNeighbour;
import Algorithms.RandomWalk;
import Algorithms.Spiral;
import Algorithms.ZigZag;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Rendering.Renderer1;
import main.MainFrame;

public class Thread1 extends Thread {
	
    private int _iteration;
    public static Timer _timer;
    private static JFrame _frame;
    
    private static Renderer1 _render;
	private static Container _contentPane;
    
	public static long _start;
	
	private ZigZag _zigzag;
	private Spiral _spiral;
	private CustomAlgorithm _custom;
	private RandomWalk _random;
	
	public static String _cpp;

    public Thread1(int iteration, JFrame frame)
    {
    	_iteration = iteration;
    	_frame = frame;
    	
    	_contentPane = _frame.getContentPane();
		_render = ((MainFrame) _frame).getRenderer();
    }
   
    
    public void startSpiral()
    {
		NearestNeighbour._pathMatrix = new char[64][64];
    	_start = System.currentTimeMillis();
     	_spiral = new Spiral((MainFrame) _frame, _iteration);
     	_cpp = "Spiral";
 		_timer = new Timer(50, _spiral);

        _timer.start();
        _timer.setRepeats(true);   
        
        throw new RuntimeException("Stopping the thread " + _cpp);
    }
    
    public void startZigZag()
    {
		NearestNeighbour._pathMatrix = new char[64][64];
     	_timer.stop();
		_start = System.currentTimeMillis();
   	 	_zigzag = new ZigZag((MainFrame) _frame, _iteration);
     	_cpp = "ZigZag";
   	 	
    	for(ActionListener listener : _timer.getActionListeners())
     	{
       	 	_timer.removeActionListener(listener);
     	}

    	 _timer.addActionListener(_zigzag);

    	_timer.start();
        _timer.setRepeats(true);       
        
        throw new RuntimeException("Stopping the thread " + _cpp);
    }
    
    public void startCustom()
    {
		NearestNeighbour._pathMatrix = new char[64][64];
     	_timer.stop();
		_start = System.currentTimeMillis();
   	 	_custom = new CustomAlgorithm((MainFrame) _frame, _iteration);
     	_cpp = "Custom";

    	for(ActionListener listener : _timer.getActionListeners())
     	{
       	 	_timer.removeActionListener(listener);
     	}

    	 _timer.addActionListener(_custom);

    	_timer.start();
        _timer.setRepeats(true);       
        
        throw new RuntimeException("Stopping the thread " + _cpp);
    }
    
    public void startRandom()
    {
		NearestNeighbour._pathMatrix = new char[64][64];
     	_timer.stop();
		_start = System.currentTimeMillis();
   	 	_random = new RandomWalk((MainFrame) _frame, _iteration);
     	_cpp = "Random";

    	for(ActionListener listener : _timer.getActionListeners())
     	{
       	 	_timer.removeActionListener(listener);
     	}

    	 _timer.addActionListener(_random);

    	_timer.start();
        _timer.setRepeats(true);       
        
        throw new RuntimeException("Stopping the thread " + _cpp);
    }
     
     public static Timer getTimer()
     {
    	 return _timer;
     }

     public void clearAlgorithm()
 	{
 		Table.clearMarkedPath();
 		_render.clearMarks();
 		Robot.setStartingPos(Robot._startingPos.getRow(), Robot._startingPos.getCol());
 		_contentPane.repaint();
 	}
 	
 	public void clearMap()
 	{
 		_render.clearMarks();

 		_contentPane.repaint();
 	}
 	
	public void startIteration(Thread1 t1, int i) 
	{
		setupRandomMap(i);
    	
		t1.start();
		t1.startSpiral();
	}


	public void setupRandomMap(int iteration) 
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

}
