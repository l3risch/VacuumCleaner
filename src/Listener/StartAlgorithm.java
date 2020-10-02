package Listener;
import java.awt.event.ActionEvent;




import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.Timer;

import Algorithms.WallFollowing;
import Algorithms.SpiralAlgorithm;
import Algorithms.CustomAlgorithm;
import Algorithms.RandomWalk;
import main.MainFrame;


public class StartAlgorithm implements ActionListener {

	private MainFrame _frame; 
	public static Timer _timer;
	public static long _start;
	
	public StartAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		_start = System.currentTimeMillis() / 1000l;
		switch(_frame.getAlgorithm()) {
		case "Wall Following": WallFollowing test = new WallFollowing(_frame);
			_timer = new Timer(50, test);
	        _timer.start();
	        _timer.setRepeats(true);
		break;
		case "Custom": CustomAlgorithm custom = new CustomAlgorithm(_frame);
			_timer = new Timer(50, custom);
	        _timer.start();
	        _timer.setRepeats(true);
		break;
		case "Random Walk": RandomWalk random = new RandomWalk(_frame);
			_timer = new Timer(50, random);
	        _timer.start();
	        _timer.setRepeats(true);
		break;	
		case "Spiral": SpiralAlgorithm cellDec = new SpiralAlgorithm(_frame);
			_timer = new Timer(50, cellDec);
	        _timer.start();
	        _timer.setRepeats(true);
	break;
		}
		


	}
	
}
