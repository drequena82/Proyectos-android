package es.davesoft.wheresmycar.handlers;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import es.davesoft.wheresmycar.R;
import es.davesoft.wheresmycar.beans.ImageBean;
import es.davesoft.wheresmycar.overlays.ImageItemizedOverlay;
import es.davesoft.wheresmycar.overlays.ImageOverlayItem;
import es.davesoft.wheresmycar.overlays.MyPositionItemizedOverlay;

public class GeoUpdateHandler implements LocationListener
{   
    private GeoPoint currentPoint;
    private MapController mapController;
    private MapView mapView;
    private GeoPoint myCarPoint;
    private Context c;
    private float distance;
    private ImageBean image;
    
    public GeoUpdateHandler(Context c, MapController mapController,
	    MapView mapView)
    {
	this.c = c;
	this.mapView = mapView;
	this.mapController = mapController;
	this.currentPoint = this.mapView.getMapCenter();
	this.image = new ImageBean();
    }

    @Override
    public void onLocationChanged(Location location)
    {

	int lat = (int) (location.getLatitude() * 1E6);// 38.345278,
						       // -0.483056
	int lng = (int) (location.getLongitude() * 1E6);
	GeoPoint point = new GeoPoint(lat, lng);
	this.currentPoint = point;
	float[] results=new float[2];
	if (mapController != null)
	{
	    if(this.myCarPoint!=null)
	    {
		Location.distanceBetween(this.myCarPoint.getLatitudeE6(), this.myCarPoint.getLongitudeE6(), lat, lng, results);
		this.distance = results[0];
	    }
	    mapController.animateTo(point); // mapController.setCenter(point);
	    drawIcons();
	}
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    public void drawIcons()
    {
	ImageOverlayItem itemMyCar = null;
	OverlayItem itemMyPosition = null;
	mapView.getOverlays().clear();
	List<Overlay> mapOverlays = mapView.getOverlays();

	MyPositionItemizedOverlay itemizedOverlay = new MyPositionItemizedOverlay(
		c.getResources().getDrawable(R.drawable.bubble), mapView);
	ImageItemizedOverlay<ImageOverlayItem> itemizedOverlay2 = new ImageItemizedOverlay<ImageOverlayItem>(
		c.getResources().getDrawable(R.drawable.carposition), mapView);
	if (this.currentPoint != null)
	{
	    /* Tenemos el punto de posicion del coche */
	    itemMyPosition = new OverlayItem(this.currentPoint,
		    c.getString(R.string.overlay_title_me), distance + " "
			    + c.getString(R.string.label_metros));
	    itemizedOverlay.addOverlay(itemMyPosition);
	    mapOverlays.add(itemizedOverlay);
	}

	if (this.myCarPoint != null)
	{
	    	    itemMyCar = new ImageOverlayItem(this.myCarPoint,
		    c.getString(R.string.overlay_title_mycar),
		    c.getString(R.string.overlay_text_mycar),this.image.getUriImage(),this.image.getBitmapImage());
	    itemizedOverlay2.addOverlay(itemMyCar);
	    mapOverlays.add(itemizedOverlay2);
	}
	mapView.refreshDrawableState();
    }

    public GeoPoint getCurrentPoint()
    {
	return currentPoint;
    }

    public void setCurrentPoint(GeoPoint currentPoint)
    {
	this.currentPoint = currentPoint;
    }

    public GeoPoint getMyCarPoint()
    {
	return myCarPoint;
    }

    public void setMyCarPoint(GeoPoint myCarPoint)
    {
	this.myCarPoint = myCarPoint;
    }

    public float getDistance()
    {
        return distance;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public ImageBean getImage()
    {
        return image;
    }

    public void setImage(ImageBean image)
    {
        this.image = image;
    }
}
