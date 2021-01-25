package Rendering;

/**
 * Class rendering the movements of the robot
 */
import java.awt.AlphaComposite;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Algorithms.Basic;
import Algorithms.Basic.CellState;
import Objects.Obstacle;
import Objects.Obstacle.Shape;
import Objects.Robot;
import Objects.Table;
import Physics.Coordinates2D;

public class Renderer1 extends JPanel{
			
	private static Graphics _g;

	private static int[][] _opacity = new int[Table._rows][Table._cols];
	
	public Renderer1()
	{
		setLayout(null);
		validate();
	}


	@Override
	public void paintComponent(Graphics g)
	{
		_g = g;
        super.paintComponent(_g);
		
        paintCells();
		renderRobot(_g);
		renderTable();	
	}


	private void renderRobot(Graphics g)
	{
		ImageIcon round = createRoundImage(g);
		Image img = round.getImage();
		Image scaledImg = getScaledImage(img, 40, 40);
		double angle = Robot.getMovement().getAng() + 90;
		BufferedImage rotatedImg = rotateRobot(angle, scaledImg);
	    
		_g.drawImage(rotatedImg, Robot.getX(), Robot.getY(), this);
	}
	
	private void renderTable()
	{		
		_g.drawRect(100, 100, 10 * Table._cols, 10 * Table._rows + 40);
		drawGrid(Table._rows, Table._cols);
		
		if(Table._listObs != null)
		{
			for(Obstacle obs: Table._listObs)
			{
				getObstacle(obs);
			}
		}
		
		_g.dispose();
	}

	
	private BufferedImage rotateRobot(double angle, Image img)
	{
		BufferedImage bufferedimg = (BufferedImage) img;
		final double rads = Math.toRadians(angle);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(bufferedimg.getWidth() * cos + bufferedimg.getHeight() * sin);
		final int h = (int) Math.floor(bufferedimg.getHeight() * cos + bufferedimg.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, bufferedimg.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads, 0, 0);
		at.translate(-bufferedimg.getWidth() / 2, -bufferedimg.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(bufferedimg,rotatedImage);
		
		return rotatedImage;
	}


	private void drawGrid(int rows, int cols)
	{
		int x1 = 100;
		int y1 = 100;
		

		for(int i = 0; i < rows+4; i++)
		{
			_g.drawLine(x1, y1, (cols * 10) + 100, y1);
			y1 += 10;
		}
		
		y1 = 100;
		
		for(int j = 0; j < cols ; j++)
		{
			_g.drawLine(x1, y1, x1, (rows * 10) +140);
			x1 += 10;
		}
	}
	
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	
	private void getObstacle(Obstacle obs) {
		
		if(obs.getShape().equals(Shape.RECTANGLE))
		{
			_g.fillRect(obs.getX(), obs.getY(), obs.getWidth(), obs.getHeight());
		} else if (obs.getShape().equals(Shape.OVAL))
		{
			_g.fillOval(obs.getX(), obs.getY(), obs.getWidth(), obs.getHeight());
		}
	}
	
	private ImageIcon createRoundImage(Graphics g)
	{
		 BufferedImage master;
			try {
				master = ImageIO.read(new File(("./resources/vacuum2.jpg")));
			
			    int diameter = Math.min(master.getWidth(), master.getHeight());
			    BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);

			    Graphics2D g2d = mask.createGraphics();
			    g2d.fillOval(0, 0, diameter - 1, diameter - 1);
			    g2d.dispose();

			    BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
			    g2d = masked.createGraphics();
			    //applyQualityRenderingHints(g2d);
			    int x = (diameter - master.getWidth()) / 2;
			    int y = (diameter - master.getHeight()) / 2;
			    g2d.drawImage(master, x, y, this);
			    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
			    g2d.drawImage(mask, 0, 0, this);
			    g2d.dispose();

//			    JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(masked)));
				return new ImageIcon(masked);
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	
	public void clearMarks()
	{
		//Clear marked free cells
		for(String key : Basic._mentalMap.keySet())
 		{
			int row = Integer.parseInt(key.substring(0, 2));
			int col = Integer.parseInt(key.substring(2, 4));


			if(Basic._mentalMap.get(key).equals(CellState.FREE))
 			{
					_g.setColor(new Color(0,0,0,0));
					_g.fillRect(col * 10 + 100, row * 10 + 140, 10, 10);
					_g.setColor(Color.BLACK);
 			}	
		
 		}
		
		
		
		_opacity = new int[Table._rows][Table._cols];
		Basic._mentalMap.clear();
	}
	
	public static void paintCells()
	{
		resetOpacity();
		for(String key : Basic._mentalMap.keySet())
 		{
			int row = Integer.parseInt(key.substring(0, 2));
			int col = Integer.parseInt(key.substring(2, 4));


			if(Basic._mentalMap.get(key).equals(CellState.FREE))
 			{
					_g.setColor(Color.GRAY);
					_g.fillRect(col * 10 + 100, row * 10 + 140, 10, 10);
					_g.setColor(Color.BLACK);
 			}	
			
			if(Basic._mentalMap.get(key).equals(CellState.VISITED) && row <64 && row >= 0 && col <64 && col >= 0)
			{
				Color color = new Color(55, 235, 52,  _opacity[row][col]);
				_g.setColor(color);
				_g.fillRect(col * 10 + 100, row * 10 + 140, 10, 10);
				_g.setColor(Color.BLACK);
			}
		
 		}
		
//		paintNearestCell();

	}
	
	//Reset opacity after each coverage
	private static void resetOpacity() {
		Coordinates2D[][] coordinates = Robot.getCoordinates(Robot.getYasRow(), Robot.getXasCol());
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(coordinates[i][j].getRow() > 0 && coordinates[i][j].getRow() < 64 && coordinates[i][j].getCol() > 0 && coordinates[i][j].getCol() < 64)
				{
					if(_opacity[coordinates[i][j].getRow()][coordinates[i][j].getCol()] < 235)
					{
					_opacity[coordinates[i][j].getRow()][coordinates[i][j].getCol()] = _opacity[coordinates[i][j].getRow()][coordinates[i][j].getCol()] + 20;
				
					}
				}
			}
		}		
	}
	
	
//	private static void paintNearestCell() 
//	{
//		
//		Coordinates2D nn = NearestNeighbour.getNearestNeighbour(Robot.getYasRow(), Robot.getXasCol());
//		_g.setColor(Color.BLUE);
//		_g.fillRect(nn.getCol() * 10 + 100, nn.getRow() * 10 + 140, 10, 10);
//		_g.setColor(Color.BLACK);
//	}

}
