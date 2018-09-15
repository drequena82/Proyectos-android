package com.wakeupnobattery.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;

import com.wakeupnobattery.ActivityDialogBattery;
import com.wakeupnobattery.beans.Options;
import com.wakeupnobattery.utils.AlertWakeLock;

public class BroadcastReceiverAlertBattery extends BroadcastReceiver
{
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		context.unregisterReceiver(this);
		Intent dialogView = null;
		
		Options options = new Options();
		SharedPreferences settings = context.getSharedPreferences(Options.PREFS_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		options.loadOptions(settings);
		
		
		int minBattery = options.getBatteryMinLevel();
		int result = -1;
		int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
		Log.i(getClass().getSimpleName(),"Nivel de bateria: " + rawlevel);
		
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		int isCharging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
		
		Log.i(getClass().getSimpleName(),"Escala de batería: " + scale);
		
		if (rawlevel >= 0 && scale > 0 && isCharging == 0)
		{
			result = (rawlevel * 100) / scale;
			/*El battery level no se obtiene automáticamente
			 * */
			if (result <= minBattery)
			{
				try
				{
					AlertWakeLock.acquireCpuWakeLock(context);
				}catch(IllegalArgumentException iex)
				{
					Log.e(getClass().getSimpleName(), "Error al intentar desbloquear la pantalla: " + iex.getMessage());
				}
				Log.i(getClass().getSimpleName(),"Arranca el aviso, nivel bateria : " + result +" minimo: "+ minBattery);
				dialogView = new Intent(context, ActivityDialogBattery.class);
				dialogView.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				dialogView.putExtra("batteryLevel", result);
				context.startActivity(dialogView);
			}
		}
	}
}
