package Objects;

/**
 * Class defining size position and shape of obstacles
 * Currently Oval shape is not used
 *
 */

public class Obstacle {

	public int _x;
	public int _width;
	public int _y;
	public int _height;
	
	public enum Shape{OVAL, RECTANGLE}
	public Shape _shape;
	
	public Obstacle(int x, int y, int width, int height, Shape shape)
	{
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		_shape = shape;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public int getWidth()
	{
		return _width;
	}
	
	public int getHeight()
	{
		return _height;
	}
	
	public Shape getShape()
	{
		return _shape;
	}

}
