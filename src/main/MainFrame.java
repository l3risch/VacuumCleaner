package main;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Listener.StartAlgorithm;
import Listener.StopAlgorithm;
import MapGeneration.MapGenerator;
import Objects.Robot;
import Objects.Table;
import Rendering.Renderer1;

public class MainFrame extends JFrame{

	public Renderer1 _render;
	public static Table _table;
	public static Robot _robot;
	private Container _contentPane;
	
	private int _cols = 64;
	public static final String VERSION = "0.0.1";
	public static final String TITLE = "Vacuum Cleaner Simulator " + VERSION;
	public static Dimension SCREEN_SIZE;
	
	public String _algorithm;
	public JComboBox<String> _comboBox_1;
		
	public static JFrame _frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_frame = new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainFrame()
	{		
		SCREEN_SIZE = new Dimension(200 + 10 * 64, 250 + 10 * Table._cols );
		setSize(SCREEN_SIZE);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JPanel menu = new JPanel();
		menu.setLayout(new GridBagLayout());
		menu.setBounds(0, 20, 200 + 10 * _cols, 120);
		GridBagConstraints c = new GridBagConstraints();
		
	    JLabel mapLabel;
	    mapLabel = new JLabel("Select Map:");
		c.insets = new Insets(0,10,10,0); 
		c.gridx = 0;
		c.gridy = 0;
		menu.add(mapLabel, c);
		
		/**
		 * Choose Map
		 */
		String mapList[] = {"1", "2", "3", "4", "5"};
		JComboBox<String> comboBox_2 = new JComboBox<String>(mapList);
		c.insets = new Insets(0,10,10,10); 
		c.gridwidth = 2;
		c.gridx = 2;
		c.gridy = 0;	
		menu.add(comboBox_2, c);
		
		
	    JButton randomButton;
	    randomButton = new JButton("Random Map");
		c.insets = new Insets(0,10,10,0); 
		c.gridx = 4;
		c.gridy = 0;
		menu.add(randomButton, c);
		
		new Table();
        new Robot();
		
        //Set preselected Map
		comboBox_2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
			Table.setType(comboBox_2.getSelectedItem().toString());
			Robot.setStartingPos(1, 60);

			_contentPane.repaint();
			}
		});
		
		//Set random Map
		randomButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
			if(Table._listObs != null)
			{
				Table._listObs.clear();
			}
			new MapGenerator();
			Robot.setRandomStartingPos();
			System.out.println(Table._listObs.size());

			_contentPane.repaint();
			}
		});
		
		
		
	    JLabel algLabel;
	    algLabel = new JLabel("Select Algorithm:");
		c.gridx = 0;
		c.gridy = 1;
		menu.add(algLabel, c);
		
		/**
		 * Choose Algorithm
		 */
		String algList[] = {"Test", "Custom", "Random Walk", "Cellular Decomposition"};
		_comboBox_1 = new JComboBox<String>(algList);
		_comboBox_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
			_contentPane.repaint();
			}
		});
		c.gridwidth = 2;
		c.gridx = 2;
		c.gridy = 1;
		menu.add(_comboBox_1, c);
		
		
	    JLabel startLabel;
		startLabel = new JLabel("Start Vaccum Run:");
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0,10,10,0); 
		menu.add(startLabel, c);
						
		JButton startButton = new JButton("Start");
		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(0,10,10,10); 
		menu.add(startButton, c);
		//Setze Algorithmus
		startButton.addActionListener(new StartAlgorithm(this));
		
		JButton stopButton = new JButton("Stop");
		c.gridx = 4;
		c.gridy = 2;
		menu.add(stopButton, c);
		stopButton.addActionListener(new StopAlgorithm());
		
		_render = new Renderer1();

		_contentPane = getContentPane();
		_contentPane.add(menu);
		_contentPane.add(_render);
				
		setVisible(true);

	}
	
	public static Table getTable()
	{
		return _table;
	}
	
	public static Robot getRobot()
	{
		return _robot;
	}
	
	public String getAlgorithm()
	{
		_algorithm = _comboBox_1.getSelectedItem().toString();
		return _algorithm;
	}
}


