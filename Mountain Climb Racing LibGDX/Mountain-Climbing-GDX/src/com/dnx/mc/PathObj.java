package com.dnx.mc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PathObj {
	
	public static void drawLevel01(World world){
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("levels/level01/level1.json"));
		FixtureDef fixDef = new FixtureDef();
		fixDef.friction = 1f;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		float x = 10, y = 0;
		def.position.set(x, y);
		Body body = world.createBody(def);
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal("levels/level01/path_01.png")));
		sprite.setSize(sprite.getWidth() / 25, sprite.getHeight() / 25);
		loader.attachFixture(body, "path_01", fixDef, sprite.getWidth());

		body.setUserData(sprite);	
		
		Sprite sprite2 = new Sprite(new Texture(Gdx.files.internal("levels/level01/path_02.png")));
		sprite2.setSize(sprite2.getWidth() / 25, sprite2.getHeight() / 25);
		
		def.position.set(x += 2048 / 25f, y);
		
		body = world.createBody(def);
		loader.attachFixture(body, "path_02", fixDef, sprite2.getWidth());
		
		body.setUserData(sprite2);
		
		
		
	}
}
