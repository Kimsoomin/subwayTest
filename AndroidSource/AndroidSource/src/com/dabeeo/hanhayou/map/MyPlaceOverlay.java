package com.dabeeo.hanhayou.map;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.dabeeo.hanhayou.R;

public class MyPlaceOverlay extends ItemizedIconOverlay<OverlayItem>
{
	Context m_context;
	Bitmap m_bmpPosition = null;
	private boolean m_bShowMarker = false;
	
	public MyPlaceOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct)
    {
        super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
        m_context = ct;
    }

	@Override
	protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords)
	{
		super.onDrawItem(canvas, item, curScreenCoords);

		int nOffset = 20;

		if(m_bShowMarker == true)
			canvas.drawBitmap(m_bmpPosition, curScreenCoords.x - m_bmpPosition.getWidth()/2, curScreenCoords.y - m_bmpPosition.getHeight()/2 - nOffset, null);
	}

	public void SetMarkerID(Context c, int nMarkerID)
	{
		Drawable marker = Global.GetDrawable(c, nMarkerID);
		Bitmap d = ((BitmapDrawable)marker).getBitmap();
		
		m_bmpPosition = Bitmap.createScaledBitmap(d, 82, 100, false);
	}

	@Override
	public boolean onLongPress(MotionEvent event, MapView mapView)
	{
		Log.i("...", "" + event);
		
		BlinkingMap parentActivity = (BlinkingMap) m_context;
		
		IGeoPoint igp = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY());
		
		GeoPoint gp = new GeoPoint(igp.getLatitudeE6(), igp.getLongitudeE6());
		
		double fLatitude = gp.getLatitudeE6() / 1e6;
		double fLongitude = gp.getLongitudeE6() / 1e6;
		
//		parentActivity.ShowMyPlacePopup(fLatitude, fLongitude);
		//dialogcreate(fLatitude, fLongitude);
		
		return super.onLongPress(event, mapView);
	}
	
	public void dialogcreate(final float lat, final float lng) 
	{
		final BlinkingMap blinkingMap = (BlinkingMap) m_context;
		
		AlertDialog.Builder ab = new AlertDialog.Builder(blinkingMap);

		final EditText tv1 = new EditText(blinkingMap);
		ab.setTitle("장소 저장");
		ab.setCancelable(false);
		ab.setView(tv1);
		ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				//TO DO setBallonMarker, MyPlace Save
//				if(tv1.getText().length() != 0)
//					blinkingMap.setBalloonMarker(0,0,lat,lng, tv1.getText().toString());
//				else
//					blinkingMap.RemoveMyPlacePopup(lat, lng);
			}
		});

		ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
//				blinkingMap.RemoveMyPlacePopup(lat, lng);
			}
		});
		ab.show();
	}

	public void ShowMarker(boolean b)
	{
		m_bShowMarker = b;
	}
	
	public boolean GetMarkerShow()
	{
		return m_bShowMarker;
	}

}
