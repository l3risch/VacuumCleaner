package Listener;
import java.awt.event.ActionEvent;




import java.awt.event.ActionListener;

import javax.swing.Timer;

import Algorithms.TestAlgorithm;
import Algorithms.CellularDecomposition;
import Algorithms.CustomAlgorithm;
import Algorithms.RandomWalk;
import main.MainFrame;


public class StartAlgorithm implements ActionListener {

	private MainFrame _frame; 
	public static Timer _timer;
	
	public StartAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch(_frame.getAlgorithm()) {
		case "Test": TestAlgorithm test = new TestAlgorithm(_frame);
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
		case "Cellular Decomposition": CellularDecomposition cellDec = new CellularDecomposition(_frame);
			_timer = new Timer(50, cellDec);
	        _timer.start();
	        _timer.setRepeats(true);
	break;
		}
		


	}
	
}
