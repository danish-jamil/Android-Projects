package com.dnx.mountainclimbing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.dnx.mc.MountainClimb;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Mountain Climb Racing";
		cfg.useGL20 = true;
		cfg.width = 720;
		cfg.height = 480;
		cfg.resizable = true;
		
		new LwjglApplication(new MountainClimb(), cfg);
	}
}
