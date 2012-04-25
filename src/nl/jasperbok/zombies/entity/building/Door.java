package nl.jasperbok.zombies.entity.building;

import nl.jasperbok.zombies.entity.Entity;
import nl.jasperbok.zombies.entity.Observable;
import nl.jasperbok.zombies.entity.Observer;
import nl.jasperbok.zombies.level.Level;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Door extends Entity implements Observer {
	private Switch onOffSwitch;
	
	public Door(Level level) throws SlickException {
		super.init(level);
		Animation door = new Animation();
		door.addFrame(new Image("data/sprites/entity/building/door.png"), 5000);
		this.anims.put("door", door);
		this.currentAnim = this.anims.get("door");
		this.boundingBox = new Rectangle(position.x, position.y, this.currentAnim.getWidth(), this.currentAnim.getHeight());
	}
	
	public Door(Level level, Switch onOffSwitch, Vector2f position) throws SlickException {
		this(level);
		this.setPosition(position.x, position.y);
		this.onOffSwitch = onOffSwitch;
		this.onOffSwitch.registerObserver(this);
		this.isBlocking = true;
	}
	
	public void update(Input input, int delta) {
		super.update(input, delta);
		boundingBox.setBounds(position.x, position.y, this.currentAnim.getWidth(), this.currentAnim.getHeight());
		
		// Ugly quickfix
		//---
		if (level.env.getPlayer().boundingBox.intersects(boundingBox)) {
			level.env.getPlayer().position.x -= 1;
		}
		//---
	}

	public void notify(Observable observable, String message) {
		if (message == "on") {
			level.env.removeEntity(this);
		}
	}
}