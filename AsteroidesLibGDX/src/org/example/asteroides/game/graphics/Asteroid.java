package org.example.asteroides.game.graphics;

import java.util.ArrayList;

import org.example.asteroides.game.Assets;
import org.example.asteroides.utils.GameUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Asteroid extends Actor
{
    private Sprite sprite;
    private Animation animation;
    private Animation animationExplosion;
    private TextureAtlas texture;
    private float elapsedTime = 0;
    private float elapsedTimeExplosion = 0;
    private float factorX;
    private float factorY;
    private float aceleration;
    private boolean isDestroyed;
    private float posX;
    private float posY;
    private float rotation; 
    private float collisionRadius;
    private int type;
    private int fragments;
    private boolean isExplosionFinish;
    
    public Asteroid(float x, float y, float factorX, float factorY,
	    float aceleration,int fragments,int type)
    {
	this.fragments = fragments;
	this.rotation = (float)Math.random() * 10f;
	this.type = type;
	
	if(type == 0)
	{
	    this.texture = Assets.TEXTURE_ASTEROID;
	    this.animation = Assets.ANIMATION_ASTEROID;
	    this.animationExplosion = Assets.ANIMATION_SHIP_EXPLOSION;
	}else if(type == 1)
	{
	    this.texture = Assets.TEXTURE_MEDASTEROID;
	    this.animation = Assets.ANIMATION_MEDASTEROID;
	    this.animationExplosion = Assets.ANIMATION_SHIP_EXPLOSION;
	}else if(type == 2)
	{
	    this.texture = Assets.TEXTURE_MINIASTEROID;
	    this.animation = Assets.ANIMATION_MINIASTEROID;
	    this.animationExplosion = Assets.ANIMATION_EXPLOSION;
	}

	
	this.sprite = texture.createSprites().get(0);
	this.sprite.setPosition(x, y);
	
	this.collisionRadius = this.sprite.getHeight()/2f;
		
	this.posX = this.sprite.getX();
	this.posY = this.sprite.getY();

	this.factorX = factorX;
	this.factorY = factorY;
	this.aceleration = aceleration;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	if (!isDestroyed)
	{
	    elapsedTime += Gdx.graphics.getDeltaTime();

	    sprite.setBounds(this.sprite.getX()
		    + (factorX * aceleration), this.sprite.getY()
		    + (factorY * aceleration),sprite.getWidth(),sprite.getHeight());

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

	    posX = sprite.getX();
	    posY = sprite.getY();

	    sprite = new Sprite(animation.getKeyFrame(elapsedTime,
		    true));
	    sprite.setPosition(posX,posY);
	    sprite.setRotation(rotation);
	    
	    batch.draw(sprite, sprite.getX(), sprite.getY(),
		    sprite.getOriginX(), sprite.getOriginY(),
		    sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
		    sprite.getScaleY(), sprite.getRotation());
	} else
	{
	    // Explosión del meteorito
	    elapsedTimeExplosion += Gdx.graphics.getDeltaTime();

	    sprite = new Sprite(animationExplosion.getKeyFrame(
		    elapsedTimeExplosion, false));
	    if (sprite != null)
	    {
		sprite.setBounds(this.posX, this.posY,sprite.getWidth(),sprite.getHeight());

		batch.draw(this.sprite, sprite.getX(), sprite.getY(),
			sprite.getOriginX(), sprite.getOriginY(),
			sprite.getWidth(), sprite.getHeight(),
			sprite.getScaleX(), sprite.getScaleY(),
			sprite.getRotation());
	    }
	    
	    if (this.animationExplosion
		    .isAnimationFinished(elapsedTimeExplosion))
	    {
		isExplosionFinish = true;
	    }
	}
    }

    public ArrayList<Asteroid> createFragments(int type)
    {
	ArrayList<Asteroid> fragments = new ArrayList<Asteroid>();
	float factorX = 1f;
	float factorY = 1f;
	
	for(int index = 0;index < this.fragments;index++)
	{
	    factorX = (float) Math.random() * GameUtils.getFactor();
	    factorY = (float) Math.random() * GameUtils.getFactor();
		
	    fragments.add(new Asteroid(posX + sprite.getWidth()/2f, posY + sprite.getHeight()/2f, 
		    factorX, factorY, aceleration, this.fragments, type));
	}
	
	return fragments;
    }
    
    public Sprite getSprite()
    {
        return sprite;
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
    }

    public float getFactorX()
    {
        return factorX;
    }

    public void setFactorX(float factorX)
    {
        this.factorX = factorX;
    }

    public float getFactorY()
    {
        return factorY;
    }

    public void setFactorY(float factorY)
    {
        this.factorY = factorY;
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

    public float getCollisionRadius()
    {
        return collisionRadius;
    }

    public void setCollisionRadius(float collisionRadius)
    {
        this.collisionRadius = collisionRadius;
    }

    public int getFragments()
    {
        return fragments;
    }

    public void setFragments(int fragments)
    {
        this.fragments = fragments;
    }

    public boolean isExplosionFinish()
    {
        return isExplosionFinish;
    }

    public void setExplosionFinish(boolean isExplosionFinish)
    {
        this.isExplosionFinish = isExplosionFinish;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
