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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import android.graphics.Typeface;

import com.dabeeo.hangouyou.R;

public class PlanOverlay  extends ItemizedIconOverlay<OverlayItem> 
{

	Context m_context;

	int offset = 20;
	Paint paint;
	Paint textpaint;
	Bitmap pinPlan;
	Bitmap flagPlan;
	String day;
	int size;
	int bmpWidth;
	int bmpHeight;
	
	int spotNum;
	int textsize;
	Typeface typeface;
	float scale;

	public PlanOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
			ResourceProxy pResourceProxy, Context ct, String day) 
	{
		super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);

		m_context = ct;
		this.day = day;

		size = Global.DpToPixel(m_context, 30);		
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

		paint = new Paint();
		paint.setARGB(255, 172, 186, 191);
		paint.setStrokeWidth(4);
		paint.setStrokeCap(Cap.ROUND);
		
		textpaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
		textpaint.setColor(Color.rgb(255, 255, 255));
		textpaint.setTextSize(19);
		textpaint.setTextAlign(Align.CENTER);
		textpaint.setTypeface(typeface);
	}

	public void dayset(String day)
	{
		this.day = day;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);

		int nSize = size();
		String dayStr;
		if(getItem(0).mDescription.equals(day))
		{
			pinPlan = Global.fitImageSize(m_context, R.drawable.pin_plan_on, size, size);
			flagPlan = Global.fitImageSize(m_context, R.drawable.flag_plan_day_on, size, size);
		}else
		{
			pinPlan = Global.fitImageSize(m_context, R.drawable.pin_plan, size, size);
			flagPlan = Global.fitImageSize(m_context, R.drawable.flag_plan_day, size, size);
		}
		
		for(int i = 0; i < nSize; i++)
		{
			OverlayItem overlayitem = getItem(i);

			GeoPoint geoPoint = overlayitem.getPoint();
			Point pt1 = mapView.getProjection().toPixels(geoPoint, null);
			
			if(i != 0)
			{
				OverlayItem nextItem = getItem(i-1);
				Point pt2 = mapView.getProjection().toPixels(nextItem.getPoint(), null);
				canvas.drawLine(pt1.x+pinPlan.getWidth()/2, pt1.y-offset, pt2.x+pinPlan.getWidth()/2, pt2.y-offset, paint);
			}
		}

		for (int i = 0; i < nSize; i++)
		{
			spotNum = i+1;
			OverlayItem overlayitem = getItem(i);

			GeoPoint geoPoint = overlayitem.getPoint();
			Point pt1 = mapView.getProjection().toPixels(geoPoint, null);
			
			if(i==0)
			{
				dayStr = "第"+overlayitem.mDescription+"天";
				canvas.drawBitmap(flagPlan, pt1.x+pinPlan.getWidth()-5, pt1.y-pinPlan.getHeight()-10,null);
				canvas.drawText(dayStr, pt1.x+pinPlan.getWidth()+23, pt1.y-pinPlan.getHeight()+10, textpaint);
			}
			
			canvas.drawBitmap(pinPlan, pt1.x, pt1.y - pinPlan.getHeight() - offset, null);
			canvas.drawText(""+spotNum, pt1.x+pinPlan.getWidth()/2-1, pt1.y- pinPlan.getHeight()+5, textpaint);
		}
	}
}
