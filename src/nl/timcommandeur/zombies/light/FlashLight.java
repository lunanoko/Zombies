package nl.timcommandeur.zombies.light;

import java.util.Arrays;
import java.util.List;

import nl.timcommandeur.zombies.screen.Camera;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import LightTest.LoopingList;
import LightTest.Vec2;

public class FlashLight {
	
	public LightSource flashLightLight;
	public List<LightSource> lights;
	public List<ShadowHull> hulls;
	public List<ShadowHull> flashLightHulls;
	
	public Camera camera;
	
	protected float radius = 800;
	protected int height = 30;
	protected int width = 60;
	protected int currentAngle;
	protected Vector2f angleDisplacement = new Vector2f(0, 0);
	protected boolean on = true;
	
	public Vector2f position;
	
	public FlashLight(List<LightSource> lights, List<ShadowHull> hulls, Vector2f position) {
		this(lights, hulls, position, null);
	}
	
	public FlashLight(List<LightSource> lights, List<ShadowHull> hulls, Vector2f position, Camera camera) {
		this(lights, hulls, position, 800, camera);
	}
	
	public FlashLight(List<LightSource> lights, List<ShadowHull> hulls, Vector2f position, float radius, Camera camera) {
		this.lights = lights;
		this.hulls = hulls;
		this.radius = radius;
		this.camera = camera;
		
		init();
		setPosition(position);
		rotate(0);
	}
	
	/**
	 * Old constructor.
	 * 
	 * @param lights
	 * @param hulls
	 * @param pos
	 */
	public FlashLight(List<LightSource> lights, List<ShadowHull> hulls, Vec2 pos) {
		this.lights = lights;
		this.hulls = hulls;
		
		init();
		setPos(pos);
		rotate(0);
	}
	
	public void switchOnOff() {
		if (on) {
			turnOff();
		} else {
			turnOn();
		}
	}
	
	public void turnOn() {
		on = true;
		setColor(new Color(170, 170, 170));
	}
	
	public void turnOff() {
		on = false;
		setColor(new Color(0, 0, 0, 0));
	}
	
	public void setColor(Color c)
	{
		flashLightLight.setColor(c);
	}
	
	public void setPos(Vec2 v) {
		setPosition(v.x, v.y);
	}
	
	public void setPosition(float x, float y) {
		setPosition(new Vector2f(x, y), 0);
	}
	
	public void setPosition(float x, float y, float displacementDistance) {
		setPosition(new Vector2f(x, y), displacementDistance);
	}
	
	public void setPosition(Vector2f position) {
		this.setPosition(position, 0);
	}
	
	public void setPosition(Vector2f position, float displacementDistance) {
		position.x = position.x + (this.angleDisplacement.x * displacementDistance);
		position.y = position.y + (this.angleDisplacement.y * displacementDistance);
		this.position = position;
		flashLightLight.setPosition(position);
		for (ShadowHull hull : flashLightHulls) {
			hull.setPos(new Vec2(position.x, position.y));
		}
		rotate(currentAngle);
	}
	
	public void setFlicker(float floor, float ceil) {
		flashLightLight.setFlicker(floor, ceil);
	}
	
	public void init() {
		createLights(new Color(200, 200, 200));
		createHulls();
	}
	
	public void rotate(double angle) {
		currentAngle = (int) angle;
		for (ShadowHull hull : flashLightHulls) {
			hull.rotate((float) angle, 10, 30);
		}
	}
	
	public void point(Vec2 to) {
		int angle = (int) (vecAngle(new Vec2(to.x - position.x, to.y - position.y)) / Math.PI * 180);
		this.rotate(angle);
	}
	
	public void pointToMouse(GameContainer container) {
		Camera camera = Camera.getInstance();
		int difX = container.getInput().getAbsoluteMouseX() - 600;
		int difY = container.getInput().getAbsoluteMouseY() - 400;
		Vec2 to = new Vec2(difX, difY);
		double angleInRadians = vecAngle(new Vec2(to.x, to.y));
		double angle = angleInRadians / Math.PI * 180;
		this.angleDisplacement.x = (float) (Math.cos(angleInRadians));
		this.angleDisplacement.y = (float) (Math.sin(angleInRadians));
		
		//System.out.println(this.getClass().toString() + ".pointToMouse: x " + (float) difX / Math.abs(difX));
		//System.out.println(this.getClass().toString() + ".pointToMouse: y " + (float) difY / Math.abs(difY));
		
		this.rotate(angle);
	}
	
	public void createLights(Color c) {
		LightSource light = new LightSource(new Vector2f(0, 0), radius, 0, c, camera);
		lights.add(light);
		flashLightLight = light;
	}
	
	public void createHulls() {
		// The drawing is orientated so that the flashlight will be pointing from left to right.
		//
		Vec2 centerLeft = new Vec2(-4, 30);
		Vec2 topRight = new Vec2(20, 38);
		Vec2 bottomRight = new Vec2(20, 22);
		
		Vec2 points2[] = {centerLeft, bottomRight, centerLeft};
		ShadowHull hull2 = new ShadowHull(new Vec2(400, 400), Arrays.asList(points2), 0.1f, Color.black);
		hulls.add(hull2);
		
		Vec2 points3[] = {centerLeft, topRight, centerLeft};
		ShadowHull hull3 = new ShadowHull(new Vec2(400, 400), Arrays.asList(points3), 0.1f, Color.black);
		hulls.add(hull3);
		
		Vec2 points4[] = {new Vec2(centerLeft.x, centerLeft.y + 1), new Vec2(centerLeft.x, centerLeft.y - 1)};
		ShadowHull hull4 = new ShadowHull(new Vec2(400, 400), Arrays.asList(points4), 0.1f, Color.black);
		hulls.add(hull4);
		
        flashLightHulls = new LoopingList<ShadowHull>();
        
        flashLightHulls.add(hull2);
        flashLightHulls.add(hull3);
        flashLightHulls.add(hull4);
	}
	
	public float vecAngle(Vec2 v) {
        return (float) Math.atan2(v.y, v.x);
    }
	
	public void kill() {
		this.flashLightHulls = null;
		this.flashLightLight = null;
	}
}
