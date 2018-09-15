package com.libgdxexamples.samples;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.physics.box2d.World;
import com.libgdxexamples.android.utils.Assets;
import com.libgdxexamples.android.utils.Constants;
import com.libgdxexamples.samples.graphics.Player;

public class Scene2 extends ApplicationAdapter
{
    private SpriteBatch batch;
    private ArrayList<Player> listPlayers;
    private World world;
    private Body bodyEdgeScreen;
    Body bodyEdgeScreenRight;
    Body bodyEdgeScreenLeft;
    private int numSprites = 20;
    private OrthographicCamera camera;
    private int atlas = Constants.ATLAS_STANDING;

    private final float PIXELS_TO_METERS = 100f;

    @Override
    public void create()
    {
	//Carga de las texturas y animaciones
	Assets.load();
	
	// Pieza global de la pantalla, aqui se cargan todos los objetos del
	// juego
	world = new World(new Vector2(0, -1f), true);

	listPlayers = new ArrayList<Player>();

	batch = new SpriteBatch();

	// Create two identical sprites slightly offset from each other
	// vertically

	for (int index = 0; index < numSprites; index++)
	{
	    atlas = (index % 5);
	    Player player = new Player(world, atlas,10,0.1f,01.f,
		   100 * ((index % 2 == 0) ? 10 : -10), index * 100);
	    listPlayers.add(player);
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
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.setProjectionMatrix(camera.combined);
	batch.begin();

	// Step the physics simulation forward at a rate of 60hz
	world.step(1f / 60f, 6, 2);

	for (int index = 0; index < listPlayers.size(); index++)
	{
	    listPlayers.get(index).renderPlayer(batch, true);
	}

	batch.end();
    }

    @Override
    public void dispose()
    {
	world.dispose();
    }
}
