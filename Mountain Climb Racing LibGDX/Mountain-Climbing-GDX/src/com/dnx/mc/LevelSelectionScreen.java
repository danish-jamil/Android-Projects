package com.dnx.mc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class LevelSelectionScreen implements Screen {

	Game game;
	Stage stage;
	Table table, container;
	Button level1, level2, level3, level4, level5, btnBack, btnMoreCoins, btnRace;
	Sound menuClickSound;
	
	public LevelSelectionScreen(Game game){
		this.game = game;
	}
	
	@Override
	public void show() {
		float width = GameScreen.V_WIDTH, 
				height = GameScreen.V_HEIGHT;
		
		float widgetWidth = 180, widgetHeight = 120;
		
		menuClickSound = Assets.Sounds.get("menuclick.mp3");
		
		Image backgroundImage = new Image(Assets.Textures.get("screens/menu_bg.png"));
        backgroundImage.setBounds(0, 0, width, height);
		
		table = new Table();
		
		ButtonStyle level1Style = new ButtonStyle();
		level1Style.down 	= Assets.Drawables.get("levels/thumbnails/level1sel.png");
		level1Style.up 		= Assets.Drawables.get("levels/thumbnails/level1.png");
		level1Style.checked = Assets.Drawables.get("levels/thumbnails/level1sel.png");
		
		ButtonStyle level2Style = new ButtonStyle();
		level2Style.down 	= Assets.Drawables.get("levels/thumbnails/level2locked.png");
		level2Style.up 		= Assets.Drawables.get("levels/thumbnails/level2locked.png");
		level2Style.checked = Assets.Drawables.get("levels/thumbnails/level2locked.png");
		
		ButtonStyle level3Style = new ButtonStyle();
		level3Style.down 	= Assets.Drawables.get("levels/thumbnails/level3locked.png");
		level3Style.up 		= Assets.Drawables.get("levels/thumbnails/level3locked.png");
		level3Style.checked = Assets.Drawables.get("levels/thumbnails/level3locked.png");
		
		ButtonStyle btnBackStyle = new ButtonStyle();
		btnBackStyle.up = Assets.Drawables.get("buttons/button_back.png");
		btnBackStyle.down = Assets.Drawables.get("buttons/button_back_pressed.png");
		ButtonStyle btnMoreCoinsStyle = new ButtonStyle();
		btnMoreCoinsStyle.up = Assets.Drawables.get("buttons/button_morecoins.png");
		btnMoreCoinsStyle.down = Assets.Drawables.get("buttons/button_morecoins_pressed.png");
		ButtonStyle btnLevelsStyle = new ButtonStyle();
		btnLevelsStyle.up = Assets.Drawables.get("buttons/button_race.png");
		btnLevelsStyle.down = Assets.Drawables.get("buttons/button_race_pressed.png");
		
		
		level1 = new Button(level1Style);
		level2 = new Button(level2Style);
		level3 = new Button(level3Style);
		level4 = new Button(level3Style);
		level5 = new Button(level3Style);	
		level1.setChecked(true);
		
		btnBack = new Button(btnBackStyle);
		btnMoreCoins = new Button(btnMoreCoinsStyle);
		btnRace = new Button(btnLevelsStyle);
		
		container = new Table();
		container.setBounds(0, 0, width, height);
		
		table = new Table();
		//table.setBounds(0, 0, width, height);
		table.add(level1).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(level2).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(level3).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(level4).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(level5).height(widgetHeight).width(widgetWidth).spaceRight(3).row();

		ScrollPane pane = new ScrollPane(table);
		pane.setBounds(0, 0, width, height);
		
		container.add(pane).width(width).colspan(3).padTop(80).spaceBottom(20).row();
		container.add(btnBack);
		container.add(btnMoreCoins);
		container.add(btnRace);
		
		
		stage = new Stage(width, height, false);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		stage.addActor(backgroundImage);
		stage.addActor(container);
		stage.addAction(Actions.fadeIn(0.5f));
		addListeners();
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
        
	}
	
	private void addListeners(){
		level1.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level1.setChecked(true);
				level2.setChecked(false);
				level3.setChecked(false);
				level4.setChecked(false);
				level5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		level2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level1.setChecked(false);
				level2.setChecked(true);
				level3.setChecked(false);
				level4.setChecked(false);
				level5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		level3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level1.setChecked(false);
				level2.setChecked(false);
				level3.setChecked(true);
				level4.setChecked(false);
				level5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		level4.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level1.setChecked(false);
				level2.setChecked(false);
				level3.setChecked(false);
				level4.setChecked(true);
				level5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		level5.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level1.setChecked(false);
				level2.setChecked(false);
				level3.setChecked(false);
				level4.setChecked(false);
				level5.setChecked(true);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		btnBack.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						game.setScreen(new CarSelectionScreen(game));
					}
				})));
				
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		btnMoreCoins.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//game.setScreen(new MoreCoinsScreen(game));
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		btnRace.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						game.setScreen(new GameScreen(game));
					}
				})));
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
	}

	@Override
	public void resize(int width, int height) {
		
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
