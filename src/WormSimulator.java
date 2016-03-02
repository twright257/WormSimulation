import java.awt.Color;

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;

/*
 * Tyler Wright
 * Feb. 28, 2016
 * WormSimulator controls a GUI for visualizing a worm attack on a network of 10000
 * Computers.  Takes input parameters n(number of vulnerable computers), d(number of computes randomly
 * selected by infected computer and p(probability of re infection) and visualizes infection spread. 
 * white indicates not vulnerable, black indicates vulnerable, orange indicates infected and red indicates
 * overloaded (greater than 100 viruses running).  Program runs until all overloaded or 5000 cycles reached. 
 * 
 */

public class WormSimulator extends JPanel {
	private JFrame frame;
	private JPanel gridDisplay;
	private int numComps = 10000;	//parameter for number of computers on network
	private int numVulnerable = 0;	//number vulnerable 
	private double probability = 0;	//probability of reinfection 
	private int infectRate = 0;		//infection rate 
	private Computer[][] compArray;	//computers arranged in array corresponding to grid placement 

	JTextPane nInfoText = new JTextPane();
	JTextPane nTextEnter = new JTextPane();
	private final JButton nSubmitButton = new JButton("Submit");

	JTextPane dInfoText = new JTextPane();
	JTextPane dTextEnter = new JTextPane();
	private final JButton dSubmitButton = new JButton("Submit");

	JTextPane pInfoText = new JTextPane();
	JTextPane pTextEnter = new JTextPane();
	private final JButton pSubmitButton = new JButton("Submit");
	private final JButton runButton = new JButton("Run");
	private JTextPane cyclesText = new JTextPane();
	private JTextPane overloadText = new JTextPane();
	private JTextPane infectedText = new JTextPane();

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
	public void initialize() {
		//frame
		frame = new JFrame();
		frame.setBounds(100, 100, 623, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//grid
		gridDisplay = this;
		gridDisplay.setBounds(46, 39, 521, 521);
		frame.getContentPane().add(gridDisplay);

		//text display for n 
		nInfoText.setEditable(false);
		nInfoText.setBackground(UIManager.getColor("CheckBox.background"));
		nInfoText.setText("n = 0");
		nInfoText.setBounds(64, 582, 68, 16);
		frame.getContentPane().add(nInfoText);

		//text box for typing n 
		nTextEnter.setBounds(64, 604, 74, 16);
		frame.getContentPane().add(nTextEnter);

		//button to submit n 
		nSubmitButton.setBounds(46, 621, 117, 29);
		frame.getContentPane().add(nSubmitButton);

		//display d info 
		dInfoText.setBackground(UIManager.getColor("Button.background"));
		dInfoText.setEditable(false);
		dInfoText.setText("d = 0");
		dInfoText.setBounds(275, 582, 61, 16);
		frame.getContentPane().add(dInfoText);

		//enter d info 
		dTextEnter.setBounds(275, 604, 74, 16);
		frame.getContentPane().add(dTextEnter);

		//display p info 
		pInfoText.setEditable(false);
		pInfoText.setBackground(UIManager.getColor("Button.background"));
		pInfoText.setText("p = 0");
		pInfoText.setBounds(472, 582, 62, 16);
		frame.getContentPane().add(pInfoText);

		//enter p info 
		pTextEnter.setBounds(472, 604, 74, 16);
		frame.getContentPane().add(pTextEnter);

		//submit d info 
		dSubmitButton.setBounds(252, 621, 117, 29);
		frame.getContentPane().add(dSubmitButton);
		
		//p submit button 
		pSubmitButton.setBounds(450, 621, 117, 29);
		frame.getContentPane().add(pSubmitButton);
		
		//run button 
		runButton.setBounds(252, 677, 117, 29);
		frame.getContentPane().add(runButton);
		
		//cycles text display 
		cyclesText.setEditable(false);
		cyclesText.setBackground(UIManager.getColor("Button.background"));
		cyclesText.setText("Cycles: 0");
		cyclesText.setBounds(46, 11, 189, 16);
		frame.getContentPane().add(cyclesText);
		
		//overload text display 
		overloadText.setEditable(false);
		overloadText.setBackground(UIManager.getColor("Button.background"));
		overloadText.setText("Overloaded: 0");
		overloadText.setBounds(452, 11, 131, 16);
		frame.getContentPane().add(overloadText);

		//infected text display 
		infectedText.setEditable(false);
		infectedText.setBackground(UIManager.getColor("Button.background"));
		infectedText.setText("Infected: 0");
		infectedText.setBounds(252, 11, 102, 16);
		frame.getContentPane().add(infectedText);
		

		
		//button action listeners 
		nSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setN(nTextEnter.getText());
				nTextEnter.setText("");
			}
		});
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
		pSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setP(pTextEnter.getText());
				pTextEnter.setText("");
			}
		});
	}

	//paint grid panel 
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gridDisplay.getWidth(), gridDisplay.getHeight());
		double nRoot = Math.sqrt(numComps);
		int width = (int) (gridDisplay.getWidth() / nRoot);
		
		//computer array has been populated 
		if (compArray != null) {
			for (int i = 0; i < compArray.length; i++) {
				for (int j = 0; j < compArray.length; j++) {
					//paint overloaded 
					if (compArray[i][j].getInfection() >= 100) {
						g.setColor(Color.RED);
						g.fillRect(compArray[i][j].getX(), compArray[i][j].getY(), width, width);
					//paint infected 
					} else if (compArray[i][j].getInfection() > 0) {
						g.setColor(Color.ORANGE);
						g.fillRect(compArray[i][j].getX(), compArray[i][j].getY(), width, width);
					//paint vulnerable 
					} else if (compArray[i][j].getVulnerable()) {
						g.setColor(Color.BLACK);
						g.fillRect(compArray[i][j].getX(), compArray[i][j].getY(), width, width);
					}
				}
			}
		}
		//draw grid lines 
		g.setColor(Color.BLACK);
		for (int i = 0; i <= gridDisplay.getWidth(); i += width) {
			g.drawLine(i, 0, i, gridDisplay.getHeight());
			g.drawLine(0, i, gridDisplay.getWidth(), i);
		}

	}

	//set number vulnerable and update view
	public void setN(String n) {
		int nVal;
		try {
			//if number entered within network sizerange, set n values 
			nVal = Integer.valueOf(n);
			if (nVal >= 0 && nVal <= numComps) {
				nInfoText.setText("n = " + nVal);
				numVulnerable = nVal;
			}
		} catch (NumberFormatException e) {

		}
	}

	//set number of comps randomly selected by each infected and update view
	public void setD(String d) {
		int dVal;
		try {
			//if number entered within network size range, set d values
			dVal = Integer.valueOf(d);
			if (dVal >= 0 && dVal <= numComps) {
				dInfoText.setText("d = " + dVal);
				infectRate = dVal;
			}
		} catch (NumberFormatException e) {

		}
	}

	//set probability and update view
	public void setP(String p) {
		double pVal;
		try {
			//if number entered between 0 and 1, set p
			pVal = Double.parseDouble(p);
			if (pVal >= 0 && pVal <= 1) {
				pInfoText.setText("p = " + pVal);
				probability = pVal;
			}
		} catch (NumberFormatException e) {

		}
	}

	//run infection simulation 
	public void runSim() {
		Random random = new Random();
		ArrayList<Computer> allVuln = new ArrayList();
		int numInfected = 0;
		// array holding computers structured similar to grid
		compArray = new Computer[(int) Math.sqrt(numComps)][(int) Math.sqrt(numComps)];
		// initialize computers
		int x = 0;
		for (int i = 0; i < compArray.length; i++) {
			int y = 0;
			for (int j = 0; j < compArray.length; j++) {
				//create new computer with coordinates corresponding to grid placement 
				Computer comp = new Computer(x, y);
				compArray[i][j] = comp;

				y += gridDisplay.getWidth() / compArray.length;
			}
			x += gridDisplay.getWidth() / compArray.length;
		}
		// set n computers to vulnerable
		for (int i = 0; i < numVulnerable; i++) {
			boolean alreadyVuln = false;
			do {
				//randomly select computer and set vulnerable, select another if already vulnerable 
				int first = random.nextInt(compArray.length);
				int second = random.nextInt(compArray.length);
				alreadyVuln = compArray[first][second].getVulnerable();
				compArray[first][second].setVulnerable();
				if (!alreadyVuln) {
					allVuln.add(compArray[first][second]);
				}
			} while (alreadyVuln);
		}

		// set first infected computer
		allVuln.get(0).infect();
		numInfected++;

		// infect until all overloaded
		int cycles = 0;
		boolean overloaded = false;
		while (!overloaded && cycles < 5000) {
			//for each infected computer, select d other computers to try and infect 
			for (int i = 0; i < numInfected; i++) {
				for (int j = 0; j < infectRate; j++) {
					int i1 = random.nextInt(compArray.length);
					int i2 = random.nextInt(compArray.length);
					Computer comp = compArray[i1][i2];
					//selected computer already infected 
					if (comp.getInfection() > 0) {
						if (random.nextDouble() <= probability) {
							comp.infect();
						}
					//selected is vulnerable 
					} else if (comp.getVulnerable()) {
						comp.infect();
						numInfected++;
					}
				}
			}
			overloaded = true;
			int loadedNum = 0; 
			for (Computer c : allVuln) {
				if (c.getInfection() < 100) {
					overloaded = false;
				} else {
					loadedNum++; 
				}
			}
			cycles++;
			//update view
			paintImmediately(0, 0, gridDisplay.getWidth(), gridDisplay.getHeight());
			infectedText.setText("Infected: " + numInfected);
			infectedText.update(infectedText.getGraphics());
			cyclesText.setText("Cycles: " + cycles);
			cyclesText.update(cyclesText.getGraphics());
			overloadText.setText("Overloaded: " + loadedNum);
			overloadText.update(overloadText.getGraphics());
		}
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
