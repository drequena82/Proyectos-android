package com.libgdxexamples.android.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class Assets
{
    public static String TEXTURE_RUN_PATH = "data/run/pack.atlas";
    public static String TEXTURE_JUMP_PATH = "data/jump/pack.atlas";
    public static String TEXTURE_SLIDING_PATH = "data/sliding/pack.atlas";
    public static String TEXTURE_FAINT_PATH = "data/faint/pack.atlas";
    public static String TEXTURE_DIZZY_PATH = "data/dizzy/pack.atlas";
    public static String TEXTURE_STANDING_PATH = "data/standing/pack.atlas";
    
    public static TextureAtlas TEXTURE_RUN;
    public static TextureAtlas TEXTURE_JUMP;
    public static TextureAtlas TEXTURE_SLIDING;
    public static TextureAtlas TEXTURE_FAINT;
    public static TextureAtlas TEXTURE_DIZZY;
    public static TextureAtlas TEXTURE_STANDING;
    
    public static Animation TEXTURE_RUN_ANIMATION;
    public static Animation TEXTURE_JUMP_ANIMATION;
    public static Animation TEXTURE_SLIDING_ANIMATION;
    public static Animation TEXTURE_FAINT_ANIMATION;
    public static Animation TEXTURE_DIZZY_ANIMATION;
    public static Animation TEXTURE_STANDING_ANIMATION;
    
    public static float VELOCITY_ANIMATION = 0.5f;
    
    public static Texture BLUE_BLOCK; 
    public static Texture DARK_BLOCK;
    public static Texture GRAY_BLOCK;
    public static Texture LBLUE_BLOCK;
    public static Texture ORANGE_BLOCK;
    public static Texture PINK_BLOCK;
    public static Texture PURPLE_BLOCK;
    public static Texture RED_BLOCK;
    public static Texture RED2_BLOCK;
    public static Texture WHITE_BLOCK;
    public static Texture GREEN_BLOCK;
    
    public static Texture TEXTURE_MISIL;
    public static TextureAtlas TEXTURE_EXPLOSION;
    public static Animation ANIMATION_EXPLOSION;
    
    public static void load()
    {
	Assets.TEXTURE_RUN = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_RUN_PATH));
	Assets.TEXTURE_JUMP = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_JUMP_PATH));
	Assets.TEXTURE_SLIDING = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_SLIDING_PATH));
	Assets.TEXTURE_FAINT = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_FAINT_PATH));
	Assets.TEXTURE_DIZZY = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_DIZZY_PATH));
	Assets.TEXTURE_STANDING = new TextureAtlas(Gdx.files.internal(Assets.TEXTURE_STANDING_PATH));
	
	Assets.TEXTURE_RUN_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_RUN.getRegions());
	Assets.TEXTURE_JUMP_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_JUMP.getRegions());
	Assets.TEXTURE_SLIDING_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_SLIDING.getRegions());
	Assets.TEXTURE_FAINT_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_FAINT.getRegions());
	Assets.TEXTURE_DIZZY_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_DIZZY.getRegions());
	Assets.TEXTURE_STANDING_ANIMATION = new Animation(Assets.VELOCITY_ANIMATION, Assets.TEXTURE_STANDING.getRegions());
	
	BLUE_BLOCK = new Texture(Gdx.files.internal("data/blocks/blue.png")); 
	DARK_BLOCK = new Texture(Gdx.files.internal("data/blocks/darkgray.png"));
	GRAY_BLOCK = new Texture(Gdx.files.internal("data/blocks/gray.png"));
	LBLUE_BLOCK = new Texture(Gdx.files.internal("data/blocks/lightblue.png"));
	ORANGE_BLOCK = new Texture(Gdx.files.internal("data/blocks/orange.png"));
	PINK_BLOCK = new Texture(Gdx.files.internal("data/blocks/pink.png"));
	PURPLE_BLOCK = new Texture(Gdx.files.internal("data/blocks/purple.png"));
	RED_BLOCK = new Texture(Gdx.files.internal("data/blocks/red.png"));
	RED2_BLOCK = new Texture(Gdx.files.internal("data/blocks/red2.png"));
	WHITE_BLOCK = new Texture(Gdx.files.internal("data/blocks/white.png"));
	GREEN_BLOCK = new Texture(Gdx.files.internal("data/blocks/green.png"));
	    
	Assets.TEXTURE_EXPLOSION = new TextureAtlas(
		Gdx.files.internal("data/explosion/pack.atlas"));

	Assets.ANIMATION_EXPLOSION = new Animation(1f/24f,
		Assets.TEXTURE_EXPLOSION.getRegions());
	
	Assets.TEXTURE_MISIL = new Texture(
		Gdx.files.internal("data/misil2.png"));
    }

}
