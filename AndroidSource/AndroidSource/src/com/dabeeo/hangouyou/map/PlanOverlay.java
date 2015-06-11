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
  
  int offset = 100;
  
  Paint paint;
  Paint textpaint;
  
  Bitmap pinPlan;
  Bitmap pinPlan_on;
  Bitmap flagPlan;
  Bitmap flagPlan_on;
  Bitmap pin_map;
  
  String day;
  int size;
  
  String dayStr;
  
  int nSize;
  int spotNum = 0;
  int textsize;
  Typeface typeface;
  float scale;
  String idx = "";
  
  public PlanOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
      ResourceProxy pResourceProxy, Context ct, String day) 
  {
    super(pList, Global.GetDrawable(ct, R.drawable.transparent), pOnItemGestureListener, pResourceProxy);
    
    m_context = ct;
    this.day = day;
    
    size = Global.DpToPixel(m_context, 35);
    
    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    
    pinPlan = Global.fitImageSize(m_context, R.drawable.pin_plan, size, size);
    flagPlan = Global.fitImageSize(m_context, R.drawable.flag_plan_day, size, size);
    
    pinPlan_on = Global.fitImageSize(m_context, R.drawable.pin_plan_on, size, size);
    flagPlan_on = Global.fitImageSize(m_context, R.drawable.flag_plan_day_on, size, size);
    
    int nSzie = Global.DpToPixel(m_context, 40);
    pin_map = Global.fitImageSize(m_context, R.drawable.pin_map_place, nSzie, nSzie);
    
    paint = new Paint();
    paint.setARGB(200, 172, 186, 191);
    paint.setStrokeWidth(6);
    paint.setStrokeCap(Cap.ROUND);
    
    textpaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
    textpaint.setColor(Color.rgb(255, 255, 255));
    textpaint.setTextSize(40);
    textpaint.setTextAlign(Align.CENTER);
    textpaint.setTypeface(typeface);
  }
  
  public void dayset(String day)
  {
    this.day = day;
  }
  
  public void changeMethod(String idx)
  {
    this.idx = idx;
  }
  
  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow) 
  {
    // TODO Auto-generated method stub
    super.draw(canvas, mapView, shadow);
    
    nSize = size();
    
    for(int i = 0; i < nSize; i++)
    {
      OverlayItem overlayitem = getItem(i);
      
      GeoPoint geoPoint = overlayitem.getPoint();
      Point pt1 = mapView.getProjection().toPixels(geoPoint, null);
      
      if(i != nSize-1)
      {
        OverlayItem nextItem = getItem(i+1);
        Point pt2 = mapView.getProjection().toPixels(nextItem.getPoint(), null);
        if(overlayitem.mDescription.equals(nextItem.mDescription))
          canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
      }
    }
    
    for (int i = 0; i < nSize; i++)
    {
      OverlayItem overlayitem = getItem(i);
      
      GeoPoint geoPoint = overlayitem.getPoint();
      Point pt1 = mapView.getProjection().toPixels(geoPoint, null);
      
      if(i > 0)
      {
        OverlayItem preItem = getItem(i-1);
        if(overlayitem.mDescription.equals(preItem.mDescription) == false)
          spotNum = 0;
      }else if(i == 0)
      {
        spotNum = 0;
      }
      
      spotNum = spotNum + 1;
      
      if(overlayitem.mUid.equals(idx))
      {
        canvas.drawBitmap(pin_map, pt1.x-offset, pt1.y - pinPlan.getHeight()-offset, null);
      }else
      {
        if(overlayitem.mDescription.equals(day))
        {      
          if(spotNum == 1)
          {
            dayStr = "第"+overlayitem.mDescription+"天";
            canvas.drawBitmap(flagPlan_on, pt1.x+pinPlan_on.getWidth()/3, pt1.y-pinPlan_on.getHeight()/7*6 ,null);
            canvas.drawText(dayStr, pt1.x+pinPlan_on.getWidth(), pt1.y-pinPlan_on.getHeight()/2, textpaint);
          }
          
          canvas.drawBitmap(pinPlan_on, pt1.x-pinPlan_on.getWidth()/2, pt1.y - pinPlan_on.getHeight(), null);
          canvas.drawText(""+spotNum, pt1.x, pt1.y- pinPlan_on.getHeight()/2, textpaint);
        }else
        {
          if(spotNum == 1)
          {
            dayStr = "第"+overlayitem.mDescription+"天";
            canvas.drawBitmap(flagPlan, pt1.x+pinPlan_on.getWidth()/3, pt1.y-pinPlan_on.getHeight()/7*6 ,null);
            canvas.drawText(dayStr, pt1.x+pinPlan_on.getWidth(), pt1.y-pinPlan_on.getHeight()/2, textpaint);
          }
          
          canvas.drawBitmap(pinPlan, pt1.x-pinPlan_on.getWidth()/2, pt1.y - pinPlan_on.getHeight(), null);
          canvas.drawText(""+spotNum, pt1.x, pt1.y- pinPlan_on.getHeight()/2, textpaint);
        }
      }
    }
  }
}
