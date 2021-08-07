import java.io.IOException;

import javax.swing.JFrame;
public class Teacher_main {
	
    public static void main(String[] args) throws IOException {
      
    	WhiteBoard wb = new WhiteBoard();
    	
    	wb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	wb.setVisible(true);
    	wb.setSize(800,600);
    	wb.setLocation(500, 250);
    	wb.runServer();
    	
    }
}
