package com.dabeeo.hanhayou.map;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class ItemizedIconOverlayEx<Item extends OverlayItem> extends ItemizedIconOverlay<Item>
{
	private int m_nOffsetY = 0;

	public ItemizedIconOverlayEx(
			Context pContext,
			List<Item> pList,
			org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<Item> pOnItemGestureListener) 
	{
		super(pContext, pList, pOnItemGestureListener);
	}

	public ItemizedIconOverlayEx(
			final List<Item> pList,
			final Drawable pDefaultMarker,
			final org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<Item> pOnItemGestureListener,
			final ResourceProxy pResourceProxy)
	{
		super(pList, pDefaultMarker, pOnItemGestureListener, pResourceProxy);
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent event, final MapView mapView)
	{
		float x = event.getX();
		float y = event.getY();
		
		MotionEvent me = event;
		
		me.setLocation(event.getX(), event.getY() + m_nOffsetY);
		
		boolean b = super.onSingleTapUp(me, mapView);

		event.setLocation(x, y);
		
		if(b == true)
			return true;

		return super.onSingleTapUp(event, mapView);
	}
	
	public void SetTouchOffset(int nOffsetY)
	{
		m_nOffsetY = nOffsetY;
	}
}
