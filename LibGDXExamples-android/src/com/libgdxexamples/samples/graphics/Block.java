package com.libgdxexamples.samples.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.libgdxexamples.android.utils.Assets;
import com.libgdxexamples.android.utils.Constants;

public class Block extends Actor
{
    // private ParticleEffect prototype;

    private World world;
    private Sprite sprite;

    private Body body;
    private BodyDef bodyDef;
    private float velocity;
    private float posX;
    private float posY;
    private float density;
    private float friction;
    private float elasticity;
    private int colour;

    /**
     * Constuctor
     * 
     * @param world
     * @param atlas
     * @param density
     * @param friction
     * @param elasticity
     * @param posX
     * @param posY
     */
    public Block(World world, float density, float friction, float elasticity,
	    float posX, float posY, int colour)
    {
	/*
	 * if (prototype == null) { prototype = new ParticleEffect();
	 * prototype.load(Gdx.files.internal("data/effects/SuperFire.party"),
	 * Gdx.files.internal("data"));
	 * 
	 * prototype.setPosition(Gdx.graphics.getWidth() / 2,
	 * Gdx.graphics.getHeight() / 2); prototype.start();
	 * 
	 * }
	 */
	this.posX = posX;
	this.posY = posY;
	this.world = world;

	this.density = density;
	this.friction = friction;
	this.elasticity = elasticity;
	this.colour = colour;

	this.generateBlock();
    }

    public void generateBlock()
    {
	switch (colour)
	{
	case 0:
	    sprite = new Sprite(Assets.BLUE_BLOCK);
	    break;
	case 1:
	    sprite = new Sprite(Assets.DARK_BLOCK);
	    break;
	case 2:
	    sprite = new Sprite(Assets.GRAY_BLOCK);
	    break;
	case 3:
	    sprite = new Sprite(Assets.GREEN_BLOCK);
	    break;
	case 4:
	    sprite = new Sprite(Assets.LBLUE_BLOCK);
	    break;
	case 5:
	    sprite = new Sprite(Assets.ORANGE_BLOCK);
	    break;
	case 6:
	    sprite = new Sprite(Assets.PINK_BLOCK);
	    break;
	case 7:
	    sprite = new Sprite(Assets.PURPLE_BLOCK);
	    break;
	case 8:
	    sprite = new Sprite(Assets.RED2_BLOCK);
	    break;
	case 9:
	    sprite = new Sprite(Assets.RED_BLOCK);
	    break;
	case 10:
	    sprite = new Sprite(Assets.WHITE_BLOCK);
	    break;
	}

	sprite.setPosition(posX, posY);

	bodyDef = new BodyDef();
	bodyDef.type = BodyDef.BodyType.DynamicBody;
	bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2)
		/ Constants.PIXELS_TO_METERS,
		(sprite.getY() + sprite.getHeight() / 2)
			/ Constants.PIXELS_TO_METERS);

	body = world.createBody(bodyDef);

	// Both bodies have identical shape
	PolygonShape shape = new PolygonShape();
	shape.setAsBox(sprite.getWidth() / 2 / Constants.PIXELS_TO_METERS,
		sprite.getHeight() / 2 / Constants.PIXELS_TO_METERS);
	// Definición de las propiedades
	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = shape;
	fixtureDef.density = this.density;
	fixtureDef.restitution = this.elasticity;
	fixtureDef.friction = this.friction;

	body.createFixture(fixtureDef);
	shape.dispose();

    }

    public void renderBlock(SpriteBatch batch, boolean isLoop)
    {
	sprite.setPosition(
		(body.getPosition().x * Constants.PIXELS_TO_METERS)
			- sprite.getWidth() / 2,
		(body.getPosition().y * Constants.PIXELS_TO_METERS)
			- sprite.getHeight() / 2);

	sprite.setRotation((float) Math.toDegrees(body.getAngle()));

	batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
		sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(),
		sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
	/*
	 * prototype.setPosition(sprite.getX() + (sprite.getWidth()/2),
	 * sprite.getY() + (sprite.getHeight()/2)); prototype.draw(batch,0.05f);
	 */
	/*
	 * effect.setPosition(sprite.getX() + (sprite.getWidth()/2),
	 * sprite.getY() + (sprite.getHeight()/2)); effect.draw(batch,0.05f); if
	 * (effect.isComplete()) { effect.free(); }
	 */
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

    public Sprite getSprite()
    {
	return sprite;
    }

    public void setSprite(Sprite sprite)
    {
	this.sprite = sprite;
    }

    public Body getBody()
    {
	return body;
    }

    public void setBody(Body body)
    {
	this.body = body;
    }

    public BodyDef getBodyDef()
    {
	return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef)
    {
	this.bodyDef = bodyDef;
    }

    public float getDensity()
    {
	return density;
    }

    public void setDensity(float density)
    {
	this.density = density;
    }

    public float getFriction()
    {
	return friction;
    }

    public void setFriction(float friction)
    {
	this.friction = friction;
    }

    public float getElasticity()
    {
	return elasticity;
    }

    public void setElasticity(float elasticity)
    {
	this.elasticity = elasticity;
    }

    public int getColour()
    {
	return colour;
    }

    public void setColour(int colour)
    {
	this.colour = colour;
    }
}
