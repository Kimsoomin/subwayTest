package com.dabeeo.hanhayou.map;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.dabeeo.hanhayou.R;

public class OnePlaceOverlay extends ItemizedIconOverlay<OverlayItem> {
	
	Context m_context;
	Bitmap m_bmpPosition = null;
	
	int nOffset = 20;

	public OnePlaceOverlay(ArrayList<OverlayItem> pList,ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
			ResourceProxy pResourceProxy, Context ct) 
	{
		super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
		m_context = ct;
		
		int size = Global.DpToPixel(ct, 40);
		m_bmpPosition = Global.fitImageSize(ct, R.drawable.pin_map_place, size, size);
	}
	
	@Override
	protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords, float orientation) 
	{
		super.onDrawItem(canvas, item, curScreenCoords, orientation);
		canvas.drawBitmap(m_bmpPosition,curScreenCoords.x,curScreenCoords.y - m_bmpPosition.getHeight()+2 - nOffset, null);
	}	

}
