package main;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.Timer;

import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Rendering.Renderer1;
import Threads.Thread1;

public class TestSeries {

	public String _algorithm;
	public static int _iterations;
	
	public static Timer _timer;
	
	public static long _start;
			
	public static JFrame _frame;
	
	public static boolean _series = false;
	
	public static int _iteration = 0;
		
	public static void main(String[] args) 
	{
		try {
			_series = true;
			_iterations = 2;
			_frame = new MainFrame();

			Thread1 t1 = new Thread1(_iteration, _frame);

			t1.startIteration(t1, _iteration);
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
			
	


}
