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
import android.graphics.Paint;
import android.graphics.Point;

import com.dabeeo.hangouyou.R;

public class CurrentPositionOverlay extends ItemizedIconOverlay<OverlayItem>
{
	Bitmap m_marker;
	Paint paint;
	int nOffset = 20;
	
	public CurrentPositionOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct, int mylocationstate,float fRotate)
    {
		super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
		
		
		if(mylocationstate == 1)
		{
			int size = Global.DpToPixel(ct, 20);
			m_marker = Global.fitImageSize(ct, R.drawable.icon_map_mylocation, size, size);
		}
		else
		{
			int size = Global.DpToPixel(ct, 27);
			m_marker = Global.fitImageSize(ct, R.drawable.icon_map_mylocation_navi, size, size);
			m_marker = Global.rotate(m_marker, -fRotate);
		}
		
    }

	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3)
	{
		return false;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		//super.draw(canvas, mapView, shadow);
		
		int nSize = size();

		if(nSize == 0)
			return;
		

		OverlayItem item = getItem(0);
		
		GeoPoint geoPoint = (GeoPoint) item.getPoint();
		Point pt1 = new Point();

		// 지리좌표를 화면상의 픽셀좌표로 변환
		mapView.getProjection().toPixels(geoPoint, pt1);

		canvas.drawBitmap(m_marker, pt1.x - m_marker.getWidth()/2, pt1.y - m_marker.getHeight()/2 - nOffset, null);
		
	}
	
	
}
