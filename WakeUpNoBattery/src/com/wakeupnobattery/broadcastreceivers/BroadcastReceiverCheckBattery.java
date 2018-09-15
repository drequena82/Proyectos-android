package com.wakeupnobattery.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.wakeupnobattery.R;

public class BroadcastReceiverCheckBattery extends BroadcastReceiver
{
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		context.unregisterReceiver(this);
		int result = -1;
		int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
		Log.i(getClass().getSimpleName(),"Nivel de bateria: " + rawlevel);
		
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		
		Log.i(getClass().getSimpleName(),"Escala de batería: " + scale);
		
		if (rawlevel >= 0 && scale > 0)
		{
			result = (rawlevel * 100) / scale;
			
			Toast.makeText(context, context.getString(R.string.label_checkbattery) + " " + result +" %", Toast.LENGTH_LONG).show();
		}
	}
}
