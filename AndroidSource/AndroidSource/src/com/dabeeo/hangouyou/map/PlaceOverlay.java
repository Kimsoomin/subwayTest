package com.dabeeo.hangouyou.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.dabeeo.hangouyou.R;

public class PlaceOverlay extends ItemizedIconOverlay<OverlayItem> {

	Integer[] indexCategory = {1,2,3,4,5,6,7,8,40,50,60,70,80,99};
	Integer[] passCategory = {9};

	Context m_context;
	BitmapDrawable d;
	BitmapDrawable d2;
	Bitmap place_marker = null;
	Bitmap pin_map = null;
	int category;
	String idx;
	int nOffset = 20;

	GeoPoint topLeftGpt;
	GeoPoint bottomRightGpt;

	List<Drawable> categoryDrawable;

	public PlaceOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
			ResourceProxy pResourceProxy, Context ct, List<Drawable> categoryDrawable) 
	{
		super(pList, Global.GetDrawable(ct, R.drawable.transparent),
				pOnItemGestureListener, pResourceProxy);
		m_context = ct;
		
		int size = Global.DpToPixel(m_context, 40);
		pin_map = Global.fitImageSize(m_context, R.drawable.pin_map_place, size, size);
    
		this.categoryDrawable = categoryDrawable;
	}

	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) 
	{
		Log.d("onSnapToItem", "arg0 = " + arg0 + ", arg1 = " + arg1 + ", arg2 = " + arg2 + ", arg3 = " + arg3);
		return false;
	}

	// - category 선택
	public BitmapDrawable intDrawalbe() 
	{
		if(Arrays.asList(passCategory).contains(category)) 
		{
			return null;
		}
		int index = Arrays.asList(indexCategory).indexOf(category);
		d = (BitmapDrawable) categoryDrawable.get(index);

		return d;
	}

	public void changeMethod(String idx) 
	{
		this.idx = idx;
	}

	@Override
	protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords, float orientation) 
	{
		super.onDrawItem(canvas, item, curScreenCoords, orientation);

		category = Integer.parseInt(item.getUid());

		this.d = intDrawalbe();

		if(d == null) {
			return;
		}

		if (item.getSnippet().equals(idx)) 
		{
			canvas.drawBitmap(pin_map, curScreenCoords.x, curScreenCoords.y - pin_map.getHeight()+2 - nOffset, null);
		} else 
		{
		  place_marker = this.d.getBitmap();
			canvas.drawBitmap(place_marker, curScreenCoords.x - place_marker.getWidth()/2, curScreenCoords.y - place_marker.getHeight()/2 - nOffset, null);
		}
	}

}
