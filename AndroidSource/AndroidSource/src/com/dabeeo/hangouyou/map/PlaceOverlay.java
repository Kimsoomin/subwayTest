package com.dabeeo.hangouyou.map;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.dabeeo.hangouyou.R;

public class PlaceOverlay extends ItemizedIconOverlay<OverlayItem>
{
  Context m_context;
  Bitmap m_bmpPosition = null;
  
  
  public PlaceOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct)
  {
    super(pList, Global.GetDrawable(ct, R.drawable.marker_default), pOnItemGestureListener, pResourceProxy);
    m_context = ct;
  }
  
  
  public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3)
  {
    Log.d("onSnapToItem", "arg0 = " + arg0 + ", arg1 = " + arg1 + ", arg2 = " + arg2 + ", arg3 = " + arg3);
    return false;
  }
  
  
  @Override
  protected void onDrawItem(Canvas canvas, OverlayItem item, Point curScreenCoords)
  {
    super.onDrawItem(canvas, item, curScreenCoords);
    
    if (m_bmpPosition == null)
    {
      BitmapDrawable d = (BitmapDrawable) Global.GetDrawable(m_context, R.drawable.place_icon);
      
      if (item != null && item.mDescription != null && item.mDescription.equals("0"))
      {
        d = (BitmapDrawable) Global.GetDrawable(m_context, R.drawable.user_place_icon);
      }
      
      Bitmap bmp = d.getBitmap();
      
      m_bmpPosition = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
      
    }
    
    int nOffset = 20;
    
    canvas.drawBitmap(m_bmpPosition, curScreenCoords.x - m_bmpPosition.getWidth() / 2, curScreenCoords.y - m_bmpPosition.getHeight() / 2 - nOffset, null);
  }
  
}
