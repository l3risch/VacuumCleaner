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

import Performance.Performance;
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
	
	private static Object[][] _spiral;
	private static Object[][] _zigzag;
	private static Object[][] _random;


	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		_iterations = 2;

		_spiral = new Object[13][_iterations+1];
		_zigzag = new Object[13][_iterations+1];
		_random = new Object[13][_iterations+1];

		
		Performance.initExcel(_spiral, _zigzag, _random);

		try {
			_series = true;
			_frame = new MainFrame();

			Thread1 t1 = new Thread1(_iteration, _frame);

			t1.startIteration(_iteration, _obstacles);
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


}
