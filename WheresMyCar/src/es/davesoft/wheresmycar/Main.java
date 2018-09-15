package es.davesoft.wheresmycar;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import es.davesoft.wheresmycar.beans.ImageBean;
import es.davesoft.wheresmycar.handlers.GeoUpdateHandler;
import es.davesoft.wheresmycar.utils.Config;

public class Main extends MapActivity
{

    private MapController mapController;
    private MapView mapView;
    private LocationManager locationManager;
    private GeoPoint myCarPoint;

    private GeoUpdateHandler geoHandler;

    public void onCreate(Bundle bundle)
    {
	super.onCreate(bundle);
	setContentView(R.layout.main);

	mapView = (MapView) findViewById(R.id.mapview);
	mapView.setBuiltInZoomControls(true);
	mapView.setStreetView(true);
	mapView.setClickable(true);

	mapController = mapView.getController();
	mapController.setZoom(18); // Zoom 1 is world view
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
	super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
	super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
	super.onStart();
	initApp();
    }

    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
    }

    @Override
    protected void onRestart()
    {
	// TODO Auto-generated method stub
	super.onRestart();
	initApp();
    }

    @Override
    protected void onStop()
    {
	// TODO Auto-generated method stub
	super.onStop();
    }

    public void initApp()
    {
	Builder builder;
	AlertDialog dialog;

	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	{
	    /*
	     * Problema al girar el movil!!! se tienen que guardar los
	     * parámetros de image
	     */
	    if (geoHandler == null)
	    {
		geoHandler = new GeoUpdateHandler(this, mapController, mapView);
	    }
	    /*
	     * Comprobamos si el punto de la localización del coche se ha puesto
	     * ya
	     */
	    this.myCarPoint = loadMyCarPosition();
	    geoHandler.setMyCarPoint(this.myCarPoint);

	    locationManager.requestLocationUpdates(
		    LocationManager.GPS_PROVIDER, 0, 0, geoHandler);

	    if (locationManager
		    .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
	    {
		this.geoHandler
			.setCurrentPoint(new GeoPoint((int) (locationManager
				.getLastKnownLocation(
					LocationManager.GPS_PROVIDER)
				.getLatitude() * 1E6), (int) (locationManager
				.getLastKnownLocation(
					LocationManager.GPS_PROVIDER)
				.getLongitude() * 1E6)));
	    }

	    // createNotification();

	    if (this.myCarPoint != null)
	    {
		mapController.animateTo(this.myCarPoint);

	    } else
	    {
		/*
		 * Si no hemos puesto la posición del coche mostramos un Alert
		 * de muestra
		 */
		builder = new Builder(this);
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle(getString(R.string.incoming_alert_title));
		dialog.setMessage(getString(R.string.incoming_alert_text));
		dialog.setButton(getString(R.string.ok_button),
			new DialogInterface.OnClickListener()
			{

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which)
			    {
				dialog.cancel();
			    }

			});

		dialog.setButton2(getString(R.string.label_item_salir),
			new DialogInterface.OnClickListener()
			{

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which)
			    {
				dialog.cancel();
				finish();
			    }

			});

		dialog.show();
		mapController.animateTo(this.geoHandler.getCurrentPoint());
	    }

	    this.geoHandler.drawIcons();
	} else
	{
	    /*
	     * Si no está activado el GPS, podemos mandar a usuario a la
	     * pantalla de conexiones
	     */
	    builder = new Builder(this);
	    dialog = builder.create();
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setCancelable(false);
	    dialog.setTitle(getString(R.string.app_name));
	    dialog.setMessage(getString(R.string.label_enabled_gps));
	    dialog.setButton(getString(R.string.ok_button),
		    new DialogInterface.OnClickListener()
		    {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			    Intent enabledGPS = new Intent(
				    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

			    startActivity(enabledGPS);
			    dialog.cancel();
			}

		    });

	    dialog.setButton2(getString(R.string.label_item_salir),
		    new DialogInterface.OnClickListener()
		    {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			    finish();
			}

		    });
	    dialog.show();
	}
    }

    public void initCamera()
    {
	Intent intent;
	try
	{
	    if (this.geoHandler.getImage() != null)
	    {
		intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent,
			Config.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    }
	} catch (Exception e)
	{
	    Log.v(Main.class.toString(), "Can't create file to take picture!");
	    Toast.makeText(this,
		    "Please check SD card! Image shot is impossible!", 10000);
	}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	Uri uri;
	Bitmap originalBitmap;
	int originalXPix = 0;
	int originalYPix = 0;
	int proportionY = 0;
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == Config.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
	{
	    if (resultCode == RESULT_OK)
	    {
		/* Este es el resultado de la camara */
		{

		    try
		    {
			originalBitmap = (Bitmap) data.getExtras().get("data");
			uri = data.getData();
			originalXPix = originalBitmap.getWidth();
			originalYPix = originalBitmap.getHeight();
			Log.d("Original image", "tam x image: " + originalXPix
				+ ", tam y image:" + originalYPix);
			proportionY = (Config.THUMNAIL_SIZE * originalYPix)
				/ originalXPix;

			Log.d("Resize image", "tam x image: "
				+ Config.THUMNAIL_SIZE + ", tam y image:"
				+ proportionY);
			this.geoHandler.getImage().setBitmapImage(
				Bitmap.createScaledBitmap(originalBitmap,
					Config.THUMNAIL_SIZE, proportionY,
					false));
			this.geoHandler.getImage().setUriImage(uri);

			this.geoHandler.setImage(this.geoHandler.getImage());
			saveMyCarPosition();

			this.geoHandler.drawIcons();
		    } catch (Exception ex)
		    {
			Toast.makeText(this, "The picture was not taken",
				Toast.LENGTH_SHORT);
		    }
		}

	    } else
	    {
		this.geoHandler.setImage(new ImageBean());
		Toast.makeText(this, "The picture was not taken",
			Toast.LENGTH_SHORT);
	    }
	}
    }

    @Override
    protected boolean isRouteDisplayed()
    {
	return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	Builder builder = new Builder(this);
	AlertDialog dialog;
	switch (item.getItemId())
	{
	case R.id.myCarIsWhere:
	    if (this.myCarPoint == null)
	    {
		createNotification();
		/*
		 * Pintamos la posicion del coche
		 */

		this.myCarPoint = this.geoHandler.getCurrentPoint();
		this.geoHandler.setMyCarPoint(this.myCarPoint);

		this.geoHandler
			.setMyCarPoint(this.geoHandler.getCurrentPoint());

		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.app_name));
		dialog.setMessage(getString(R.string.label_take_photo));
		dialog.setButton(getString(R.string.ok_button),
			new DialogInterface.OnClickListener()
			{

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which)
			    {
				dialog.cancel();
				initCamera();
			    }

			});

		dialog.setButton2(getString(R.string.label_item_salir),
			new DialogInterface.OnClickListener()
			{

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which)
			    {
				geoHandler.drawIcons();
				dialog.cancel();
			    }

			});
		dialog.show();
	    } else
	    {
		mapController.animateTo(this.myCarPoint);
		Toast.makeText(this, getString(R.string.overlay_title_mycar),
			Toast.LENGTH_SHORT).show();
	    }
	    saveMyCarPosition();
	    break;
	case R.id.itemAcerca:
	    dialog = builder.create();
	    dialog.setTitle(getString(R.string.app_name));
	    dialog.setMessage(getString(R.string.label_acerca));
	    dialog.setCanceledOnTouchOutside(true);
	    dialog.show();
	    break;
	case R.id.itemSalir:
	    removeMyCarPosition();
	    deleteNotification();
	    locationManager.removeUpdates(this.geoHandler);

	    finish();
	    break;
	}
	return true;
    }

    private void saveMyCarPosition()
    {
	String urlString = "";
	SharedPreferences prefs = getSharedPreferences("wheresmycar",
		Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putInt("myCarPositionLatitude", this.geoHandler
		.getCurrentPoint().getLatitudeE6());
	editor.putInt("myCarPositionLongitude", this.geoHandler
		.getCurrentPoint().getLongitudeE6());
	editor.putBoolean("myCarPosition", true);
	if (this.geoHandler.getImage() != null
		&& this.geoHandler.getImage().getUriImage() != null)
	{
	    urlString = this.geoHandler.getImage().getUriImage().getPath();
	    editor.putString("myCarPositionImageUri", urlString);
	} else
	{
	    editor.putString("myCarPositionImageUri", null);
	}

	editor.commit();
    }

    private void removeMyCarPosition()
    {
	String imageUri;
	Uri uri;
	SharedPreferences prefs = getSharedPreferences("wheresmycar",
		Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = prefs.edit();
	editor.putInt("myCarPositionLatitude", 0);
	editor.putInt("myCarPositionLongitude", 0);
	editor.putBoolean("myCarPosition", false);
	imageUri = prefs.getString("myCarPositionImageUri", null);
	if (imageUri != null)
	{
	    Uri.Builder builder = new Uri.Builder();
	    builder.encodedPath(imageUri);
	    builder.authority("media");
	    builder.scheme("content");
	    builder.path(imageUri);

	    uri = builder.build();
	    getContentResolver().delete(uri, null, null);
	}
	editor.putString("myCarPositionImageUri", null);
	editor.commit();
    }

    private GeoPoint loadMyCarPosition()
    {
	GeoPoint myCarPosition = null;
	int longitude = 0;
	int latitude = 0;
	boolean isPosition = false;
	String uriPath;
	Uri uri;
	SharedPreferences prefs = getSharedPreferences("wheresmycar",
		Context.MODE_PRIVATE);
	latitude = prefs.getInt("myCarPositionLatitude", 0);
	longitude = prefs.getInt("myCarPositionLongitude", 0);
	isPosition = prefs.getBoolean("myCarPosition", false);
	if (isPosition)
	{
	    myCarPosition = new GeoPoint(latitude, longitude);
	}
	try
	{
	    uriPath = prefs.getString("myCarPositionImageUri", null);
	    if (uriPath != null)
	    {
		Uri.Builder builder = new Uri.Builder();
		builder.encodedPath(uriPath);
		builder.authority("media");
		builder.scheme("content");
		builder.path(uriPath);

		uri = builder.build();

		this.geoHandler.getImage().setUriImage(uri);
		this.geoHandler.getImage().setBitmapImage(
			getBitmap(this.geoHandler.getImage().getUriImage(),
				true));
	    }
	} catch (FileNotFoundException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return myCarPosition;
    }

    private Bitmap getBitmap(Uri uri, boolean isScaled)
	    throws FileNotFoundException, IOException
    {
	ContentResolver content = this.getContentResolver();
	Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(content, uri);
	Bitmap bitmap;
	int originalXPix;
	int originalYPix;
	int proportionY;
	if (isScaled)
	{
	    originalXPix = originalBitmap.getWidth();
	    originalYPix = originalBitmap.getHeight();
	    Log.d("Original image", "tam x image: " + originalXPix
		    + ", tam y image:" + originalYPix);
	    proportionY = (Config.THUMNAIL_SIZE * originalYPix) / originalXPix;

	    Log.d("Resize image", "tam x image: " + Config.THUMNAIL_SIZE
		    + ", tam y image:" + proportionY);
	    bitmap = Bitmap.createScaledBitmap(originalBitmap,
		    Config.THUMNAIL_SIZE, proportionY, false);
	} else
	{
	    bitmap = originalBitmap;
	}
	return bitmap;
    }

    private void createNotification()
    {
	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	// Agregando el icono, texto y momento para lanzar la notificaci�n
	int icon = R.drawable.chrysler;
	CharSequence tickerText = getString(R.string.notification_ticker);

	Notification notification = new Notification(icon, tickerText, 0);

	Context context = getApplicationContext();
	CharSequence contentTitle = getString(R.string.notification_title);
	CharSequence contentText = getString(R.string.notification_text);

	// Agregando que no se elimine cuando se pulse sobre la notificaci�n o
	// sobre el
	// boton clear
	notification.flags |= Notification.FLAG_ONGOING_EVENT
		| Notification.FLAG_NO_CLEAR;

	Intent notificationIntent = new Intent(this, Main.class);
	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		notificationIntent, 0);
	notification.setLatestEventInfo(context, contentTitle, contentText,
		contentIntent);

	mNotificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    private void deleteNotification()
    {
	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	mNotificationManager.cancel(Config.NOTIFICATION_ID);
    }

}