package com.libgdxexamples.samples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectsTutorial extends ApplicationAdapter
{

    private SpriteBatch batch;
    private ParticleEffect prototype;
    private ParticleEffectPool pool;
    private Array<PooledEffect> effects;

    @Override
    public void create()
    {
	batch = new SpriteBatch();

	prototype = new ParticleEffect();
	prototype.load(Gdx.files.internal("data/effects/Purple.party"),
		Gdx.files.internal("data"));
	prototype.setPosition(Gdx.graphics.getWidth() / 2,
		Gdx.graphics.getHeight() / 2);
	prototype.start();

	pool = new ParticleEffectPool(prototype, 0, 70);
	effects = new Array<PooledEffect>();

	Gdx.input.setInputProcessor(new InputProcessor()
	{

	    @Override
	    public boolean touchUp(int arg0, int arg1, int arg2, int arg3)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean touchDragged(int screenX, int screenY, int pointer)
	    {
		PooledEffect effect = pool.obtain();
		effect.setPosition(screenX, -screenY + Gdx.graphics.getHeight());
		effects.add(effect);
		return true;
	    }

	    @Override
	    public boolean touchDown(int arg0, int arg1, int arg2, int arg3)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean scrolled(int arg0)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean mouseMoved(int arg0, int arg1)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean keyUp(int arg0)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean keyTyped(char arg0)
	    {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean keyDown(int arg0)
	    {
		// TODO Auto-generated method stub
		return false;
	    }
	});
    }

    @Override
    public void render()
    {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.begin();
	for (PooledEffect effect : effects)
	{
	    effect.draw(batch, 0.07f);
	    if (effect.isComplete())
	    {
		effects.removeValue(effect, true);
		effect.free();
	    }
	}
	batch.end();
	Gdx.app.log("pool stats", "active: " + effects.size + " | free: "
		+ pool.getFree() + "/" + pool.max + " | record: " + pool.peak);
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

    @Override
    public void dispose()
    {
	batch.dispose();
	prototype.dispose();
    }

}
