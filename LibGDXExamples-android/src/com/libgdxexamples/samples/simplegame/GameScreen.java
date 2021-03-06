package com.libgdxexamples.samples.simplegame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen
{
    final Drop game;

    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    private BitmapFont font2;
    boolean available = false;

    public GameScreen(final Drop gam)
    {
	this.game = gam;

	// Acelerometer
	available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

	// load the images for the droplet and the bucket, 64x64 pixels each
	dropImage = new Texture(Gdx.files.internal("data/droplet.png"));
	bucketImage = new Texture(Gdx.files.internal("data/bucket.png"));

	// load the drop sound effect and the rain background "music"
	dropSound = Gdx.audio.newSound(Gdx.files.internal("data/drop.wav"));
	rainMusic = Gdx.audio.newMusic(Gdx.files.internal("data/rain.mp3"));
	rainMusic.setLooping(true);

	// create the camera and the SpriteBatch
	camera = new OrthographicCamera();
	camera.setToOrtho(false, 800, 480);

	// create a Rectangle to logically represent the bucket
	bucket = new Rectangle();
	bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
	bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
		       // the bottom screen edge
	bucket.width = 64;
	bucket.height = 64;

	// create the raindrops array and spawn the first raindrop
	raindrops = new Array<Rectangle>();
	spawnRaindrop();

	font2 = new BitmapFont();
	font2.setColor(Color.GRAY);
	font2.setScale(2f, 2f);

    }

    private void spawnRaindrop()
    {
	Rectangle raindrop = new Rectangle();
	raindrop.x = MathUtils.random(0, 800 - 64);
	raindrop.y = 480;
	raindrop.width = 64;
	raindrop.height = 64;
	raindrops.add(raindrop);
	lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta)
    {
	// clear the screen with a dark blue color. The
	// arguments to glClearColor are the red, green
	// blue and alpha component in the range [0,1]
	// of the color to be used to clear the screen.
	Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	// tell the camera to update its matrices.
	camera.update();

	// tell the SpriteBatch to render in the
	// coordinate system specified by the camera.
	game.batch.setProjectionMatrix(camera.combined);

	// begin a new batch and draw the bucket and
	// all drops
	game.batch.begin();
	game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
	game.batch.draw(bucketImage, bucket.x, bucket.y);
	for (Rectangle raindrop : raindrops)
	{
	    game.batch.draw(dropImage, raindrop.x, raindrop.y);
	}

	/* Control con el acelerometro */

	float accelX = 0;
	float accelY = 0;
	float accelZ = 0;

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



	    game.font2.draw(
		    game.batch,
		    "Accelerometer: posX:" + Math.round(accelX) + " posY:"
			    + Math.round(accelY) + " posZ:"
			    + Math.round(accelZ), -Gdx.graphics.getWidth() / 2,
		    (Gdx.graphics.getHeight() / 2) - 50);

	}

	game.batch.end();

	//Control con el sensor
	bucket.x += accelY * 100 * Gdx.graphics.getDeltaTime();

	// process user input
	if (Gdx.input.isTouched())
	{
	    Vector3 touchPos = new Vector3();
	    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    camera.unproject(touchPos);
	    bucket.x = touchPos.x - 64 / 2;
	}

	if (Gdx.input.isKeyPressed(Keys.LEFT))
	    bucket.x -= 200 * Gdx.graphics.getDeltaTime();
	if (Gdx.input.isKeyPressed(Keys.RIGHT))
	    bucket.x += 200 * Gdx.graphics.getDeltaTime();

	// make sure the bucket stays within the screen bounds
	if (bucket.x < 0)
	    bucket.x = 0;
	if (bucket.x > 800 - 64)
	    bucket.x = 800 - 64;

	// check if we need to create a new raindrop
	if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
	    spawnRaindrop();

	// move the raindrops, remove any that are beneath the bottom edge of
	// the screen or that hit the bucket. In the later case we increase the
	// value our drops counter and add a sound effect.
	Iterator<Rectangle> iter = raindrops.iterator();
	while (iter.hasNext())
	{
	    Rectangle raindrop = iter.next();
	    raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
	    if (raindrop.y + 64 < 0)
		iter.remove();
	    if (raindrop.overlaps(bucket))
	    {
		dropsGathered++;
		dropSound.play();
		iter.remove();
	    }
	}
    }

    @Override
    public void resize(int width, int height)
    {
    }

    @Override
    public void show()
    {
	// start the playback of the background music
	// when the screen is shown
	rainMusic.play();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
	dropImage.dispose();
	bucketImage.dispose();
	dropSound.dispose();
	rainMusic.dispose();
    }

}
