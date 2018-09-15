package com.wakeupnobattery.beans;

import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;

public class Options
{
	/**
	 * 
	 */
	private Uri toneUri;
	private int adviceInterval;
	private int batteryMinLevel;
	private int volume;
	public static int ID_NOTIFICATION = 32;
	public static final String PREFS_NAME = "Options";
	
	/**
	 * Constructor por defecto
	 */
	public Options()
	{
		this.toneUri = Settings.System.DEFAULT_NOTIFICATION_URI;
		this.adviceInterval = 5;
	    this.batteryMinLevel=15;
	    this.volume = 6;
	}
	
	/**
	 * Cargar los datos de opciones de las variables persistentes
	 * @param preferences
	 */
	public void loadOptions(SharedPreferences preferences)
	{
		String strUri="";
		strUri = preferences.getString("Uri", Settings.System.DEFAULT_NOTIFICATION_URI.toString());
		this.toneUri = Uri.parse(strUri);
		this.adviceInterval = preferences.getInt("adviceInterval",10);
		this.batteryMinLevel = preferences.getInt("batteryMinLevel",50);
		this.volume = preferences.getInt("volume",4);
	}

	/**
	 * Método que guarda los datos del bean en el registro de variables
	 */
	public void saveOptions(SharedPreferences preferences)
	{
		String strUri="";
		if(this.toneUri!=null)
		{
			strUri = this.toneUri.toString();
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Uri", strUri);
		editor.putInt("adviceInterval", this.adviceInterval);
		editor.putInt("batteryMinLevel", this.batteryMinLevel);
		editor.putInt("volume", this.volume);
		editor.commit();
	}

	public Uri getToneUri()
	{
		return toneUri;
	}

	public void setToneUri(Uri toneUri)
	{
		this.toneUri = toneUri;
	}

	public int getAdviceInterval()
	{
		return adviceInterval;
	}

	public void setAdviceInterval(int adviceInterval)
	{
		this.adviceInterval = adviceInterval;
	}

	public int getBatteryMinLevel()
	{
		return batteryMinLevel;
	}

	public void setBatteryMinLevel(int batteryMinLevel)
	{
		this.batteryMinLevel = batteryMinLevel;
	}

	public int getVolume()
	{
		return volume;
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}
}
