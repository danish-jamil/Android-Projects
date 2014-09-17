package com.dnx.mc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class CarSelectionScreen implements Screen {

	Game game;
	Stage stage;
	Table table, container;
	Button car1, car2, car3, car4, car5, btnBack, btnMoreCoins, btnLevels;
	Sound menuClickSound;
	
	public CarSelectionScreen(Game game){
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
		
        SpriteDrawable buttonUp = Assets.Drawables.get("selectcars/car_selection1.png");
        SpriteDrawable buttonDown = Assets.Drawables.get("selectcars/car_selection1_selected.png");
        
		table = new Table();
		
		Group topBar = new Group();
		Group coinsGroup = new Group();
		
		
		
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.down = buttonDown;
		buttonStyle.up = buttonUp;
		buttonStyle.checked = buttonDown;
		
		ButtonStyle btnBackStyle = new ButtonStyle();
		btnBackStyle.up = Assets.Drawables.get("buttons/button_back.png");
		btnBackStyle.down = Assets.Drawables.get("buttons/button_back_pressed.png");
		ButtonStyle btnMoreCoinsStyle = new ButtonStyle();
		btnMoreCoinsStyle.up = Assets.Drawables.get("buttons/button_morecoins.png");
		btnMoreCoinsStyle.down = Assets.Drawables.get("buttons/button_morecoins_pressed.png");
		ButtonStyle btnLevelsStyle = new ButtonStyle();
		btnLevelsStyle.up = Assets.Drawables.get("buttons/button_level.png");
		btnLevelsStyle.down = Assets.Drawables.get("buttons/button_level_pressed.png");
		
		
		car1 = new Button(buttonStyle);
		car2 = new Button(buttonStyle);
		car3 = new Button(buttonStyle);
		car4 = new Button(buttonStyle);
		car5 = new Button(buttonStyle);	
		car1.setChecked(true);
		
		btnBack = new Button(btnBackStyle);
		btnMoreCoins = new Button(btnMoreCoinsStyle);
		btnLevels = new Button(btnLevelsStyle);
		
		container = new Table();
		container.setBounds(0, 0, width, height);
		
		table = new Table();
		//table.setBounds(0, 0, width, height);
		table.add(car1).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(car2).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(car3).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(car4).height(widgetHeight).width(widgetWidth).spaceRight(3);
		table.add(car5).height(widgetHeight).width(widgetWidth).spaceRight(3).row();

		ScrollPane pane = new ScrollPane(table);
		pane.setBounds(0, 0, width, height);
		
		container.add(pane).width(width).colspan(3).padTop(80).spaceBottom(20).row();
		container.add(btnBack);
		container.add(btnMoreCoins);
		container.add(btnLevels);
		
		
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
		car1.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				car1.setChecked(true);
				car2.setChecked(false);
				car3.setChecked(false);
				car4.setChecked(false);
				car5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		car2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				car1.setChecked(false);
				car2.setChecked(true);
				car3.setChecked(false);
				car4.setChecked(false);
				car5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		car3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				car1.setChecked(false);
				car2.setChecked(false);
				car3.setChecked(true);
				car4.setChecked(false);
				car5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		car4.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				car1.setChecked(false);
				car2.setChecked(false);
				car3.setChecked(false);
				car4.setChecked(true);
				car5.setChecked(false);
				menuClickSound.play();
				super.clicked(event, x, y);
			}
		});
		
		car5.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				car1.setChecked(false);
				car2.setChecked(false);
				car3.setChecked(false);
				car4.setChecked(false);
				car5.setChecked(true);
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
						game.setScreen(new MainMenuScreen(game));
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
		
		btnLevels.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						game.setScreen(new LevelSelectionScreen(game));
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
