package org.example.asteroides.game;

import java.util.ArrayList;

import org.example.asteroides.ResumenPuntuacion;
import org.example.asteroides.game.graphics.Asteroid;
import org.example.asteroides.game.graphics.Ship;
import org.example.asteroides.utils.GameUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class AsteroidsGame extends Game implements ApplicationListener,
	GestureListener
{
    public static int puntuacion = 0;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private BitmapFont font3;
    private Texture background;
    private Ship nave;
    private ArrayList<Asteroid> asteroides;
    private Context context;
    private Intent intent;
    private int numAsteroids = 1;
    private int numFragmentos;
    
    public AsteroidsGame(Context context)
    {
	this.context = context;
	/* Para recoger los valores de la pantalla de preferencias */
	SharedPreferences pref = context.getSharedPreferences(
		"org.example.asteroides_preferences", Context.MODE_PRIVATE);
	numAsteroids = Integer.valueOf(pref.getString("asteroides", "5"));
	numFragmentos = Integer.valueOf(pref.getString("fragmentos", "3"));
    }

    public void finishGame()
    {
	if (intent == null)
	{
	    intent = new Intent(context, ResumenPuntuacion.class);
	    intent.putExtra("PUNTUACION", puntuacion);
	    context.startActivity(intent);
	}
    }

    @Override
    public void create()
    {
	// Carga de recursos
	Assets.load();

	puntuacion = 0;
	
	asteroides = new ArrayList<Asteroid>();

	for (int index = 1; index <= numAsteroids; index++)
	{
	    asteroides.add(new Asteroid(GameUtils.getPosX((Gdx.graphics
		    .getWidth() / 2)), GameUtils.getPosY((Gdx.graphics
		    .getHeight() / 2)), GameUtils.getFactor(), GameUtils
		    .getFactor(), 0.5f,numFragmentos,0));
	}

	nave = new Ship(context,0, 0, 5f);

	font = new BitmapFont();
	font.setColor(Color.WHITE);
	font.setScale(2f, 2f);

	font2 = new BitmapFont();
	font2.setColor(Color.GRAY);
	font2.setScale(2f, 2f);

	font3 = new BitmapFont();
	font3.setColor(Color.RED);
	font3.setScale(2f, 2f);

	Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

	//
	camera = new OrthographicCamera(Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());

	batch = new SpriteBatch();
	background = Assets.TEXTURE_BACKGROUND;
	
	Gdx.input.setInputProcessor(new GestureDetector(this));

    }

    @Override
    public void render()
    {
	camera.update();

	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.setProjectionMatrix(camera.combined);

	batch.begin();

	// Renderizar los srpites
	batch.draw(background, -(Gdx.graphics.getWidth() / 2),
		-(Gdx.graphics.getHeight() / 2));

	/* checkea las colisiones y añade los fragmentos */
	asteroides.addAll(nave.checkCollision(asteroides));

	nave.draw(batch, 0);

	for (Asteroid asteroid : asteroides)
	{
	    asteroid.draw(batch, 0);
	}

	font.draw(batch, "PosX:" + (int) nave.getSprite().getX() + " PosY:"
		+ (int) nave.getSprite().getY() + " Puntuaciones: "
		+ puntuacion, -Gdx.graphics.getWidth() / 2,
		Gdx.graphics.getHeight() / 2);

	if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
	{
	    font2.draw(batch, "AccelX: " + (int) Gdx.input.getAccelerometerX()
		    + " AccelY: " + (int) Gdx.input.getAccelerometerY()
		    + " AccelZ: " + (int) Gdx.input.getAccelerometerZ(),
		    -Gdx.graphics.getWidth() / 2,
		    Gdx.graphics.getHeight() / 2 - 50);
	}

	batch.end();

	//Eliminamos el asteroide de la lista una vez destruido
	
	/*
	for (Asteroid asteroid : asteroides)
	{
	    if(asteroid != null && asteroid.isExplosionFinish())
	    {
		asteroides.remove(asteroid);
	    }
	}
	*/
	
	if (nave.isExposionFinish())
	{
	    //Gdx.graphics.setContinuousRendering(false);
	    
	    //finishGame();
	}
    }

    @Override
    public void dispose()
    {
	batch.dispose();
    }

    @Override
    public void pause()
    {
	super.pause();

	// finishGame();
    }

    @Override
    public void resume()
    {
	super.resume();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
	nave.shoot(5f);

	return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
	// TODO Auto-generated method stub
	// camera.translate(deltaX, 0);
	// camera.update();
	return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
	Gdx.app.log("zoom", "initialDistance: " + initialDistance);
	Gdx.app.log("zoom", "distance: " + distance);
	Gdx.app.log("zoom", "diffs: " + (distance - initialDistance));

	if ((distance - initialDistance) > 0f)
	{
	    nave.setAceleration((distance - initialDistance) / 50f);
	} else
	{
	    nave.setAceleration(0.1f);
	}
	return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
	    Vector2 pointer1, Vector2 pointer2)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean panStop(float arg0, float arg1, int arg2, int arg3)
    {
	// TODO Auto-generated method stub
	return false;
    }
}
