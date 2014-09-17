package com.dnx.mc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Coin {
	Animation animation;
	Body body;
	float width, height;
	float stateTime;
	TextureRegion currentFrame;
	
	public Coin(Body body){
		this.body = body;
		animation = getCoinAnimation();
		stateTime = 0f;
		currentFrame = new TextureRegion();
	}

	private Animation getCoinAnimation(){
		int rows = 1, cols = 10;
		Texture sheet = new Texture(Gdx.files.internal("coinsheet.png"));
		TextureRegion[][] keyframes = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
		TextureRegion[] frames = new TextureRegion[rows * cols];
		
		int index = 0;
		for(int i = 0; i < 1; i++){
			for(int j = 0; j < 10; j++){
				frames[index++] = keyframes[i][j];
			}
		}
		width = frames[0].getRegionWidth();
		height = frames[0].getRegionHeight();
		return new Animation(0.05f, frames);
    }
	
	public void update(float dt){
		stateTime += dt;
		currentFrame = animation.getKeyFrame(stateTime);
	}
	
	public void render(SpriteBatch batch){
		batch.draw(currentFrame, body.getPosition().x, body.getPosition().y);
		
	}
	
	public Body getBody(){return body;}
	public Vector2 getPosition(){return body.getPosition();}
	public float getWidth(){return width;}
	public float getHeight(){return height;}
	
}
