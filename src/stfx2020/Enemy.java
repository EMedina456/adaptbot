package stfx2020;

import java.util.Comparator;

/**
 * Class of enemies for AdaptBot to analyze
 * @author Eric Medina
 * @course ICS4UC
 * @date 2020/11/10
 */

public class Enemy {
	
	// Attributes
	private String name = "";
	private double bearer = 0;
	private double distance = 0;
	private double energy = 0;
	private double heading = 0;
	
	// Constructor
	public Enemy(String name, double bearer, double distance, double energy, double heading) {
		super();
		this.name = name;
		this.bearer = bearer;
		this.distance = distance;
		this.energy = energy;
		this.heading = heading;
	}
	

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getBearer() {
		return bearer;
	}
	
	public void setBearer(double bearer) {
		this.bearer = bearer;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}
	
	/**
	 * Comparator to sort an enemy by the life attribute in ascending order
	 * https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
	 */
	public static Comparator<Enemy> energyComparator = new Comparator<Enemy>() {
		// Compare enemy 1 and 2
		public int compare(Enemy o1, Enemy o2) {
			
			// Find which robot has more energy
			double compareTo = o2.getEnergy() - o1.getEnergy();
			return (int) compareTo;
		}
	};
}

