package es.davesoft.wheresmycar.overlays;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ImageOverlayItem extends OverlayItem
{
    protected Uri mImageURL;
    protected Bitmap image;

    public ImageOverlayItem(GeoPoint point, String title, String snippet,
	    Uri imageURL, Bitmap image)
    {
	super(point, title, snippet);
	mImageURL = imageURL;
	this.image = image;
    }

    public Uri getImageURL()
    {
	return mImageURL;
    }

    public void setImageURL(Uri imageURL)
    {
	this.mImageURL = imageURL;
    }

    public Bitmap getImage()
    {
	return image;
    }

    public void setImage(Bitmap image)
    {
	this.image = image;
    }
}
