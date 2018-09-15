package org.example.asteroides.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets
{

    public static Texture TEXTURE_BACKGROUND;

    public static TextureAtlas TEXTURE_ASTEROID;
    public static Animation ANIMATION_ASTEROID;

    public static TextureAtlas TEXTURE_MINIASTEROID;
    public static Animation ANIMATION_MINIASTEROID;

    public static TextureAtlas TEXTURE_MEDASTEROID;
    public static Animation ANIMATION_MEDASTEROID;

    // public static TextureAtlas TEXTURE_MISIL;
    // public static Animation ANIMATION_MISIL;

    public static Texture TEXTURE_MISIL;

    public static TextureAtlas TEXTURE_EXPLOSION;
    public static Animation ANIMATION_EXPLOSION;

    public static TextureAtlas TEXTURE_SHIP_EXPLOSION;
    public static Animation ANIMATION_SHIP_EXPLOSION;

    public static Sprite SPRITE_SHIP;

    public static void load()
    {

	Assets.TEXTURE_ASTEROID = new TextureAtlas(
		Gdx.files.internal("data/asteroids/pack.atlas"));

	Assets.TEXTURE_MINIASTEROID = new TextureAtlas(
		Gdx.files.internal("data/miniAsteroids/pack.atlas"));

	Assets.TEXTURE_MEDASTEROID = new TextureAtlas(
		Gdx.files.internal("data/medAsteroids/pack.atlas"));

	Assets.SPRITE_SHIP = new Sprite(new Texture(
		Gdx.files.internal("data/nave.png")));

	Assets.ANIMATION_ASTEROID = new Animation(1f / 24f,
		Assets.TEXTURE_ASTEROID.getRegions());

	Assets.ANIMATION_MINIASTEROID = new Animation(1f / 24f,
		Assets.TEXTURE_MINIASTEROID.getRegions());

	Assets.ANIMATION_MEDASTEROID = new Animation(1f / 24f,
		Assets.TEXTURE_MEDASTEROID.getRegions());

	Assets.TEXTURE_MISIL = new Texture(
		Gdx.files.internal("data/misil/misil1.png"));

	/*
	 * Assets.TEXTURE_MISIL = new TextureAtlas(
	 * Gdx.files.internal("data/misil/pack.atlas"));
	 * 
	 * Assets.ANIMATION_MISIL = new Animation(1f/24f,
	 * Assets.TEXTURE_MISIL.getRegions());
	 */
	Assets.TEXTURE_EXPLOSION = new TextureAtlas(
		Gdx.files.internal("data/explosion/pack.atlas"));

	Assets.ANIMATION_EXPLOSION = new Animation(1f / 24f,
		Assets.TEXTURE_EXPLOSION.getRegions());

	Assets.TEXTURE_SHIP_EXPLOSION = new TextureAtlas(
		Gdx.files.internal("data/explosionNave/pack.atlas"));

	Assets.ANIMATION_SHIP_EXPLOSION = new Animation(1f / 24f,
		Assets.TEXTURE_SHIP_EXPLOSION.getRegions());

	loadBackground((int)((Math.random() * 10))%4);
    }

    public static void loadBackground(int type)
    {
	switch (type)
	{
	case 0:
	    Assets.TEXTURE_BACKGROUND = new Texture(
		    Gdx.files.internal("data/background.jpg"));
	    break;
	case 1:
	    Assets.TEXTURE_BACKGROUND = new Texture(
		    Gdx.files.internal("data/background1.jpg"));
	    break;
	case 2:
	    Assets.TEXTURE_BACKGROUND = new Texture(
		    Gdx.files.internal("data/background2.jpg"));
	    break;
	case 3:
	    Assets.TEXTURE_BACKGROUND = new Texture(
		    Gdx.files.internal("data/background3.jpg"));
	}
    }
}
