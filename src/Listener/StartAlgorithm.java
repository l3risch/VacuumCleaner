package Listener;
import java.awt.event.ActionEvent;




import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.Timer;

import Algorithms.WallFollowing;
import Algorithms.Spiral2;
import Algorithms.Spiral;
import Algorithms.BackAndForth;
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
		case "Spiral": Spiral spiral = new Spiral(_frame);
			_timer = new Timer(50, spiral);
	        _timer.start();
	        _timer.setRepeats(true);
		break;
		case "Random Walk": RandomWalk random = new RandomWalk(_frame);
			_timer = new Timer(50, random);
	        _timer.start();
	        _timer.setRepeats(true);
		break;	
		case "Spiral2": Spiral2 spiral2 = new Spiral2(_frame);
			_timer = new Timer(50, spiral2);
	        _timer.start();
	        _timer.setRepeats(true);
	break;
		case "BackForth": BackAndForth backforth = new BackAndForth(_frame);
		_timer = new Timer(50, backforth);
        _timer.start();
        _timer.setRepeats(true);
        break;
	
		}
		


	}
	
}
