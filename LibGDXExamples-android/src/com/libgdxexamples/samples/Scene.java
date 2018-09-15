package com.libgdxexamples.samples;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Scene extends ApplicationAdapter
{
    private SpriteBatch batch;
    private ArrayList<Sprite> listSprites;
    private ArrayList<Body> listBody;
    private Texture img;
    private World world;
    private Body bodyEdgeScreen;
    private Body bodyEdgeScreenRight;
    private Body bodyEdgeScreenLeft;
    private int numSprites = 20;
    private OrthographicCamera camera;
    private boolean compassAvail;
    private final float PIXELS_TO_METERS = 100f;
    private boolean available;

    @Override
    public void create()
    {
	// Compass
	compassAvail = Gdx.input.isPeripheralAvailable(Peripheral.Compass);

	// Acelerometer
	available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

	// Pieza global de la pantalla, aqui se cargan todos los objetos del
	// juego
	world = new World(new Vector2(0, -1f), true);

	listSprites = new ArrayList<Sprite>();
	listBody = new ArrayList<Body>();

	batch = new SpriteBatch();
	img = new Texture("data/standing/frame-1.png");

	// Create two identical sprites slightly offset from each other
	// vertically

	for (int index = 0; index < numSprites; index++)
	{
	    Sprite sprite = new Sprite(img);
	    sprite.setPosition(-sprite.getWidth() / 2
		    + (index * ((index % 2 == 0) ? 10 : -10)),
		    -sprite.getHeight() / 2 + (index * 100));

	    BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyDef.BodyType.DynamicBody;
	    bodyDef.position
		    .set((sprite.getX() + sprite.getWidth() / 2)
			    / PIXELS_TO_METERS,
			    (sprite.getY() + sprite.getHeight() / 2)
				    / PIXELS_TO_METERS);

	    // Define las propiedades de un cuerpo dentro del escenario
	    Body body = world.createBody(bodyDef);

	    listBody.add(body);

	    listSprites.add(sprite);

	    // Both bodies have identical shape
	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(sprite.getWidth() / 2 / PIXELS_TO_METERS,
		    sprite.getHeight() / 2 / PIXELS_TO_METERS);

	    // Definicion de las propiedades físicas del objeto
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = shape;
	    fixtureDef.density = 0.1f;
	    fixtureDef.restitution = 0.5f;

	    body.createFixture(fixtureDef);
	    shape.dispose();
	}

	// Now the physics body of the bottom edge of the screen
	BodyDef bodyDef3 = new BodyDef();
	bodyDef3.type = BodyDef.BodyType.StaticBody;

	// largo de la pantalla
	float w = Gdx.graphics.getWidth() / PIXELS_TO_METERS;

	// alto de la pantalla
	float h = Gdx.graphics.getHeight() / PIXELS_TO_METERS;

	bodyDef3.position.set(0, 0);
	FixtureDef fixtureDef3 = new FixtureDef();

	// Se trata de un vector, una linea de un punto x,y a otro punto x,y
	EdgeShape edgeShape = new EdgeShape();
	edgeShape.set(-w / 2, -h / 2, w / 2, -h / 2);
	fixtureDef3.shape = edgeShape;

	bodyEdgeScreen = world.createBody(bodyDef3);
	bodyEdgeScreen.createFixture(fixtureDef3);
	edgeShape.dispose();

	// Borde derecho
	BodyDef bodyDefRight = new BodyDef();
	bodyDefRight.type = BodyDef.BodyType.StaticBody;

	bodyDefRight.position.set(0, 0);
	FixtureDef fixtureDefRight = new FixtureDef();

	EdgeShape edgeShapeRight = new EdgeShape();
	edgeShapeRight.set(w / 2, -h / 2, w / 2, h / 2);
	fixtureDefRight.shape = edgeShapeRight;

	bodyEdgeScreenRight = world.createBody(bodyDefRight);
	bodyEdgeScreenRight.createFixture(fixtureDefRight);
	edgeShapeRight.dispose();

	// Borde izquierdo
	BodyDef bodyDefLeft = new BodyDef();
	bodyDefLeft.type = BodyDef.BodyType.StaticBody;

	bodyDefLeft.position.set(0, 0);
	FixtureDef fixtureDefLeft = new FixtureDef();

	EdgeShape edgeShapeLeft = new EdgeShape();
	edgeShapeLeft.set(-w / 2, -h / 2, -w / 2, h / 2);
	fixtureDefLeft.shape = edgeShapeLeft;

	bodyEdgeScreenLeft = world.createBody(bodyDefLeft);
	bodyEdgeScreenLeft.createFixture(fixtureDefLeft);
	edgeShapeLeft.dispose();

	camera = new OrthographicCamera(Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());

	world.setContactListener(new ContactListener()
	{
	    @Override
	    public void beginContact(Contact contact)
	    {
		// Check to see if the collision is between the second sprite
		// and the bottom of the screen
		// If so apply a random amount of upward force to both
		// objects... just because
		/*
		 * for (int index = 0; index < listSprites.size() && index <
		 * listBody.size(); index++) { if
		 * ((contact.getFixtureA().getBody() == bodyEdgeScreen &&
		 * contact .getFixtureB().getBody() == body2) ||
		 * (contact.getFixtureA().getBody() == body2 && contact
		 * .getFixtureB().getBody() == bodyEdgeScreen)) {
		 * 
		 * listBody.get(index).applyForceToCenter(0,
		 * MathUtils.random(20, 50), true);
		 * 
		 * } }
		 */
	    }

	    @Override
	    public void endContact(Contact contact)
	    {
	    }

	    @Override
	    public void preSolve(Contact contact, Manifold oldManifold)
	    {
	    }

	    @Override
	    public void postSolve(Contact contact, ContactImpulse impulse)
	    {
	    }
	});
    }

    @Override
    public void render()
    {
	camera.update();

	//Matrix4 matrix;

	if (available)
	{
	    /*
	    matrix = new Matrix4();
	    
	    
	    matrix.rotate(new Vector3(0, 0, 1), Gdx.input.getRotation());
	    Gdx.input.getRotationMatrix(matrix.val);
	     
	    Gdx.app.log("Orientation","orientation: " + Gdx.input.getRotation() +
		    " accelX:"+Gdx.input.getAccelerometerX()+
		    " accelY:"+Gdx.input.getAccelerometerY()+
		    " accelZ:"+ Gdx.input.getAccelerometerZ() );
	    */
	    /*
	     * float accelX = Gdx.input.getAccelerometerX(); float accelY =
	     * Gdx.input.getAccelerometerY(); float accelZ =
	     * Gdx.input.getAccelerometerZ();
	     */
	}

	if (compassAvail)
	{
	    //azimuth = Gdx.input.getAzimuth();
	    //pitch = Gdx.input.getPitch();
	    //roll = Gdx.input.getRoll();
	    /*
	    Gdx.app.log("Compass", "azimut: " + azimuth + " pitch: " + pitch
		    + " roll: " + roll);
	     */
	}

	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.setProjectionMatrix(camera.combined);
	batch.begin();

	// Step the physics simulation forward at a rate of 60hz
	world.step(1f / 60f, 6, 2);
	for (int index = 0; index < listBody.size()
		&& index < listSprites.size(); index++)
	{
	    listSprites.get(index).setPosition(
		    (listBody.get(index).getPosition().x * PIXELS_TO_METERS)
			    - listSprites.get(index).getWidth() / 2,
		    (listBody.get(index).getPosition().y * PIXELS_TO_METERS)
			    - listSprites.get(index).getHeight() / 2);

	    listSprites.get(index).setRotation(
		    (float) Math.toDegrees(listBody.get(index).getAngle()));

	    batch.draw(listSprites.get(index), listSprites.get(index).getX(),
		    listSprites.get(index).getY(), listSprites.get(index)
			    .getOriginX(), listSprites.get(index).getOriginY(),
		    listSprites.get(index).getWidth(), listSprites.get(index)
			    .getHeight(), listSprites.get(index).getScaleX(),
		    listSprites.get(index).getScaleY(), listSprites.get(index)
			    .getRotation());
	}

	batch.end();
    }

    @Override
    public void dispose()
    {
	img.dispose();
	world.dispose();
    }
}
