package com.libgdxexamples.samples;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Test1 extends Game implements ApplicationListener
{
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Animation animation;
    private float elapsedTime = 0;

    @Override
    public void create()
    {
	batch = new SpriteBatch();
	textureAtlas = new TextureAtlas(
		Gdx.files.internal("data/standing/pack.atlas"));
	animation = new Animation(1 / 05f, textureAtlas.getRegions());
    }

    @Override
    public void dispose()
    {
	batch.dispose();
	textureAtlas.dispose();
    }

    @Override
    public void render()
    {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

	batch.begin();
	// sprite.draw(batch);
	elapsedTime += Gdx.graphics.getDeltaTime();
	batch.draw(animation.getKeyFrame(elapsedTime, true), 200, 200);
	batch.end();
    }

    @Override
    public void resize(int width, int height)
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
    
    /*
    private SpriteBatch batch;
    //private TextureAtlas textureAtlas;
    private TextureRegion split[][];
    private Animation animation;
    private float elapsedTime = 0;
    private Texture texture;
    
    @Override
    public void create()
    {
	batch = new SpriteBatch();
	texture = new Texture(
		Gdx.files.internal("data/standing/pack.png"));
	split = new TextureRegion(texture).split(49, 71);
	animation = new Animation(1 / 50f, split[0][0],split[1][0]);
    }

    @Override
    public void dispose()
    {
	batch.dispose();
	texture.dispose();
    }

    @Override
    public void render()
    {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

	batch.begin();
	//sprite.draw(batch);
	elapsedTime += Gdx.graphics.getDeltaTime() + 1;
	
	System.out.println("Time: " + elapsedTime);
	
	batch.draw(animation.getKeyFrame(elapsedTime,true ), 100, 100);
	batch.end();
    }

    @Override
    public void resize(int width, int height)
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
    */
}

