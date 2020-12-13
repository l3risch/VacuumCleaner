package Performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
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
	
	private static XSSFSheet _sheetSpiral;
	private static XSSFSheet _sheetZigZag;
	private static XSSFSheet _sheetRandom;

	private static XSSFWorkbook _workbook;
	
	private static Object[][] _spiral;
	private static Object[][] _zigzag;
	private static Object[][] _random;

	private List<Object> _statList;
	private List<String> _statNameList;

	
	public Thread1 _t1;
	
	
	public Performance(MainFrame frame, int iteration, String algorithm, Map<Integer, Double> secondsMap)
	{
		_frame = frame;
		_iteration = iteration;
		_algorithm = algorithm;
		_obstacles = Table._numberObs;
		_secondsMap = secondsMap;
	}

	public void evaluate(int timeLimit) throws IOException 
	{
		computeStats();
		
		generateStatNameList();
		generateStatList();

		archive(timeLimit);

		_frame.saveImage(_algorithm, _iteration);
		
		if(Thread1._cpp == "Random")
		{
			nextIteration();
		} else {
			nextAlgorithm();
		}
	}



	private void nextIteration() throws FileNotFoundException, IOException 
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
			printExcel(_iteration);
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
				break;
	
				case "ZigZag" :
				_t1 = new Thread1(_iteration, _frame);
				_t1.clearAlgorithm();
				_t1.startRandom();
				_t1.clearMap();
				break;
			
			}
		}		
	}

	private void archive(int timeLimit) throws IOException {
		     
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
	
	public static void initExcel(Object[][] spiral, Object[][] zigzag, Object[][] random)
	{
		 _spiral = spiral;
		 _zigzag = zigzag;
		 _random = random;
		 _workbook = new XSSFWorkbook();
         _sheetSpiral = _workbook.createSheet("spiral");
         _sheetZigZag = _workbook.createSheet("zigzag");
         _sheetRandom = _workbook.createSheet("random");
	}
	
	private void updateExcelCols(int iteration) throws FileNotFoundException, IOException
	{
		switch(Thread1._cpp)
		{
			case "Spiral" : 
				for(int i = 0; i < 13; i ++)
				{
					_spiral[i][0] = _statNameList.get(i);
					_spiral[i][iteration+1] = _statList.get(i);
				}
			break;

			case "ZigZag" :
				for(int i = 0; i < 13; i ++)
				{
					_zigzag[i][0] = _statNameList.get(i);
					_zigzag[i][iteration+1] = _statList.get(i);
				}
			break;
		
			case "Random" :
				for(int i = 0; i < 13; i ++)
				{
					_random[i][0] = _statNameList.get(i);
					_random[i][iteration+1] = _statList.get(i);
				}
			break;
		}
		
	}
	
	private void printExcel(int iteration) throws FileNotFoundException, IOException
	{
        int rowCount = 0;

		for(Object[] arow : _spiral)
		{
            Row row = _sheetSpiral.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Long) {
                    cell.setCellValue((Long) field);
                }                               
            }		
		}
	    
		rowCount = 0;
		
		for(Object[] arow : _zigzag)
		{
            Row row = _sheetZigZag.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Long) {
                    cell.setCellValue((Long) field);
                }                               
            }		
		}
		
		rowCount = 0;
		
		for(Object[] arow : _random)
		{
            Row row = _sheetRandom.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Long) {
                    cell.setCellValue((Long) field);
                }                               
            }		
		}
		
	    try (FileOutputStream outputStream = new FileOutputStream("./results/evaluation_java.xlsx")) {
	        _workbook.write(outputStream);
	    }
	    
	    try {
			_workbook.close();
		} catch (IOException e) {
	
		}
	}
    
	
	private void generateStatNameList()
	{
		_statNameList = new ArrayList<String>();
		_statNameList.add("Distance");
		_statNameList.add("Number of Turns");
		_statNameList.add("Number of Movements");
		_statNameList.add("Number of Obstacles");
		_statNameList.add("Free Cells");
		_statNameList.add("Visited Cells");
		_statNameList.add("Revisited Cells");
		_statNameList.add("Fraction of Revisited Cells");
		_statNameList.add("Obstacle Cells");
		_statNameList.add("Accessable Cells");
		_statNameList.add("Dijkstra Executions");
		_statNameList.add("Duration");
		_statNameList.add("Coverage");
	}
	
	
	private void generateStatList()
	{
		_statList = new ArrayList<Object>();
		_statList.add(_totalDistance);
		_statList.add(_numberOfTurns);
		_statList.add(_totalMovements);
		_statList.add(_obstacles);
		_statList.add(_freeCells);
		_statList.add(_visitedCells);
		_statList.add((_revisitedCells-4));
		_statList.add(_fracRevisited);
		_statList.add(_obstacleCells);
		_statList.add(_accessableCells);
		_statList.add(_dijkstraExecutions);
		_statList.add(CPPAlgorithm._duration);
		_statList.add(_coverage);
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
