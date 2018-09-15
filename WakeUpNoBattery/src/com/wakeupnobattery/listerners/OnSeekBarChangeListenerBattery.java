package com.wakeupnobattery.listerners;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class OnSeekBarChangeListenerBattery implements OnSeekBarChangeListener
{

	private TextView labelBattery;
	
	public OnSeekBarChangeListenerBattery(TextView labelBattery)
	{
		this.labelBattery = labelBattery;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		// TODO Auto-generated method stub
		labelBattery.setText(String.valueOf(progress) 
				+ " %");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
	}

}
