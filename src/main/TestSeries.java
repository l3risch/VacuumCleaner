package main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Threads.Thread1;

public class TestSeries {

	public String _algorithm;
	public static int _iterations;
	
	public static Timer _timer;
	
	public static long _start;
			
	public static JFrame _frame;
	
	public static boolean _series = false;
	
	public static int _iteration = 0;
	
	public static int _obstacles = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		
		try {
			_series = true;
			_iterations = 20;
			_frame = new MainFrame();

			Thread1 t1 = new Thread1(_iteration, _frame);

			t1.startIteration(_iteration, _obstacles);
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


}
