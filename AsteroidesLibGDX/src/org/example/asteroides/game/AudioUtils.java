package org.example.asteroides.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioUtils
{
    public static String EXPLOSION = "data/sounds/explosion.mp3";
    public static String SHOOT = "data/sounds/disparo.mp3";
    
    public static Music EXPLOSION_SOUND; 
    public static Music SHOOT_SOUND;
    
    public static void load()
    {
	EXPLOSION_SOUND = AudioUtils.createMusic(AudioUtils.EXPLOSION);
	EXPLOSION_SOUND.setLooping(false);
	SHOOT_SOUND = AudioUtils.createMusic(AudioUtils.SHOOT);
	SHOOT_SOUND.setLooping(false);
    }
    
    public static Sound createSound(String soundFileName) {
        return Gdx.audio.newSound(Gdx.files.internal(soundFileName));
    }

    public static Music createMusic(String soundFileName) {
        return Gdx.audio.newMusic(Gdx.files.internal(soundFileName));
    }
}
