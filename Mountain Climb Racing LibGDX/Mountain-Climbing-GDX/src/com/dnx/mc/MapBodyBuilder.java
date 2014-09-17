package com.dnx.mc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;

public class MapBodyBuilder {

    // The pixels per tile.
    private static float ppm = 0;

    public static Array<Body> buildShapes(Map map, String layer, float pixels, World world) {
        ppm = pixels;
        
        BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;
        FixtureDef def = new FixtureDef();
        def.friction = 0.50f;
        def.density = 10;
        
        MapObjects objects = map.getLayers().get(layer).getObjects();

        Array<Body> bodies = new Array<Body>();
        
        Shape shape = null;

        if(layer.equals("ropeTunnels")){
        	Body[] ropeTunnels = getRopeTunnels(map, layer, pixels, world);
        	for(int i = 0; i < ropeTunnels.length; i++){
        		if(i % 2 == 0){
        			int length = (int) (ropeTunnels[i + 1].getPosition().x - ropeTunnels[i].getPosition().x);
        			System.out.println(length);
        			createRope(length, ropeTunnels[i], ropeTunnels[i + 1], world);
        		}
        	}
        }else{
        	for(MapObject object : objects) {

                if (object instanceof TextureMapObject) {
                    continue;
                }

                if (object instanceof RectangleMapObject) {
//                    shape = getRectangle((RectangleMapObject)object);
                	//createRope(10, map, layer, pixels, world);
                	shape = new PolygonShape();
                }
                else if (object instanceof PolygonMapObject) {
                    shape = getPolygon((PolygonMapObject)object);
                }
                else if (object instanceof PolylineMapObject) {
                    shape = getPolyline((PolylineMapObject)object);
                }
                else if (object instanceof CircleMapObject) {
                    shape = getCircle((CircleMapObject)object);
                }
                else if (object instanceof EllipseMapObject){
                	createEllipse((EllipseMapObject)object, world);
                }
                else {
                    continue;
                }
                
                if(layer.equals("path")){
                	def.shape = shape;
                    def.filter.categoryBits = GameVars.BIT_GROUND;
                    def.filter.maskBits = GameVars.BIT_CAR | GameVars.BIT_WHEEL | GameVars.BIT_CHAIN;
                    Body body = world.createBody(bd);
                    body.setUserData(layer);
                    body.createFixture(def).setUserData(GameVars.BodyType.GROUND);
                    bodies.add(body);
                    shape.dispose();
                }
                
            }
        }
        
        return bodies;
    }
    
    public static Animation getCoinAnimation(){
		int rows = 1, cols = 10;
		Texture sheet = new Texture(Gdx.files.internal("coinsheet.png"));
		TextureRegion[][] keyframes = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
		TextureRegion[] frames = new TextureRegion[rows * cols];
		int index = 0;
		for(int i = 0; i < 1; i++){
			for(int j = 0; j < 10; j++){
				frames[index++] = keyframes[i][j];
			}
		}
		return new Animation(0.05f, frames);
    }
    
    private static Body[] getRopeTunnels(Map map, String layer, float pixels, World world){
    	MapObjects objects = map.getLayers().get(layer).getObjects();
    	Body[] ropeTunnels = new Body[objects.getCount()];
    	BodyDef ropeBodyDef = new BodyDef();
    	ropeBodyDef.type = BodyType.StaticBody;
    	
    	float boxWidth = 0, boxHeight = 0;
    	
    	for (int i = 0; i < objects.getCount(); i++){
    		if(objects.get(i) instanceof RectangleMapObject){
    			Rectangle rect = ((RectangleMapObject) objects.get(i)).getRectangle();
    			PolygonShape box = new PolygonShape();
    			boxWidth = rect.width / pixels;
    			boxHeight = rect.height / pixels;
    			box.setAsBox(boxWidth / 2, boxHeight / 2);
    			ropeBodyDef.position.x = (rect.x / pixels) + boxWidth / 2;
    			ropeBodyDef.position.y = (rect.y / pixels) + boxHeight / 2;
    			ropeTunnels[i] = world.createBody(ropeBodyDef);
    			ropeTunnels[i].createFixture(box, 2);
    			ropeTunnels[i].setUserData(new Vector2(boxWidth, boxHeight));
    		}
    	}
    	
    	return ropeTunnels;
    }
    
    private static Body[] createRope(int length, Body bodyA, Body bodyB, World world){
    	
    	float boxWidth = ((Vector2) bodyA.getUserData()).x;
    	float boxHeight = ((Vector2) bodyA.getUserData()).y;
    	
    	Body[] segments = new Body[length];
    	
    	RevoluteJoint[] joints = new RevoluteJoint[length + 1];
    	RopeJoint[] ropeJoints = new RopeJoint[length + 1];
    	
    	BodyDef def = new BodyDef();
    	def.type = BodyType.DynamicBody;
    	def.position.x = bodyA.getPosition().x;
    	def.position.y = bodyA.getPosition().y;
    	
    	float width = 0.465f, height = 0.125f;
    	
    	PolygonShape shape = new PolygonShape();
    	shape.setAsBox(width, height);
    	
    	FixtureDef fixDef = new FixtureDef();
    	fixDef.shape = shape;
    	fixDef.density = 10;
    	fixDef.filter.categoryBits = GameVars.BIT_CHAIN;
    	fixDef.filter.maskBits = GameVars.BIT_CAR | GameVars.BIT_WHEEL;
    	
    	Sprite ropeSprite = new Sprite(new Texture(Gdx.files.internal("rope.png")));
    	ropeSprite.setSize(width * 2, height * 2);
    	ropeSprite.setOrigin(ropeSprite.getWidth() / 2, ropeSprite.getHeight() / 2);
    	
    	for(int i = 0; i < length; i++) {
    		segments[i] = world.createBody(def);
    		def.position.x += width;
    		segments[i].createFixture(fixDef);
    		segments[i].setUserData(ropeSprite);
    	}
    	
    	RevoluteJointDef jointDef = new RevoluteJointDef();
    	jointDef.localAnchorA.x = boxWidth / 2;
    	jointDef.localAnchorA.y = boxHeight / 2 / 1.3f;
    	jointDef.localAnchorB.x = -width;
    	
    	jointDef.bodyA = bodyA;
    	jointDef.bodyB = segments[0];
    	joints[0] = (RevoluteJoint) world.createJoint(jointDef);
    	
    	jointDef.localAnchorA.x = width;
    	jointDef.localAnchorA.y = 0;
    	jointDef.localAnchorB.x = -width;
    	
    	for(int i = 0; i < joints.length - 2; i++){
    		jointDef.bodyA = segments[i];
    		jointDef.bodyB = segments[i + 1];
    		joints[i] = (RevoluteJoint) world.createJoint(jointDef);
    	}
    	
    	jointDef.localAnchorA.x = width;
    	jointDef.localAnchorB.x = -boxWidth / 2;
    	jointDef.localAnchorB.y = boxHeight / 2 / 1.3f;
    	
    	jointDef.bodyA = segments[length - 1];
    	jointDef.bodyB = bodyB;
    	joints[length] = (RevoluteJoint) world.createJoint(jointDef);
    	
//    	
//    	//Create Rope Joints same as Revolute Joints to keep bodies together within a specific distance
//    	RopeJointDef ropeJointDef =  new RopeJointDef();
//    	ropeJointDef.localAnchorA.x = boxWidth / 2;
//    	ropeJointDef.localAnchorA.y = boxHeight / 2 / 1.2f;
//    	ropeJointDef.localAnchorB.x = -width;
//    	ropeJointDef.maxLength = width;
//    	
//    	ropeJointDef.bodyA = bodyA;
//    	ropeJointDef.bodyB = segments[0];
//    	ropeJoints[0] = (RopeJoint) world.createJoint(ropeJointDef);
//    	
//    	ropeJointDef.localAnchorA.x = width;
//    	ropeJointDef.localAnchorA.y = 0;
//    	ropeJointDef.localAnchorB.x = -width;
//    	
//    	for(int i = 0; i < ropeJoints.length - 2; i++){
//    		ropeJointDef.bodyA = segments[i];
//    		ropeJointDef.bodyB = segments[i + 1];
//    		ropeJoints[i] = (RopeJoint) world.createJoint(ropeJointDef);
//    	}
//    	
//    	ropeJointDef.localAnchorA.x = width;
//    	ropeJointDef.localAnchorB.x = -boxWidth / 2;
//    	ropeJointDef.localAnchorB.y = boxHeight / 2 / 1.3f;
//    	
//    	ropeJointDef.bodyA = segments[length - 1];
//    	ropeJointDef.bodyB = bodyB;
//    	ropeJoints[length] = (RopeJoint) world.createJoint(ropeJointDef);
    	
    	return segments;
    }

    private static void createEllipse(EllipseMapObject ellipseObject, World world){
    	BodyDef bd = new BodyDef();
    	FixtureDef def = new FixtureDef();
    	bd.type = BodyType.StaticBody;
    	Ellipse ellipse = ellipseObject.getEllipse();
    	bd.position.x = ellipse.x / ppm + (0.5f);
    	bd.position.y = ellipse.y / ppm + (0.5f);
    	def.isSensor = true;
    	CircleShape circle = new CircleShape();
    	circle.setRadius(ellipse.width * 0.6f / ppm);
    	def.shape = circle;
    	//Collision
    	def.filter.categoryBits = GameVars.BIT_COIN;
    	def.filter.maskBits = GameVars.BIT_CAR | GameVars.BIT_WHEEL;
    	
    	Body body = world.createBody(bd);
    	body.setUserData(GameVars.BodyType.COIN);
    	body.createFixture(def).setUserData(GameVars.BodyType.COIN);
    	circle.dispose();
    }
    
    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppm,
                                   (rectangle.y + rectangle.height * 0.5f ) / ppm);
        polygon.setAsBox(rectangle.width * 0.5f / ppm,
                         rectangle.height * 0.5f / ppm,
                         size,
                         0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppm);
        circleShape.setPosition(new Vector2(circle.x / ppm, circle.y / ppm));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / ppm;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
//        Vector2[] worldVertices = new Vector2[vertices.length / 2];
//
//        for (int i = 0; i < vertices.length / 2; ++i) {
//            worldVertices[i] = new Vector2();
//            worldVertices[i].x = vertices[i * 2] / ppt;
//            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
//        }

        for(int i = 0; i < vertices.length; i++){
        	vertices[i] = vertices[i] / ppm;
        }
        
        ChainShape chain = new ChainShape(); 
        chain.createChain(vertices);
        return chain;
    }
}
