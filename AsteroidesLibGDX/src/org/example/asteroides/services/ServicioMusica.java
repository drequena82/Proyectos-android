package org.example.asteroides.services;

import org.example.asteroides.Asteroides;
import org.example.asteroides.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServicioMusica extends Service
{

	MediaPlayer reproductor;

	private NotificationManager nm;
	private static final int ID_NOTIFICACION_CREAR = 1234;

	@Override
	public void onCreate()
	{
		// Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
		reproductor = MediaPlayer.create(this, R.raw.mainaudio);

		/* Cogemos el notification manager */
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intenc, int flags, int idArranque)
	{
		/* Creamos una entrada en las notificaciones */
		Notification notificacion = new Notification(R.drawable.ic_launcher,
				getString(R.string.notification_label), System.currentTimeMillis());

		/* Intencion a la que llamaremos cuando hagamos click en la notificación */
		PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0,
				new Intent(this, Asteroides.class), 0);

		/* Asignamos la intención */
		notificacion.setLatestEventInfo(this, "Reproduciendo música",
				"información adicional", intencionPendiente);

		/* Apilamos la notificación */
		nm.notify(ID_NOTIFICACION_CREAR, notificacion);

		//Toast.makeText(this, "Servicio arrancado " + idArranque,Toast.LENGTH_SHORT).show();

		reproductor.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		// Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
		reproductor.stop();

		nm.cancel(ID_NOTIFICACION_CREAR);
	}

	@Override
	public IBinder onBind(Intent intencion)
	{
		return null;
	}
}
