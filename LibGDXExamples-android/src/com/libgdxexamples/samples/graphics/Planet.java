package com.libgdxexamples.samples.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.libgdxexamples.android.utils.Constants;

public class Planet extends Actor
{
    private ParticleEffect prototype;

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
    public Planet(World world, float density, float friction, float elasticity,
	    float posX, float posY)
    {
	if (prototype == null)
	{
	    prototype = new ParticleEffect();
	    prototype.load(Gdx.files.internal("data/effects/SuperFire.party"),
		    Gdx.files.internal("data"));

	    prototype.setPosition(Gdx.graphics.getWidth() / 2,
		    Gdx.graphics.getHeight() / 2);
	    prototype.start();

	}

	this.posX = posX;
	this.posY = posY;
	this.world = world;

	this.density = density;
	this.friction = friction;
	this.elasticity = elasticity;

	this.generatePlanet();
    }

    public void generatePlanet()
    {
	sprite = new Sprite(new Texture("data/moon.png"));
	sprite.setPosition(posX, posY);

	bodyDef = new BodyDef();
	bodyDef.type = BodyDef.BodyType.DynamicBody;
	bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2)
		/ Constants.PIXELS_TO_METERS,
		(sprite.getY() + sprite.getHeight() / 2)
			/ Constants.PIXELS_TO_METERS);

	body = world.createBody(bodyDef);

	// Both bodies have identical shape
	CircleShape shape = new CircleShape();
	shape.setRadius(sprite.getWidth() / 2 / Constants.PIXELS_TO_METERS);

	// Definición de las propiedades
	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = shape;
	fixtureDef.density = this.density;
	fixtureDef.restitution = this.elasticity;
	fixtureDef.friction = this.friction;

	body.createFixture(fixtureDef);
	shape.dispose();

	

    }

    public void renderPlanet(SpriteBatch batch, boolean isLoop)
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
	
	prototype.setPosition(sprite.getX() + (sprite.getWidth()/2), sprite.getY() + (sprite.getHeight()/2));
	prototype.draw(batch,0.05f);
	
	/*
	effect.setPosition(sprite.getX() + (sprite.getWidth()/2), sprite.getY() + (sprite.getHeight()/2));
	effect.draw(batch,0.05f);
	if (effect.isComplete())
	{
	    effect.free();
	}
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
}
