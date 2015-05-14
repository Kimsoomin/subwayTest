package com.dabeeo.hangouyou.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import microsoft.mappoint.TileSystem;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.map.SensorUpdater.SensorUpdaterCallback;

public class BlinkingMap extends Activity implements OnClickListener, SensorUpdaterCallback
{
  
  static public boolean g_bNeedToReloadActivity = false;
  
  // - Location
  private double m_fLatitude = 37.5716550;//37.5664700;
  private double m_fLongitude = 126.9862330;//126.9779630;
  private int zoomLevel = 15;
  final int nMinZoomLevel = 14;
  final int nMaxZoomLevel = 17;
  private BoundedMapView m_mapView;
  private Location m_locTarget;
  private Location loc;
  private boolean isShowMarker = false;
  private FrameLayout m_frNavigation;
  
  private int m_nIndexNearbyPlaceOverlay = 0;
  private boolean m_bPlaceInfoMap = true;
  
  private ToggleButton m_btnCurrent;
  
  private Location m_locMarkTarget;
  
  // 위치 정보 사용 여부
  private boolean m_bUseLocationInfo = false;
  private boolean m_bTrackingCurrentPosition = false;
  
  private LinearLayout m_mapLayout;
  private RotateViewGroup m_rotateView;
  
  private boolean m_bTarget = false; // - 임의 위치 외에 다른 Target이 있을 때
  
  private int m_nPlaceID = -1; // - Nearby에서 중복으로 표시되는 부분 구별 (Nearby에서만 사용)
  
  private int m_nBalloonHeight = -1;
  
  private Drawable noTile = null;
  
  BoundingBoxE6 m_boundingBox = new BoundingBoxE6(37.70453488762476, 127.17361450195312, 37.43677099171195, 126.76300048828125);
  
  private final int m_iLimitDistance = 100;// 하단 네비게이션 목적지의 대표 이미지가 보이는 거리.
  private int m_iMeter = 0;
  private String m_strToastItem[] = { "현 위치와 거리오차가 발생 할 수 있습니다", "GPS point  will have some tolerance with your current location." };
  private boolean m_bImageCheck = false; // 하단 대표 이미지의 Visible/InVisible을 결정.
  private boolean m_bImageLock = true; // 하단 대표 이미지 선택시 이미지 변경 안되게 잠금 결정.
  private String m_strPlaceImage;
  private ImageButton ib_nearImage;
  
  String[] strMessage = { "“Blinking Tour Seoul” would like to use\nYour current location.", "“블링킹 투어 서울”에서\n현재 위치정보를 사용하고자 합니다." };
  String[] strToastMessage = { "You arrived at your destination.", "목적지에 도착하였습니다." };
  String[] strTopInfoMessage = { "You can get detailed transportation info on blinking website.", "현재 코스에 대한 자세한 교통 정보는 Blinking Web Site 에서 참고 할 수 있습니다." };
  
  private final int LANGUAGE_DEFUALT = 0;
  private final int LANGUAGE_KOREA = 1;
  private final int LANGUAGE_ENGLISH = 2;
  
  private boolean m_bComplete = false;
  // private boolean m_bCousrseInfoMap = false;
  // private boolean m_bMyCousrseInfoMap = false;
  
  int nCount = 100;
  
  // - Marker 관련.
  private MyPlaceOverlay m_myplaceOverlay;
  private BalloonOverlay m_balloonOverlay;
  private PlaceOverlay m_placeOverlay;
  private CurrentPositionOverlay m_curPosOverlay;
  
  //- Place 임시 정보 
  PlaceInfo m_cache;
  
  //- map button
  public ImageButton myLocationBtn;
  public ImageButton nearByBtn;
  public ImageButton subwayBtn;
  public ImageButton zoomInBtn;
  public ImageButton zoomOutBtn;
  int myLocationState = 0;
  
  //- summary view
  public FrameLayout summaryview;
  public ImageButton summaryCloseBtn;
  public ImageView summaryImage;
  public TextView summaryTitle;
  public TextView summarysubTitle;
  public TextView summarylikeCount;
  public Button subwayStartBtn;
  public Button subwayEndBtn;
  public ImageButton naviBtn;
  
  private float subway_m_fLatitude;
  private float subway_m_fLongitude;
  
  // - Class 간 전달할 데이터
  public BigInteger idx;
  public String lineId;
  public int placeType = 0;
  public int planType = 1;
  
  ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
  
//  public testDB testDB;
  List<PlaceInfo> placeinfo = new ArrayList<PlaceInfo>();
  
  /**
   * ======================================================================== ==
   * =========== 단말기의 방향을 감지. - 네비게이션에 필요.
   * ======================================
   * ===============================================
   */
  private SensorManager m_sensorManager;
  private LocationManager m_locManager;
  private LocationListenerEx m_locListenerGPS;
  private LocationListenerEx m_locListenerNetwork;
  
  static public Location g_locationHere = null; // 자신의 위치.
  static public SensorUpdaterCallback g_sensorcallback = null;
  static public float g_fLastAngle = 0; // - 1,2도 오차는 return
  
  private final SensorEventListener m_sensorListener = new SensorEventListener()
  {
    private int iRotation = -1;
    static private final int RING_BUFFER_SIZE = 10;
    private float[][][] mAnglesRingBuffer = new float[RING_BUFFER_SIZE][3][2];
    private int mNumAngles;
    private int mRingBufferIndex;
    private float[][] mAngles = new float[3][2];
    
    
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
      
    }
    
    
    /** 130416 공식과 동작이 정확하지 않다. 다시 분석 요망 */
    public void onSensorChanged(SensorEvent event)
    {
      // sensor type 이 방향 센서이면 동작.
      if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
      {
        if (mNumAngles == RING_BUFFER_SIZE)
        {
          // subtract oldest vector
          mAngles[0][0] -= mAnglesRingBuffer[mRingBufferIndex][0][0];
          mAngles[0][1] -= mAnglesRingBuffer[mRingBufferIndex][0][1];
          mAngles[1][0] -= mAnglesRingBuffer[mRingBufferIndex][1][0];
          mAngles[1][1] -= mAnglesRingBuffer[mRingBufferIndex][1][1];
          mAngles[2][0] -= mAnglesRingBuffer[mRingBufferIndex][2][0];
          mAngles[2][1] -= mAnglesRingBuffer[mRingBufferIndex][2][1];
        }
        else
        {
          mNumAngles++;
        }
        
        // convert angles into x/y
        mAnglesRingBuffer[mRingBufferIndex][0][0] = (float) Math.cos(Math.toRadians(event.values[0]));
        mAnglesRingBuffer[mRingBufferIndex][0][1] = (float) Math.sin(Math.toRadians(event.values[0]));
        mAnglesRingBuffer[mRingBufferIndex][1][0] = (float) Math.cos(Math.toRadians(event.values[1]));
        mAnglesRingBuffer[mRingBufferIndex][1][1] = (float) Math.sin(Math.toRadians(event.values[1]));
        mAnglesRingBuffer[mRingBufferIndex][2][0] = (float) Math.cos(Math.toRadians(event.values[2]));
        mAnglesRingBuffer[mRingBufferIndex][2][1] = (float) Math.sin(Math.toRadians(event.values[2]));
        
        // accumulate new x/y vector
        mAngles[0][0] += mAnglesRingBuffer[mRingBufferIndex][0][0];
        mAngles[0][1] += mAnglesRingBuffer[mRingBufferIndex][0][1];
        mAngles[1][0] += mAnglesRingBuffer[mRingBufferIndex][1][0];
        mAngles[1][1] += mAnglesRingBuffer[mRingBufferIndex][1][1];
        mAngles[2][0] += mAnglesRingBuffer[mRingBufferIndex][2][0];
        mAngles[2][1] += mAnglesRingBuffer[mRingBufferIndex][2][1];
        
        mRingBufferIndex++;
        if (mRingBufferIndex == RING_BUFFER_SIZE)
        {
          mRingBufferIndex = 0;
        }
        
        // convert back x/y into angles
        float azimuth = (float) Math.toDegrees(Math.atan2((double) mAngles[0][1], (double) mAngles[0][0]));
        float pitch = (float) Math.toDegrees(Math.atan2((double) mAngles[1][1], (double) mAngles[1][0]));
        float roll = (float) Math.toDegrees(Math.atan2((double) mAngles[2][1], (double) mAngles[2][0]));
        // mCompassRenderer.setOrientation(azimuth, pitch, roll);
        
        if (azimuth < 0)
          azimuth = (360 + azimuth) % 360;
        
        // mHeadingView.setText(getString(R.string.heading)+": "+(int)azimuth+"°");
        
        float fAngle = -azimuth;
        
        fAngle += 360;
        
        float absAngle = g_fLastAngle - fAngle;
        
        if (Math.abs(absAngle) < 2)
          return;
        
        g_fLastAngle = fAngle;
        m_mapView.startAnimation(Global.GetViewRotation(g_fLastAngle, 500));
        
        try
        {
          if (g_sensorcallback != null)
            g_sensorcallback.UpdateDirection(fAngle);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  };
  
  public class LocationListenerEx implements LocationListener
  {
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
      // GPS 상태 변화
      Log.e("GPS", String.format("[onStatusChanged] [Provider: %s] [Status: %d]", provider, status));
    }
    
    
    public void onProviderEnabled(String provider)
    {
      // GPS 사용 가능
      Log.e("GPS", String.format("[onProviderEnabled] [Provider: %s]", provider));
    }
    
    
    public void onProviderDisabled(String provider)
    {
      // GPS 사용 불가능
      Log.e("GPS", String.format("[onProviderDisabled] [Provider: %s]", provider));
    }
    
    
    public void onLocationChanged(Location loc)
    {
      // 바뀐 위치를 수신
      if (loc != null)
      {
        g_locationHere = loc;
        try
        {
          if (g_sensorcallback != null)
            g_sensorcallback.UpdateLocation(loc);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  };
  
  
  /**
   * ======================================================================== ==
   * =========== GPS init
   * ========================================================
   * =============================
   */
  public void userLocationInit()
  {
    m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    m_locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    
    // 마지막으로 기억하는 위치를 설정한다.
    loc = m_locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    g_locationHere = new Location("Here");
    
    if (loc == null)
      loc = m_locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    
    // 둘다 GPS를 수신하는 리스너 변수들이다.
    m_locListenerGPS = new LocationListenerEx();
    m_locListenerNetwork = new LocationListenerEx();
    
    /** 130416 여기 부터 정리 시작하면 됨. */
    try
    {
      // 위치 정보 갱신.
      m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, m_locListenerGPS);
      m_locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 10, m_locListenerNetwork);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
//		// - 센서 매니저 등록 (gSensorEventListner,gSensorType,gDelay)
//		m_sensorManager.registerListener(m_sensorListener,
//				m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//				SensorManager.SENSOR_DELAY_UI);
  }
  
  
//	public void removeLocationUpdate()
//	{
//		try {
//			// 위치 정보 갱신.
//			m_locManager.removeUpdates(m_locListenerGPS);
//			m_locManager.removeUpdates(m_locListenerNetwork);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
  
  /**
   * ======================================================================== ==
   * =========== Called when the activity is first created.
   * ======================
   * ===============================================================
   */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_mapview);
    
    MapButtonSetting();
    summaryViewSetting();
    userLocationInit();
    
    items = new ArrayList<OverlayItem>();
    /*
     * ======================================================================
     * ==== =========== 맵 타일 다운로드 구문.
     * ================================================
     * =====================================
     */
    noTile = getResources().getDrawable(R.drawable.no_tile);
    Global.strMapDBFilePath = Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName);
    Global.strMapDBFilePath2 = Global.GetPathWithSDCard("/Hangouyou/" + Global.testDB);
    
//    testDB = new testDB(this);
    placeinfo = MapPlaceDataManager.getInstance(BlinkingMap.this).getAllPlaces();
//		placeinfo = testDB.getAllPlace();
    Log.w("WARN", "PlaceInfo size : " + placeinfo.size());
    
    /*
     * ======================================================================
     * ==== =========== 오픈 스트리트 맵 API 참고
     * http://wiki.openstreetmap.org/wiki/API_v0.6 osmdroid의 class 타일 생성을 위한 정보의
     * 초기화 상속 범위 : XYTileSource > OnlineTileSourceBase >BitmapTileSourceBase
     * ("이름","네트워크 모드","맵 이미지 최소 범위",맵 이미지 최대 범위,"타일 사이즈","이미지 파일 종류","생략된듯.")
     * String aName, string aResourceId, int aZoomMinLevel, int aZoomMaxLevel,
     * int aTileSizePixels, String aImageFilenameEnding, String aBaseUrl
     * ========
     * ==================================================================
     * ===========
     */
    
    //final float scale = getBaseContext().getResources().getDisplayMetrics().density;
    final int nTileSize = (int) 512;//(256*1.5);
    
    final XYTileSource tileSource = new XYTileSource("My Tile Source", ResourceProxy.string.offline_mode, nMinZoomLevel, nMaxZoomLevel, nTileSize, ".png");
    
    // MapView를 초기 설정하기 위한 함수 Context , TileSize , , Tile 이미지를 추출해 내기 위한 전반적인 설정.-by 형철.
    m_mapView = new BoundedMapView(this, nTileSize, new DefaultResourceProxyImpl(this), new MapTileProvderEx(tileSource, noTile, getApplicationContext()));
    
    // 나도 처음엔 이해 안갔는데 OSM Droid 의 tile을 MS에서 만든 Mappoint를 사용한다.
    TileSystem.setTileSize(nTileSize);
    
    /*
     * ======================================================================
     * ==== =========== 지도 회전을 위해서..
     * ==================================================
     * ===================================
     */
    m_mapLayout = new LinearLayout(this);
    
    m_mapLayout.addView(m_mapView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    
    LinearLayout parentLayout = (LinearLayout) findViewById(R.id.layout_map_view);
    
    m_rotateView = new RotateViewGroup(this);
    
    m_rotateView.addView(m_mapLayout);
    parentLayout.addView(m_rotateView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    
    /*
     * ======================================================================
     * ==== ===========
     * ==============================================================
     * =======================
     */
    
    m_mapView.setUseDataConnection(false);
    m_mapView.setBuiltInZoomControls(false); // 줌 컨트롤 버튼 hidden
    m_mapView.setMultiTouchControls(true); // 핀치줌 가능.
    m_mapView.setClickable(true);
    m_mapView.setBackgroundColor(Color.WHITE);
    
    // zoom level 14 ~ 17
    m_mapView.getController().setZoom(15); // 처음 화면에 보이는 Zoom level
    
    // MapView 리스너 (줌 확인 체크, 상태 체크하여 업데이트 관여.)
    m_mapView.setMapListener(new MapListener()
    {
      public boolean onZoom(ZoomEvent arg0)
      {
        Log.e("ZOOM", "" + arg0);
        int nZoomLevel = arg0.getZoomLevel();
        
        if (nZoomLevel <= nMinZoomLevel)
          nZoomLevel = nMinZoomLevel;
        else if (nZoomLevel >= nMaxZoomLevel)
          nZoomLevel = nMaxZoomLevel;
        
        zoomLevel = nZoomLevel;
        
        m_mapView.getController().setZoom(zoomLevel);
        
        // 제한된 맵 범위 설정 및 Scroll시 이동 반경 제한 세팅.
        m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel);
        
        return false;
      }
      
      
      public boolean onScroll(ScrollEvent arg0)
      {
        return false;
      }
    });
    
//		m_mapView.setOnTouchListener(new OnTouchListener() 
//		{			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				int action = event.getAction();
//								
//				switch (action) 
//				{
//					case MotionEvent.ACTION_UP:
//						if(summaryview.getVisibility() == View.VISIBLE)
//						{
//							summaryViewInvisible();
//						}
//					break;
//				}
//				
//				Log.i("INFO","action " + action);
//				return false;
//			}
//		});
    
    // osmdroid 의 api 를 이용하여 사용자의 현재 위치의 맵을 보여줌.
    m_mapView.getController().setCenter(new GeoPoint(m_fLatitude, m_fLongitude));
    m_mapView.setScrollableAreaLimit(m_boundingBox, 15);
    
    final int nHeight = DpToPixel(65);
    
    g_sensorcallback = this;
    g_bNeedToReloadActivity = true; //초기 터치를 위해서	
  } // - onCreate end..
  
  
  /**
   * ======================================================================== ==
   * =========== 버튼 세팅
   * ==========================================================
   * ===========================
   */
  public void MapButtonSetting()
  {
    myLocationBtn = (ImageButton) findViewById(R.id.myLocationBtn);
    nearByBtn = (ImageButton) findViewById(R.id.nearbyBtn);
    subwayBtn = (ImageButton) findViewById(R.id.subwayBtn);
    zoomInBtn = (ImageButton) findViewById(R.id.zoomInbtn);
    zoomOutBtn = (ImageButton) findViewById(R.id.zoomOutbtn);
    
    myLocationBtn.setOnClickListener(this);
    nearByBtn.setOnClickListener(this);
    subwayBtn.setOnClickListener(this);
    zoomInBtn.setOnClickListener(this);
    zoomOutBtn.setOnClickListener(this);
  }
  
  
  public void summaryViewSetting()
  {
    summaryview = (FrameLayout) findViewById(R.id.summaryView);
    summaryCloseBtn = (ImageButton) findViewById(R.id.summaryCloseBtn);
    summaryImage = (ImageView) findViewById(R.id.summaryImage);
    summaryTitle = (TextView) findViewById(R.id.summaryTitle);
    summarysubTitle = (TextView) findViewById(R.id.summarySubTitle);
    summarylikeCount = (TextView) findViewById(R.id.summarylikecount);
    subwayStartBtn = (Button) findViewById(R.id.subwaystartBtn);
    subwayEndBtn = (Button) findViewById(R.id.subwayendBtn);
    naviBtn = (ImageButton) findViewById(R.id.naviBtn);
    
    summaryview.setClickable(true);
    summaryview.setOnClickListener(this);
    summaryCloseBtn.setOnClickListener(this);
    subwayStartBtn.setOnClickListener(this);
    subwayEndBtn.setOnClickListener(this);
    naviBtn.setOnClickListener(this);
  }
  
  
  public void summaryViewVisible()
  {
    summaryview.setVisibility(View.VISIBLE);
    FrameLayout btnlayout = (FrameLayout) findViewById(R.id.btn_layout);
    FrameLayout.LayoutParams btnLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    btnLayoutParams.bottomMargin = 364;
    btnLayoutParams.gravity = Gravity.BOTTOM;
    btnlayout.setLayoutParams(btnLayoutParams);
  }
  
  
  public void summaryViewInvisible()
  {
    summaryview.setVisibility(View.INVISIBLE);
    FrameLayout btnlayout = (FrameLayout) findViewById(R.id.btn_layout);
    FrameLayout.LayoutParams btnLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    btnLayoutParams.bottomMargin = 0;
    btnLayoutParams.gravity = Gravity.BOTTOM;
    btnlayout.setLayoutParams(btnLayoutParams);
  }
  
  
  @Override
  public void onClick(View v)
  {
    switch (v.getId())
    {
      case R.id.myLocationBtn:
        if (m_locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) // GPS 활성화 체크, 활성화 - return true
        {
          if (myLocationState == 0)
          {
            myLocationState = 1;
            myLocation();
            RotateMap(0, 100);
            MoveToCurrentPosition();
            ShowCurrentPosition(myLocationState);
            myLocationBtn.setBackgroundResource(R.drawable.btn_my_location_on);
          }
          else if (myLocationState == 1)
          {
            myLocationState = 2;
            myLocation();
            
            float fAngle = g_fLastAngle;
//						RotateMap(fAngle, 100);
            
            Location locHere = g_locationHere;
            if (locHere != null && m_locMarkTarget != null)
            {
              float fBearingTo = locHere.bearingTo(m_locMarkTarget);
              
              Log.d("SENSSOR", "[Angle] " + fAngle + " [BearingTo] " + fBearingTo);
              
              float fRotate = fAngle + fBearingTo;
              
              if (fRotate < 0)
                fRotate += 360;
              
              fRotate %= 360;
              m_mapView.startAnimation(Global.GetViewRotation(fRotate, 0));
            }
            
            // - 센서 매니저 등록 (gSensorEventListner,gSensorType,gDelay)
            m_sensorManager.registerListener(m_sensorListener, m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
            
            MoveToCurrentPosition();
            ShowCurrentPosition(myLocationState);
            myLocationBtn.setBackgroundResource(R.drawable.btn_my_location_direction);
          }
          else
          {
            myLocationState = 0;
            if (m_curPosOverlay != null)
            {
              m_mapView.getOverlays().remove(m_curPosOverlay);
              m_curPosOverlay = null;
            }
            myLocationBtn.setBackgroundResource(R.drawable.btn_my_location);
            RotateMap(0, 100);
            m_sensorManager.unregisterListener(m_sensorListener);
          }
          m_mapView.invalidate();
        }
        else
        {
          AlertDialog.Builder ab = new AlertDialog.Builder(this);
          ab.setTitle(R.string.map_notification);
          ab.setMessage(R.string.map_dialog_message01);
          ab.setPositiveButton(R.string.map_confirm, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
              // TODO Auto-generated method stub
              dialog.dismiss();
            }
          });
          ab.show();
        }
        break;
      
      case R.id.nearbyBtn:
        BlinkingCommon.smlLibDebug("BUTTON ACTION", "nearbyBtn Action");
        //가까운 지하철 역 찾기
//        Intent i = new Intent(BlinkingMap.this, SubwayActivity.class);
//        double[] latLon = new double[2];
//        latLon[0] = ;
//        latLon[1] = ;
//        i.putExtra("near_by_station_lat_lon", latLon);
//        startActivity(i);
        break;
      
      case R.id.subwayBtn:
        startActivity(new Intent(this, SubwayActivity.class));
        break;
      
      case R.id.zoomInbtn:
        if (zoomLevel < nMaxZoomLevel)
          zoomLevel = zoomLevel + 1;
        m_mapView.getController().setZoom(zoomLevel);
        
        // 제한된 맵 범위 설정 및 Scroll시 이동 반경 제한 세팅.
        m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel);
        
        break;
      
      case R.id.zoomOutbtn:
        if (zoomLevel > nMinZoomLevel)
          zoomLevel = zoomLevel - 1;
        m_mapView.getController().setZoom(zoomLevel);
        
        // 제한된 맵 범위 설정 및 Scroll시 이동 반경 제한 세팅.
        m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel);
        break;
      
      case R.id.summaryView:
        Intent detailPlaceIntent = new Intent();
        detailPlaceIntent.putExtra("idx", idx);
        Log.i("INFO", "idx : " + idx);
        break;
      
      case R.id.summaryCloseBtn:
        summaryViewInvisible();
        break;
      
      case R.id.subwaystartBtn:
        Log.w("WARN","start");
        Intent i = new Intent(BlinkingMap.this, SubwayActivity.class);
        double[] latLon = new double[3];
        latLon[0] = subway_m_fLatitude;
        latLon[1] = subway_m_fLongitude;
        latLon[2] = 0;
        i.putExtra("set_dest_station_lat_lon", latLon);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        break;
        
      case R.id.subwayendBtn:
        Log.w("WARN","end");
        Intent destSubwayIntent = new Intent(BlinkingMap.this, SubwayActivity.class);
        double[] destLatLong = new double[3];
        destLatLong[0] = subway_m_fLatitude;
        destLatLong[1] = subway_m_fLongitude;
        destLatLong[2] = 1;
        destSubwayIntent.putExtra("set_dest_station_lat_lon", destLatLong);
        destSubwayIntent.putExtra("dest_name", summaryTitle.getText().toString());
        destSubwayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(destSubwayIntent);
        break;
      
      case R.id.naviBtn:
        break;
    }
  }
  
  
  public void myLocation()
  {
    if (loc != null)
    {
      m_fLatitude = loc.getLatitude();
      m_fLongitude = loc.getLongitude();
      g_locationHere = loc;
    }
    else
    {
      Log.e("ERROR", "locManager.getLastKnownLocation return null");
      
      m_fLatitude = 37.5664700;
      m_fLongitude = 126.9779630;
      
      g_locationHere.setLatitude(m_fLatitude);
      g_locationHere.setLongitude(m_fLongitude);
      
      // g_sensorcallback에 값이 있으면 설정이지만 처음엔 해당사항이 없는듯하다.
      try
      {
        if (g_sensorcallback != null)
          g_sensorcallback.UpdateLocation(g_locationHere);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      AlertDialog.Builder ab = new AlertDialog.Builder(this);
      ab.setTitle(R.string.map_notification);
      ab.setMessage(R.string.map_dialog_message02);
      ab.setPositiveButton(R.string.map_confirm, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          // TODO Auto-generated method stub
          dialog.dismiss();
        }
      });
      ab.show();
    }
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 네비게이션 관련 로직.
   * ====================================================
   * =================================
   */
  protected void ToggleNavigationState()
  {
    m_bUseLocationInfo = !m_bUseLocationInfo;
    MoveToCurrentPosition();
    
    if (m_bUseLocationInfo == true)
    {
      m_bComplete = false; // -v1.5 Toast 팝업 한번만 노출 시켜주기 위한. 신호값.
      HideBalloon();
      
      float fAngle = g_fLastAngle;
      
      RotateMap(fAngle, 100);
      
      Location locHere = g_locationHere;
      if (locHere != null && m_locMarkTarget != null)
      {
        float fBearingTo = locHere.bearingTo(m_locMarkTarget);
        
        Log.d("SENSSOR", "[Angle] " + fAngle + " [BearingTo] " + fBearingTo);
        
        float fRotate = fAngle + fBearingTo;
        
        if (fRotate < 0)
          fRotate += 360;
        
        fRotate %= 360;
        
        // --------- 방향 ------------------------
//				m_frNavigation.startAnimation(Global
//						.GetViewRotation(fRotate, 0));
        
        String pattern = "##.##";
        DecimalFormat dformat = new DecimalFormat(pattern);
        double distance = locHere.distanceTo(m_locMarkTarget) * 0.001;
        String kmeter = dformat.format(distance);
        
        if (distance < 1)
        {
          String pMeter = "###";
          DecimalFormat dMeterformat = new DecimalFormat(pMeter);
          
          Double dMeter = Double.parseDouble(kmeter);
          String meter = dMeterformat.format(dMeter * 1000);
//					m_tvNavigation.setText(meter + "m");
        }
        else
        {
//					m_tvNavigation.setText(kmeter + "km");
        }
      }
      
    }
    else
    {
      // - 아이콘 켜짐 -> 아이콘 끔
//			m_frNavigation.setVisibility(View.GONE);
//			m_tvNavigation.setVisibility(View.GONE);
//			m_frNavigation.setBackgroundDrawable(null);
      
      // naviImageThread.setState(NaviImageThread.STATE_DONE);
      
      RotateMap(0, 0);
      
      if (m_myplaceOverlay.GetMarkerShow() == true)
        m_myplaceOverlay.ShowMarker(true);
      else
      {
        m_myplaceOverlay.ShowMarker(false);
        // onResume();
        g_bNeedToReloadActivity = true;
        ReloadActivity();
      }
      
      // - 호출한 말풍선을 다시 호출...
      if (m_cache != null)
      {
//				ShowBalloon(m_cache, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
//				{
//					public boolean onItemSingleTapUp(final int index, final OverlayItem item)
//					{
//						setonClickBalloon("내용");
//						return true; // We 'handled' this event.
//					}
//					
//					
//					public boolean onItemLongPress(final int index, final OverlayItem item)
//					{
//						return true;
//					}
//				});
      }
    }
  }
  
  
  private void ShowNavCurrentPosition(final double latitude, final double longitude, boolean bFind)
  {
    // 일단 지워놓음 
//		if(m_nearbyPlaceNodeOverlay != null)
//			m_mapView.getOverlays().remove(m_nearbyPlaceNodeOverlay);
//		m_nearbyPlaceNodeOverlay = null;
    
    RotateMap(0, 0);
    
    m_locMarkTarget = new Location("MarkTarget");
    m_locMarkTarget.setLatitude(latitude);
    m_locMarkTarget.setLongitude(longitude);
    
//		mBtnRotation.setVisibility(View.VISIBLE);		
//
//		mBtnRotation.setChecked(false);
//		mBtnRotation.setBackgroundDrawable(Global.GetDrawable(this, R.drawable.navi_btn));
    
    HideBalloon();
  }
  
  
  private void HideNavCurrentPosition()
  {
    RotateMap(0, 0);
    
//		m_frNavigation.setVisibility(View.GONE);
//		m_frNavigation.setBackgroundDrawable(null);
//		m_tvNavigation.setVisibility(View.GONE);
    
    m_bUseLocationInfo = false;
    
//		mBtnRotation.setChecked(false);
//		mBtnRotation.setBackgroundDrawable(Global.GetDrawable(this, R.drawable.navi_btn));
    
    //onResume();
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 마커 표시 관련 로직.
   * ====================================================
   * =================================
   */
  
  private void ShowMyPlaceOverlay(double fLatitude, double fLongitude)
  {
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    
    OverlayItem item = new OverlayItem("", "", new GeoPoint(fLatitude, fLongitude));
    
    item.setMarker(Global.GetDrawable(this, R.drawable.transparent));
    
    items.add(item);
    
    if (m_myplaceOverlay != null)
      m_mapView.getOverlays().remove(m_myplaceOverlay);
    
    m_myplaceOverlay = new MyPlaceOverlay(items, null, new DefaultResourceProxyImpl(this), this);
    
    m_myplaceOverlay.SetMarkerID(this, R.drawable.user_place_icon);
    
    if (fLatitude <= 0 || fLongitude <= 0)
      m_myplaceOverlay.ShowMarker(false);
    else
      m_myplaceOverlay.ShowMarker(true);
    
    m_mapView.getOverlays().add(m_myplaceOverlay);
    
    m_mapView.invalidate();//화면 갱신.
  }
  
  
  // 마커 표시 이동 시키는 로직.
  public void ShowMyPlacePopup(final double fLatitude, final double fLongitude)
  {
//		if(mBtnRotation.isChecked() == true) return;
//
//		ShowMyPlaceOverlay(fLatitude, fLongitude);
//
//		mBtnRotation.setVisibility(View.INVISIBLE);
    
    // 위치 찾기..
    Log.e("temp", "find location : " + fLatitude + " , " + fLongitude);
    
    ShowNavCurrentPosition(fLatitude, fLongitude, false);
    
    if (m_bTarget == true)
    {
      m_mapView.getOverlays().remove(m_placeOverlay);
      //m_placeOverlay = null;
    }
  }
  
  
  public void RemoveMyPlacePopup(final double fLatitude, final double fLongitude)
  {
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    
    OverlayItem item = new OverlayItem("", "", new GeoPoint(fLatitude, fLongitude));
    
    item.setMarker(Global.GetDrawable(this, R.drawable.transparent));
    
    items.add(item);
    
    BlinkingCommon.smlLibDebug("Remove", "RemovePlacePopup");
    m_mapView.getOverlays().remove(m_placeOverlay);
    m_myplaceOverlay.ShowMarker(false);
//		mBtnRotation.setVisibility(View.GONE);
    m_mapView.invalidate();//화면 갱신.
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 현재 위치 관련 로직.(마커 x, 원 표시)
   * ========================================
   * =============================================
   */
  private void ShowCurrentPosition(int mylocationstate)
  {
    if (m_curPosOverlay != null)
    {
      m_mapView.getOverlays().remove(m_curPosOverlay);
      m_curPosOverlay = null;
    }
    
    if (m_fLatitude > 0 && m_fLongitude > 0)
    {
      ArrayList<OverlayItem> pList = new ArrayList<OverlayItem>();
      OverlayItem item = new OverlayItem("", "", new GeoPoint(m_fLatitude, m_fLongitude));
      pList.add(item);
      m_curPosOverlay = new CurrentPositionOverlay(pList, null, new DefaultResourceProxyImpl(this), this, myLocationState);
      
      m_mapView.getOverlays().add(m_curPosOverlay);
    }
    
    m_mapView.invalidate();
  }
  
  
  private void MoveToCurrentPosition()
  {
    if (m_fLatitude > 0 && m_fLongitude > 0)
    {
      m_mapView.getController().animateTo(new GeoPoint(m_fLatitude, m_fLongitude));
    }
    else
    {
      Log.d("debug", "lat = " + m_fLatitude + "  lon = " + m_fLongitude);
    }
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 터치나 여러가지 사항을 세팅해주기 위한 메소드 : Place 관련 컨트롤
   * ========================
   * =============================================================
   */
  
  //TODO 터치나 여러가지 사항을 세팅해주기 위한 메소드 : Place 관련 컨트롤
  public void ReloadActivity()
  {
    if (g_bNeedToReloadActivity == false)
    {
      Log.d("DEBUG", "업데이트 필요없음");
      return;
    }
    
    g_bNeedToReloadActivity = false;
    
    m_mapView.getOverlays().clear();
    
    // TODO 터치하고 추가할 수 있게 하기 위해서..
    ShowMyPlaceOverlay(0, 0);
    
//		ShowNearbyPlaceOverlay(m_nIndexNearbyPlaceOverlay);
    
//		// 회전 버튼 초기화 (장소/코스 정보가 없는 경우에 안보이게 함)
//		mBtnRotation.setVisibility(View.INVISIBLE);
//
//		if(mBtnRotation.getVisibility() == View.INVISIBLE) HideNavCurrentPosition();
    
    // 코스 정보 레이아웃 초기화
    {
      if (m_bPlaceInfoMap == true)
      {
        Global.GC();
        return;
      }
      
      ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
      
      // 장소/코스 정보 없는 경우.
//			mBtnRotation.setVisibility(View.INVISIBLE);
      
      if (m_fLatitude == 0 || m_fLongitude == 0)
        m_mapView.getController().setCenter(new GeoPoint(37.5664700, 126.9779630));
      else
        m_mapView.getController().setCenter(new GeoPoint(m_fLatitude, m_fLongitude));
    }
    
    // 현 위치 아이콘 추가
//		ShowCurrentPosition();
    
    Global.GC();
  }
  
  
  /**
   * ======================================================================== ==
   * =========== Interface Callback Part!
   * ========================================
   * =============================================
   */
  
  @Override
  public void UpdateDirection(float fAngle)
  {
    if (m_bUseLocationInfo == false)
    {
      return;
    }
    
    Location locHere = g_locationHere;
    
    if (m_myplaceOverlay.GetMarkerShow() == true)
    {
      //TODO 사용자 임의 선택일때 
      if (m_locMarkTarget == null)
      {
        Log.e("ERROR", "m_locTarget == null");
        return;
      }
      
      //			m_lastTime = currentTime;
      RotateMap(fAngle, 100);
      
      float fBearingTo = locHere.bearingTo(m_locMarkTarget);
      
//			Log.d("SENSSOR", "[Angle1] " + fAngle + " [BearingTo] " + fBearingTo);
      
      //--------- 방향 ------------------------		
      float fRotate = fAngle + fBearingTo + 45;
      
      if (fRotate < 0)
        fRotate += 360;
      
      fRotate %= 360;
      
//			m_frNavigation.startAnimation(Global.GetViewRotation(fRotate, 0));
      
      String pattern = "##.##";
      DecimalFormat dformat = new DecimalFormat(pattern);
      double distance = locHere.distanceTo(m_locMarkTarget) * 0.001;
      String kmeter = dformat.format(distance);
      
      Double dMeter = Double.parseDouble(kmeter);
      m_iMeter = (int) (dMeter * 1000);
//			Log.i("INFO","meter : "+m_iMeter);
      
      //- v1.5 오차 알림 문구 Toast : 10미터 이하로 진입했을 경우 목적지에 도착했다는 팝업 보였다 사라짐.
      if (m_iMeter <= 10 && m_bComplete == false)
      {
        int nLangState = LANGUAGE_DEFUALT;
        
        if (Global.strSetLanguage.equals("kor"))
          nLangState = LANGUAGE_KOREA;
        else
          nLangState = LANGUAGE_DEFUALT;
        
        Toast.makeText(getApplicationContext(), strToastMessage[nLangState], Toast.LENGTH_LONG).show();
        m_bComplete = true; // 한번만 Toast 실행을 위해.
      }
      
      if (distance < 1)
      {
        String pMeter = "###";
        DecimalFormat dMeterformat = new DecimalFormat(pMeter);
        
//				Double dMeter = Double.parseDouble(kmeter);
        String meter = dMeterformat.format(dMeter * 1000);
//				m_tvNavigation.setText(meter+"m");
      }
      else
      {
//				m_tvNavigation.setText(kmeter+"km");
      }
      //-------------- ------------------------
    }
    else
    {
      //TODO 사용자 임의 선택이 아닐때 
      if (m_locTarget == null)
      {
        Log.e("ERROR", "m_locTarget == null");
        return;
      }
      
//			Log.d("SENSSOR", "[Angle] " + fAngle);
      
      RotateMap(fAngle, 100);
      
      float fBearingTo = locHere.bearingTo(m_locTarget);
      
      float fRotate = fAngle + fBearingTo + 45;
      
      if (fRotate < 0)
        fRotate += 360;
      
      fRotate %= 360;
      
      //--------- 방향 ------------------------		
//			m_frNavigation.startAnimation(Global.GetViewRotation(fRotate, 0));
      
      String pattern = "##.##";
      DecimalFormat dformat = new DecimalFormat(pattern);
      double distance = locHere.distanceTo(m_locTarget) * 0.001;
      String kmeter = dformat.format(distance);
      
      Double dMeter = Double.parseDouble(kmeter);
      m_iMeter = (int) (dMeter * 1000);
//			Log.i("INFO","meter : "+m_iMeter);
      
      //- v1.5 오차 알림 문구 Toast : 10미터 이하로 진입했을 경우 목적지에 도착했다는 팝업 보였다 사라짐.
      if (m_iMeter <= 10 && m_bComplete == false)
      {
        int nLangState = LANGUAGE_DEFUALT;
        
        if (Global.strSetLanguage.equals("kor"))
          nLangState = LANGUAGE_KOREA;
        else
          nLangState = LANGUAGE_DEFUALT;
        
        Toast.makeText(getApplicationContext(), strToastMessage[nLangState], Toast.LENGTH_LONG).show();
        m_bComplete = true; // 한번만 Toast 실행을 위해.
      }
      
      if (distance < 1)
      {
        String pMeter = "###";
        DecimalFormat dMeterformat = new DecimalFormat(pMeter);
        
//				Double dMeter = Double.parseDouble(kmeter);
        String meter = dMeterformat.format(dMeter * 1000);
//				m_tvNavigation.setText(meter+"m");
      }
      else
      {
//				m_tvNavigation.setText(kmeter+"km");
      }
      //-------------- ------------------------
    }
    
    // 이걸 하면.. 계속 화면을 못벗어날듯.
    //			if(m_bTrackingCurrentPosition == true)
    //			{
    //				MoveToCurrentPosition();
    //			}
    
//		ShowCurrentPosition();
  }
  
  
  @Override
  public void UpdateLocation(Location locCur)
  {
    // TODO Auto-generated method stub		
  }
  
  
  /**
   * ======================================================================== ==
   * =========== DB File asset에서 SD카드로 복사
   * ========================================
   * =============================================
   */
  public void CopyPlaceInfoDBfile() throws IOException
  {
    
  }
  
  
  // Map file 복사.
  public void CopyMapDBFile() throws IOException
  {
    //			  ver 1.7까지의 방식 MapFile Download 형식으로 변경함. 
    String strMapDBDir = Global.GetPathWithSDCard("BlinkingMap");
    
    File file = new File(strMapDBDir);
    
    // 폴더 안에 싹~ 지우고..
    Global.deleteFolder(file);
    
    // BlinkingMap 폴더 생성하고.. 
    file.mkdirs();
    
    Log.e("testLog", "strMapDBDir : " + strMapDBDir);
    
    //Map data DB Name 으로 경로 설정. 
    Global.strMapDBFilePath = Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName);
    
    Log.e("testLog", "strMapDBFilePath : " + Global.strMapDBFilePath);
    //파일 복사 로직. Assets에서 내부 폴더 경로로 복사. 
    AssetManager am = getResources().getAssets();
    
    try
    {
      InputStream is = am.open(Global.g_strMapDBFileName);
      OutputStream out = new FileOutputStream(new File(Global.strMapDBFilePath));
      
      int size = is.available();
      
      Log.i("INFO", "맵 파일 복사.. : size = " + size);
      
      byte[] buffer = new byte[1024];
      
      int nRead = 0;
      while ((nRead = is.read(buffer)) != -1)
        out.write(buffer, 0, nRead);
      out.flush();
      out.close();
      
      Log.i("INFO", "맵 파일 복사됨 : " + Global.g_strMapDBFileName);
      
      is.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
  
  
  @Override
  protected void onResume()
  {
    // TODO Auto-ge                                                                                                                                                                                                                                                                                                                          nerated method stub
    super.onResume();
    
    ReloadActivity();
    
    //getNaviFlag(37.55655375544381,126.86307907104492,"test");
    
    //int nId, int nType,double lat, double lng, String name
//		setBalloonMarker(0, 0, 37.55655375544381,126.86307907104492,"test");
    Markertest();
  }
  
  
  public void Markertest()
  {
    if (isShowMarker)
      return;
    
    isShowMarker = true;
    if (placeinfo.size() > items.size())
    {
      OverlayItem item = new OverlayItem(placeinfo.get(0).m_strName, placeinfo.get(0).m_strIntro, new GeoPoint(placeinfo.get(0).m_fLatitude, placeinfo.get(0).m_fLongitude));
      OverlayItem item2 = new OverlayItem(placeinfo.get(1).m_strName, placeinfo.get(1).m_strIntro, new GeoPoint(placeinfo.get(1).m_fLatitude, placeinfo.get(1).m_fLongitude));
      OverlayItem item3 = new OverlayItem(placeinfo.get(2).m_strName, placeinfo.get(2).m_strIntro, new GeoPoint(placeinfo.get(2).m_fLatitude, placeinfo.get(2).m_fLongitude));
      
      // 아이템 클릭때문에 추가함.
      item3.setMarker(Global.GetDrawable(this, R.drawable.marker_default));
      item2.setMarker(Global.GetDrawable(this, R.drawable.marker_default));
      item.setMarker(Global.GetDrawable(this, R.drawable.marker_default));
      
      items.add(item);
      items.add(item2);
      items.add(item3);
      
      Log.w("WARN", "item: " + item.mTitle);
      PlaceOverlay placeOverlay = new PlaceOverlay(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
      {
        @Override
        public boolean onItemLongPress(int arg0, OverlayItem arg1)
        {
          return true;
        }
        
        
        @Override
        public boolean onItemSingleTapUp(int arg0, OverlayItem arg1)
        {
          // TODO Auto-generated method stub
          Log.i("INFO", "Singletapup");
          try
          {
            m_mapView.getController().setCenter(arg1.mGeoPoint);
            idx = new BigInteger("" + placeinfo.get(arg0).m_nID);
            subway_m_fLatitude = placeinfo.get(arg0).m_fLatitude;
            subway_m_fLongitude = placeinfo.get(arg0).m_fLongitude;
            summaryTitle.setText(arg1.mTitle);
            summarysubTitle.setText(arg1.mDescription);
            Log.i("INFO", "Lat : " + subway_m_fLatitude + " Long : " + subway_m_fLongitude);
            summaryViewVisible();
            m_mapView.invalidate();
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          return false;
        }
      }, new DefaultResourceProxyImpl(this), this);
      MyPlaceOverlay myPlaceOverlay = new MyPlaceOverlay(items, null, new DefaultResourceProxyImpl(this), this);
      m_mapView.getOverlays().add(placeOverlay);
    }
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 단순 계산...
   * ========================================================
   * =============================
   */
  
  @SuppressLint("NewApi")
  private void RotateMap(float fAngle, long duration)
  {
    if (fAngle < 0)
      fAngle += 360;
    
    if (fAngle > 360)
      fAngle -= 360;
    
    if (Build.VERSION_CODES.GINGERBREAD_MR1 >= Build.VERSION.SDK_INT)
    {
      RotateAnimation r = new RotateAnimation(fAngle, fAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
      r.setDuration(duration);
      r.setFillAfter(true);
      
      m_mapLayout.startAnimation(r);
      m_rotateView.m_fAngle = fAngle;
      m_rotateView.invalidate();
    }
    else
    {
      if (m_mapView != null)
        m_mapView.setRotation(fAngle);
    }
  }
  
  
  public int DpToPixel(int DP)
  {
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, getResources().getDisplayMetrics());
    
    return (int) px;
  }
  
  
  /**
   * ======================================================================== ==
   * =========== 말풍선 로직. - 실제 말풍선 로직 - 말풍선 숨김.. PlaceInfo 객체 형식 중에
   * 위,경도,m_nID,m_nType 값이 존재 해야한다.
   * ==============================================
   * =======================================
   */
  private void ShowBalloon(PlaceInfo placeInfo, OnItemGestureListener<OverlayItem> onItemGestureListener)
  {
    // 내비게이션 모드에서는 말풍선 띄우지 않음.
    if (m_bUseLocationInfo == true)
      return;
    
    HideBalloon();
    
    double dBoxlat = placeInfo.m_fLatitude;
    double dBoxlong = placeInfo.m_fLongitude;
    
    ArrayList<OverlayItem> balloonItem = new ArrayList<OverlayItem>();
    OverlayItem item = new OverlayItem(placeInfo.m_strName, String.format("%d", placeInfo.m_nID), new GeoPoint(dBoxlat, dBoxlong));
    balloonItem.add(item);
    
    int color;
    if (placeInfo.m_nType == 5)
      color = Color.BLACK;
    else
      color = Color.WHITE;
    
    item.setMarker(Global.GetDrawable(this, R.drawable.transparent_balloon));
    
    m_balloonOverlay = new BalloonOverlay(balloonItem, onItemGestureListener, new DefaultResourceProxyImpl(this), this, color, m_nBalloonHeight);
    
    m_mapView.getOverlays().add(m_balloonOverlay);
    m_mapView.invalidate();
  }
  
  
  private void HideBalloon()
  {
    if (m_balloonOverlay != null)
    {
      Log.d("testLog", "들어옴..");
      m_mapView.getOverlays().remove(m_balloonOverlay);
      m_balloonOverlay = null;
      
      m_mapView.invalidate();
      Global.GC();
    }
  }
  
  
  /**
   * ======================================================================== ==
   * =========== ShowNavCurrentPosition 을 외부에서 호출했을때 용도에 맞게 타겟에 대한 위치를 설정해주기 위한
   * 로직. (final double latitude, final double longitude, boolean bFind)
   * ========== ========================================================
   * ===================
   */
  
  public void getNaviFlag(double lat, double lng, String name)
  {
    ShowMyPlacePopup(lat, lng);
    m_bTrackingCurrentPosition = true;
    m_btnCurrent.setChecked(true);
//		ShowCurrentPosition();
  }
  
  
//	public void setBalloonMarker(int nId, int nType, float lat, float lng, String name)
//	{		
//		PlaceInfo _info = new PlaceInfo();
//		
//		_info.m_strName = name;
//		_info.m_fLatitude = lat;
//		_info.m_fLongitude = lng;
//		_info.m_nID = nId;
//		_info.m_nType = nType;
//		
//		m_cache = _info;		
//		
//		getNaviFlag(lat,lng,name);
//		
//		m_mapView.getController().animateTo(new GeoPoint(lat, lng));
//		
//		ShowBalloon(_info, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
//		{
//			public boolean onItemSingleTapUp(final int index, final OverlayItem item)
//			{
//				setonClickBalloon("내용");
//				return true; // We 'handled' this event.
//			}
//
//			public boolean onItemLongPress(final int index, final OverlayItem item)
//			{
//				BlinkingCommon.smlLibDebug("LongPress", "LongPress");
//				return true;
//			}
//		 });
//	}
  
  public void setonClickBalloon(String strTemp)
  {
    Log.d("testLog", strTemp);
  }
}
