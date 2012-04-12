package nl.jasperbok.zombies.entity.mob;

import org.newdawn.slick.Animation;

import nl.jasperbok.zombies.entity.Entity;
import nl.jasperbok.zombies.entity.state.EntityState;

public abstract class Mob extends Entity {
	public int agressionRange = 50;
	protected int health = 50;
	protected int maxHealth = 100;
	
	// Animations
	public Animation idleAnimation;
	public Animation walkLeftAnimation;
	public Animation walkRightAnimation;
	public Animation climbAnimation;
	public Animation fallAnimation;
	public Animation currentAnimation;
	public Animation attackAnimation;
	
	// Status variables
	public boolean wasOnGround = false;
	public boolean isOnGround = false;
	public boolean wasFalling = false;
	public boolean isFalling = false;
	public boolean wasClimbing = false;
	public boolean isClimbing = false;
	
	public EntityState currentState;
	
	/**
	 * Heals the mob.
	 * 
	 * @param amount The amount of HP to heal.
	 */
	public void heal(int amount) {
		health += amount;
		if (health > maxHealth) health = maxHealth;
	}
	
	/**
	 * Deals damage to the mob.
	 * 
	 * @param damage The amount of damage to deal.
	 */
	public void hurt(int damage) {
		health -= damage;
		if (health <= 0) die();
	}
	
	/**
	 * Makes the mob die.
	 */
	protected void die() {
		//this.level.remove(this);
	}
	
	public void update(int delta) {
		
	}
}
