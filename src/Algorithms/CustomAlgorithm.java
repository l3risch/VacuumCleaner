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

public class CustomAlgorithm implements ActionListener{

	
	private MainFrame _frame; 
	public boolean _distanceExceeded = false;
	private int _actualRow;
	private int _actualCol;
	public Table _table;
	
	
	public CustomAlgorithm(MainFrame frame, Table table) {
		_frame = frame;
		_table = table;
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
			Robot.getMovement().turnRight();
			StartAlgorithm._timer.start();
		} else {
			Robot.getMovement().moveForward();

		}
		
		if(Movement._listOfMovements.size() > 3)
		{
			if(reachedStoppingCriteria())
			{
				StartAlgorithm._timer.stop();
			}
		}

		_frame.repaint();		
		
		
	}

	private boolean getSensorData(int row, int col) {

		Coordinates2D[] scannedArea = getScannedArea(row, col);
		
		boolean unaccesable = false;
		
		for(int i = 0; i < scannedArea.length; i++)
		try {

			if(_table.getMarkedObstacles(scannedArea[i].getRow(), scannedArea[i].getCol()))
			{
				unaccesable = true;
			} 
			
	
		} catch(ArrayIndexOutOfBoundsException e)
		{
			e.getStackTrace();
			unaccesable = true;
		}
		
		return unaccesable;
	}

	private Coordinates2D[] getScannedArea(int row, int col) {
		
		Coordinates2D[][] arrayPosition = new Coordinates2D[4][4];
		arrayPosition = Robot.getCoordinates(row, col);

		Coordinates2D[] scannedArea = new Coordinates2D[4];
		switch(Movement._dir) {
		case UP:
			
			for(int j = 0; j <4; j++)
			{
				arrayPosition[0][j].setRow(arrayPosition[0][j].getRow() + (int) Math.sin(Math.toRadians(Movement._ang)));
				scannedArea[j] = arrayPosition[0][j];
			}
			
		break;
		case DOWN:
			
			for(int j = 0; j <4; j++)
			{
				arrayPosition[3][j].setRow(arrayPosition[3][j].getRow() + (int) Math.sin(Math.toRadians(Movement._ang)));
				scannedArea[j] = arrayPosition[3][j];
			}
		break;
		case RIGHT:
			
			for(int i = 0; i <4; i++)
			{
				arrayPosition[i][3].setCol(arrayPosition[i][3].getCol() + (int) Math.cos(Math.toRadians(Movement._ang)));
				scannedArea[i] = arrayPosition[i][3];
			}
		break;
		case LEFT:
			
			for(int i = 0; i <4; i++)
			{
				arrayPosition[i][0].setCol(arrayPosition[i][0].getCol() + (int) Math.cos(Math.toRadians(Movement._ang)));
				scannedArea[i] = arrayPosition[i][0];
			}
		break;
		
		default:
			System.out.println("Direction not defined!");
			break;
		}
		
		return scannedArea;
	}

	private boolean reachedStoppingCriteria() {
		int count = 0;
		
		for(int i = 1; i <= 4; i++)
		if(Movement._listOfMovements.get(Movement._listOfMovements.size()-i) == LastMove.RIGHT)
		{
			count += 1;
		}
		
		if(count > 3)
		{
			return true;
		} 
		
		return false;
	}
	
}
