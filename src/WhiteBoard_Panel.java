import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;

public class WhiteBoard_Panel extends JPanel {
	
	public void paint(Graphics g) {
		super.paint(g);
	    	int i = 0;	
		g.setColor(WhiteBoard.c);
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		String time = formatter.format(now);
		
		if(WhiteBoard.Lflag == false) {
	 		if(WhiteBoard.type == 1) {
				for(i = 0;i < WhiteBoard.lines.size();i++) {
		              		g.drawLine(WhiteBoard.lines.get(i).x, WhiteBoard.lines.get(i).y, WhiteBoard.lines.get(i).dx, WhiteBoard.lines.get(i).y);
	       				}
			}
			else if(WhiteBoard.type == 2) {
				for(i = 0;i < WhiteBoard.lines.size();i++) {
			        	g.drawLine(WhiteBoard.lines.get(i).x, WhiteBoard.lines.get(i).y, WhiteBoard.lines.get(i).x, WhiteBoard.lines.get(i).dy);
		        		}
			}
			else if(WhiteBoard.type == 3) {
				for(i = 0;i < WhiteBoard.lines.size();i++) {
			        	g.drawLine(WhiteBoard.lines.get(i).x, WhiteBoard.lines.get(i).y, WhiteBoard.lines.get(i).dx, WhiteBoard.lines.get(i).dy);
		            	}
			}
			   
		}
		else if(WhiteBoard.Sflag == false) {
			if(WhiteBoard.Stype == 1) {
				for(i = 0;i < WhiteBoard.squares.size();i++) {
					g.drawRect(WhiteBoard.squares.get(i).x, WhiteBoard.squares.get(i).y, 100, 100);
				 }
			}
			else if(WhiteBoard.Stype == 2) {
				for(i = 0;i < WhiteBoard.squares.size();i++) {
					   g.fillRect(WhiteBoard.squares.get(i).x, WhiteBoard.squares.get(i).y, 100, 100);
				}
			}
		}
		else if(WhiteBoard.Cflag == false) {
			if(WhiteBoard.Ctype == 1) {
		    		for(i = 0;i < WhiteBoard.circles.size();i++) {
		    			g.drawOval(WhiteBoard.circles.get(i).x, WhiteBoard.circles.get(i).y, 100, 100);
		    		}
		    	}
		    	else if(WhiteBoard.Ctype == 2) {
		    		for(i = 0;i < WhiteBoard.circles.size();i++) {
		    			g.fillOval(WhiteBoard.circles.get(i).x, WhiteBoard.circles.get(i).y, 100, 100);
		    		}
		    	}
		}
		
		WhiteBoard.lcd.setText("Time Information: "+ time+"                                    " + "Number Of Shapes: " + i);
	    
	}
}

