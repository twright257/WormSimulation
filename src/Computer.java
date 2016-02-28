
public class Computer {

	private int infectionNum = 0;
	private int xCoord;
	private int yCoord;
	private boolean vulnerable = false;

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
