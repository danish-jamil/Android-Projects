package com.dnx.mc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    
    private Stage stage;
    Game game;
    float width, height;

    public MainMenuScreen(Game game){
    	this.game = game;
    }
    
	@Override
	public void show() {
		width = GameScreen.V_WIDTH; 
		height = GameScreen.V_HEIGHT;
        
        Image backgroundImage = new Image(Assets.Textures.get("screens/menu_screen_small.png"));
        backgroundImage.setBounds(0, 0, width, height);
        
        stage = new Stage(width, height, false);
        stage.clear();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage); 
        
        //Button Start
        //#####################################################3
        
        ButtonStyle style = new TextButtonStyle(); //** Button properties **//
        style.up = Assets.Drawables.get("buttons/button_start_normal.png");
        style.down = Assets.Drawables.get("buttons/button_start_pressed.png");
        
        float btnWidth = 200, btnHeight = 70;
        final Button btnStart = new Button(style);
        btnStart.setWidth(btnWidth);
        btnStart.setHeight(btnHeight);
        btnStart.setPosition(width / 2 - btnWidth / 2, height / 2);
        
        //Options Button
        //########################################
        
        ButtonStyle optionsButtonStyle = new ButtonStyle();
        optionsButtonStyle.up = Assets.Drawables.get("buttons/button_options_normal.png");
        optionsButtonStyle.down = Assets.Drawables.get("buttons/button_options_pressed.png");
        
        final Button btnOptions = new Button(optionsButtonStyle);
        btnOptions.setWidth(btnWidth);
        btnOptions.setHeight(btnHeight);
        btnOptions.setPosition(width / 2 - btnWidth / 2, height / 2 - btnHeight);
        
        //Exit Button 
        //############################################
        ButtonStyle exitButtonStyle = new ButtonStyle();
        exitButtonStyle.up = Assets.Drawables.get("buttons/button_exit_normal.png");
        exitButtonStyle.down = Assets.Drawables.get("buttons/button_exit_pressed.png");
        
        final Button btnExit = new Button(exitButtonStyle);
        btnExit.setWidth(btnWidth);
        btnExit.setHeight(btnHeight);
        btnExit.setPosition(width / 2 - btnWidth / 2, height / 2 - btnHeight * 2);
        
        final Sound menuClickSound = Assets.Sounds.get("menuclick.mp3");
        
        ClickListener listener = new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		if(event.getTarget() == btnStart){
        			stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
    					
    					@Override
    					public void run() {
    						game.setScreen(new CarSelectionScreen(game));
    					}
    				})));
        		}else if(event.getTarget() == btnOptions){
        			
        		}else if(event.getTarget() == btnExit){
        			
        			Table table = new Table();
        			TextButton buttonYes = new TextButton("Yes", GameDialog.buttonStyle);
        			TextButton buttonNo = new TextButton("No", GameDialog.buttonStyle);
        			Label label = new Label("Are you sure?", GameDialog.labelStyle);
        			table.add(label).spaceBottom(15).row();
        			table.add(buttonYes).spaceBottom(5).row();
        			table.add(buttonNo);
        			table.center();
        			
        			final GameDialog window = new GameDialog("Confirm exit!", GameDialog.windowStyle);
        			window.setBounds(width / 2 - 256 / 2, height / 2 - 256 / 2, 256, 256);
        			window.add(table);
        			stage.addActor(window);
        			window.setVisible(true);
        			
        			buttonYes.addListener(new ClickListener(){
        				public void clicked(InputEvent event, float x, float y) {
        					stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
        						
        						@Override
        						public void run() {
        							Gdx.app.exit();
        						}
        					})));
        					menuClickSound.play();
        				};
        			});
        			
        			buttonNo.addListener(new ClickListener(){
        				public void clicked(InputEvent event, float x, float y) {
        					window.setVisible(false);
        					menuClickSound.play();
        				};
        			});
        			
        		}
        		menuClickSound.play();
        		super.clicked(event, x, y);
        	}
        };
        
        
        btnStart.addListener(listener);
        btnOptions.addListener(listener);
        btnExit.addListener(listener);
        
        stage.addActor(backgroundImage);
        stage.addActor(btnStart);
        stage.addActor(btnOptions);
        stage.addActor(btnExit);
        
        stage.addAction(Actions.fadeIn(0.5f));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        
        
        stage.act();

        stage.draw();
        
        Dialog.drawDebug(stage);
        Table.drawDebug(stage);
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

	@Override
	public void dispose() {
        stage.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
