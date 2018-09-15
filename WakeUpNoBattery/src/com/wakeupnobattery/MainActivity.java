package com.wakeupnobattery;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import com.wakeupnobattery.beans.Options;

public class MainActivity extends Activity
{
	private Options options;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        /*Inicializamos los valores de configuracion*/
	    options = new Options();
        SharedPreferences settings = getSharedPreferences(Options.PREFS_NAME, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        options.saveOptions(settings);
        
        Intent service = new Intent("com.wakeupnobattery.BATTERYCONTROLSERVICE");
        startService(service);
        this.createNotification();
		finish();
	}

	@Override
	protected void onDestroy()
	{
		finish();
		super.onDestroy();
	}
	private void createNotification()
	{
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Agregando el icono, texto y momento para lanzar la notificación
		int icon = R.drawable.battery;
		CharSequence tickerText = getString(R.string.notification_ticker);

		Notification notification = new Notification(icon, tickerText, 0);

		Context context = getApplicationContext();
		CharSequence contentTitle = getString(R.string.notification_title);
		CharSequence contentText =getString(R.string.notification_text);

		// Agregando que no se elimine cuando se pulse sobre la notificación o
		// sobre el
		// boton clear
		notification.flags |= Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_NO_CLEAR;

		Intent notificationIntent = new Intent(this, OptionsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		
		mNotificationManager.notify(Options.ID_NOTIFICATION, notification);
	}
	
	public String createLabelNotification()
	{
		String tono = "";
		String resultado = "";
		try
		{
			if (this.options.getToneUri() != null)
			{
				tono = RingtoneManager.getRingtone(this,
						this.options.getToneUri()).getTitle(this);
			} else
			{
				tono = "Default";
			}
		} catch (Exception ex)
		{
			Log.e(getClass().getSimpleName(),
					"Error al cargar el nombre del ringtone:" + ex.getMessage());
		}

		resultado = tono + " - " + this.options.getAdviceInterval() + " "
				+ getString(R.string.label_minutes) + " - "
				+ this.options.getBatteryMinLevel() + " %";
		
		return resultado;
	}
}