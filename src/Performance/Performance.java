package Performance;

/**
 * Class evaluating the performance for a algorithm applied on a specific environment
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Algorithms.Basic;
import Algorithms.CPPAlgorithm;
import Algorithms.NearestNeighbour;
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
	private long _duration = 0;
	
	private static int _numberStats = 12;
	
	private MainFrame _frame;
	private int _iteration;
	private String _algorithm;
	
	private Map<Integer, Double> _secondsMap;
	
	private static XSSFSheet _sheetSpiral;
	private static XSSFSheet _sheetZigZag;
	private static XSSFSheet _sheetRandom;
	
	private static XSSFSheet _sheetSpiralCumulative;
	private static XSSFSheet _sheetZigZagCumulative;
	private static XSSFSheet _sheetRandomCumulative;

	private static XSSFWorkbook _workbook;
	
	private static Object[][] _spiral;
	private static Object[][] _zigzag;
	private static Object[][] _random;
	
	private static Object[][] _spiralCumulative;
	private static Object[][] _zigzagCumulative;
	private static Object[][] _randomCumulative;

	private List<Object> _statList;
	private List<String> _statNameList;
	
	public Thread1 _t1;
	
	/**
	 * Constructor 
	 * @param frame 	GUI
	 * @param iteration		current iteration of test series
	 * @param algorithm		algorithm to be evaluated
	 * @param secondsMap	map containing execution time stamps for each second and the corresponding cumulative coverage
	 */
	public Performance(MainFrame frame, int iteration, String algorithm, Map<Integer, Double> secondsMap)
	{
		_frame = frame;
		_iteration = iteration;
		_algorithm = algorithm;
		_obstacles = Table._numberObs;
		_secondsMap = secondsMap;
	}

	/**
	 * Evaluation of the test series
	 * @param timeLimit 	predefined limit after the algorithm stops
	 * @param duration		execution time of the algorithm
	 * @throws IOException
	 */
	public void evaluateSeries(int timeLimit, long duration) throws IOException 
	{
		_duration = duration;
		Thread1._durationsList.add(_duration);
		
		computeStats();
		
		generateStatNameList();
		generateStatList();

		archive(timeLimit);

		_frame.saveImage(_algorithm, _iteration, _obstacles);
		
		if(CPPAlgorithm._cpp == "Random")
		{
			nextIteration();
		} else {
			nextAlgorithm();
		}
	}
	
	/**
	 * Evaluation of a single test executed via GUI
	 * @param timeLimit 	predefined limit after the algorithm stops
	 */
	public void evaluateSolo(int timeLimit) throws IOException 
	{
		computeStats();

		archive(timeLimit);
	}


	/**
	 * Initiating next iteration if all 3 algorithms have been tested during the test series
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void nextIteration() throws FileNotFoundException, IOException 
	{
		TestSeries._iteration++;
		int i = TestSeries._iteration;
		if(i < TestSeries._iterations) 
		{
			_t1 = new Thread1(i, _frame);
			_t1.clearAlgorithm();	
			_t1.startIteration(i, TestSeries._obstacles);
		} else {
			TestSeries._iteration = TestSeries._iterations;
			i = TestSeries._iterations;
			
			printExcel(_iteration);

			System.exit(0);
			
		}
	}
	
	/**
	 * Initiating the next algorithm after the beforehand has finished durinf test series
	 */
	private void nextAlgorithm() 
	{
		switch(CPPAlgorithm._cpp)
		{
			case "Spiral" : 
				_t1 = new Thread1(_iteration, _frame);
				_t1.clearAlgorithm();
				_t1.startZigZag();
			break;

			case "ZigZag" :
			_t1 = new Thread1(_iteration, _frame);
			_t1.clearAlgorithm();
			
			long maxDuration = Collections.max(Thread1._durationsList);
			_t1.startRandom(maxDuration-1);
			
			_t1.clearMap();
			break;
		
		}	
	}

	/**
	 * Archiving results in txt files and updating the excel file
	 * @param timeLimit
	 * @throws IOException
	 */
	private void archive(int timeLimit) throws IOException {
		     
		int nextCol =  _iteration;
        updateExcelCols(nextCol, _iteration);
        
	        
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./results/" + _algorithm + "_" + _obstacles + "_" + _iteration + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);

		    writeToFile("Local Time:_" + java.time.LocalDateTime.now() + "_____________________\n\n", osw);
		    writeToFile("Algorithm: " + _frame.getAlgorithm() + " with time limit: " + timeLimit + "\n\n", osw);

			writeToFile("\nDistance: " + _totalDistance, osw);
			writeToFile("\nNumber of turns: " + _numberOfTurns, osw);
			writeToFile("\nTotal Movements: " + _totalMovements, osw);
			writeToFile("\nNumber of Obstacles: " + _obstacles, osw);

			
			writeToFile("\n\nFree Cells: " + _freeCells, osw);
			writeToFile("\nVisited Cells: " + _visitedCells, osw);
			
			//Subtract 4 from the set of revisited Cells due to initialization error
			writeToFile("\nRevisited Cells: " + (_revisitedCells-4), osw);
			writeToFile("\nObstacle Cells: " + _obstacleCells, osw);
			writeToFile("\nAccessable Cells: " + _accessableCells, osw);
			writeToFile("\nTimes Dijkstra executed: " +  _dijkstraExecutions, osw);
			writeToFile("\n\nDuration: " +  _duration , osw);

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


	/**
	 * Computation of some performance measures
	 */
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
				
		System.out.println("Coverage: " +  _coverage);
	}

	public Thread1 getNewThread()
	{
		return _t1;
	}
	
	/**
	 * Computing total coverage of robot
	 * @return Coverage as abs value
	 */
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
	
	/**
	 * Initiating excel sheets
	 * @param spiral	Sheet for Spiral algorithm
	 * @param zigzag	Sheet for ZigZag algorithm
	 * @param random	Sheet for RandomWalk
	 * @param spiralCumulative	Sheet for cumulative coverage of Spiral algorithm for each second
	 * @param zigzagCumulative	Sheet for cumulative coverage of ZigZag algorithm for each second
	 * @param randomCumulative	Sheet for cumulative coverage of RandomWalk for each second
	 */
	public static void initExcel(Object[][] spiral, Object[][] zigzag, Object[][] random, Object[][] spiralCumulative, Object[][] zigzagCumulative, Object[][] randomCumulative)
	{
		 _spiral = spiral;
		 _zigzag = zigzag;
		 _random = random;
		 _spiralCumulative = spiralCumulative;
		 _zigzagCumulative = zigzagCumulative;
		 _randomCumulative = randomCumulative;
		 
		 _workbook = new XSSFWorkbook();
         _sheetSpiral = _workbook.createSheet("spiral");
         _sheetZigZag = _workbook.createSheet("zigzag");
         _sheetRandom = _workbook.createSheet("random");
         _sheetSpiralCumulative = _workbook.createSheet("spiral_cumulative");
         _sheetZigZagCumulative = _workbook.createSheet("zigzag_cumulative");
         _sheetRandomCumulative = _workbook.createSheet("random_cumulative");
	}
	
	/**
	 * Updating the excel cols 
	 * @param nextCol
	 * @param iteration
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void updateExcelCols(int nextCol, int iteration) throws FileNotFoundException, IOException
	{
		switch(CPPAlgorithm._cpp)
		{
			case "Spiral" : 
				_spiral[0][0] = "Stats for";
				_spiral[0][nextCol] = _algorithm + iteration;
				for(int i = 0; i < _numberStats; i ++)
				{
					_spiral[i+1][0] = _statNameList.get(i);
					_spiral[i+1][nextCol] = _statList.get(i);
				}
				_spiralCumulative[0][0] = "Coverage at second";
				_spiralCumulative[0][nextCol] = _algorithm + iteration;
				for(int key : _secondsMap.keySet())
				{
					_spiralCumulative[key+1][0] = key;
					_spiralCumulative[key+1][nextCol] = _secondsMap.get(key);
				}
			break;

			case "ZigZag" :
				_zigzag[0][0] = "Stats for";
				_zigzag[0][nextCol] = _algorithm + iteration;
				for(int i = 0; i < _numberStats; i ++)
				{
					_zigzag[i+1][0] = _statNameList.get(i);
					_zigzag[i+1][nextCol] = _statList.get(i);

				}
				_zigzagCumulative[0][0] = "Coverage at second";
				_zigzagCumulative[0][nextCol] = _algorithm + iteration;
				for(int key : _secondsMap.keySet())
				{
					_zigzagCumulative[key+1][0] = key;
					_zigzagCumulative[key+1][nextCol] = _secondsMap.get(key);
				}
			break;
		
			case "Random" :
				_random[0][0] = "Stats for";
				_random[0][nextCol] = _algorithm + iteration;
				for(int i = 0; i < _numberStats; i ++)
				{
					_random[i+1][0] = _statNameList.get(i);
					_random[i+1][nextCol] = _statList.get(i);
				}
				_randomCumulative[0][0] = "Coverage at second";
				_randomCumulative[0][nextCol] = _algorithm + iteration;
				for(int key : _secondsMap.keySet())
				{
					_randomCumulative[key+1][0] = key;
					_randomCumulative[key+1][nextCol] = _secondsMap.get(key);
				}
			break;
		}
		
	}
	
	/**
	 * Archiving results in excel
	 * @param iteration
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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

		for(Object[] arow : _spiralCumulative)
		{
            Row row = _sheetSpiralCumulative.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
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

		for(Object[] arow : _zigzagCumulative)
		{
            Row row = _sheetZigZagCumulative.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
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
		 
		rowCount = 0;

		for(Object[] arow : _randomCumulative)
		{
            Row row = _sheetRandomCumulative.createRow(++rowCount);
            
            int columnCount = 1;
            
			for (Object field : arow) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof String) {
                    cell.setCellValue((String) field);
                }               
            }		
		}
		
	    try (FileOutputStream outputStream = new FileOutputStream("./results/evaluation_java.xlsx")) {
	        _workbook.write(outputStream);
	    }
	    
	}
    
	
	/**
	 * Generating first column of excel with number of the corresponding performance measurements
	 */
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
		_statNameList.add("Obstacle Cells");
		_statNameList.add("Accessable Cells");
		_statNameList.add("Dijkstra Executions");
		_statNameList.add("Duration");
		_statNameList.add("Coverage");
	}
	
	
	/**
	 * Generating array containing the performance measurements corresponding to the statNameList
	 */
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
		_statList.add(_obstacleCells);
		_statList.add(_accessableCells);
		_statList.add(_dijkstraExecutions);
		_statList.add(_duration);
		_statList.add(_coverage);
	}
	
	
	//Writing to txt file
	private static void writeToFile(String message, OutputStreamWriter osw){
	    try {
	        osw.write(message);
	        osw.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
