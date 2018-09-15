package com.libgdxexamples.samples.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.libgdxexamples.android.utils.Assets;

public class Misil extends Actor
{
    private Sprite sprite;
    private Animation animationExplosion;
    private float elapsedTimeExplosion = 0;
    private float aceleration;

    private float posX;
    private float posY;
    
    private float finishPosX;
    private float finishPosY;
    
    private float rotation;

    private boolean isAlive = true;

    // Tiempo de duración del misil
    private int duration = 3;
    private long iniTime;

    public Misil(float x, float y, float aceleration, boolean isRight)
    {
	sprite = new Sprite(Assets.TEXTURE_MISIL);
	animationExplosion = Assets.ANIMATION_EXPLOSION;

	this.aceleration = aceleration;
	if(isRight)
	{
	    this.posX = -(Gdx.graphics.getWidth()/2f);
	    this.posY = -(Gdx.graphics.getHeight()/2f);
	}else
	{
	    this.posX = (Gdx.graphics.getWidth()/2f);
	    this.posY = -(Gdx.graphics.getHeight()/2f);
	}
	
	sprite.setPosition(posX, posY);
	
	this.finishPosX = x;
	this.finishPosY = y;
    }

    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	if (iniTime == 0l)
	{
	    iniTime = System.currentTimeMillis();
	}
	
	if(posX == finishPosX && posY == finishPosY)
	{
	    isAlive = false;
	}
	
	if (isAlive
		&& ((System.currentTimeMillis() - iniTime) <= (duration * 1000)))
	{
	    
	    sprite.setPosition(posX, posY);
	    /*
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
	     */
	    
	    sprite.setPosition(
		    sprite.getX()
			    + (float) (Math.sin(Math.toRadians(360 - rotation)) * aceleration),
		    sprite.getY()
			    + (float) (Math.cos(Math.toRadians(360 - rotation)) * aceleration));

	    posX = sprite.getX();
	    posY = sprite.getY();

	    sprite.setRotation(rotation);

	    batch.draw(this.sprite, sprite.getX(), sprite.getY(),
		    sprite.getOriginX(), sprite.getOriginY(),
		    sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
		    sprite.getScaleY(), sprite.getRotation());
	} else
	{
	    // Explosión del misil
	    elapsedTimeExplosion += Gdx.graphics.getDeltaTime();

	    //Gdx.app.log("Misil", "elapsedTime: " + elapsedTimeExplosion);

	    this.sprite = new Sprite(this.animationExplosion.getKeyFrame(
		    elapsedTimeExplosion, false));
	    if (sprite != null)
	    {
		this.sprite.setPosition(this.posX, this.posY);

		batch.draw(this.sprite, sprite.getX(), sprite.getY(),
			sprite.getOriginX(), sprite.getOriginY(),
			sprite.getWidth(), sprite.getHeight(),
			sprite.getScaleX(), sprite.getScaleY(),
			sprite.getRotation());
	    } else
	    {
		isAlive = false;
	    }

	}
    }

    public float getRotation()
    {
	return rotation;
    }

    public void setRotation(float rotation)
    {
	this.rotation = rotation;
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

    public float getPosX()
    {
	return posX;
    }

    public void setPosX(float posX)
    {
	this.posX = posX;
    }

    public float getPosY()
    {
	return posY;
    }

    public void setPosY(float posY)
    {
	this.posY = posY;
    }

    public boolean isAlive()
    {
	return isAlive;
    }

    public void setAlive(boolean isAlive)
    {
	this.isAlive = isAlive;
    }

    public int getDuration()
    {
	return duration;
    }

    public void setDuration(int duration)
    {
	this.duration = duration;
    }

    public long getIniTime()
    {
	return iniTime;
    }

    public void setIniTime(long iniTime)
    {
	this.iniTime = iniTime;
    }
}
