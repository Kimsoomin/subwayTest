package com.dabeeo.hangouyou.map;

import microsoft.mappoint.TileSystem;

import org.osmdroid.ResourceProxy;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Scroller;

/**
 * MapView that limits scrolling to the area specified. Based on code from Marc
 * Kurtz (http://code.google.com/u/107017135012155810755/)
 */
/** 10-2 MapView 설정 페이지 */
public class BoundedMapView extends MapView
{
  
  protected Rect mScrollableAreaLimit;
  protected BoundingBoxE6 box;
  protected int m_nLastZoomLevel = 0;
  
  
  public BoundedMapView(Context context, ResourceProxy resourceProxy, MapTileProviderBase provider)
  {
    
    super(context, provider.getTileSource().getTileSizePixels(), resourceProxy, provider);
    
  }
  
  
  public BoundedMapView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    // TODO Auto-generated constructor stub
  }
  
  
  public BoundedMapView(Context arg0, int arg1, ResourceProxy arg2, MapTileProviderBase arg3, Handler arg4, AttributeSet arg5)
  {
    super(arg0, arg1, arg2, arg3, arg4, arg5);
    // TODO Auto-generated constructor stub
  }
  
  
  public BoundedMapView(Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler)
  {
    super(context, tileSizePixels, resourceProxy, aTileProvider, tileRequestCompleteHandler);
    // TODO Auto-generated constructor stub
  }
  
  
  // MapView Activity 에서 MapView를 설정해주기 위해 생성자 설정.
  public BoundedMapView(Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider)
  {
    super(context, tileSizePixels, resourceProxy, aTileProvider);
    // TODO Auto-generated constructor stub
  }
  
  
  public BoundedMapView(Context context, int tileSizePixels, ResourceProxy resourceProxy)
  {
    super(context, tileSizePixels, resourceProxy);
    // TODO Auto-generated constructor stub
  }
  
  
  public BoundedMapView(Context context, int tileSizePixels)
  {
    super(context, tileSizePixels);
    // TODO Auto-generated constructor stub
  }
  
  
  /**
   * Set the map to limit it's scrollable view to the specified BoundingBoxE6.
   * Note that, like North/South bounds limiting, this allows an overscroll of
   * half the screen size. This means each border can be scrolled to the center
   * of the screen.
   * 
   * @param box
   *          A lat/long bounding box to limit scrolling to, or null to remove
   *          any scrolling limitations
   */
  /** Map 에 대한 zoom 제한.- PS.형철. */
  public void setScrollableAreaLimit(BoundingBoxE6 box, final int nLastZoomLevel)
  {
    //pixel로 계산 256 의 값을 shift연산으로 ZoomLevel 만큼 곱한 값을 world Map size로 지정.
    final int worldSize_2 = TileSystem.MapSize(nLastZoomLevel) / 2;
    
    // Clear scrollable area limit if null passed.
    if (box == null)
    {
      mScrollableAreaLimit = null;
      return;
    }
    
//        Log.e("DEBUG", "ZOOM LEVEL: " + nLastZoomLevel + "  [MAP SIZE] " + worldSize_2);
    
    m_nLastZoomLevel = nLastZoomLevel;
    
    int nScreenWidth = 720;
    int nScreenHeight = 1280;
    
    // 좌표가 중간값으로 처리되는 것으로 보임. 
    // zoom level 12에 맞춘 위도/경도 중간값으로, 범위 계산에 Offset을 줌. 
    // 
    GeoPoint gp1 = TileSystem.PixelXYToLatLong(0, 0, 12, null);
    GeoPoint gp2 = TileSystem.PixelXYToLatLong(nScreenWidth / 2, nScreenHeight / 2, 12, null);
    
    int nLatitudeOffset = (gp2.getLatitudeE6() - gp1.getLatitudeE6());
    int nLongitudeOffset = (gp2.getLongitudeE6() - gp1.getLongitudeE6());
    
    // Get NW/upper-left
    final Point upperLeft = TileSystem.LatLongToPixelXY((box.getLatNorthE6() - nLatitudeOffset) / 1E6, (box.getLonWestE6() - nLongitudeOffset) / 1E6, nLastZoomLevel, null);
    
    upperLeft.x += nScreenWidth / 2;
    upperLeft.y += nScreenHeight / 2;
    
    upperLeft.offset(-worldSize_2, -worldSize_2);
    
//        Log.e("DEBUG", "upperLeft = " + upperLeft);
    
    // Get SE/lower-right
    final Point lowerRight = TileSystem.LatLongToPixelXY((box.getLatSouthE6() + nLatitudeOffset) / 1E6, (box.getLonEastE6() + nLongitudeOffset) / 1E6, nLastZoomLevel, null);
    
    lowerRight.x -= nScreenWidth / 2;
    lowerRight.y -= nScreenHeight / 2;
    
    lowerRight.offset(-worldSize_2, -worldSize_2);
    
//        Log.e("DEBUG", "lowerRight = " + lowerRight);
    
    mScrollableAreaLimit = new Rect(upperLeft.x, upperLeft.y, lowerRight.x, lowerRight.y);
  }
  
  
  @Override
  public void scrollTo(int x, int y)
  {
    final int worldSize_2 = TileSystem.MapSize(this.getZoomLevel(true)) / 2;
    while (x < -worldSize_2)
    {
      x += worldSize_2 * 2;
    }
    while (x > worldSize_2)
    {
      x -= worldSize_2 * 2;
    }
    if (y < -worldSize_2)
    {
      y = -worldSize_2;
    }
    if (y > worldSize_2)
    {
      y = worldSize_2;
    }
    
    if (mScrollableAreaLimit != null && (m_nLastZoomLevel == getZoomLevel()))
    {
//            final int zoomDiff = m_nLastZoomLevel - getZoomLevel();
//            final int minX = mScrollableAreaLimit.left >> zoomDiff;
//            final int minY = mScrollableAreaLimit.top >> zoomDiff;
//            final int maxX = mScrollableAreaLimit.right >> zoomDiff;
//            final int maxY = mScrollableAreaLimit.bottom >> zoomDiff;
      
      final int minX = mScrollableAreaLimit.left;
      final int minY = mScrollableAreaLimit.top;
      final int maxX = mScrollableAreaLimit.right;
      final int maxY = mScrollableAreaLimit.bottom;
      if (x < minX)
        x = minX;
      else if (x > maxX)
        x = maxX;
      if (y < minY)
        y = minY;
      else if (y > maxY)
        y = maxY;
      
      Log.i("SCROLL", String.format("[x] %d [y] %d [minX] %d [minY] %d", x, y, minX, minY));
    }
    super.scrollTo(x, y);
    
    // do callback on listener
    if (mListener != null)
    {
      final ScrollEvent event = new ScrollEvent(this, x, y);
      mListener.onScroll(event);
    }
  }
  
  
  @Override
  public void computeScroll()
  {
    final Scroller mScroller = getScroller();
    final int mZoomLevel = getZoomLevel(false);
    
    if (mScroller.computeScrollOffset())
    {
      if (mScroller.isFinished())
      {
        /**
         * Need to jump through some accessibility hoops here Silly enough the
         * only thing MapController.setZoom does is call
         * MapView.setZoomLevel(zoomlevel). But noooo .. if I try that directly
         * setZoomLevel needs to be set to "protected". Explanation can be found
         * at http://docs.oracle.com/javase/tutorial
         * /java/javaOO/accesscontrol.html This also suggests that if the
         * subclass is made to be part of the package, this can be replaced by a
         * simple call to setZoomLevel(mZoomLevel)
         */
        // This will facilitate snapping-to any Snappable points.
        getController().setZoom(mZoomLevel);
      }
      else
      {
        /* correction for double tap */
        int targetZoomLevel = getZoomLevel();
        if (targetZoomLevel == mZoomLevel)
          scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      }
      postInvalidate(); // Keep on drawing until the animation has
      // finished.
    }
  }
}
