package com.dnx.mc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Assets {
	static AssetManager manager;
	public static Texture START_BUTTON_UP;
	public static BitmapFont font12, font25, font30;
	
	public static void load(){
		manager = new AssetManager();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gill-sans-cond.ttf"));
		font12 = generator.generateFont(12);
		font25 = generator.generateFont(25);
		font30 = generator.generateFont(30);
		generator.dispose();
		Textures.load();
		Drawables.load();
		Sprites.load();
		Sounds.load();
		manager.finishLoading();
		GameDialog.init();
	}
	
	public static class Textures{
		public static void load(){
			manager.load("screens/splash.png", Texture.class);
			manager.load("screens/menu_screen_small.png", Texture.class);
			manager.load("screens/menu_bg.png", Texture.class);
			manager.load("buttons/button_start_normal.png", Texture.class);
			manager.load("buttons/button_start_pressed.png", Texture.class);
			manager.load("buttons/button_options_normal.png", Texture.class);
			manager.load("buttons/button_options_pressed.png", Texture.class);
			manager.load("buttons/button_exit_normal.png", Texture.class);
			manager.load("buttons/button_exit_pressed.png", Texture.class);
			manager.load("buttons/button_back.png", Texture.class);
			manager.load("buttons/button_back_pressed.png", Texture.class);
			manager.load("buttons/button_level.png", Texture.class);
			manager.load("buttons/button_level_pressed.png", Texture.class);
			manager.load("buttons/button_morecoins.png", Texture.class);
			manager.load("buttons/button_morecoins_pressed.png", Texture.class);
			manager.load("buttons/button_race.png", Texture.class);
			manager.load("buttons/button_race_pressed.png", Texture.class);
			manager.load("buttons/button-normal.png", Texture.class);
			manager.load("buttons/button-pressed.png", Texture.class);
			manager.load("selectcars/car_selection1.png", Texture.class);
			manager.load("selectcars/car_selection1_selected.png", Texture.class);
			manager.load("buttons/button_play.png", Texture.class);
			manager.load("buttons/button_pause.png", Texture.class);
			manager.load("levels/thumbnails/level1.png", Texture.class);
			manager.load("levels/thumbnails/level1sel.png", Texture.class);
			manager.load("levels/thumbnails/level2.png", Texture.class);
			manager.load("levels/thumbnails/level2sel.png", Texture.class);
			manager.load("levels/thumbnails/level2locked.png", Texture.class);
			manager.load("levels/thumbnails/level3.png", Texture.class);
			manager.load("levels/thumbnails/level3sel.png", Texture.class);
			manager.load("levels/thumbnails/level3locked.png", Texture.class);
			manager.load("data/top-bar.png", Texture.class);
			manager.load("data/pause-window.png", Texture.class);
			manager.load("data/window-stage-bg.png", Texture.class);
			manager.load("backgrounds/bg-scroll.png", Texture.class);
			
		}
		
		public static Texture get(String fileName){
			return manager.get(fileName, Texture.class);
		}
	}
	
	public static class Drawables{
		public static void load(){
			
		}
		public static SpriteDrawable get(String fileName){
			return new SpriteDrawable(new Sprite(Textures.get(fileName)));
		}
	}
	
	public static class Sprites{
		public static Sprite get(String fileName){
			return new Sprite(Textures.get(fileName));
		}
		
		public static void load(){
			
		}
	}
	
	public static class Sounds{
		public static void load(){
			manager.load("sounds/menuclick.mp3", Sound.class);
			manager.load("sounds/engine-loop.wav", Sound.class);
			manager.load("sounds/truckstandby.mp3", Sound.class);
			manager.load("sounds/engine-police.ogg", Sound.class);
			manager.load("sounds/engine-diesel.ogg", Sound.class);
			manager.load("sounds/coin-pickup.mp3", Sound.class);
		}
		
		public static Sound get(String fileName){
			return manager.get("sounds/" + fileName, Sound.class);
		}
	}
	
	public static class Music{
		public static void load(){
			
		}
	}

	public static void clear(){
		manager.clear();
	}
	
}
