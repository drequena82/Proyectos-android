package com.libgdxexamples.samples.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor
{
    private final float PIXELS_TO_METERS = 100f;
    private World world;
    private Sprite sprite;
    private Body body;
    private BodyDef bodyDef;
    private TextureAtlas textureAtlas;
    private Animation animationPlayer;
    private float elapsedTime = 0;
    private String atlasPath;
    private float velocity;
    private float posX;
    private float posY;
    private float originX;
    private float originY;
    private float width;
    private float height;
    private float scaleX;
    private float scaleY;
    private float rotation;

    public Player(World world, SpriteBatch batch)
    {
	posX = 0;
	posY = 0;

	atlasPath = "data/standing/pack.atlas";
	velocity = 1 / 05f;

	textureAtlas = new TextureAtlas(Gdx.files.internal(atlasPath));
	animationPlayer = new Animation(velocity, textureAtlas.getRegions());
	
	this.generatePlayer();
    }

    public void generatePlayer()
    {
	Sprite sprite = textureAtlas.createSprites().get(0);
	sprite.setPosition(posX, posY);

	bodyDef = new BodyDef();
	bodyDef.type = BodyDef.BodyType.DynamicBody;
	bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2)
		/ PIXELS_TO_METERS, (sprite.getY() + sprite.getHeight() / 2)
		/ PIXELS_TO_METERS);

	body = world.createBody(bodyDef);

	// Both bodies have identical shape
	PolygonShape shape = new PolygonShape();
	shape.setAsBox(sprite.getWidth() / 2 / PIXELS_TO_METERS,
		sprite.getHeight() / 2 / PIXELS_TO_METERS);

	// Sprite1
	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = shape;
	fixtureDef.density = 0.1f;
	fixtureDef.restitution = 0.5f;

	body.createFixture(fixtureDef);
	shape.dispose();
    }
    public void drawPlayer()
    {
	
    }
    
    
    public void changeAtlas(int pos)
    {
	switch (pos)
	{
	case 1:
	    atlasPath = "data/run/pack.atlas";
	    velocity = 1 / 05f;
	    break;
	case 2:
	    atlasPath = "data/jump/pack.atlas";
	    velocity = 1 / 05f;
	    break;
	case 3:
	    atlasPath = "data/sliding/pack.atlas";
	    velocity = 1 / 05f;
	    break;
	case 4:
	    atlasPath = "data/faint/pack.atlas";
	    velocity = 1 / 05f;
	    break;
	case 5:
	    atlasPath = "data/dizzy/pack.atlas";
	    velocity = 1 / 05f;
	    break;
	default:
	    atlasPath = "data/standing/pack.atlas";
	    velocity = 1 / 05f;
	}

	textureAtlas = new TextureAtlas(Gdx.files.internal(atlasPath));
	animationPlayer = new Animation(velocity, textureAtlas.getRegions());
    }

    public void disposePlayer()
    {
	textureAtlas.dispose();
    }

    public void setPosition(float posX, float posY)
    {
	this.posX = posX;
	this.posY = posY;
    }

    public void renderPlayer(boolean isLoop)
    {
	/*
	elapsedTime += Gdx.graphics.getDeltaTime();
	batch.draw(animationPlayer.getKeyFrame(elapsedTime, isLoop), posX, posY,
		originX, originY, scaleX, scaleY, width, height, rotation);
		*/
    }

    public TextureAtlas getTextureAtlas()
    {
	return textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas textureAtlas)
    {
	this.textureAtlas = textureAtlas;
    }

    public float getVelocity()
    {
	return velocity;
    }

    public void setVelocity(float velocity)
    {
	this.velocity = velocity;
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

    public float getOriginX()
    {
	return originX;
    }

    public void setOriginX(float originX)
    {
	this.originX = originX;
    }

    public float getOriginY()
    {
	return originY;
    }

    public void setOriginY(float originY)
    {
	this.originY = originY;
    }

    public float getWidth()
    {
	return width;
    }

    public void setWidth(float width)
    {
	this.width = width;
    }

    public float getHeight()
    {
	return height;
    }

    public void setHeight(float height)
    {
	this.height = height;
    }

    public float getScaleX()
    {
	return scaleX;
    }

    public void setScaleX(float scaleX)
    {
	this.scaleX = scaleX;
    }

    public float getScaleY()
    {
	return scaleY;
    }

    public void setScaleY(float scaleY)
    {
	this.scaleY = scaleY;
    }

    public float getRotation()
    {
	return rotation;
    }

    public void setRotation(float rotation)
    {
	this.rotation = rotation;
    }
}
