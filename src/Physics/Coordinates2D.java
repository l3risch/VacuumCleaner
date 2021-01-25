package Physics;

/**
 * Class providing a 2D coordinate system for robot and environment 
 * @author Christian
 *
 */
public class Coordinates2D {
	
	public int _row;
	public int _col;

	public Coordinates2D(int row, int col)
	{
		_row = row;
		_col = col;
	}
	
	public int getRow()
	{
		return _row;
	}
	
	public int getCol()
	{
		return _col;
	}
	
	public void setRow(int row)
	{
		_row = row;
	}
	
	public void setCol(int col)
	{
		_col = col;
	}
	
	public static Coordinates2D copyOf(Coordinates2D coordinates2D)
	{
		Coordinates2D copy = new Coordinates2D(coordinates2D.getRow(), coordinates2D.getCol());
		
		return copy;
	}
	
}
