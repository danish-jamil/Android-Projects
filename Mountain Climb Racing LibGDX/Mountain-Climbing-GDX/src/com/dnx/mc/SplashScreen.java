package com.dnx.mc;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	Sprite sprite;
	SpriteBatch batch;
	Texture texture;
	Game game;
	TweenManager tweenManager;
	
	public SplashScreen(Game game){
		this.game = game;
		Assets.load();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
		if(Gdx.input.isTouched()){
			
		}
	}

	@Override
	public void resize(int width, int height) {

		
	}

	@Override
	public void show() {
		
		batch = new SpriteBatch();
		Texture texture = Assets.Textures.get("screens/splash.png");
		sprite = new Sprite(texture);
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Tween.set(sprite, SpriteAccessor.SPLASH_TYPE).target(0).start(tweenManager);
		Tween.to(sprite, SpriteAccessor.SPLASH_TYPE, 1).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				game.setScreen(new MainMenuScreen(game));
			}
		}).start(tweenManager);
		
	}

	@Override
	public void hide() {

		
	}

	@Override
	public void pause() {

		
	}

	@Override
	public void resume() {

		
	}

	@Override
	public void dispose() {
		
		
	}

}
