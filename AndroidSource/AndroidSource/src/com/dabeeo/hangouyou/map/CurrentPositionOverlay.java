package com.dabeeo.hangouyou.map;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

import com.dabeeo.hangouyou.R;

public class CurrentPositionOverlay extends ItemizedIconOverlay<OverlayItem>
{
	Bitmap m_marker;

	public CurrentPositionOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct, int mylocationstate)
    {
		super(pList, Global.GetDrawable(ct, R.drawable.marker_default), pOnItemGestureListener, pResourceProxy);
		
		BitmapDrawable marker = null;
		
		if(mylocationstate == 1)
		{
			marker = (BitmapDrawable) Global.GetDrawable(ct, R.drawable.icon_map_mylocation);
			 m_marker = Bitmap.createScaledBitmap(marker.getBitmap(), 50, 50, false);
		}
		else
		{
			marker = (BitmapDrawable) Global.GetDrawable(ct, R.drawable.icon_map_mylocation_navi);
			 m_marker = Bitmap.createScaledBitmap(marker.getBitmap(), 50, 50, false);
		}
        
       
    }

	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3)
	{
		return false;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
//		super.draw(canvas, mapView, shadow);

		int nSize = size();

		if(nSize == 0)
			return;
		
		int nOffset = 20;

		OverlayItem item = getItem(0);
		
		GeoPoint geoPoint = item.getPoint();
		Point pt1 = new Point();

		// 지리좌표를 화면상의 픽셀좌표로 변환
		mapView.getProjection().toPixels(geoPoint, pt1);
		
		canvas.drawBitmap(m_marker, pt1.x - 25, pt1.y - 25 - nOffset, null);
	}
}
