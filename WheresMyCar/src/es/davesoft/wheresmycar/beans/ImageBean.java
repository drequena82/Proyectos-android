package es.davesoft.wheresmycar.beans;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageBean implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 709429612123155712L;
    
    private Bitmap bitmapImage;
    private Uri uriImage;
    public Bitmap getBitmapImage()
    {
        return bitmapImage;
    }
    public void setBitmapImage(Bitmap bitmapImage)
    {
        this.bitmapImage = bitmapImage;
    }
    public Uri getUriImage()
    {
        return uriImage;
    }
    public void setUriImage(Uri uriImage)
    {
        this.uriImage = uriImage;
    }
    
    

}
