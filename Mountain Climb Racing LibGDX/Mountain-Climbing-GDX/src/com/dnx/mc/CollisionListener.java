package com.dnx.mc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dnx.mc.GameVars.BodyType;

public class CollisionListener implements ContactListener {
	
	Array<Body> bodiesToDelete;
	
	public CollisionListener(Array<Body> bodiesToDelete){
		this.bodiesToDelete = bodiesToDelete;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		if( (a.getUserData() != null && a.getUserData() instanceof GameVars.BodyType) && 
				( (a.getUserData() == GameVars.BodyType.CAR ||
				a.getUserData() == GameVars.BodyType.WHEEL ) &&
				b.getUserData() == GameVars.BodyType.COIN ) ) {
			if(!bodiesToDelete.contains(b.getBody(), true)){
				bodiesToDelete.add(b.getBody());
			}
			
			System.out.println("Body added for deletion");
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
