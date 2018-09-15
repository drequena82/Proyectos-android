package com.libgdxexamples.samples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameTest extends ApplicationAdapter implements InputProcessor
{
    private SpriteBatch batch;
    private Sprite puntero;
    private Texture img;

    private OrthographicCamera camera;
    private BitmapFont font;
    private BitmapFont font2;

    boolean drawSprite = true;
    private float accelX;
    private float accelY;
    private float accelZ;
    
    private float azimuth;
    private float pitch;
    private float roll;
    
    private float posX;
    private float posY;
    
    @Override
    public void create()
    {

	batch = new SpriteBatch();
	
	img = new Texture("data/miraBlack.png");
	puntero = new Sprite(img);

	puntero.setPosition(-puntero.getWidth() / 2, -puntero.getHeight() / 2);

	Gdx.input.setInputProcessor(this);

	font = new BitmapFont();
	font.setColor(Color.BLACK);
	font.setScale(2f, 2f);

	font2 = new BitmapFont();
	font2.setColor(Color.GRAY);
	font2.setScale(2f, 2f);

	camera = new OrthographicCamera(Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
    }

    @Override
    public void render()
    {
	camera.update();

	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.setProjectionMatrix(camera.combined);

	batch.begin();

	if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
	{

	    accelX = Gdx.input.getAccelerometerX();
	    accelY = Gdx.input.getAccelerometerY();
	    accelZ = Gdx.input.getAccelerometerZ();

	    font2.draw(
		    batch,
		    "Accelerometer: posX:" + Math.round(accelX) + " posY:"
			    + Math.round(accelY) + " posZ:"
			    + Math.round(accelZ), -Gdx.graphics.getWidth() / 2,
		    (Gdx.graphics.getHeight() / 2) - 50);

	}

	if (Gdx.input.isPeripheralAvailable(Peripheral.Compass))
	{
	    azimuth = Gdx.input.getAzimuth();
	    pitch = Gdx.input.getPitch();
	    roll = Gdx.input.getRoll();

	    font.draw(batch, "Compass: az:" + Math.round(azimuth) + " pt:"
		    + Math.round(pitch) + " rl:" + Math.round(roll),
		    -Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

	}

	posX = accelY * 100f;
	posY = accelX * 100f;
	
	batch.draw(puntero,  posX, posY,
		    puntero.getOriginX(), puntero.getOriginY(),
		    puntero.getWidth(), puntero.getHeight(), puntero.getScaleX(),
		    puntero.getScaleY(), puntero.getRotation());
	
	batch.end();

    }

    @Override
    public void dispose()
    {
	img.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
	return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
	return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
	return false;
    }

    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
	if(screenX < (Gdx.graphics.getWidth()/2f))
	{
	    //Lanzar disparo izquierdo
	}else if(screenX > (Gdx.graphics.getWidth()/2f))
	{
	  //Lanzar disparo derecho
	}
	
	return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
	return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
	return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
	return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
	return false;
    }
}
