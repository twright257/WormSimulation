/*
 * Tyler Wright
 * Feb. 28, 2016
 * Computer class represents a single computer within a network that is being 
 * attacked by a worm. Used in WormSimulator class 
 */

public class Computer {

	private int infectionNum = 0;		//number times infected 
	private int xCoord;		//upper left x coord for painting
	private int yCoord;		//upper left y coord for painting 
	private boolean vulnerable = false;		//can computer be infected 

	public Computer(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public void setVulnerable() {
		vulnerable = true;
	}

	public boolean getVulnerable() {
		return vulnerable;
	}

	public int getInfection() {
		return infectionNum;
	}

	public void infect() {
		infectionNum++;
	}

	public int getX() {
		return xCoord;
	}

	public int getY() {
		return yCoord;
	}

}
