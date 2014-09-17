package com.dnx.mc;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

	public static final float PPM = 32f;
	public static final float V_WIDTH = 720f;
	public static final float V_HEIGHT = 480f;
	private World world;
	private Box2DDebugRenderer renderer;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Car car;
	TiledMap map;
	OrthogonalTiledMapRenderer tiledRenderer;
	float stateTime;
	Animation coinAnimation;
	Coin coin;
	Game game;
	Stage stage;
	
	Array<Body> bodiesToDelete;
	Sound coinPickupSound;
	
	Sprite background;
	
	boolean paused = false;
	boolean debug = false;
	
	public GameScreen(Game game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//System.out.println(car.getChassis().getLinearVelocity().x);
		
		car.update(delta);
		
		stateTime += Gdx.graphics.getDeltaTime();
		
		if(car.getX() > 6.5){
			camera.position.x = car.getX() + 5;
		}
		if(car.getY() > 6){
			camera.position.y = car.getY() + 2;
		}
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		
		if(bodiesToDelete.size > 0 && !world.isLocked()){
			for(Body body : bodiesToDelete){
				world.destroyBody(body);
//				body.setUserData(null);
//				body = null;
				bodiesToDelete.removeValue(body, true);
				coinPickupSound.play();
			}
		}
		
		background.setBounds(camera.position.x - V_WIDTH / PPM / 2, camera.position.y - V_HEIGHT / PPM / 2,
				V_WIDTH / PPM, V_HEIGHT / PPM);
		batch.begin();
		background.draw(batch);
		batch.end();
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		batch.begin();

		for (Body body : bodies)
			if (body.getUserData() != null && body.getUserData() instanceof Sprite){
				Sprite sprite = (Sprite) body.getUserData();
				sprite.setPosition((body.getPosition().x - sprite.getWidth()/2), 
						(body.getPosition().y - sprite.getHeight()/2) );
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}else if(body.getUserData() != null && body.getUserData() instanceof GameVars.BodyType){
				if(body.getUserData() == GameVars.BodyType.COIN){
					batch.draw(coinAnimation.getKeyFrame(stateTime, true), body.getPosition().x - 22 / PPM, 
							body.getPosition().y - 20 / PPM,
							44 / PPM, 40 / PPM
							);
				}
			}
		
		batch.end();
		
		if(debug)
			renderer.render(world, camera.combined);
		
		tiledRenderer.setView(camera);
		tiledRenderer.render();
		
		stage.act();
		stage.draw();
		Table.drawDebug(stage);
		if(!paused)
		// Copied these values from Box2D Example
		world.step(1f/60f, 8, 3);
		
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = V_WIDTH / PPM;
		camera.viewportHeight = V_HEIGHT / PPM;
		camera.update();
		
	}

	@Override
	public void show() {

		world = new World(new Vector2(0f, -9.814f), true);
		Gdx.graphics.setTitle("Mountain Climb Racing");
		renderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
		batch = new SpriteBatch();
		stage = new Stage(V_WIDTH / PPM, V_HEIGHT / PPM, false);
		bodiesToDelete = new Array<Body>();
		coinPickupSound = Assets.Sounds.get("coin-pickup.mp3");
		background = Assets.Sprites.get("backgrounds/bg-scroll.png");
		background.setBounds(camera.position.x, camera.position.y, V_WIDTH / PPM, V_HEIGHT / PPM);
		
		ButtonStyle btnPlayStyle = new ButtonStyle();
		btnPlayStyle.up = Assets.Drawables.get("buttons/button_pause.png");
		btnPlayStyle.down = Assets.Drawables.get("buttons/button_play.png");
		btnPlayStyle.checked = Assets.Drawables.get("buttons/button_play.png");
		final Button btnPlay = new Button(btnPlayStyle);
		
//		Texture fontTexture = new Texture(Gdx.files.internal("brady.png"));
//        BitmapFont font = new BitmapFont(Gdx.files.internal("brady.fnt"), new TextureRegion(fontTexture), false);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont();
		labelStyle.font.setUseIntegerPositions(false);
		labelStyle.font.setScale(1 / 32f);
		Label score = new Label("Score", labelStyle);
		
		Button btnAccelerate = new Button(btnPlayStyle);
		Button btnBrake = new Button(btnPlayStyle);
		
		Table container = new Table();
		container.setFillParent(false);
		container.debug();
		container.setBounds(0, 0, V_WIDTH / PPM, V_HEIGHT / PPM);
		container.add(score).width(1.55f).height(1.55f).expandX().expandY().left().top().padLeft(0.3f).padTop(0.0f);
		container.add(btnPlay).width(1.55f).height(1.55f).expandX().expandY().right().top().padRight(0.3f).padTop(0.0f).row();
		container.add(btnBrake).width(1.55f).height(1.55f).expandX().expandY().left().bottom().padLeft(0.3f).padBottom(0.0f);
		container.add(btnAccelerate).width(1.55f).height(1.55f).expandX().expandY().right().bottom().padRight(0.3f).padBottom(0.0f);
		stage.addActor(container);
		
		btnPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				paused = btnPlay.isChecked();
				super.clicked(event, x, y);
			}
		});
		
		
		coinAnimation = MapBodyBuilder.getCoinAnimation();
		
		//Load TiledMap
		map = new TmxMapLoader().load("level1-data.tmx");
		tiledRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM, batch);
		
		//Car
		
		FixtureDef chassisFixtureDef = new FixtureDef(), wheelFixtureDef = new FixtureDef();
		chassisFixtureDef.density = 10;
		chassisFixtureDef.friction = .4f;
		chassisFixtureDef.restitution = .3f;
		chassisFixtureDef.filter.categoryBits = GameVars.BIT_CAR;
		chassisFixtureDef.filter.maskBits = GameVars.BIT_CHAIN | GameVars.BIT_COIN | GameVars.BIT_GROUND;

		wheelFixtureDef.density = chassisFixtureDef.density * 1.5f;
		wheelFixtureDef.friction = 0.5f;
		wheelFixtureDef.restitution = 0.1f;
		//Collision Filters
		wheelFixtureDef.filter.categoryBits = GameVars.BIT_CAR;
		wheelFixtureDef.filter.maskBits = GameVars.BIT_CHAIN | GameVars.BIT_COIN | GameVars.BIT_GROUND;
		
		car = new Car(world, chassisFixtureDef, wheelFixtureDef, 10, 15, 1.7f, 0.5f);

		// GROUND
		//PathObj.drawLevel01(world);
		MapBodyBuilder.buildShapes(map, "path", PPM, world);
		MapBodyBuilder.buildShapes(map, "coins", PPM, world);
		MapBodyBuilder.buildShapes(map, "ropeTunnels", PPM, world);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter(){
			@Override
			public boolean scrolled(int amount) {
				if(amount == 1){
					camera.zoom += 0.5f;
					
				}else if(amount == -1){
					camera.zoom -= 0.1f;
				}
				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.BACKSPACE){
					game.setScreen(new LevelSelectionScreen(game));
				}
				return false;
			}
			
		}, car));
		
		world.setContactListener(new CollisionListener(bodiesToDelete));
	}
	
	public void createHurdle(World world, float x, float y, float width, float height){
		PolygonShape hurdle = new PolygonShape();
		hurdle.set(new float[]{
			0, 0,
			width, 0,
			width / 2, height
		});
		
		FixtureDef hurdleDef = new FixtureDef();
		hurdleDef.shape = hurdle;
		hurdleDef.density = 10;
		hurdleDef.friction = 2;
		hurdleDef.restitution = 0;
		
		BodyDef hurdleBody = new BodyDef();
		hurdleBody.type = BodyType.StaticBody;
		hurdleBody.position.set(x, y);
		world.createBody(hurdleBody)
			.createFixture(hurdle, 5);
	}
	
	public void createCircle(World world, float x, float y, float radius){
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		FixtureDef hurdleDef = new FixtureDef();
		hurdleDef.shape = circle;
		hurdleDef.density = 10;
		hurdleDef.friction = 2;
		hurdleDef.restitution = 0;
		
		BodyDef hurdleBody = new BodyDef();
		hurdleBody.type = BodyType.StaticBody;
		hurdleBody.position.set(x, y);
		world.createBody(hurdleBody).createFixture(hurdleDef);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		world.dispose();
		renderer.dispose();
//		ren.dispose();
		batch.dispose();
		stage.dispose();
		car.dispose();
//		world.dispose();
		Gdx.app.log("GameScreen", "Disposed");
	}
	
}
