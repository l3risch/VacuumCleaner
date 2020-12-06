package Threads;


import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import Algorithms.RandomWalk;
import Algorithms.ZigZag;
import Objects.Robot;
import Objects.Table;
import Rendering.Renderer1;
import main.MainFrame;
import main.TestSeries;

public class Thread1 extends Thread {
	
    private int _iteration;
    public static Timer _timer;
    private static JFrame _frame;
    
    private static Renderer1 _render;
	private static Container _contentPane;
    
	public static long _start;

    public Thread1(int iteration, JFrame frame)
    {
    	_iteration = iteration;
    	_frame = frame;
    	
    	_contentPane = _frame.getContentPane();
		_render = ((MainFrame) _frame).getRenderer();
    }
    
    
//    @Override
//    public void run() {
//		 ZigZag zigzag = new ZigZag((MainFrame) _frame, _iteration);
//		_timer = new Timer(50, zigzag);
//		TestSeries.setTimer(_timer);
//       _timer.start();
//       _timer.setRepeats(true);       
//     }
     
//     public void start () {
//        System.out.println("Starting " +  _algorithm );
//        if (t == null) {
//           t = new Thread (this, _algorithm);
//           t.start ();
//        }
//     }
    
    public void startZigZag()
    {
		_start = System.currentTimeMillis() / 1000l;

    	 ZigZag zigzag = new ZigZag((MainFrame) _frame, _iteration);
 		_timer = new Timer(50, zigzag);
        _timer.start();
        _timer.setRepeats(true);       
        
        throw new RuntimeException("Stopping the thread");
    }
    
    public void startRandom()
    {
    	_start = System.currentTimeMillis() / 1000l;
    	 RandomWalk random = new RandomWalk((MainFrame) _frame, _iteration);
     	_timer = null;
 		_timer = new Timer(50, random);
        _timer.start();
        _timer.setRepeats(true);   

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
 	

}
