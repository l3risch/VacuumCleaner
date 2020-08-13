package Algorithms;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import Listener.StartAlgorithm;
import Objects.Robot;
import Objects.Table;
import main.MainFrame;

public class TestAlgorithm implements ActionListener{

	private MainFrame _frame; 
	public boolean _distanceExceeded = false;
	
	public TestAlgorithm(MainFrame frame)
	{
		_frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Robot.getMovement().moveForward();

		_frame.repaint();

		if( Math.abs(Robot.getY() - Robot.getInitY()) > (Table._rows - 9) * 10)
		{

			if(Math.abs(Robot.getX() - Robot.getInitX()) % 40 == 0)
			{
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnRight();
				StartAlgorithm._timer.start();
			}
		}
		
		if(Math.abs(Robot.getY() - Robot.getInitY()) == 0) 
		{
			
			if(Math.abs(Robot.getX() - Robot.getInitX()) % 40 == 0 )
			{
				StartAlgorithm._timer.stop();
				Robot.getMovement().turnLeft();
				StartAlgorithm._timer.start();
			}
		}
		
		//Stopping criteria for robot (End of Track)
		if(Math.abs(Robot.getX() - Robot.getInitX()) == (Table._cols -4) * 10)
		{
			if(Math.abs(Robot.getY() - Robot.getInitY()) == 0) 
			{
				StartAlgorithm._timer.stop();
			}
		}
	}
		
}


