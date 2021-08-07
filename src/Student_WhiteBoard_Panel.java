import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;

public class Student_WhiteBoard_Panel extends JPanel {
	
	public void paint(Graphics g) {
		super.paint(g);
	    	int i = 0;	
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		String time = formatter.format(now);
		Student_WhiteBoard.lcd.setText("Time Information: "+ time+"                                    " + "Number Of Shapes: " + i);
	}
}
