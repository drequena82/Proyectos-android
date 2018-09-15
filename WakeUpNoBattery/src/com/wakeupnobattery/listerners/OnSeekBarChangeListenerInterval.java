package com.wakeupnobattery.listerners;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class OnSeekBarChangeListenerInterval implements OnSeekBarChangeListener
{

	private TextView labelInterval;
	private String label;
	
	public OnSeekBarChangeListenerInterval(TextView labelInterval,String label)
	{
		this.labelInterval = labelInterval;
		this.label = label;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		// TODO Auto-generated method stub
		labelInterval.setText(String.valueOf(progress) + " " + label);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub

	}

}
