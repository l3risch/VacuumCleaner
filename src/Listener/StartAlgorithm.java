package Listener;
import java.awt.event.ActionEvent;




import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.Timer;

import Algorithms.Spiral;
import Algorithms.ZigZag;
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
		
		_start = System.currentTimeMillis();
		switch(_frame.getAlgorithm()) {
		case "Spiral": Spiral spiral = new Spiral(_frame, 0);
			_timer = new Timer(50, spiral);
	        _timer.start();
	        _timer.setRepeats(true);
		break;
		case "Random Walk": RandomWalk random = new RandomWalk(_frame, 0);
			_timer = new Timer(50, random);
	        _timer.start();
	        _timer.setRepeats(true);
		break;	
		case "Custom": CustomAlgorithm custom = new CustomAlgorithm(_frame, 0);
		_timer = new Timer(50, custom);
        _timer.start();
        _timer.setRepeats(true);
        break;
		case "ZigZag": ZigZag zigzag = new ZigZag(_frame, 0);
		_timer = new Timer(50, zigzag);
        _timer.start();
        _timer.setRepeats(true);
        break;
		}
		


	}
	
}
