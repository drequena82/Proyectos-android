package org.example.asteroides.game.graphics;

import java.util.ArrayList;

import org.example.asteroides.R;
import org.example.asteroides.game.Assets;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

public class Ship extends Actor
{
    private Sprite sprite;
    private float aceleration;
    private float accelY;
    private boolean isDestroyed;
    private boolean isExposionFinish;
    private float collisionRadius;
    private float posYant;
    private float posXant;
    private float elapsedTimeExplosion = 0;
    private Animation animationExplosion;
    private float rotation;
    private ArrayList<Misil> misiles;
    private SoundPool soundPool;
    private int idDisparo;
    private int idExplosion;
    private Context context;
    
    @SuppressWarnings("deprecation")
    public Ship(Context context,float x, float y, float aceleration)
    {
	this.context = context;
	sprite = Assets.SPRITE_SHIP;
	sprite.setRotation(270);
	rotation = sprite.getRotation();
	sprite.setPosition(x, y);
	collisionRadius = sprite.getWidth() / 2f;
	this.aceleration = aceleration;
	animationExplosion = Assets.ANIMATION_SHIP_EXPLOSION;
	misiles = new ArrayList<Misil>();
	
	/* Sonidos */
	soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	idDisparo = soundPool.load(context, R.raw.disparo, 0);
	idExplosion = soundPool.load(context, R.raw.explosion, 0);
    }

    public ArrayList<Asteroid> checkCollision(ArrayList<Asteroid> asteroides)
    {
	ArrayList<Asteroid> fragments = new ArrayList<Asteroid>();
	if (!isDestroyed)
	{
	    for (Asteroid asteroide : asteroides)
	    {
		if (!asteroide.isDestroyed())
		{
		    if (

		    ((sprite.getX() + sprite.getWidth()) >= asteroide
			    .getSprite().getX())
			    && ((sprite.getX() + sprite.getWidth()) <= (asteroide
				    .getSprite().getX() + asteroide.getSprite()
				    .getWidth()))
			    && ((sprite.getY() + sprite.getHeight()) >= asteroide
				    .getSprite().getY())
			    && ((sprite.getY()) <= (asteroide
				    .getSprite().getY() + asteroide.getSprite()
				    .getHeight()))

		    )
		    {
			
			isDestroyed = true;
			asteroide.setDestroyed(true);
			
			soundPool.play(idExplosion, 1, 1, 0, 0, 1);
			
		    }
		}
	    }

	    for (Misil misil : misiles)
	    {
		if (misil.isAlive())
		{
		    if (
			    ((sprite.getX() + sprite.getWidth()) >= misil
			    .getSprite().getX())
			    && ((sprite.getX() + sprite.getWidth()) <= (misil
				    .getSprite().getX() + misil.getSprite()
				    .getWidth()))
			    && ((sprite.getY() + sprite.getHeight()) >= misil
				    .getSprite().getY())
			    && ((sprite.getY()) <= (misil
				    .getSprite().getY() + misil.getSprite()
				    .getHeight()))
		    )
		    {
			isDestroyed = true;
			misil.setAlive(false);
			
			soundPool.play(idExplosion, 1, 1, 0, 0, 1);
		    }
		}
	    }

	    if (misiles != null && misiles.size() > 0)
	    {
		for (Misil misil : misiles)
		{
		    if (misil.isAlive())
		    {
			fragments.addAll(misil.checkCollision(asteroides));
		    }
		}
	    }
	}
	
	return fragments;
    }
    
    public void shoot(float aceleration)
    {
	Gdx.app.log("shoot", "ship width: " + sprite.getWidth() + " posX: "
		+ sprite.getX());
	Gdx.app.log("shoot", "ship height: " + sprite.getHeight() + " posY: "
		+ sprite.getY());

	// float x = sprite.getWidth() + sprite.getX();
	// float y = sprite.getHeight() / 2 + sprite.getY();

	float x = sprite.getX() + sprite.getWidth() + 15f;
	float y = sprite.getY() + sprite.getHeight()/2f - 10f;
	float speed = this.aceleration + aceleration;

	// Lanzamos el misil justo delante de la nave.
	misiles.add(new Misil(context,x, y, speed, sprite.getRotation()));
	
	soundPool.play(idDisparo, 1, 1, 0, 0, 1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	if (!isDestroyed)
	{
	    // Restablecemos la posicion para que no se salga del screen
	    if (sprite.getX() > (Gdx.graphics.getWidth() / 2))
	    {
		sprite.setX(-(Gdx.graphics.getWidth() / 2));
	    } else if (sprite.getX() < -(Gdx.graphics.getWidth() / 2))
	    {
		sprite.setX(Gdx.graphics.getWidth() / 2);
	    }

	    if (sprite.getY() > (Gdx.graphics.getHeight() / 2))
	    {
		sprite.setY(-(Gdx.graphics.getHeight() / 2));
	    } else if (sprite.getY() < -(Gdx.graphics.getHeight() / 2))
	    {
		sprite.setY(Gdx.graphics.getHeight() / 2);
	    }

	    if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
	    {
		Gdx.input.getAccelerometerX();
		accelY = Gdx.input.getAccelerometerY();
		// float accelZ = Gdx.input.getAccelerometerZ();
	    }

	    sprite.setRotation(rotation + (accelY * aceleration));

	    sprite.setBounds(
		    sprite.getX()
			    + ((float) Math.sin(Math.toRadians(360 - sprite
				    .getRotation())) * aceleration),
		    sprite.getY()
			    + ((float) Math.cos(Math.toRadians(360 - sprite
				    .getRotation())) * aceleration),
		    sprite.getWidth(), sprite.getHeight());

	    batch.draw(sprite, sprite.getX(), sprite.getY(),
		    sprite.getOriginX(), sprite.getOriginY(),
		    sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
		    sprite.getScaleY(), sprite.getRotation());

	    posYant = sprite.getY();
	    posXant = sprite.getX();

	    // Render del misil
	    if (misiles != null && misiles.size() > 0)
	    {
		for (int index = 0; index < misiles.size(); index++)
		{
		    misiles.get(index).draw(batch, parentAlpha);
		}
	    }
	} else
	{
	    // Explosión del meteorito
	    elapsedTimeExplosion += Gdx.graphics.getDeltaTime();

	    this.sprite = new Sprite(this.animationExplosion.getKeyFrame(
		    elapsedTimeExplosion, false));
	    if (sprite != null)
	    {
		this.sprite.setPosition(this.posXant, posYant);

		batch.draw(this.sprite, sprite.getX(), sprite.getY(),
			sprite.getOriginX(), sprite.getOriginY(),
			sprite.getWidth(), sprite.getHeight(),
			sprite.getScaleX(), sprite.getScaleY(),
			sprite.getRotation());
	    }
	    if (this.animationExplosion
		    .isAnimationFinished(elapsedTimeExplosion))
	    {
		isExposionFinish = true;
	    }
	}
    }

    @Override
    public void addAction(Action action)
    {
	// TODO Auto-generated method stub
	super.addAction(action);
    }

    @Override
    public void act(float arg0)
    {
	// TODO Auto-generated method stub
	super.act(arg0);
    }

    @Override
    public boolean fire(Event arg0)
    {
	// TODO Auto-generated method stub
	return super.fire(arg0);
    }

    public Sprite getSprite()
    {
	return sprite;
    }

    public void setSprite(Sprite sprite)
    {
	this.sprite = sprite;
    }

    public float getAceleration()
    {
	return aceleration;
    }

    public void setAceleration(float aceleration)
    {
	this.aceleration = aceleration;
    }

    public boolean isDestroyed()
    {
	return isDestroyed;
    }

    public void setDestroyed(boolean isDestroyed)
    {
	this.isDestroyed = isDestroyed;
    }

    public boolean isExposionFinish()
    {
	return isExposionFinish;
    }

    public void setExposionFinish(boolean isExposionFinish)
    {
	this.isExposionFinish = isExposionFinish;
    }

    public float getCollisionRadius()
    {
	return collisionRadius;
    }

    public void setCollisionRadius(float collisionRadius)
    {
	this.collisionRadius = collisionRadius;
    }

}
