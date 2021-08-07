import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
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


public class Student_WhiteBoard extends JFrame implements ActionListener {

	BorderLayout b1;
	protected static int x;
	protected static int y;
	static JLabel lcd = new JLabel("0",JLabel.RIGHT);
	Timer t = new Timer(1000,this);
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String srv;
	private Socket myClient;
	JPanel wbPanel;
	public Student_WhiteBoard(String info) {
		super("Students WhiteBoard");
		b1 = new BorderLayout();
		setLayout(b1);
		srv = info;
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
	JMenu exit;
	JMenuItem exit_ýtem;
	
	public void menu() {
		menubar = new JMenuBar();
		exit = new JMenu("Exit");
		exit_ýtem = new JMenuItem("Exit from the System");
		exit_ýtem.addActionListener(this);
		exit.add(exit_ýtem);
		menubar.add(exit);
		add(menubar);
		setJMenuBar(menubar);
	}
	
	JPanel JPEast;
	GridLayout g1;
	JButton JPAttandance;
	JButton JPHand;
	public void east() {
		JPEast = new JPanel();
		g1 = new GridLayout(2,1);
		JPEast.setLayout(g1);
		add(JPEast,BorderLayout.EAST);
		
		JPAttandance = new JButton("Attandance");
		JPAttandance.addActionListener(this);
		JPEast.add(JPAttandance);
		
		JPHand = new JButton("Raise Hand");
		JPHand.addActionListener(this);
		JPEast.add(JPHand);
		JPHand.setFocusable(true);
	}
	JButton     jbSend;
    	JTextField  messageBox;
    	JTextArea   chatBox;
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
		chatBox.setLineWrap(false);
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
	public void runClient() {
		try {
			connToServer();
			streams();
			processConn();
		}
		catch(EOFException e){
			dispMessage("\nC:Terminated Conn");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			closeConn();
		}
	}
	private void connToServer() throws IOException {
		dispMessage("Attempting\n");
		myClient = new Socket(InetAddress.getByName(srv),6666);
	}
	private void streams() throws IOException {
		oos = new ObjectOutputStream(myClient.getOutputStream());
		oos.flush();
		
		ois = new ObjectInputStream(myClient.getInputStream());
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
		}while(!msg.equals("T:Exit The System"));
	}
	
	private void closeConn() {
		dispMessage("\nTerminating Conn\n");
		setButtonEnabled(false);
		try {
			oos.close();
			ois.close();
			myClient.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void send(String text) {
		try {
			oos.writeObject("S:" + text);
			oos.flush();
			dispMessage("\nS:"+text);
		}
		catch(Exception e) {
			chatBox.append("\nError");
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
	
	public void center() {
		wbPanel = new Student_WhiteBoard_Panel();
		wbPanel.setBackground(Color.white);
		add(wbPanel,BorderLayout.CENTER);
	}
	private void AttListWriter(String input) throws IOException{
		FileWriter fw = new FileWriter("C:\\Users\\ABRA\\eclipse-workspace\\Java-Projesi2\\AttandanceList.txt",true);
		PrintWriter out = new PrintWriter(fw);
		out.write(input+"\n");
		out.close();
	}
	JFrame f;
	JTextField txt;
	boolean bool = false;
	private void AttlistP(boolean bool) {
		if(bool == true) {	
			f = new JFrame("Attandance");
			txt = new JTextField(30);
			JButton btt = new JButton("Enter");
			JLabel lab = new JLabel("Name:");
			txt.setBounds(100,100,100,30);
			btt.setBounds(100,130,100,30);
			lab.setBounds(20,100,80,30);
			f.add(txt);
			f.add(btt);
			f.add(lab);
			f.setSize(300,300);
			f.setVisible(true);
			btt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						AttListWriter(txt.getText());
						f.setVisible(false);
					} catch(IOException ss) {
						ss.printStackTrace();
					}
				}
			
			});
		}
		else {
		f.setVisible(false);
		}
	
	}

    	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		if(e.getSource() == JPHand) {
			JOptionPane.showMessageDialog(this,"We are sending your attempt!");
			send("A Student has a question!!!");
			
		}
		else if(e.getSource() == JPAttandance) {
			JOptionPane.showMessageDialog(this,"Attandance is Taking Now");
			AttlistP(true);	
			JPAttandance.setEnabled(false);
		}
		else if(e.getSource() == exit_ýtem) {
			System.exit(0);
		}
	}
	public void setDefaultCloseOperation(int exitOnClose) {
		// TODO Auto-generated method stub
		
	}
}
