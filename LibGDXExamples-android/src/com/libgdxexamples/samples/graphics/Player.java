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
import com.libgdxexamples.android.utils.Assets;
import com.libgdxexamples.android.utils.Constants;

public class Player extends Actor
{
    private World world;
    private Sprite sprite;
    private Body body;
    private BodyDef bodyDef;
    private Animation animationPlayer;
    private TextureAtlas textureAtlas;
    private float elapsedTime = 0;
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
    public Player(World world, int atlas, float density, float friction,
	    float elasticity, float posX, float posY)
    {
	this.posX = posX;
	this.posY = posY;
	this.world = world;

	this.density = density;
	this.friction = friction;
	this.elasticity = elasticity;

	this.changeAtlas(atlas);
    }

    public void generatePlayer()
    {
	sprite = textureAtlas.createSprites().get(0);
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

    public void changeAtlas(int pos)
    {
	switch (pos)
	{
	case Constants.ATLAS_RUN:
	    animationPlayer = Assets.TEXTURE_RUN_ANIMATION;
	    textureAtlas = Assets.TEXTURE_RUN;
	    break;
	case Constants.ATLAS_JUMP:
	    animationPlayer = Assets.TEXTURE_JUMP_ANIMATION;
	    textureAtlas = Assets.TEXTURE_JUMP;
	    break;
	case Constants.ATLAS_SLIDING:
	    animationPlayer = Assets.TEXTURE_SLIDING_ANIMATION;
	    textureAtlas = Assets.TEXTURE_SLIDING;
	    break;
	case Constants.ATLAS_FAINT:
	    animationPlayer = Assets.TEXTURE_FAINT_ANIMATION;
	    textureAtlas = Assets.TEXTURE_FAINT;
	    break;
	case Constants.ATLAS_DIZZY:
	    animationPlayer = Assets.TEXTURE_DIZZY_ANIMATION;
	    textureAtlas = Assets.TEXTURE_DIZZY;
	    break;
	default:
	    animationPlayer = Assets.TEXTURE_STANDING_ANIMATION;
	    textureAtlas = Assets.TEXTURE_STANDING;
	}

	this.generatePlayer();
    }

    public void disposePlayer()
    {
	textureAtlas.dispose();
    }

    public void renderPlayer(SpriteBatch batch, boolean isLoop)
    {
	sprite.setPosition(
		(body.getPosition().x * Constants.PIXELS_TO_METERS)
			- sprite.getWidth() / 2,
		(body.getPosition().y * Constants.PIXELS_TO_METERS)
			- sprite.getHeight() / 2);

	sprite.setRotation((float) Math.toDegrees(body.getAngle()));

	elapsedTime += Gdx.graphics.getDeltaTime();
	batch.draw(animationPlayer.getKeyFrame(elapsedTime, isLoop),
		sprite.getX(), sprite.getY(), sprite.getOriginX(),
		sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(),
		sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());

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
}
