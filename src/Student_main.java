import java.io.IOException;

import javax.swing.JFrame;

public class Student_main {
	
    public static void main(String[] args) throws IOException {
      
    	Student_WhiteBoard wb = new Student_WhiteBoard("127.0.0.1");
    	
    	wb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	wb.setVisible(true);
    	wb.setSize(800,600);
    	wb.setLocation(500, 250);
        wb.runClient();
   
    }
}
