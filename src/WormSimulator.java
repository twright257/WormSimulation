import java.awt.Color;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point; 

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.util.Random; 

public class WormSimulator extends JPanel{
	private JFrame frame;
	private JPanel gridDisplay; 
	private int numComps = 9; 
	private int numVulnerable = 0; 
	private double probability = 0; 
	private int infectRate = 0; 
	private Computer[][] compArray; 
	
	JTextPane nInfoText = new JTextPane();
	JTextPane nTextEnter = new JTextPane();
	JButton nSubmitButton = new JButton("Submit");
	
	JTextPane dInfoText = new JTextPane();
	JTextPane dTextEnter = new JTextPane();
	JButton dSubmitButton = new JButton("Submit");
	
	JTextPane pInfoText = new JTextPane();
	JTextPane pTextEnter = new JTextPane();
	JButton pSubmitButton = new JButton("Submit");
	private final JButton runButton = new JButton("Run");
	private final JButton stopButton = new JButton("Stop");
	private  JTextPane cyclesText = new JTextPane();
	private JTextPane overloadText = new JTextPane();
	/**
	 * Create the application.
	 */
	public WormSimulator() {
		setBounds(46, 19, 500, 141);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 623, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		gridDisplay = this;
		gridDisplay.setBounds(46, 39, 521, 521);
		frame.getContentPane().add(gridDisplay);
		
		
		nInfoText.setEditable(false);
		nInfoText.setBackground(UIManager.getColor("CheckBox.background"));
		nInfoText.setText("n = 0");
		nInfoText.setBounds(64, 582, 68, 16);
		frame.getContentPane().add(nInfoText);
		
		
		nTextEnter.setBounds(64, 604, 74, 16);
		frame.getContentPane().add(nTextEnter);
		
		nSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setN(nTextEnter.getText());
				nTextEnter.setText("");
			}
		});
		nSubmitButton.setBounds(46, 621, 117, 29);
		frame.getContentPane().add(nSubmitButton);
		
		
		dInfoText.setBackground(UIManager.getColor("Button.background"));
		dInfoText.setEditable(false);
		dInfoText.setText("d = 0");
		dInfoText.setBounds(275, 582, 61, 16);
		frame.getContentPane().add(dInfoText);
		
		
		dTextEnter.setBounds(275, 604, 74, 16);
		frame.getContentPane().add(dTextEnter);
		
		
		pInfoText.setEditable(false);
		pInfoText.setBackground(UIManager.getColor("Button.background"));
		pInfoText.setText("p = 0");
		pInfoText.setBounds(472, 582, 62, 16);
		frame.getContentPane().add(pInfoText);
		
		
		pTextEnter.setBounds(472, 604, 74, 16);
		frame.getContentPane().add(pTextEnter);
		
		
		dSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setD(dTextEnter.getText());
				dTextEnter.setText("");
			}
		});
		
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSim(); 
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		dSubmitButton.setBounds(252, 621, 117, 29);
		frame.getContentPane().add(dSubmitButton);
		
		
		pSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setP(pTextEnter.getText());
				pTextEnter.setText("");
			}
		});
		pSubmitButton.setBounds(450, 621, 117, 29);
		frame.getContentPane().add(pSubmitButton);
		runButton.setBounds(149, 679, 117, 29);
		
		frame.getContentPane().add(runButton);
		stopButton.setBounds(360, 679, 117, 29);
		
		frame.getContentPane().add(stopButton);
		cyclesText.setEditable(false);
		cyclesText.setBackground(UIManager.getColor("Button.background"));
		cyclesText.setText("Cycles: 0");
		cyclesText.setBounds(131, 11, 74, 16);
		
		frame.getContentPane().add(cyclesText);
		overloadText.setEditable(false);
		overloadText.setBackground(UIManager.getColor("Button.background"));
		overloadText.setText("Overloaded: False");
		overloadText.setBounds(383, 11, 136, 16);
		
		frame.getContentPane().add(overloadText);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gridDisplay.getWidth(), gridDisplay.getHeight());
		double nRoot = Math.sqrt(numComps);
		int width = (int) (gridDisplay.getWidth() / nRoot);
		
        if (compArray != null) {
        	for (int i = 0; i < compArray.length; i++) {
        		for (int j = 0; j < compArray.length; j++) {
        			if (compArray[i][j].getInfection() >= 100) {
        				g.setColor(Color.RED);
        				g.fillRect(compArray[i][j].getX(), compArray[i][j].getY(), width, width);
        			}
        		}
        	}
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i <= gridDisplay.getWidth(); i += width) {
            g.drawLine(i, 0, i, gridDisplay.getHeight());
            g.drawLine(0, i, gridDisplay.getWidth(), i);
        }
        
        
	}
	
	
	public void setN(String n) {
		int nVal; 
		try {
			nVal = Integer.valueOf(n);
			nInfoText.setText("n = " + nVal);
			numVulnerable = nVal; 
		} catch (NumberFormatException e) {
			
		}
	}
	
	public void setD(String d) {
		int dVal; 
		try {
			dVal = Integer.valueOf(d);
			dInfoText.setText("d = " + dVal);
			infectRate = dVal; 
		} catch (NumberFormatException e) {
			
		}
	}
	
	public void setP(String p) {
		int pVal; 
		try {
			pVal = Integer.valueOf(p);
			pInfoText.setText("p = " + pVal);
			probability = pVal; 
		} catch (NumberFormatException e) {
			
		}
	}
	
	public void runSim() {
		Random random = new Random(); 
		ArrayList<Computer> allVuln = new ArrayList(); 
		int numInfected = 0; 
		//array holding computers structured similar to grid
		compArray= new Computer[(int) Math.sqrt(numComps)][(int) Math.sqrt(numComps)]; 
		//initialize computers
		int x = 0;  
		for (int i = 0; i < compArray.length; i++) {
			int y = 0;
			for (int j = 0; j < compArray.length; j++) {
				Computer comp = new Computer(x, y); 
				compArray[i][j] = comp; 
				System.out.println(x + ", " + y);
				y += gridDisplay.getWidth() / compArray.length; 
			}
			x += gridDisplay.getWidth() / compArray.length; 
		}
		//set vulnerable
		for (int i = 0; i < numVulnerable; i++) {
			boolean alreadyVuln = false; 
			do {
				int first = random.nextInt(compArray.length);
				int second = random.nextInt(compArray.length); 
				alreadyVuln = compArray[first][second].getVulnerable(); 
				compArray[first][second].setVulnerable();
				if (!alreadyVuln) {
					allVuln.add(compArray[first][second]); 
				}
			} while (alreadyVuln); 
		}
		
		//set first infected computer
		allVuln.get(0).infect();
		numInfected++; 
		
		//infect until all overloaded
		int cycles = 0; 
		boolean overloaded = false; 
		while (!overloaded) {
			for (int i = 0; i < numInfected; i++) {
				int i1 = random.nextInt(compArray.length); 
				int i2 = random.nextInt(compArray.length);
				Computer comp = compArray[i1][i2]; 
				if (comp.getInfection() > 0) {
					if (random.nextDouble() <= probability) {
						comp.infect();
					}
				} else if (comp.getVulnerable()) {
					comp.infect();
					numInfected++; 
				}
			}
			overloaded = true; 
			for (Computer c : allVuln) {
				if (c.getInfection() < 100) {
					overloaded = false; 
				}
			}
			cyclesText.setText("Cycles: " + cycles);
			cycles++; 
			repaint(); 
		}
		overloadText.setText("Overloaded: True");
	}

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WormSimulator window = new WormSimulator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
