package nl.jasperbok.zombies.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import nl.jasperbok.zombies.level.Level;
import nl.jasperbok.zombies.math.Vector2;
import nl.jasperbok.zombies.entity.component.Component;
import nl.jasperbok.zombies.entity.component.GravityComponent;
import nl.jasperbok.zombies.entity.component.LifeComponent;
import nl.jasperbok.zombies.entity.component.PlayerInputComponent;
import nl.jasperbok.zombies.entity.item.Inventory;
import nl.jasperbok.zombies.entity.object.WoodenCrate;
import nl.jasperbok.zombies.gui.Hud;

public class Player extends Entity {
	public float climbSpeed = 0.1f;
	
	// Status variables.
	protected boolean wasGoingLeft = false;
	protected boolean wasGoingRight = false;
	
	public Player(Level level, Vector2f pos) throws SlickException {
		super.init(level);
		this.addComponent(new GravityComponent(0.01f, this));
		this.addComponent(new PlayerInputComponent(this));
		this.addComponent(new LifeComponent(this, 5));
		this.acceleration = new Vector2f(0.06f, 0);
		this.maxVelocity = new Vector2f(0.4f, 10f);
		this.position = pos;
		this.playerControlled = true;
		this.boundingBox = new Rectangle(position.x, position.y, 10, 10);
		this.init();
		
		inventory = new Inventory();
	}
	
	public void init() throws SlickException {
		// Fix the walking animations.
		SpriteSheet walkSprites = new SpriteSheet("data/sprites/entity/walkingwalking.png", 75, 150);
		Animation walkRight = new Animation();
		for (int i = 0; i < 8; i++) {
			walkRight.addFrame(walkSprites.getSprite(i, 0), 150);
		}
		Animation walkLeft = new Animation();
		for (int i = 0; i < 8; i++) {
			walkLeft.addFrame(walkSprites.getSprite(i, 0).getFlippedCopy(true, false), 150);
		}
		
		// Fix the idle animation.
		SpriteSheet idleSprites = new SpriteSheet("data/sprites/entity/girl_stand.png", 51, 166);
		Animation idle = new Animation();
		idle.addFrame(idleSprites.getSprite(0, 0), 500);
		
		// Fix the climb animation.
		SpriteSheet climbSprites = new SpriteSheet("data/sprites/entity/girl_climb_sprite.png", 56, 147);
		Animation climb = new Animation();
		climb.addFrame(climbSprites.getSprite(0, 0), 250);
		climb.addFrame(climbSprites.getSprite(0, 0).getFlippedCopy(true, false), 250);
		
		// Fix the hide animation.
		SpriteSheet hideSprite = new SpriteSheet("data/sprites/entity/girl_hide.png", 75, 150);
		Animation hide = new Animation();
		hide.addFrame(hideSprite.getSprite(0, 0), 250);
		
		this.anims.put("walkLeft", walkLeft);
		this.anims.put("walkRight", walkRight);
		this.anims.put("idle", idle);
		this.anims.put("climb", climb);
		this.anims.put("hide", hide);
		
		// Set the initial animation.
		this.currentAnim = this.anims.get("idle");
	}
	
	protected void updateBoundingBox() {
		if (this.currentAnim != null) {
			this.boundingBox.setBounds(position.x, position.y, this.currentAnim.getWidth(), this.currentAnim.getHeight());
		}
	}
	
	public void update(Input input, int delta) {
		updateBoundingBox();
		
		wasClimbing = isClimbing;
		isClimbing = false;
		
		this.isOnGround = level.env.isOnGround(this, false);
		if (wasClimbing && level.env.isOnClimableSurface(this)) {
			isClimbing = true;
			velocity.set(new Vector2f(velocity.getX(), 0));
		} else if (!level.env.isOnClimableSurface(this)) {
			isClimbing = false;
		}
		
		try {
			Hud.getInstance().setPlayerHealth(((LifeComponent)getComponent(Component.LIFE)).getHealth());
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.update(input, delta);
		
		// Decide what animation should be played.
		if (isHidden()) {
			this.currentAnim = this.anims.get("hide");
		} else if (isClimbing) {
			System.out.println(this.getClass().toString() + ".update: not hidden");
			this.currentAnim = this.anims.get("climb");
			if (!input.isKeyDown(Input.KEY_W) && !input.isKeyDown(Input.KEY_S)) {
				this.currentAnim.stop();
			} else if (this.currentAnim.isStopped()) {
				this.currentAnim.start();
			}
		} else if (isOnGround) {
			if (velocity.getX() < 0f) {
				this.currentAnim = this.anims.get("walkLeft");
			} else if (velocity.getX() > 0f) {
				this.currentAnim =this.anims.get("walkRight");
			} else if (velocity.getX() == 0f) {
				this.currentAnim = this.anims.get("idle");
			}
		} else {
			// Not on ground and not climbing, surely the player is falling!
			//currentAnimation = fallAnimation;
		}
	}
	
	private void climbObject() {
		Entity target = (Entity) level.env.getUsableEntity(boundingBox);
		if (target != null && target instanceof WoodenCrate) {
			this.setPosition(target.position.getX(), target.position.getY() - this.boundingBox.getHeight());
		}
	}
	
	public void switchHide() {
		LifeComponent lifeComponent = (LifeComponent)getComponent(Component.LIFE);
		if (lifeComponent.getDamageable() == true) {
			hide();
		} else {
			unHide();
		}
	}
	
	public void hide() {
		LifeComponent lifeComponent = (LifeComponent)getComponent(Component.LIFE);
		if (lifeComponent.getDamageable() == true) {
			lifeComponent.setDamageable(false);
			System.out.println("Player.hide: hiding");
		}
	}
	
	public void unHide() {
		LifeComponent lifeComponent = (LifeComponent)getComponent(Component.LIFE);
		if (lifeComponent.getDamageable() == false) {
			lifeComponent.setDamageable(true);
			System.out.println("Player.hide: unhiding");
		}
	}
	
	public boolean isHidden() {
		LifeComponent lifeComponent = (LifeComponent)getComponent(Component.LIFE);
		if (lifeComponent.getDamageable() == true) {
			return false;
		} else {
			return true;
		}
	}
}
