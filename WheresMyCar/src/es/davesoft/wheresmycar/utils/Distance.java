package es.davesoft.wheresmycar.utils;

import com.google.android.maps.GeoPoint;

public class Distance
{
    public static double calculateDistance(GeoPoint p1, GeoPoint p2, int magn)
    {
	double resultado = 0;
	double lat1 = 0;
	double long1 = 0;
	double lat2 = 0;
	double long2 = 0;
	double degtorad = 0.01745329;
	double radtodeg = 57.29577951;
	double dlong = 0;
	double dvalue = 0;
	double dd = 0;
	double miles = 0;
	double km = 0;

	if (p1 != null && p2 != null)
	{
	    lat1 = p1.getLatitudeE6();
	    long1 = p1.getLongitudeE6();
	    lat2 = p2.getLatitudeE6();
	    long2 = p2.getLongitudeE6();

	    dlong = (long1 - long2);
	    dvalue = (Math.sin(lat1 * degtorad) * Math.sin(lat2 * degtorad))
		    + (Math.cos(lat1 * degtorad) * Math.cos(lat2 * degtorad) * Math
			    .cos(dlong * degtorad));

	    dd = Math.acos(dvalue) * radtodeg;

	    /*
	     * Millas
	     */
	    miles = (dd * 69.16);

	    /*
	     * Km
	     */
	    km = (dd * 111.302);

	    if (magn == Config.MAGN_KM)
	    {
		resultado = km;
	    } else
	    {
		resultado = miles;
	    }
	}
	return roundNumber(resultado,2);
    }

    public static double roundNumber(double numero, int decimales)
    {
	return Math.round(numero * Math.pow(10, decimales))
		/ Math.pow(10, decimales);
    }
}
