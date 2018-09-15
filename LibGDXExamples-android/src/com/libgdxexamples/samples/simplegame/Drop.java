package com.libgdxexamples.samples.simplegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game
{

    SpriteBatch batch;
    BitmapFont font;
    BitmapFont font2;
    public void create()
    {
	batch = new SpriteBatch();
	// Use LibGDX's default Arial font.
	font = new BitmapFont();
	font2 = new BitmapFont();
	this.setScreen(new MainMenuScreen(this));
    }

    public void render()
    {
	super.render(); // important!
    }

    public void dispose()
    {
	batch.dispose();
	font.dispose();
	font2.dispose();
    }

}