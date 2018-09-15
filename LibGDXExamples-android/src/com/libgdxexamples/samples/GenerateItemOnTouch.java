package com.libgdxexamples.samples;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdxexamples.android.utils.Assets;
import com.libgdxexamples.android.utils.Constants;
import com.libgdxexamples.samples.graphics.Border;
import com.libgdxexamples.samples.graphics.Player;

public class GenerateItemOnTouch extends Game implements ApplicationListener
{
    private final float PIXELS_TO_METERS = 100f;

    private ArrayList<Player> listPlayer;
    private World world;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private float azimuth;
    private float pitch;
    private float roll;
    private boolean compassAvail;
    private boolean available;
    private float accelX;
    private float accelY;
    private float accelZ;
    private float screenWidth;
    private float screenHeigth;
    private float posX;
    private float posY;

    private Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    private BitmapFont font;
    private BitmapFont font2;

    private long touchTime = 0;

    @Override
    public void create()
    {
	// Carga de las texturas y animaciones
	Assets.load();

	// Pieza global de la pantalla, aqui se cargan todos los objetos del
	// juego
	world = new World(new Vector2(0, -1f), true);

	listPlayer = new ArrayList<Player>();

	batch = new SpriteBatch();

	// Compass
	compassAvail = Gdx.input.isPeripheralAvailable(Peripheral.Compass);

	// Acelerometer
	available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

	// largo de la pantalla
	screenWidth = Gdx.graphics.getWidth() / PIXELS_TO_METERS;

	// alto de la pantalla
	screenHeigth = Gdx.graphics.getHeight() / PIXELS_TO_METERS;

	new Border(world, 0.5f, screenWidth, screenHeigth,
		Constants.BORDER_BOTTOM);
	new Border(world, 0.5f, screenWidth, screenHeigth,
		Constants.BORDER_LEFT);
	new Border(world, 0.5f, screenWidth, screenHeigth,
		Constants.BORDER_RIGHT);

	Gdx.app.log("Pos", "screenWidth:" + screenWidth + " screenHeigth:"
		+ screenHeigth + " posX:" + posX + " posY:" + posY);

	debugRenderer = new Box2DDebugRenderer();

	font = new BitmapFont();
	font.setColor(Color.BLACK);
	font.setScale(2f, 2f);

	font2 = new BitmapFont();
	font2.setColor(Color.GRAY);
	font2.setScale(2f, 2f);

	camera = new OrthographicCamera(Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());

	/**/

	/**/

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
	int anim = 0;
	camera.update();
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	debugMatrix = batch.getProjectionMatrix().cpy()
		.scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

	batch.setProjectionMatrix(camera.combined);
	batch.begin();

	// Step the physics simulation forward at a rate of 60hz
	world.step(1f / 60f, 6, 2);
	if (available)
	{

	    Gdx.app.log("Orientation",
		    "orientation: " + Gdx.input.getRotation() + " accelX:"
			    + Gdx.input.getAccelerometerX() + " accelY:"
			    + Gdx.input.getAccelerometerY() + " accelZ:"
			    + Gdx.input.getAccelerometerZ());

	    accelX = Gdx.input.getAccelerometerX();
	    accelY = Gdx.input.getAccelerometerY();
	    accelZ = Gdx.input.getAccelerometerZ();

	    // Valores entre 10 y -10
	    Gdx.app.log("Accelerometer", "accelX: " + accelX + " accelY: "
		    + accelY + " accelZ: " + accelZ);

	    font2.draw(
		    batch,
		    "Accelerometer: posX:" + Math.round(accelX) + " posY:"
			    + Math.round(accelY) + " posZ:"
			    + Math.round(accelZ), -Gdx.graphics.getWidth() / 2,
		    (Gdx.graphics.getHeight() / 2) - 50);

	}

	if (compassAvail)
	{
	    azimuth = Gdx.input.getAzimuth();
	    pitch = Gdx.input.getPitch();
	    roll = Gdx.input.getRoll();

	    Gdx.app.log("Compass", "azimut: " + azimuth + " pitch: " + pitch
		    + " roll: " + roll);

	    font.draw(batch, "Compass: az:" + Math.round(azimuth) + " pt:"
		    + Math.round(pitch) + " rl:" + Math.round(roll),
		    -Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

	}

	if (Gdx.input.isTouched())
	{
	    Vector3 touchPos = new Vector3();
	    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    camera.unproject(touchPos);

	    Gdx.app.log("Compass", "posXTouch:" + touchPos.x + " posYTouch:"
		    + touchPos.y);

	    anim = (int) (Math.random() * 10) % 5;

	    if ((System.currentTimeMillis() - touchTime) > 200)
	    {
		listPlayer.add(new Player(world, anim, 15, 0.5f, 0.5f,
			touchPos.x, touchPos.y));
	    }

	    touchTime = System.currentTimeMillis();
	}

	for (Player player : listPlayer)
	{
	    player.renderPlayer(batch, true);
	}

	batch.end();

	debugRenderer.render(world, debugMatrix);
    }

    @Override
    public void resize(int width, int height)
    {
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void dispose()
    {
	world.dispose();
    }

}
