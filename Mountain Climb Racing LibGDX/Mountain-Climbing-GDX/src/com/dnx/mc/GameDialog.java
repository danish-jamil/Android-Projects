package com.dnx.mc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class GameDialog extends Window{
	
	public static TextButtonStyle buttonStyle = new TextButtonStyle();
	public static WindowStyle windowStyle = new WindowStyle();
	public static LabelStyle labelStyle = new LabelStyle();

	public GameDialog(String title, WindowStyle style) {
		super(title, style);
		padTop(30);
	}
	
	public static void init(){
		buttonStyle = new TextButtonStyle();
		buttonStyle.up = Assets.Drawables.get("buttons/button-normal.png");
		buttonStyle.down = Assets.Drawables.get("buttons/button-pressed.png");
		buttonStyle.font = Assets.font30;
		buttonStyle.fontColor = Color.WHITE;
		
		windowStyle.titleFont = Assets.font25;
		windowStyle.titleFontColor = Color.WHITE;
		windowStyle.background = Assets.Drawables.get("data/pause-window.png");
		windowStyle.stageBackground = Assets.Drawables.get("data/window-stage-bg.png");
		
		labelStyle.font = Assets.font25;
		labelStyle.fontColor = Color.WHITE;
	}
}
