package nl.jasperbok.zombies.entity.building;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import nl.jasperbok.zombies.entity.Entity;
import nl.jasperbok.zombies.entity.Observer;
import nl.jasperbok.zombies.entity.Observable;
import nl.jasperbok.zombies.entity.object.projectile.Bullet;
import nl.jasperbok.zombies.level.Level;

public class AutoTurret extends Entity implements Observer {

	/**
	 * A reference to the Switch that controls this AutoTurret.
	 */
	private Switch onOffSwitch;
	/**
	 * Indicates whether the AutoTurret is facing left or right.
	 */
	private boolean facingLeft;
	/**
	 * Indicates whether the AutoTurret is firing.
	 */
	private boolean firing;
	/**
	 * Delta time between successive shots.
	 */
	private int fireRate = 150;
	/**
	 * Delta counter that counts the time since the last shot.
	 */
	private int lastShotFired = 0;
	
	/**
	 * AutoTurret constructor.
	 * 
	 * @param level The Level this AutoTurret is part of.
	 * @param switch The Switch that controls this AutoTurret.
	 */
	public AutoTurret(Level level, boolean facingLeft, Switch onOffSwitch, Vector2f position) throws SlickException {
		super.init(level);
		this.gravityAffected = false;
		this.position = position;
		this.facingLeft = facingLeft;
		this.onOffSwitch = onOffSwitch;
		this.firing = false;
		
		Animation turretOn = new Animation();
		Animation turretOff = new Animation();
		Animation turretOnFlipped = new Animation();
		Animation turretOffFlipped = new Animation();
		turretOn.addFrame(new Image("data/sprites/entity/building/buildings.png").getSubImage(135, 0, 135, 121), 5000);
		turretOff.addFrame(new Image("data/sprites/entity/building/buildings.png").getSubImage(0, 0, 135, 121), 5000);
		turretOnFlipped.addFrame(new Image("data/sprites/entity/building/buildings.png").getSubImage(135, 0, 135, 121).getFlippedCopy(true, false), 5000);
		turretOffFlipped.addFrame(new Image("data/sprites/entity/building/buildings.png").getSubImage(0, 0, 135, 121).getFlippedCopy(true, false), 5000);
		this.anims.put("on", turretOn);
		this.anims.put("off", turretOff);
		this.anims.put("onFlipped", turretOnFlipped);
		this.anims.put("offFlipped", turretOffFlipped);
		this.currentAnim = this.anims.get("off");
		
		this.onOffSwitch.registerObserver(this);
	}
	
	/**
	 * Switches the AutoTurret on or off (inverses its state).
	 */
	public void switchOn() {
		this.firing = true;
		this.lastShotFired = fireRate;
		this.changeAnimation();
	}
	
	public void switchOff() {
		this.firing = false;
		this.lastShotFired = fireRate;
		this.changeAnimation();
	}
	
	public void changeAnimation() {
		if (this.firing) {
			if (this.facingLeft) {
				this.currentAnim = this.anims.get("on");
			} else {
				this.currentAnim = this.anims.get("onFlipped");
			}
		} else {
			if (this.facingLeft) {
				this.currentAnim = this.anims.get("off");
			} else {
				this.currentAnim = this.anims.get("offFlipped");
			}
		}
	}
	
	public void update(Input input, int delta) {
		lastShotFired += delta;
		
		if (firing) {
			if (lastShotFired >= fireRate) {
				lastShotFired = 0;
				
				float xPos = 0;
				float xVel = 0;
				if (facingLeft) {
					xPos = position.getX();
					xVel = -1f;
				} else {
					xPos = position.getX() + this.currentAnim.getWidth();
					xVel = 1f;
				}
				float yPos = position.getY() + 17;
				try {
					level.env.addEntity(new Bullet(level, new Vector2f(xPos, yPos), new Vector2f(xVel, 0)));
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void notify(Observable observable, String message) {
		if (message == "on") {
			this.switchOn();
		} else if (message == "off") {
			this.switchOff();
		}
	}
}