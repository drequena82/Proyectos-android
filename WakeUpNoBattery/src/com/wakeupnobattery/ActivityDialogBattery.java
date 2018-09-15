package com.wakeupnobattery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import com.wakeupnobattery.beans.Options;
import com.wakeupnobattery.utils.AlertWakeLock;

public class ActivityDialogBattery extends Activity
{
	private int batteryLevel;
	private KeyguardLock mLock;
	private Options options;
	private Ringtone tone;
	private int startFlagAudioManager;
	private AudioManager am;
	private int isVibrate;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		options = new Options();
		SharedPreferences settings = getSharedPreferences(Options.PREFS_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		options.loadOptions(settings);

		this.tone = RingtoneManager
				.getRingtone(this, this.options.getToneUri());
		setContentView(R.layout.main_layout);

		adjustVolume(this.options.getVolume());

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			this.batteryLevel = extras.getInt("batteryLevel");

		}

		KeyguardManager mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		mLock = mKeyGuardManager
				.newKeyguardLock("com.wakeupnobattery.ActivityDialogBattery");
		mLock.disableKeyguard();

		createDialog();

		try
		{
			if (this.tone == null)
			{
				this.tone = RingtoneManager.getRingtone(this,
						Settings.System.DEFAULT_NOTIFICATION_URI);
			}
			if (this.options.getVolume() == 1)
			{
				vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				vibrator.vibrate(new long[] { 250, 500, 500, 1000 }, 1);
			}
			this.tone.play();
		} catch (Exception ex)
		{
			Log.e(getClass().getSimpleName(),
					"Error to play ringtone:" + ex.getMessage());
		}
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		new CountDownTimer(30000, 1000)
		{

			@Override
			public void onTick(long millisUntilFinished)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish()
			{
				stopAlert();
			}
		}.start();
	}

	private void createDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence label = getString(R.string.advice_text2) + " "
				+ this.batteryLevel + " %\n" + getString(R.string.advice_text)
				+ " " + String.valueOf(this.options.getBatteryMinLevel())
				+ " %";
		builder.setMessage(label)
				.setTitle(getString(R.string.app_name))
				.setIcon(R.drawable.battery)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.ok_button),
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.cancel();
								stopAlert();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void stopAlert()
	{
		try
		{
			mLock.reenableKeyguard();
			restoreVolume();
			if (vibrator != null)
			{
				vibrator.cancel();
			}
			AlertWakeLock.releaseCpuLock();
			finish();
		} catch (Exception ex)
		{
			Log.e(getClass().getSimpleName(), "Error to release wake lock cpu:"
					+ ex.getMessage());
		}
	}

	/**
	 * Adjust de volume
	 */
	public void adjustVolume(int volume)
	{
		if (this.am == null)
		{
			this.am = (AudioManager) getSystemService(AUDIO_SERVICE);
		}
		int actualVolume = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
		startFlagAudioManager = actualVolume;
		isVibrate = am
				.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION);
		if (volume == 1)
		{
			volume = 0;
			am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_ON);
		}
		if (volume > actualVolume)
		{
			for (int i = actualVolume; i < volume; i++)
			{
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_RAISE,
						AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			}
		} else
		{
			for (int i = actualVolume; i > volume; i--)
			{
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_LOWER,
						AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			}
		}
	}

	public void restoreVolume()
	{
		if (this.am == null)
		{
			this.am = (AudioManager) getSystemService(AUDIO_SERVICE);
		}
		int actualVolume = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

		am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, isVibrate);
		if (startFlagAudioManager > actualVolume)
		{
			for (int i = actualVolume; i < startFlagAudioManager; i++)
			{
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_RAISE,
						AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			}
		} else
		{
			for (int i = actualVolume; i > startFlagAudioManager; i--)
			{
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_LOWER,
						AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			}
		}
	}
}
