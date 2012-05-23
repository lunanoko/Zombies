package nl.jasperbok.zombies.entity.component;

import nl.jasperbok.engine.Entity;

import org.newdawn.slick.Input;

public class LifeComponent extends Component {
	protected int hp = 1;
	protected boolean damageable = true;
	
	/**
	 * 
	 * @param owner
	 */
	public LifeComponent(Entity owner) {
		this.owner = owner;
		this.id = Component.LIFE;
	}
	
	/**
	 * 
	 * @param owner
	 * @param hp
	 */
	public LifeComponent(Entity owner, int hp) {
		this(owner);
		this.hp = hp;
	}
	
	public void update(Input input, int delta) {
		if (hp == 0) {
			owner.kill();
		}
	}
	
	public int takeDamage(int damage) {
		if (damageable) {
			if (damage > hp) {
				hp = 0;
			} else {
				hp -= damage;
			}
		}
		return hp;
	}
	
	public int getHealth() {
		return hp;
	}
	
	public boolean getDamageable() {
		return this.damageable;
	}
	
	public void setDamageable(boolean damageable) {
		this.damageable = damageable;
	}
}
