import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class WhiteBoard extends JFrame implements MouseListener, KeyListener, ActionListener {

	BorderLayout b1;
	protected static Color c;
	protected static int x;
	protected static int y;
	protected static int dx;
	protected static int dy;
	public static int type = 0;
	public static int Stype = 0;
	public static int Ctype = 0;
	public static int Atype = 0;
	public static boolean Lflag = true;
	public static boolean Sflag = true;
	public static boolean Cflag = true;
	protected static ArrayList<Points> lines = new ArrayList<Points>();
	protected static ArrayList<Points> squares = new ArrayList<Points>();
	protected static ArrayList<Points> circles = new ArrayList<Points>();
	static JLabel lcd = new JLabel("0",JLabel.RIGHT);
	Timer t = new Timer(1000,this);
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerSocket server;
	private Socket conn;
	
	public WhiteBoard() {
		super("Teacher's WhiteBoard Application");
		b1 = new BorderLayout();
		setLayout(b1);
		menu();
		east();
		south();
		center();
		lcd.setFont(new Font("Digital-7 Mono",Font.PLAIN,24));
		lcd.setForeground(Color.RED);
		add(lcd,BorderLayout.NORTH);
		t.start();
	}
	
	JMenuBar menubar;
	JMenu color_m,shape,exit;
	JMenuItem color_ýtem,line_ýtem,square_ýtem,circle_ýtem,exit_ýtem;
	
	public void menu() {
		menubar = new JMenuBar();
		
		color_m = new JMenu("Colors");
		shape = new JMenu("Shape");
		exit = new JMenu("Exit");
		
		color_ýtem = new JMenuItem("Color Selection");
		color_ýtem.addActionListener(this);
		
		line_ýtem = new JMenuItem("Line Drawing");
		line_ýtem.addActionListener(this);
		
		square_ýtem = new JMenuItem("Square Drawing");
		square_ýtem.addActionListener(this);
		
		circle_ýtem = new JMenuItem("Circle Drawing");
		circle_ýtem.addActionListener(this);
		
		exit_ýtem = new JMenuItem("Exit from the System");
		exit_ýtem.addActionListener(this);
		
		addKeyListener(this);
		color_m.add(color_ýtem);
		shape.add(line_ýtem);
		shape.add(square_ýtem);
		shape.add(circle_ýtem);
		exit.add(exit_ýtem);
		
		menubar.add(color_m);
		menubar.add(shape);
		menubar.add(exit);
		
		add(menubar);
		setJMenuBar(menubar);
		
	}
	JPanel JPEast;
	GridLayout g1;
	JButton JPAttandance;
	JButton JPAttList;
	public void east() {
		JPEast = new JPanel();
		g1 = new GridLayout(2,1);
		JPEast.setLayout(g1);
		add(JPEast,BorderLayout.EAST);
		
		JPAttandance = new JButton("Take Attandance");
		JPAttandance.addActionListener(this);
		JPEast.add(JPAttandance);
		
		JPAttList = new JButton("Attandance List");
		JPAttList.addActionListener(this);
		JPEast.add(JPAttList);
		
	}
    
	JTextArea chatBox;
	JTextField messageBox;
	JButton jbSend;
    	JPanel JPSouth;
	public void south() {
        
		JPSouth = new JPanel();
		JPSouth.setBackground(Color.BLUE);
		JPSouth.setLayout(new GridBagLayout());
		add(JPSouth,BorderLayout.SOUTH);
	
		messageBox = new JTextField(30);
		messageBox.requestFocusInWindow();
		
		jbSend = new JButton("Send Message");
		jbSend.setEnabled(false);
		
		
		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setFont(new Font("Comic Sans MS",Font.PLAIN,12));
		chatBox.setLineWrap(true); // Check it
		add(new JScrollPane(chatBox), BorderLayout.WEST);

		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;
		
	    	GridBagConstraints right = new GridBagConstraints();
	    	right.insets = new Insets(0 , 10, 0, 0);
	    	right.anchor = GridBagConstraints.LINE_END;
        	right.fill = GridBagConstraints.NONE;
        	right.weightx = 1.0D;
        	right.weighty = 1.0D;
     
                 
        	JPSouth.add(messageBox,left);
        	JPSouth.add(jbSend,right);
     
        	jbSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				send(messageBox.getText());
				messageBox.setText("");
			}
           	});
	     
	  
	}
	public void runServer() {
		try {
			server = new ServerSocket(6666,100);
			while(true) {
				try {
					waitConn();
					streams();
					processConn();
				}
				catch(EOFException e){
					dispMessage("\nT:Terminated Conn");
				}
				finally {
					closeConn();
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void waitConn() throws IOException {
		dispMessage("Please Wait...\n");
		conn = server.accept();
		dispMessage("Connection Received\n");
	}
	private void streams() throws IOException {
		oos = new ObjectOutputStream(conn.getOutputStream());
		oos.flush();
		
		ois = new ObjectInputStream(conn.getInputStream());
		dispMessage("\nStreams\n");
	}
	
	private void processConn() throws IOException{
		send("Successful");
		setButtonEnabled(true);
		String msg = "";
		do {
			try {
				msg = (String) ois.readObject();
				dispMessage("\n"+msg);
			}catch(ClassNotFoundException e) {
				dispMessage("Unknown\n");
			}
		}while(!msg.equals("S:Exit The System"));
	}
	private void closeConn() {
		dispMessage("\nTerminating Conn\n");
		setButtonEnabled(false);
		try {
			oos.close();
			ois.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void send(String text) {
		try {
			oos.writeObject("T:" + text);
			oos.flush();
			dispMessage("\nT:"+text);
		}
		catch(Exception e) {
			chatBox.append("\nError");
		}
	}
	private void AttListReader() throws IOException{
  		FileReader inputReader = new FileReader("C:\\Users\\ABRA\\eclipse-workspace\\Java-Projesi2\\AttandanceList.txt");
    		BufferedReader reader = new BufferedReader(inputReader);
    		String input2;
    		while((input2 = reader.readLine()) != null) {
    	  		ta.append(input2+"\n");
    		}
    		reader.close();
	}
	private void AttlistDeleter() throws IOException {
		FileWriter delete = new FileWriter("C:\\Users\\ABRA\\eclipse-workspace\\Java-Projesi2\\AttandanceList.txt",false);
		PrintWriter outP = new PrintWriter(delete);
		outP.write("");
		outP.close();
	}
    	JFrame f2;
    	JTextArea ta;
	boolean bool2 = false;
	private void AttlistP2(boolean bool2) {
		if(bool2 == true) {	
			f2 = new JFrame("AttandanceList");
			BorderLayout bb = new BorderLayout();
			f2.setLayout(bb);
			ta = new JTextArea();
			ta.setEditable(false);
			ta.setFont(new Font("Comic Sans MS",Font.PLAIN,15));
			ta.setLineWrap(true); // Check it
			f2.add(new JScrollPane(ta));
			f2.setSize(300,300);
			f2.setVisible(true);
	  	} else {
		 	f2.setVisible(false); 
	  	}
	}
	private void dispMessage(final String string) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				chatBox.append(string);
			}
		});
	}
	private void setButtonEnabled(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				jbSend.setEnabled(b);
				
			}
		});
	}

	JPanel wbPanel;
	public void center() {
		wbPanel = new WhiteBoard_Panel();
		wbPanel.addMouseListener(this);
		wbPanel.setBackground(Color.white);
		add(wbPanel,BorderLayout.CENTER);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {


	
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(Lflag == false) {
        		Points p = new Points();
            		p.x = e.getX();
            		p.y = e.getY();
            		p.dx = p.x * 2;
            		p.dy = p.y * 2;
            		lines.add(p);
		}
		else {	
		  	if(Sflag == false) {
				Points s = new Points();
	            		s.x = e.getX();
	            		s.y = e.getY();
	            		squares.add(s);
	           
		  	}
		  	else {
			
		    		if(Cflag == false) {
			    	Points c = new Points();
	            		c.x = e.getX();
	            		c.y = e.getY();
	            		circles.add(c);
		    		}
		  	}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void setDefaultCloseOperation(int exitOnClose) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		
		if(e.getSource() == color_ýtem) {
			c = JColorChooser.showDialog(this,"Choose Color", Color.blue);
			repaint();
		}
		else if(e.getSource() == line_ýtem) {
			Lflag = false;
			int choice = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter\n1-Horizontal Line\n2-Vertical Line\n3-Cross Line"));
			type = choice;
			JOptionPane.showMessageDialog(this,"Now click center panel for creating lines");
		}
		
		else if(e.getSource() == square_ýtem) {
			Lflag = true;
			Sflag = false;
			int choice = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter\n1-Draw Square\n2-Fill Square"));
			Stype = choice;
			JOptionPane.showMessageDialog(this,"Now click center panel for creating squares");
	
		} 
		else if(e.getSource() == circle_ýtem) {
			Sflag = true;
			Lflag = true;
			Cflag = false;
			int choice = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter\n1-Draw Circle\n2-Fill Circle"));
			Ctype = choice;
			JOptionPane.showMessageDialog(this,"Now click center panel for creating a circle");
		}
		else if(e.getSource() == JPAttandance) {
			JOptionPane.showMessageDialog(this,"Taking Attandance");
		    
		}
    		else if(e.getSource() == JPAttList) {
    			JOptionPane.showMessageDialog(this,"Now,You can see the List");
    			AttlistP2(true);
    	    		try {
				AttListReader();
			}
    			catch(IOException a){
				a.printStackTrace();
			}
       		}
		else if(e.getSource() == exit_ýtem) {
			try {
				AttlistDeleter();
			}catch(IOException d) {
				d.printStackTrace();
			}
			closeConn();	
			System.exit(0);
		}
	}
}
