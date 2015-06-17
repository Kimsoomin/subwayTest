package com.dabeeo.hangouyou.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.dabeeo.hangouyou.R;

public class SubwayExitOverlay extends ItemizedIconOverlayEx<OverlayItem> 
{
	String [] indexExit = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","2-1","3-1",
			"6-1","8-1","8-2","9-1"};
	String exit;
	BitmapDrawable d;
	Context m_context;
	Bitmap m_bmpPosition = null;
	int nOffset = 20;
	List<Drawable> subwayExitDrawable;

	public SubwayExitOverlay(ArrayList<OverlayItem> pList,ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
			ResourceProxy pResourceProxy, Context ct, List<Drawable> subwayExitDrawable) 
	{
		super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
		this.subwayExitDrawable = subwayExitDrawable;
	}

	public BitmapDrawable intDrawalbe() 
	{
		int index = Arrays.asList(indexExit).indexOf(exit);
		d = (BitmapDrawable) subwayExitDrawable.get(index);

		return d;
	}
	
	@Override
	protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords, float orientation) 
	{
		
		exit = item.getTitle();
		this.d = intDrawalbe();
		
		m_bmpPosition = this.d.getBitmap();
		
		canvas.drawBitmap(m_bmpPosition, curScreenCoords.x - m_bmpPosition.getWidth()/2, curScreenCoords.y - m_bmpPosition.getHeight()/2 - nOffset, null);
	}

}
