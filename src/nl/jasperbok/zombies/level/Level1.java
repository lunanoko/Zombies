package nl.jasperbok.zombies.level;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import nl.jasperbok.zombies.entity.Player;
import nl.jasperbok.zombies.entity.building.AutoTurret;
import nl.jasperbok.zombies.entity.building.Door;
import nl.jasperbok.zombies.entity.building.Elevator;
import nl.jasperbok.zombies.entity.building.MagneticCrane;
import nl.jasperbok.zombies.entity.building.Switch;
import nl.jasperbok.zombies.entity.item.Item;
import nl.jasperbok.zombies.entity.mob.Zombie;
import nl.jasperbok.zombies.entity.object.Crate;
import nl.jasperbok.zombies.entity.object.WoodenCrate;

public class Level1 extends Level {
	/**
	 * The game holding this state.
	 */
	private StateBasedGame game;
	public Elevator elevator;
	public MagneticCrane crane;
	public Zombie zombie;
	public Crate crate;
	
	public Zombie crateZombie;
	
	private Music bgMusic;

	public Level1() throws SlickException {
		super("level1");
		this.ID = 2;
		
		// Creating the Player.
		Player player = new Player(100, this);
		player.setPosition(1800, 660); // Start at crane controls.
		//player.setPosition(300, 320); // Regular level start.
		env.setPlayer(player);
		
		// Add the key card.
		Item item = new Item(this, Item.KEY_CARD, new Vector2f(0, 0), 32, 32);
		env.addEntity(item);
		item.setPosition(1900, 660);
		
		// Add the door.
		Door door = new Door(this, new Vector2f(2800, 960));
		env.addEntity(door);
		
		// Creating a Zombie.
		crateZombie = new Zombie(this, (float)(2400), 660);
		env.addMob(crateZombie);
		crateZombie.addBlockingPointLeft(2290);
		
		// Creating a Switch and AutoTurret.
		Switch turretSwitch = new Switch(this, true, new Vector2f(1800, 660));
		env.addEntity(new AutoTurret(this, true, turretSwitch, new Vector2f(1900, 660)));
		env.addEntity(turretSwitch);
		
		camera.setTarget(env.getPlayer());
		MagneticCrane crane = new MagneticCrane(this, new Vector2f(2160, 560));
		env.addEntity(crane);
		crate = new Crate(this, new Vector2f(2160, 660), crane);
		env.addEntity(crate);
		env.addEntity(new WoodenCrate(this, 2720, 1040));
		env.addEntity(new WoodenCrate(this, 3520, 1040));
		
		for (int i = 0; i < 2; i++) {
			Zombie zl = new Zombie(this, (float)(10 + i * 30), 80f);
			zl.addBlockingPointRight(600);
			env.addMob(zl);
		}
		env.mobDirector.addAttractor(env.getPlayer(), 50, true);
		
		bgMusic = new Music("data/sound/music/stil.ogg");
		bgMusic.loop();
		
		//craneLights[0] = new FlashLight(lights, cHulls, new Vector2f(crane.armPos.x + 30, 730), 200, camera);
		//craneLights[1] = new FlashLight(lights, cHulls, new Vector2f(crane.armPos.x + 90, 730), 200, camera);
		//craneLights[0].setPosition(new Vector2f(crane.armPos.getX() + 30 + camera.position.getX(), 730 - camera.position.getY()));
		//craneLights[1].setPosition(new Vector2f(crane.armPos.getX() + 90 + camera.position.getX(), 730 - camera.position.getY()));
		//craneLights[0].rotate(100);
		//craneLights[1].rotate(80);
		//craneLights[0].setColor(new Color(100, 100, 100));
		//craneLights[1].setColor(new Color(100, 100, 100));
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		//craneLights[0].setPosition(new Vector2f(crane.armPos.x + 30 + camera.position.x, 130 - camera.position.y));
		
		
		if (crateZombie.boundingBox.intersects(crate.boundingBox) && crate.velocity.y > 0) {
			crateZombie.position.x = 100000000;
		}
		
		super.update(container, game, delta);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
	}
}
