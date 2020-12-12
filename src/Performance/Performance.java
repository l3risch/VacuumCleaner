package Performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Observable;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Algorithms.Basic;
import Algorithms.CPPAlgorithm;
import Algorithms.NearestNeighbour;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Threads.Thread1;
import main.MainFrame;
import main.TestSeries;

public class Performance extends Basic{

	private int _numberOfTurns;
	private int _totalDistance;
	private int _totalMovements;
	
	private int _freeCells = 0;
	private static int _visitedCells = 0;
	private int _obstacleCells = 0;
	private int _accessableCells = 0;
	private double _coverage = 0;
	private int _obstacles = 0;
	private double _fracRevisited = 0;
	
	private MainFrame _frame;
	private int _iteration;
	private String _algorithm;
	
	private Map<Integer, Double> _secondsMap;
	
	private XSSFSheet _sheet;
	private XSSFWorkbook _workbook;
	
	public Thread1 _t1;
	
	
	public Performance(MainFrame frame, int iteration, String algorithm, Map<Integer, Double> secondsMap)
	{
		_frame = frame;
		_iteration = iteration;
		_algorithm = algorithm;
		_obstacles = Table._numberObs;
		_secondsMap = secondsMap;
	}

	public void evaluate(int timeLimit) 
	{
		computeStats();
		
		try {
			archive(timeLimit);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_frame.saveImage(_algorithm, _iteration);
		
		if(Thread1._cpp == "ZigZag")
		{
			nextIteration();
		} else {
			nextAlgorithm();
		}
	}



	private void nextIteration() 
	{
		TestSeries._iteration++;
		int i = TestSeries._iteration;
		if(i < TestSeries._iterations) 
		{
			_t1 = new Thread1(i, _frame);
			_t1.clearAlgorithm();	
			TestSeries._obstacles++;
			_t1.startIteration(i, TestSeries._obstacles);
		} else {
		    System.exit(0);
		}
	}

	private void nextAlgorithm() 
	{
		if(TestSeries._series == true)
		{
			switch(Thread1._cpp)
			{
				case "Spiral" : 
					_t1 = new Thread1(_iteration, _frame);
					_t1.clearAlgorithm();
					_t1.startZigZag();
					_t1.clearMap();
				break;
	
//				case "ZigZag" :
//				_t1 = new Thread1(_iteration, _frame);
//				_t1.startRandom();
//				_t1.clearMap();
//				break;
				
//				case "Custom" :
//				_t1 = new Thread1(_iteration, _frame);
//				_t1.clearAlgorithm();
//				_t1.startRandom();
//				_t1.clearMap();
//				break;
			}
		}		
	}

	private void archive(int timeLimit) throws IOException {
		     
        exportToExcel();     
        updateExcelCols(_iteration);
         
	        
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/" + _algorithm + _iteration + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);

		    writeToFile("Local Time:_" + java.time.LocalDateTime.now() + "_____________________\n\n", osw);
		    writeToFile("Algorithm: " + _frame.getAlgorithm() + " with time limit: " + timeLimit + "\n\n", osw);

			writeToFile("\nDistance: " + _totalDistance, osw);
			writeToFile("\nNumber of turns: " + _numberOfTurns, osw);
			writeToFile("\nTotal Movements: " + _totalMovements, osw);
			writeToFile("\nNumber of Obstacles: " + _obstacles, osw);

			
			writeToFile("\n\nFree Cells: " + _freeCells, osw);
			writeToFile("\nVisited Cells: " + _visitedCells, osw);
			
			//Substract 4 from the set of revisited Cells due to initialization error
			writeToFile("\nRevisited Cells: " + (_revisitedCells-4), osw);
			writeToFile("\nObstacle Cells: " + _obstacleCells, osw);
			writeToFile("\nAccessable Cells: " + _accessableCells, osw);
			writeToFile("\nTimes Dijkstra executed: " +  _dijkstraExecutions, osw);
			writeToFile("\n\nDuration: " +  CPPAlgorithm._duration , osw);

			writeToFile("\n\nCoverage: " +  _coverage, osw);
			
			
			writeToFile("\n\n________________________________________\nAchieved coverage at specific execution time in seconds\n", osw);

			for(int key : _secondsMap.keySet())
			{
				writeToFile(key + " : " + _secondsMap.get(key) + "\n", osw);
			}
			
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		_visitedCells = 0;
		_dijkstraExecutions = 0;
	}


	private void computeStats() 
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();

		_numberOfTurns = Robot.getMovement()._numberOfTurns;
		_totalDistance = Robot.getMovement()._totalDistance;
		_totalMovements= _totalDistance + _numberOfTurns;

		for(int i = 0; i<64; i++)
		{
			for(int j = 0; j<64; j++)
			{
				if(pathMatrix[i][j]=='2' || pathMatrix[i][j]=='D')
				{
					_freeCells++;
				}
				if(pathMatrix[i][j]=='1' || pathMatrix[i][j]=='S')
				{
					_visitedCells++;
				}
				if(Table._markedObstacles[i][j] == true)
				{
					_obstacleCells++;
				}
			}
		}
		
		_accessableCells = (64*64) - _obstacleCells;
		_coverage = ((double)_visitedCells/(double)_accessableCells) ;
		if(_coverage > 1)
		{
			_coverage = 1;
		}
		
		_fracRevisited = _revisitedCells/_visitedCells;
		
		System.out.println("Coverage: " +  _coverage);
	}

	public Thread1 getNewThread()
	{
		return _t1;
	}
	
	public double computeCoverage()
	{
		char[][] pathMatrix = NearestNeighbour.getPathMatrix();
		int visitedCells = 0;
		int obstacleCells = 0;
		int accessableCells = 0;


		for(int i = 0; i<64; i++)
		{
			for(int j = 0; j<64; j++)
			{
				if(pathMatrix[i][j]=='1' || pathMatrix[i][j]=='S')
				{
					visitedCells++;
				}
				if(Table._markedObstacles[i][j] == true)
				{
					obstacleCells++;
				}
			}
		}
		
		accessableCells = (64*64) - obstacleCells;
		
		double coverage = ((double)visitedCells/(double)accessableCells) * 100;
		
		return coverage;
	}
	
	private void exportToExcel()
	{
		 _workbook = new XSSFWorkbook();
         _sheet = _workbook.createSheet("Stats");

         
         Row row = _sheet.createRow(1);
         Cell cell = row.createCell(1);
         cell.setCellValue("Distance");
         row = _sheet.createRow(2);
         cell = row.createCell(1);
         cell.setCellValue("Number of Turns");     
         row = _sheet.createRow(3);
         cell = row.createCell(1);
         cell.setCellValue("Total Movements");    
         row = _sheet.createRow(4);
         cell = row.createCell(1);
         cell.setCellValue("Number of Obstacles");    
         row = _sheet.createRow(5);
         cell = row.createCell(1);
         cell.setCellValue("Free Cells");    
         row = _sheet.createRow(6);
         cell = row.createCell(1);
         cell.setCellValue("Visited Cells");    
         row = _sheet.createRow(7);
         cell = row.createCell(1);
         cell.setCellValue("Revisited Cells");    
         row = _sheet.createRow(8);
         cell = row.createCell(1);
         cell.setCellValue("Fraction of Revisited Cells");    
         row = _sheet.createRow(9);
         cell = row.createCell(1);
         cell.setCellValue("Obstacle Cells");    
         row = _sheet.createRow(10);
         cell = row.createCell(1);
         cell.setCellValue("Accessable Cells");    
         row = _sheet.createRow(11);
         cell = row.createCell(1);
         cell.setCellValue("Time Dijkstra executed");    
         row = _sheet.createRow(13);
         cell = row.createCell(1);
         cell.setCellValue("Duration"); 
         row = _sheet.createRow(14);
         cell = row.createCell(1);
         cell.setCellValue("Coverage");    
         
                 
	}
	
	private void updateExcelCols(int iteration) throws FileNotFoundException, IOException
	{
		int currentCol = 2 + iteration;
		Row row = _sheet.createRow(1);
        Cell cell = row.createCell(currentCol);
        cell.setCellValue(_totalDistance);    
        row = _sheet.createRow(2);
        cell = row.createCell(currentCol);
        cell.setCellValue(_numberOfTurns);    
        row = _sheet.createRow(3);
        cell = row.createCell(currentCol);
        cell.setCellValue(_totalMovements);    
        row = _sheet.createRow(4);
        cell = row.createCell(currentCol);
        cell.setCellValue(_obstacles);    
        row = _sheet.createRow(5);
        cell = row.createCell(currentCol);
        cell.setCellValue(_freeCells);    
        row = _sheet.createRow(6);
        cell = row.createCell(currentCol);
        cell.setCellValue(_visitedCells);    
        row = _sheet.createRow(7);
        cell = row.createCell(currentCol);
        cell.setCellValue((_revisitedCells-4));  
        row = _sheet.createRow(8);
        cell = row.createCell(currentCol);
        cell.setCellValue(_fracRevisited);    
        row = _sheet.createRow(9);
        cell = row.createCell(currentCol);
        cell.setCellValue(_obstacleCells);    
        row = _sheet.createRow(10);
        cell = row.createCell(currentCol);
        cell.setCellValue(_accessableCells);    
        row = _sheet.createRow(11);
        cell = row.createCell(currentCol);
        cell.setCellValue(_dijkstraExecutions);    
        row = _sheet.createRow(13);
        cell = row.createCell(currentCol);
        cell.setCellValue(CPPAlgorithm._duration);    
        row = _sheet.createRow(14);
        cell = row.createCell(currentCol);
        cell.setCellValue(_coverage);  
        
        try (FileOutputStream outputStream = new FileOutputStream("./results/evaluation_java.xlsx")) {
            _workbook.write(outputStream);
        }
        
        try {
			_workbook.close();
		} catch (IOException e) {

		}

	}
	
	
	private static void writeToFile(String message, OutputStreamWriter osw){
	    try {
	        osw.write(message);
	        osw.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
