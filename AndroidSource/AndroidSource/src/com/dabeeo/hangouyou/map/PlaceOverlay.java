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

	Integer[] indexCategory = {1,2,3,4,5,6,7,40,50,60,70,80,99};
	Integer[] passCategory = {8,9};

	Context m_context;
	BitmapDrawable d;
	BitmapDrawable d2;
	Bitmap m_bmpPosition = null;
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

	public void changeMethod(BitmapDrawable drawable, String idx) 
	{
		this.d2 = drawable;
		this.idx = idx;
	}

	@Override
	protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords) 
	{
		super.onDrawItem(canvas, item, curScreenCoords);

		category = Integer.parseInt(item.mUid);

		this.d = intDrawalbe();

		if(d == null) {
			return;
		}

		if (item.mDescription.equals(idx)) 
		{
			d = d2;

			int size = Global.DpToPixel(m_context, 40);
			m_bmpPosition = Global.fitImageSize(m_context, R.drawable.pin_map_place, size, size);
			canvas.drawBitmap(m_bmpPosition, curScreenCoords.x, curScreenCoords.y - m_bmpPosition.getHeight()+2 - nOffset, null);
		} else 
		{
			m_bmpPosition = this.d.getBitmap();

			canvas.drawBitmap(m_bmpPosition, curScreenCoords.x - m_bmpPosition.getWidth()/2, curScreenCoords.y - m_bmpPosition.getHeight()/2 - nOffset, null);
		}
	}

}
