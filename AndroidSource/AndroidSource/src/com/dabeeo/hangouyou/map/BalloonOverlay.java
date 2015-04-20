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
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.text.TextUtils;

import com.dabeeo.hangouyou.R;

public class BalloonOverlay extends ItemizedIconOverlayEx<OverlayItem>
{
  int m_color = Color.WHITE;
  Bitmap m_bitmap = null;
  int m_nOffset = -1;
  
  
  public BalloonOverlay(ArrayList<OverlayItem> pList, ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener, ResourceProxy pResourceProxy, Context ct, int nColor, int nHOffset)
  {
    super(pList, Global.GetDrawable(ct, R.drawable.marker_default), pOnItemGestureListener, pResourceProxy);
    
    // 터치 영역을 옮김.
    SetTouchOffset(50);
    
    m_color = nColor;
    m_nOffset = nHOffset;
//		BitmapDrawable bd = (BitmapDrawable) getItem(0).getMarker(0);
//		
//		m_bitmap = bd.getBitmap();
    OverlayItem item = getItem(0);
    
    if (item == null)
      return;
    
    int nPlaceID = Integer.valueOf(item.mDescription);
    int nDrawableID = R.drawable.detail_box_re;
//		PlaceInfo placeInfo = BlinkingSeoulActivity.g_db.GetPlaceInfo(nPlaceID);
//		if(placeInfo != null)
//		{
//			if(placeInfo.m_nType == 5)
//			{
//				nDrawableID = R.drawable.transfer_box_re;
//			}
//		}
    
    BitmapDrawable bd = (BitmapDrawable) Global.ResizeDrawable(ct.getResources(), nDrawableID, 510, 146);
    
    if (bd == null)
      return;
    
    m_bitmap = bd.getBitmap();
  }
  
  
  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow)
  {
    super.draw(canvas, mapView, shadow);
    
    int nSize = size();
    
    if (nSize == 0)
      return;
    
    TextPaint paintText = new TextPaint();
    
    paintText.setColor(m_color);
    paintText.setTextSize(30);
    paintText.setTextAlign(Paint.Align.CENTER);
    
    OverlayItem item = getItem(0);
    
    GeoPoint geoPoint = item.getPoint();
    Point pt1 = new Point();
    
    // 지리좌표를 화면상의 픽셀좌표로 변환
    mapView.getProjection().toPixels(geoPoint, pt1);
    
    int nOffset = 63; //v1.5 offset 위치 변경 말풍선 위치 변경. + 위로 - 아래 / 기존 +30
    
    if (m_nOffset > 0)
      nOffset = m_nOffset;
    
    canvas.drawBitmap(m_bitmap, pt1.x - m_bitmap.getWidth() / 2, pt1.y - m_bitmap.getHeight() - nOffset, null);
    
    String strText = (String) TextUtils.ellipsize(item.mTitle, (TextPaint) paintText, m_bitmap.getWidth() - 40, TextUtils.TruncateAt.END);
    
    canvas.drawText(strText, pt1.x, pt1.y - 76 - nOffset, paintText);
  }
  
}
