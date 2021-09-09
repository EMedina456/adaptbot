package stfx2020;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * AdaptBot
 * Revised template from SimpleMinded
 * @author Mr. Reid and Eric Medina
 * @course ICS4UC
 * @date 2020/11/10
 */

public class AdaptBot extends AdvancedRobot {

	// Attributes
	private ArrayList<Enemy> enemies;
	
	// Constructor
	public AdaptBot() {
		super();
		this.enemies = new ArrayList<>();
	}

	/** 
	 * run: AdaptBot's default behavior
	 */ 
	public void run() {

		// Decorate Robot
		this.setColors(Color.green,Color.black,Color.green);

		// Main loop (infinite - game controls when it's over)
		while (true) {
			
			// Get number of enemies on battlefield
			int numEnemies = getOthers();

			// Is numEnemies more than 10?
			if (numEnemies > 10) {

				// Go towards wall
				this.ahead(100);
				this.scan();
			}
			
			// Is numEnemies in between 5 and 10?
			else if (numEnemies >= 5 && numEnemies <= 10) {
				
				// Run back and forth
				this.ahead(100);
				this.scan();
				this.back(100);
				this.scan();
			}
			
			else {
				
				// If my list of enemies are empty run in circles
				if (enemies.isEmpty()) {
					moveInCircles();
				}
				
				else {
					
					// Find the enemy with the highest energy and head towards it in a circular motion
					Enemy enemy = getEnemyWithHighestEnergy();
					this.ahead(Utils.normalAbsoluteAngle(enemy.getBearer()));
					moveInCircles();
				}
				
				this.scan();
			}
		}
		// Code will never reach here -- and that's okay for a change ;)
	}

	/**
	 * Find the enemy with the highest energy
	 * @return enemy with highest energy
	 */
	private Enemy getEnemyWithHighestEnergy() {
		
		// Example on how to sort ArrayList
		Collections.sort(enemies, Enemy.energyComparator); //https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
		return enemies.get(0);
	}

	/**
	 * Get and Update the status of the enemy
	 * @param name
	 * @param bearer
	 * @param distance
	 * @param energy
	 * @param heading
	 * @return enemy
	 */
	private Enemy getAndUpdateEnemy(String name, double bearer, double distance, double energy, double heading) {
		
		// Find the enemy from the ArrayList of enemies
		Enemy enemy = findEnemy(name);
		
		// Are there no enemies found?
		if (enemy == null) {
			
			// Accumulate the status of scanned enemy and add it to the list of enemies
			enemy = new Enemy(name, bearer, distance, energy, heading);
			enemies.add(enemy);
		}
		
		else {
			
			// Set the new status of the existing robot
			enemy.setBearer(bearer);
			enemy.setDistance(distance);
			enemy.setEnergy(energy);
			enemy.setHeading(heading);
		}

		return enemy;
	}
	
	/**
	 * Method to move in a circular motion
	 */
	private void moveInCircles() {
		
		// Move in circles
		this.setTurnRight(1000);
		this.setMaxVelocity(5);
		this.ahead(100);
		this.scan();		
	}

	/**
	 * Finds and retrieves the status of the enemy stored in AdaptBot
	 * @param name
	 * @return result
	 */
	public Enemy findEnemy(String name) {
		
		Enemy result = null; 

		// Loop through all enemies
		for (Enemy enemy : enemies) {
			
			// Is the current name equal to the name I am searching?
			if (enemy.getName().equalsIgnoreCase(name)) {
				result = enemy;
			}
		}
		
		return result;
	}
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		// Output scanned robot and info
		System.out.println("I just saw "+e.getName());
		System.out.println("Bearing:"+e.getBearing()+"  Heading:"+e.getHeading()+"  Energy:"+e.getEnergy());

		// Update the status of enemy
		Enemy enemy = getAndUpdateEnemy(e.getName(), e.getBearing(), e.getDistance(), e.getEnergy(), e.getHeading());

		// Analyze number of enemies at this moment
		int numEnemies = this.getOthers();

		// Is numEnemies greater than 10?
		if (numEnemies > 10) {

			// Is my energy less than their energy?
			if (this.getEnergy() < e.getEnergy()) {

				// Retreat
				this.turnRight(e.getBearing() + 180);
				this.ahead(e.getBearing());
			}
		}

		else if (numEnemies >= 5 && numEnemies <= 10) {

			// Adding new concept regarding the information of energy comparison
			// Did I hit enemy more or equal than enemy hit me?
			if (this.getEnergy() > enemy.getEnergy()) {
				
				// Shoot towards enemy
				this.setTurnGunRight(e.getBearing());
				this.setFire(Math.min(400 / enemy.getDistance(), 3)); // http://mark.random-article.com/weber/java/robocode/lesson4.html
				this.scan();
			} 

			else {

				// Retreat
				this.turnRight(e.getBearing() + 180);
				this.ahead(200);
				this.scan();
			}
		}
		
		else {
			
			// Shoot towards enemy
			this.setTurnGunRight(e.getBearing()); 
			this.setFire(Math.min(400 / enemy.getDistance(), 3)); // http://mark.random-article.com/weber/java/robocode/lesson4.html
			this.scan();
		}

	}

	/**
	 * onHitByBullet: What to do when I am hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {

		this.scan();
		
		// Update the status of enemy
		Enemy enemy = getAndUpdateEnemy(e.getName(), e.getBearing(), 0, 0, e.getHeading());
		
		// Get amount of enemies on the field
		int numEnemies = this.getOthers();

		// Is numEnemies greater than 10?
		if (numEnemies > 10) {

			//Fire and retreat
			this.turnRight(e.getBearing() + 180);
			this.setTurnGunRight(180);
			this.fire(2);
			this.ahead(200);
		}
		
		else {
			
			// Is my energy greater than the enemy's or numEnemies is 1?
			if (this.getEnergy() > enemy.getEnergy() || numEnemies == 1) {
				
				// Take a shot
				this.turnGunRight(e.getBearing());
				this.fire(2);
				this.turnGunLeft(e.getBearing());
			}

		}
		
	}
	
	/**
	 * onHitWall: What to do when I hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

		// Turn away from the wall
		this.scan();
		this.turnRight(100);
		this.setMaxVelocity(5);
		this.ahead(100);
	}

	/**
	 * onHitRobot: What to do when I collide with an opponent
	 */
	public void onHitRobot(HitRobotEvent e) {

		// Aim, fire, and move towards opponent three times
		for (int i = 0; i < 3; i++) {
			this.turnRight(e.getBearing());
			this.fire(3);
			this.ahead(50);
			this.scan();
		}
		
		// Retreat
		this.turnRight(180);
	}
	
	/**
	 * onWin: Do a celebration
	 */
	public void onWin(WinEvent e) {

		// Make a list of colours to change to
		Color[] colors = {Color.BLUE, Color.RED, Color.CYAN, Color.YELLOW, Color.PINK};
		
		// Change colours, spin and fire 5 times
		for (int i = 0; i < 5; i++) {
			
			// Change colours
		    this.setBodyColor(colors[i]);
		    this.setGunColor(colors[i]);
		    this.setRadarColor(colors[i]);

		    // Spin around and fire
		    this.turnRight(90);
			this.fire(1);
		}
	}

}