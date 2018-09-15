package es.davesoft.wheresmycar.overlays;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyPositionItemizedOverlay extends
	BalloonItemizedOverlay<OverlayItem>
{
    private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
    public MyPositionItemizedOverlay(Drawable defaultMarker, MapView mapView)
    {
	super(boundCenter(defaultMarker), mapView);
	mapView.getContext();
    }
   
    public void addOverlay(OverlayItem overlay)
    {
	m_overlays.add(overlay);
	populate();
    }

    @Override
    protected OverlayItem createItem(int i)
    {
	return m_overlays.get(i);
    }

    @Override
    public int size()
    {
	return m_overlays.size();
    }

    @Override
    protected boolean onBalloonTap(int index, OverlayItem item)
    {
	return true;
    }

    @Override
    protected BalloonOverlayView<OverlayItem> createBalloonOverlayView()
    {
	// use our custom balloon view with our custom overlay item type:
	return new BalloonOverlayView<OverlayItem>(getMapView().getContext(),
		getBalloonBottomOffset());
    }

}
