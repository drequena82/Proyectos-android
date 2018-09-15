package com.wakeupnobattery;

import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.wakeupnobattery.beans.Options;
import com.wakeupnobattery.tasks.TimerTaskBattery;
import com.wakeupnobattery.utils.UtilsTime;

/**
 * Clase que implementa el servicio que controla la gestión de la batería
 * 
 * @author Dave
 * 
 */
public class BatteryControlService extends Service
{
	/*
	 * Bean que almacena los valores de la pantalla de opciones
	 */
	private Timer timer;
	private Options options;
	// This is the object that receives interactions from clients.
	private final IBinder mBinder = new BatteryControlBinder();

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class BatteryControlBinder extends Binder
	{
		BatteryControlService getService()
		{
			return BatteryControlService.this;
		}
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		options = new Options();
		SharedPreferences settings = getSharedPreferences(Options.PREFS_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		options.loadOptions(settings);
		// Iniciamos el servicio
		this.startCheckBatteryService();
		Log.i(getClass().getSimpleName(), "Started service CheckBattery");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		stopCheckBatteryService();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		stopCheckBatteryService();
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		startCheckBatteryService();
		return super.onUnbind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	public void startCheckBatteryService()
	{
		try
		{
			Log.i(getClass().getSimpleName(), "Iniciando servicio...");
			
			// Cogemos los datos del bean de opciones
			this.options = new Options();
			SharedPreferences settings = getSharedPreferences(Options.PREFS_NAME,
					Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
			this.options.loadOptions(settings);
			
			// Creamos el timer
			this.timer = new Timer();

			// Configuramos lo que tiene que hacer
			this.timer.scheduleAtFixedRate(new TimerTaskBattery(this), 1000,
							UtilsTime.convertToMillis(this.options
									.getAdviceInterval()));/*
															 * está en
															 * milisegundos
															 */

			Log.i(getClass().getSimpleName(),
					"Temporizador iniciado: intervalo cada "
							+ this.options.getAdviceInterval() + " minutos");
		} catch (Exception e)
		{
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}

	public void stopCheckBatteryService()
	{
		try
		{
			Log.i(getClass().getSimpleName(), "Finalizando servicio...");

			// Detenemos el timer
			if (this.timer != null)
			{
				this.timer.purge();
				this.timer.cancel();
			}
			Log.i(getClass().getSimpleName(), "Temporizador detenido");

		} catch (Exception e)
		{
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}
}
