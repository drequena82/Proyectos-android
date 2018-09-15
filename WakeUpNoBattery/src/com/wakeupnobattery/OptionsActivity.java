package com.wakeupnobattery;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wakeupnobattery.beans.Options;
import com.wakeupnobattery.broadcastreceivers.BroadcastReceiverCheckBattery;
import com.wakeupnobattery.listerners.OnSeekBarChangeListenerBattery;
import com.wakeupnobattery.listerners.OnSeekBarChangeListenerInterval;
import com.wakeupnobattery.utils.UtilsLabel;

public class OptionsActivity extends Activity
{
	private Uri toneUri;
	private TextView labelTone;
	private SeekBar seekBarInterval;
	private SeekBar seekBarBattery;
	private SeekBar seekBarVolume;
	private TextView labelInterval;
	private TextView labelBattery;
	private TextView labelvolume;
	private Button button;
	private BatteryControlService battService;
	private ServiceConnection conn;
	private Intent pickerTone;
	private Options options;
	private SharedPreferences settings;
	private int volume;
	public static final int PICKER_CODE = 12;
	public static final int LOADER_CODE = 13;
	public static final String EXTRA_MP3FILE = "MP3Uri";
	private Intent service;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		options = new Options();
		settings = getSharedPreferences(Options.PREFS_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		options.loadOptions(settings);

		this.toneUri = this.options.getToneUri();
		/*
		 * Elementos de la pantalla de opciones
		 */
		labelInterval = (TextView) findViewById(R.id.LblSubTituloInterval);
		labelBattery = (TextView) findViewById(R.id.LblSubTituloBattery);
		labelTone = (TextView) findViewById(R.id.LblSubTituloTone);
		labelvolume = (TextView) findViewById(R.id.LblSubTituloVolume);

		try
		{
			if (this.toneUri != null)
			{
				labelTone.setText(RingtoneManager.getRingtone(this,
						this.toneUri).getTitle(this));
			}
		} catch (Exception ex)
		{
			Log.e(getClass().getSimpleName(),
					"Error al cargar el nombre del ringtone:" + ex.getMessage());
		}

		seekBarInterval = (SeekBar) findViewById(R.id.seekBarInterval);
		seekBarBattery = (SeekBar) findViewById(R.id.seekBarBattery);
		seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
		seekBarInterval.setProgress(this.options.getAdviceInterval());
		seekBarBattery.setProgress(this.options.getBatteryMinLevel());

		volume = this.options.getVolume();

		seekBarVolume.setProgress(volume);
		labelInterval.setText(this.options.getAdviceInterval() + " "
				+ getString(R.string.label_minutes));
		labelBattery.setText(this.options.getBatteryMinLevel() + " %");

		labelvolume.setText(UtilsLabel.codeLabelVolume(volume, this));

		/*
		 * Asignamos los listeners de los seekbars
		 */
		seekBarBattery
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerBattery(
						labelBattery));
		seekBarInterval
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerInterval(
						labelInterval, getString(R.string.label_minutes)));

		seekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				String label = UtilsLabel.codeLabelVolume(progress,
						getApplicationContext());
				volume = progress;
				if (progress == 1)
				{
					Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
					vibrator.vibrate(1000);
				}
				labelvolume.setText(label);
			}
		});

		pickerTone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		pickerTone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		button = (Button) findViewById(R.id.buttonTone);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				startActivityForResult(pickerTone, OptionsActivity.PICKER_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		String labelRingTone = "";
		switch (requestCode)
		{
		case OptionsActivity.PICKER_CODE:
			if (resultCode == Activity.RESULT_OK)
			{
				Uri uri = data
						.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				if (uri != null)
				{
					this.toneUri = uri;
					try
					{
						labelRingTone = RingtoneManager.getRingtone(this,
								this.toneUri).getTitle(this);
					} catch (Exception ex)
					{
						Log.e(getClass().getSimpleName(),
								" Error al cargar el nombre del timbre");
					}
					if (!"".equals(labelRingTone))
					{
						labelTone.setText(labelRingTone);
					}
				}
			}
			break;
		case OptionsActivity.LOADER_CODE:
			File file = null;
			Uri uri = null;
			Bundle extras = null;
			String pathMP3 = "";

			if (resultCode == OptionsActivity.LOADER_CODE)
			{
				extras = data.getExtras();
				pathMP3 = extras.getString(OptionsActivity.EXTRA_MP3FILE);
				if (pathMP3 != null && !"".equals(pathMP3))
				{
					file = new File(pathMP3);
					uri = this.mp3Assigner(file);
					if (uri != null)
					{
						this.toneUri = uri;
						try
						{
							labelRingTone = file.getName();
						} catch (Exception ex)
						{
							Log.e(getClass().getSimpleName(),
									" Error al cargar el nombre del timbre");
						}
						if (!"".equals(labelRingTone))
						{
							labelTone.setText(labelRingTone);
						}
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* Métodos de gestión del menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Builder builder = new Builder(this);
		AlertDialog dialog;
		NotificationManager nm;
		switch (item.getItemId())
		{
		case R.id.save:
			/* Guardar los datos */
			this.options.setBatteryMinLevel(this.seekBarBattery.getProgress());
			this.options.setAdviceInterval(this.seekBarInterval.getProgress());
			this.options.setToneUri(this.toneUri);
			this.options.setVolume(this.seekBarVolume.getProgress());
			this.options.saveOptions(settings);
			// Reiniciar el servicio
			pauseBatteryService();
			startBatteryService();
			this.createCustomToast();
			finish();
			break;
		case R.id.about:

			dialog = builder.create();
			dialog.setTitle(getString(R.string.app_name));
			dialog.setMessage(getString(R.string.about));
			dialog.show();
			break;
		case R.id.exit:
			nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(Options.ID_NOTIFICATION);
			stopBatteryService();
			finish();
			break;
		case R.id.status:
			BroadcastReceiverCheckBattery batteryLevelReceiver = new BroadcastReceiverCheckBattery();
			IntentFilter batteryLevelFilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(batteryLevelReceiver, batteryLevelFilter);
			break;
		case R.id.loadMP3:
			/* Habría que crear un nuevo explorador de archivos */
			Intent accion = new Intent(this,
					com.wakeupnobattery.MP3Explorer.class);
			startActivityForResult(accion, OptionsActivity.LOADER_CODE);

			break;
		}
		return true;
	}

	private void createCustomToast()
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toastLayout));
		TextView toastTextView = (TextView) layout
				.findViewById(R.id.textviewToast);
		String tono = "";

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

		toastTextView.setText(getString(R.string.custom_toast_label) + tono
				+ "\n" + this.options.getAdviceInterval() + " "
				+ getString(R.string.label_minutes) + " - "
				+ this.options.getBatteryMinLevel() + " %");
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	/**
	 * Método que para el servicio que comprueba la batería
	 */
	private void stopBatteryService()
	{

		if (conn == null)
		{
			conn = new ServiceConnection()
			{
				public void onServiceConnected(ComponentName className,
						IBinder service)
				{
					// This is called when the connection with the service has
					// been
					// established, giving us the service object we can use to
					// interact with the service. Because we have bound to a
					// explicit
					// service that we know is running in our own process, we
					// can
					// cast its IBinder to a concrete class and directly access
					// it.
					battService = ((BatteryControlService.BatteryControlBinder) service).getService();
				}

				public void onServiceDisconnected(ComponentName className)
				{
					// This is called when the connection with the service has
					// been
					// unexpectedly disconnected -- that is, its process
					// crashed.
					// Because it is running in our same process, we should
					// never
					// see this happen.
					battService = null;
				}
			};
		}
		if(service==null)
		{
			service = new Intent("com.wakeupnobattery.BATTERYCONTROLSERVICE");
		}
		bindService(service, conn, Context.BIND_AUTO_CREATE);
		stopService(service);
	}

	private void pauseBatteryService()
	{
		if (conn == null)
		{
			conn = new ServiceConnection()
			{
				public void onServiceConnected(ComponentName className,
						IBinder service)
				{
					// This is called when the connection with the service has
					// been
					// established, giving us the service object we can use to
					// interact with the service. Because we have bound to a
					// explicit
					// service that we know is running in our own process, we
					// can
					// cast its IBinder to a concrete class and directly access
					// it.
					battService = ((BatteryControlService.BatteryControlBinder) service)
							.getService();
				}

				public void onServiceDisconnected(ComponentName className)
				{
					// This is called when the connection with the service has
					// been
					// unexpectedly disconnected -- that is, its process
					// crashed.
					// Because it is running in our same process, we should
					// never
					// see this happen.
					battService = null;
				}
			};
		}
		
		if(service==null)
		{
			service = new Intent("com.wakeupnobattery.BATTERYCONTROLSERVICE");
		}
		bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	public void startBatteryService()
	{
		if (conn == null)
		{
			conn = new ServiceConnection()
			{
				public void onServiceConnected(ComponentName className,
						IBinder service)
				{
					// This is called when the connection with the service has
					// been
					// established, giving us the service object we can use to
					// interact with the service. Because we have bound to a
					// explicit
					// service that we know is running in our own process, we
					// can
					// cast its IBinder to a concrete class and directly access
					// it.
					battService = ((BatteryControlService.BatteryControlBinder) service)
							.getService();
				}

				public void onServiceDisconnected(ComponentName className)
				{
					// This is called when the connection with the service has
					// been
					// unexpectedly disconnected -- that is, its process
					// crashed.
					// Because it is running in our same process, we should
					// never
					// see this happen.
					battService = null;
				}
			};
		}
		unbindService(conn);
	}

	/**
	 * Método que asigna un nuevo mp3 como sonido de notificación
	 * 
	 * @param file
	 */
	private Uri mp3Assigner(File file)
	{

		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file
				.getAbsolutePath());
		ContentValues values = new ContentValues();

		values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, file.getName());
		// values.put(MediaStore.MediaColumns.SIZE, 215454);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		// values.put(MediaStore.Audio.Media.ARTIST, "Madonna");
		values.put(MediaStore.Audio.Media.DURATION, 2000);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri newUri = this.getContentResolver().insert(uri, values);
		/*
		 * RingtoneManager.setActualDefaultRingtoneUri(this,
		 * RingtoneManager.TYPE_NOTIFICATIONS, newUri);
		 */
		return newUri;
	}
}
