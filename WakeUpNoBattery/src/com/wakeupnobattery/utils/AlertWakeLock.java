package com.wakeupnobattery.utils;

import android.content.Context;
import android.os.PowerManager;

public class AlertWakeLock
{

	private static PowerManager.WakeLock sCpuWakeLock;
	public static void acquireCpuWakeLock(Context context)
	{
		if (sCpuWakeLock != null)
		{
			return;
		}

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		sCpuWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "wakeupnobattery");
		//sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "wakeupnobattery");
		
		sCpuWakeLock.acquire();
	}

	public static void releaseCpuLock()
	{
		if (sCpuWakeLock != null)
		{
			if(sCpuWakeLock.isHeld())
	        {
				sCpuWakeLock.release();
	        }
			sCpuWakeLock = null;
		}
	}

}
