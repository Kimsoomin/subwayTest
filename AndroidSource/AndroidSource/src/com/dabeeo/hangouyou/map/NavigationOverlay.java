package com.dabeeo.hangouyou.map;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.dabeeo.hangouyou.R;

public class NavigationOverlay extends ItemizedIconOverlayEx<OverlayItem> {

	Context mContext;
	int offset = 20;
	
	Bitmap m_bmpPosition;
	Bitmap m_bmpPosition2;
	
	public NavigationOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct,float fRotate)
	{
		super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
		mContext = ct;
		
		int size = Global.DpToPixel(ct, 27);
		m_bmpPosition = Global.fitImageSize(ct, R.drawable.icon_map_mylocation_navi, size, size);
		m_bmpPosition = Global.rotate(m_bmpPosition, -fRotate);
		
		size = Global.DpToPixel(ct, 30);
		m_bmpPosition2 = Global.fitImageSize(ct, R.drawable.pin_map_place, size, size);
		
		Log.i("angle", "angle : " + fRotate);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		
		Paint paint = new Paint();
		paint.setARGB(89, 87, 106, 117);
		paint.setStrokeWidth(5);
		
		OverlayItem overlayitem = getItem(0);
		OverlayItem overlayItem2 = getItem(1);
		
		GeoPoint geoPoint = (GeoPoint) overlayitem.getPoint();
		GeoPoint geoPoint2 = (GeoPoint) overlayItem2.getPoint();
		Point pt1 = mapView.getProjection().toPixels(geoPoint, null);
		Point pt2 = mapView.getProjection().toPixels(geoPoint2, null);
		canvas.drawLine(pt1.x, pt1.y-offset, pt2.x, pt2.y-offset, paint);
		
		canvas.drawBitmap(m_bmpPosition, pt1.x - m_bmpPosition.getWidth()/2, pt1.y - m_bmpPosition.getHeight()/2 - offset, null);
		canvas.drawBitmap(m_bmpPosition2, pt2.x, pt2.y - m_bmpPosition2.getHeight()+2 - offset, null);
	}
	
}
