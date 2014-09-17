package com.dnx.mc;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class UserData {
	private GameVars.BodyType bodyType;
	private Sprite sprite;
	
	public UserData(com.dnx.mc.GameVars.BodyType bodyType, Sprite sprite) {
		this.bodyType = bodyType;
		this.sprite = sprite;
	}
	
	public GameVars.BodyType getBodyType() {
		return bodyType;
	}
	public void setBodyType(GameVars.BodyType bodyType) {
		this.bodyType = bodyType;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
}
