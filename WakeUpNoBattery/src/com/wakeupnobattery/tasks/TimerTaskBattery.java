package com.wakeupnobattery.tasks;

import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wakeupnobattery.broadcastreceivers.BroadcastReceiverAlertBattery;
/**
 * Timer que controla la gestión de bateria
 * @author Dave
 *
 */
public class TimerTaskBattery extends TimerTask
{
	private Context context;

	public TimerTaskBattery(Context context)
	{
		this.context = context;
	}

	@Override
	public void run()
	{
		this.batteryLevel();
	}

	/**
	 * Método que hace la llamada al nivel de bateria del dispositivo.
	 * Es un broadcastreceiver, asi que el resultado se devolverá de forma asíncrona y no 
	 * de forma secuencial.
	 * @return
	 */
	private void batteryLevel()
	{
		BroadcastReceiverAlertBattery batteryLevelReceiver = new BroadcastReceiverAlertBattery();
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}
}
