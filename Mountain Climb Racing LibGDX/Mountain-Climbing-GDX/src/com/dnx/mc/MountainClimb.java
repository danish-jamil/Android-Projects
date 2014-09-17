package com.dnx.mc;

import com.badlogic.gdx.Game;

public class MountainClimb extends Game {
	
	@Override
	public void create() {		
		
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		getScreen().dispose();
	}
	
	@Override
	public void render() {
		super.render();
		//fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
