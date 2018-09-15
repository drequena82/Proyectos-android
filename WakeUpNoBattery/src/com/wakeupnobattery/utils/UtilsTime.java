package com.wakeupnobattery.utils;

public class UtilsTime
{
	/**
	 * M�todo que convierte los minutos en milisegundos
	 * @param minutes
	 * @return
	 */
	public static int convertToMillis(int minutes)
	{
		return (minutes*60*1000);
	}
	
	/**
	 * M�todo que convierte los milisegundos en minutos
	 * @param millis
	 * @return
	 */
	public static int convertToMinutes(int millis)
	{
		return (millis/(60*1000));
	}
}
