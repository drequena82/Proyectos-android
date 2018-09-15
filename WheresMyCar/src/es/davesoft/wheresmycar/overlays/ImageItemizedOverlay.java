package es.davesoft.wheresmycar.overlays;

/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ImageItemizedOverlay<Item extends OverlayItem> extends
	BalloonItemizedOverlay<ImageOverlayItem>
{

    private ArrayList<ImageOverlayItem> m_overlays = new ArrayList<ImageOverlayItem>();
    private Context c;

    public ImageItemizedOverlay(Drawable defaultMarker, MapView mapView)
    {
	super(boundCenter(defaultMarker), mapView);
	c = mapView.getContext();
    }

    public void addOverlay(ImageOverlayItem overlay)
    {
	m_overlays.add(overlay);
	populate();
    }

    @Override
    protected ImageOverlayItem createItem(int i)
    {
	return m_overlays.get(i);
    }

    @Override
    public int size()
    {
	return m_overlays.size();
    }

    @Override
    protected boolean onBalloonTap(int index, ImageOverlayItem item)
    {
	Intent viewPicture = null;

	if (item.getImageURL() != null)
	{
	    viewPicture = new Intent(Intent.ACTION_VIEW);
	    viewPicture.setDataAndType(item.getImageURL(), "image/jpg");
	    c.startActivity(viewPicture);
	}
	return true;
    }

    @Override
    protected BalloonOverlayView<ImageOverlayItem> createBalloonOverlayView()
    {
	// use our custom balloon view with our custom overlay item type:
	return new ImageBalloonOverlayView<ImageOverlayItem>(getMapView()
		.getContext(), getBalloonBottomOffset());
    }

}
