package com.wakeupnobattery.utils;

import android.content.Context;

import com.wakeupnobattery.R;

public class UtilsLabel
{
	public static String codeLabelVolume(int progress,Context context)
	{
		String label = "";
		switch (progress)
		{
		case 0:
			label = context.getString(R.string.silent);
			break;
		case 1:
			label = context.getString(R.string.vibrate);
			break;
		default:
			label = ((progress - 1) * 12.5d) + " %";
			break;
		}
		return label;
	}
}
