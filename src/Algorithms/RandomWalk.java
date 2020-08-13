package Algorithms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;
import Physics.Movement;
import Physics.Movement.LastMove;
import main.MainFrame;

public class RandomWalk extends Basic implements ActionListener{

	
	private MainFrame _frame; 
	public boolean _distanceExceeded = false;
	private int _actualRow;
	private int _actualCol;
	
	
	public RandomWalk(MainFrame frame) {
		_frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		_actualCol = Robot.getXasCol();
		_actualRow = Robot.getYasRow();
		
		
		boolean unaccesableField;

		unaccesableField = getSensorData(_actualRow, _actualCol);

		if(unaccesableField)
		{
			StartAlgorithm._timer.stop();
			Robot.getMovement().turnByDegrees((int)(Math.random()*360));
			StartAlgorithm._timer.start();
		} else {
			Robot.getMovement().moveForward();
		}

		//TODO: Quitting Criteria
		reachedStoppingCriteria();

		_frame.repaint();		
		
		
	}

	
	private boolean reachedStoppingCriteria() {
		return false;
	}
	
}
