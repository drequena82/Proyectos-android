package com.libgdxexamples.samples;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SceneTest implements ApplicationListener
{

    public class MyActor extends Actor
    {
	Texture texture = new Texture(Gdx.files.internal("data/standing/frame-1.png"));

	@Override
	public void draw(Batch batch, float alpha)
	{
	    batch.draw(texture, 0, 0);
	}
    }

    private Stage stage;

    @Override
    public void create()
    {
	stage = new Stage();
	//Gdx.graphics.getWidth(); Gdx.graphics.getHeight();

	MyActor myActor = new MyActor();
	stage.addActor(myActor);
    }

    @Override
    public void dispose()
    {
	stage.dispose();
    }

    @Override
    public void render()
    {
	Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
	stage.draw();
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

}
