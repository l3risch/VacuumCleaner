package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopAlgorithm implements ActionListener {

	
	@Override
	public void actionPerformed(ActionEvent e) {
		StartAlgorithm._timer.stop();
	}

}
