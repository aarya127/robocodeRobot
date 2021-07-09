package stfx2020;

import java.awt.Color;
import robocode.*;

/**
 * Terminator
 * Make a robot that Terminates (Attacks the opponent, as well as defending)"M" pattern
 * @author Aarya Shah
 * @course ICS4UC
 * @date 2020/11/04
 */

public class Terminator extends Robot {

	// Attribute that allows us to track the other robots
	boolean target = false;

	// Allowing the robot to reach the top of battle arena
	double reachTop;

	// Allowing the robot to reach the bottom of the battle arena
	double reachBottom;

	/** 
	 * run: SimpleTrack's default behavior
	 */ 
	public void run() {
		// Set colors
		setBodyColor(Color.orange);
		setGunColor(Color.white);
		setRadarColor(Color.yellow);
		setBulletColor(Color.green);
		setScanColor(Color.blue);

		// Initialize reachTop to the maximum possible for this battlefield.
		reachTop = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

		// Initialize reachTop to the maximum possible for this battlefield.
		reachBottom = Math.min(getBattleFieldWidth(), getBattleFieldHeight());

		// Initially, robot doesn't target any other robot
		target = false;

		// Main loop
		while(true) {
			// Plan A
			if (this.getEnergy() > 30){
				this.turnLeft(90);
				this.ahead(500);
			}
			// Plan B
			else if (this.getEnergy() <= 30){
				this.turnLeft(getHeading() % 90);
				this.ahead(reachTop);
				this.turnGunRight(180);
				this.turnRight(90);
				this.turnLeft(getHeading() % 90);
				this.ahead(reachBottom);
			}
			if (target){
				// Scan the target 
				this.scan(); 
			}
			else{
				// Look around for a target
				this.turnGunLeft(180);
				this.turnGunLeft(180);
			}
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent otherRobot) {
		// Create a robot object
		Opponent enemy = new Opponent(otherRobot.getEnergy(), otherRobot.getDistance(), otherRobot.getBearing());
		enemy.energy();

		//Shoot if opponent energy is low
		if (enemy.energy < 10) {
			this.fire(1);
		}
		// Found a target
		target = true;
		System.out.println("Distance:" +otherRobot.getDistance());

		// Attack the target based on how far it is
		if (otherRobot.getDistance() < 600 && otherRobot.getDistance() >= 400) {
			this.fire(1);
		}
		else if (otherRobot.getDistance() < 400 && otherRobot.getDistance() >= 200) {
			this.fire(2);
		}
		else if (otherRobot.getDistance() < 200) {
			this.fire(3);
		}
		else {
			this.fire(0);
		}
		this.turnRight(otherRobot.getBearing());

		// If robot health is low, shoot more    
		if (this.getEnergy() < 50) {
			this.fire(2);
		} 
		else {
			// Take a quick shot if health is higher than 50
			this.fire(1);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent robot) {
		// Get out of the way
		this.turnLeft(50-robot.getBearing());
		this.ahead(200);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent event){
		// Run back and turn another direction
		this.turnLeft(70);
		this.ahead(this.getBattleFieldHeight());
	}

	/**
	 * onWin: What to do when you win
	 */
	public void onWin(WinEvent event) {
		// Celebrate on victory
		for (int i = 0; i < 50; i++) {
			this.turnRight(100);
			this.ahead(30);
			this.turnLeft(100);
		}
	}
}