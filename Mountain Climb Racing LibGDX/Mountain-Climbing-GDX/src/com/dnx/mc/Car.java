package com.dnx.mc;

import com.dnx.mc.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class Car extends InputAdapter {
	private float motorSpeed = 30;
	private Body chassis, leftWheel, rightWheel;
	private WheelJoint leftAxis, rightAxis;
	private Sprite wheel;
	BodyEditorLoader loader;
	WheelJointDef def;
	public Sound engineSound, standBySound;
	long soundId;
	public boolean isAccelerating;
	float volume;
	float offSet;
	
	public Car(World world, FixtureDef chassisFixtureDef, FixtureDef wheelFixtureDef, float x, float y, float width, float height){

		engineSound = Assets.Sounds.get("engine-diesel.ogg");
		standBySound = Assets.Sounds.get("truckstandby.mp3");
		isAccelerating = false;
		volume = 0.01f;
		offSet = 0.015f;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		//Left Wheel
		CircleShape wheelShape = new CircleShape();
		wheelShape.setRadius(height + 0.1f);
		
		wheelFixtureDef.shape = wheelShape;
		
		leftWheel = world.createBody(bodyDef);
		
		//Wheel Sprite Attachment
		wheel = new Sprite(new Texture("truckwheel.png"));
		wheel.setSize(height / 1.2f * 3f, height / 1.2f * 3f);
		wheel.setOrigin(wheel.getWidth() / 2, wheel.getHeight() / 2);
		
		leftWheel.setUserData(wheel);
		leftWheel.createFixture(wheelFixtureDef).setUserData(GameVars.BodyType.CAR);
		
		//Right Wheel
		rightWheel = world.createBody(bodyDef);
		rightWheel.setUserData(wheel);
		rightWheel.createFixture(wheelFixtureDef).setUserData(GameVars.BodyType.CAR);
		
		//CHASSIS BODY
		
		loader = new BodyEditorLoader(Gdx.files.internal("truckbody.json"));
		
		chassis = world.createBody(bodyDef);

		Texture bodyTexture = new Texture(Gdx.files.internal("truckbody.png"));
		bodyTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Sprite body = new Sprite(bodyTexture);
		body.setSize(width * 2, (width * 2)*body.getHeight()/body.getWidth());
		body.setOrigin(body.getWidth() / 2, body.getHeight() / 2);
		
		chassis.setUserData(body);
		//Attach custom shape to body created using BodyEditor
		loader.attachFixture(chassis, "truckbody", chassisFixtureDef, width * 2f);
		
		// Sensor Attachment
		PolygonShape box = new PolygonShape();
		box.setAsBox(height / 2, height / 2, new Vector2(0, height), 0);
		FixtureDef boxFixDef = new FixtureDef();
		boxFixDef.shape = box;
		boxFixDef.isSensor = true;
		chassis.createFixture(boxFixDef).setUserData(GameVars.BodyType.CAR_SENSOR);
		PolygonShape headShape = new PolygonShape();
		box.setAsBox(height, height);
		boxFixDef.shape = headShape;
		chassis.createFixture(boxFixDef);
		
		//Left Axis
		def = new WheelJointDef();
		def.bodyA = chassis;
		def.bodyB = leftWheel;
		def.frequencyHz = 2.5f; // chassisFixtureDef.density;
		def.localAnchorA.set(-width / 2 * 1.2f, -height / 2 * 3.5f);
		def.localAxisA.set(Vector2.Y);
		def.maxMotorTorque = chassisFixtureDef.density * 40;
		leftAxis = (WheelJoint) world.createJoint(def);

		def.bodyB = rightWheel;
		def.localAnchorA.x *= -1.0f;
		
		rightAxis = (WheelJoint) world.createJoint(def);

		soundId = engineSound.loop(volume);
		standBySound.setVolume(standBySound.loop(), 0.15f);
	}
	
	public void update(float delta){
		if(isAccelerating){
			volume += offSet;
			if(volume >= 0.5f){
				volume = 0.5f;
			}
		}else if(!isAccelerating){
			volume -= offSet;
			if(volume <= 0.01f){
				volume = 0.01f;
			}
		}
		engineSound.setVolume(soundId, volume);

//		System.out.println(volume + " " + offSet);
	}

	public float getX(){
		return chassis.getPosition().x;
	}
	
	public float getY(){
		return chassis.getPosition().y;
	}
	
	public Sprite getWheelSprite(){
		return wheel;
	}
	
	public Body getChassis() {
		return chassis;
	}
	

	public Body getLeftWheel() {
		return leftWheel;
	}

	public void setLeftWheel(Body leftWheel) {
		this.leftWheel = leftWheel;
	}

	public void setChassis(Body chassis) {
		this.chassis = chassis;
	}
	
	public Vector2 getLinearVelocity(){
		return chassis.getLinearVelocity();
	}
	
	public void dispose(){
		engineSound.stop();
		standBySound.stop();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.RIGHT:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(-motorSpeed);
			rightAxis.enableMotor(true);
			rightAxis.setMotorSpeed(-motorSpeed);
			isAccelerating = true;
			break;
		case Keys.LEFT:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(motorSpeed / 1.3f);
			rightAxis.enableMotor(true);
			rightAxis.setMotorSpeed(motorSpeed / 1.3f);
			isAccelerating = true;
			break;
		case Keys.SPACE:
			chassis.applyLinearImpulse(new Vector2(0, 5), new Vector2(0, 0) , true);
			break;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.RIGHT:
		case Keys.LEFT:
			rightAxis.enableMotor(false);
			leftAxis.enableMotor(false);
			isAccelerating = false;
		}
		
		return true;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX > Gdx.graphics.getWidth() / 2){
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(-motorSpeed);
			rightAxis.enableMotor(true);
			rightAxis.setMotorSpeed(-motorSpeed);
			isAccelerating = true;
		}else{
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(motorSpeed / 1.3f);
			rightAxis.enableMotor(true);
			rightAxis.setMotorSpeed(motorSpeed / 1.3f);
			isAccelerating = true;
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		rightAxis.enableMotor(false);
		leftAxis.enableMotor(false);
		isAccelerating = false;
		return true;
	}
}
