package com.dnx.mc;

import com.badlogic.gdx.Screen;

import aurelienribon.tweenengine.TweenAccessor;

public class ScreenAccessor implements TweenAccessor<Screen>{

	public static final int ALPHA = 0;
	
	@Override
	public int getValues(Screen target, int tweenType, float[] returnValues) {
		switch(tweenType){
		case ALPHA:
			return 0;
			
		}
		return 0;
	}

	@Override
	public void setValues(Screen target, int tweenType, float[] returnValues) {
		
	}

}
