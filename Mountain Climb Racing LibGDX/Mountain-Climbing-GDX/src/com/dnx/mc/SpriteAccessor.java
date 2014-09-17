package com.dnx.mc;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite>{

	public static final int SPLASH_TYPE = 1;
	
	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch(tweenType){
			case SPLASH_TYPE:
				returnValues[0] = target.getColor().a;
				return 1;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] returnValues) {
		switch(tweenType){
		case SPLASH_TYPE:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, returnValues[0]);
			break;
		default:
			assert false;
			break;
		}
	}

}
