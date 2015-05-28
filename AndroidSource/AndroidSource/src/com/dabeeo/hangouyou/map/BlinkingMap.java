package com.dabeeo.hangouyou.map;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

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
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.map.SensorUpdater.SensorUpdaterCallback;
import com.squareup.picasso.Picasso;

public class BlinkingMap extends Activity implements OnClickListener,SensorUpdaterCallback, OnEditorActionListener 
{
	static public boolean g_bNeedToReloadActivity = false;

	public Context mContext;

	// - Location
	private double m_fLatitude = 37.5716550;
	private double m_fLongitude = 126.9862330;
	public double place_fLatitude = 0;
	public double place_fLongitute = 0;
	private int zoomLevel = 16;
	final int nMinZoomLevel = 13;
	final int nMaxZoomLevel = 18;
	private BoundedMapView m_mapView;

	private Location m_myLocation;
	private Location m_locMarkTarget;

	// 위치 정보 사용 여부
	private boolean m_bUseLocationInfo = false;
	private boolean m_bTrackingCurrentPosition = false;

	private LinearLayout m_mapLayout;
	private RotateViewGroup m_rotateView;

	private int m_nBalloonHeight = -1;

	private Drawable noTile = null;

	BoundingBoxE6 m_boundingBox = new BoundingBoxE6(37.70453488762476, 127.17361450195312, 37.43677099171195, 126.76300048828125);

	// - Marker 관련.
	private BalloonOverlay m_balloonOverlay;
	private NavigationOverlay navigationOverlay;
	private CurrentPositionOverlay m_curPosOverlay;
	private PlaceOverlay allplaceOverlay;
	private PlaceOverlay premiumOverlay;
	private OnePlaceOverlay onePlaceOverlay;

	private ArrayList<OverlayItem> allPlaceInfoItems;
	private ArrayList<OverlayItem> oneItems;
	private ArrayList<OverlayItem> premiumitem;

	private Map<String,PlaceInfo> allplaceinfo;
	private List<PlaceInfo> onePlaceInfo;
	private Map<String, PremiumInfo> premiumInfo;

	// - Place 임시 정보
	PlaceInfo m_cache;

	// - map button
	public FrameLayout btnlayout;
	public ImageButton myLocationBtn;
	public ImageButton nearByBtn;
	public ImageButton subwayBtn;
	public ImageButton zoomInBtn;
	public ImageButton zoomOutBtn;

	// - search 관련
	private ImageButton backBtn;
	private ImageButton searchCancel;
	private LinearLayout searchView;
	private RelativeLayout emptysearchList;
	private EditText searchEditText;
	private TextWatcher placewatcher;

	// - summary view
	private LinearLayout summaryView;
	private ImageButton summaryCloseBtn;
	private ImageButton summaryCloseBtn2;
	private ImageView summaryImage;
	private LinearLayout summaryTextView;
	private TextView summaryTitle;
	private TextView summarysubTitle;
	private LinearLayout summaryLikeLayout;
	private TextView summarylikeCount;
	private RelativeLayout subwayLayout;
	private LinearLayout subwayStartBtn;
	private LinearLayout subwayEndBtn;
	private ImageButton naviBtn;

	public ImageView lineNumImage1;
	public ImageView lineNumImage2;
	public ImageView lineNumImage3;
	public ImageView lineNumImage4;
	public TextView lineNumText1;
	public TextView lineNumText2;
	public TextView lineNumText3;
	public TextView lineNumText4;

	boolean summaryDetail = false;

	public String DestinationTitle;

	// - navigation view
	public RelativeLayout navigationView;
	public TextView naviInfo01;
	public TextView naviInfo02;
	public TextView naviSubInfo;
	public ImageView naviStopBtn;
	private String[] naviTexts;

	// - Class 간 전달할 데이터
	public String idx;
	public String lineId = "";
	public int placeType = 0;
	public int planType = 1;

	// - Location Type
	int myLocationState = 0;
	private int DefaltLocation = 0;
	private int UseLocation = 1;
	private int UseLocationNavi = 2;

	// - Gps Type
	private int NoUseGPS = 0;
	private int LocationFail = 1;
	private int OutOfSeoul = 2;

	private int summaryViewVisible = 0;
	private int summaryViewInVisible = 1;

	// 현재 보고 있는 맵의 바운더리 정보
	private GeoPoint topLeftGpt;
	private GeoPoint bottomRightGpt;

	public static Activity blinkingactivity = null;

	private Timer 	m_timer = null;

	private ListView  m_ListView;
	private ArrayAdapter<String>    m_Adapter;

	private boolean summaryviewclose = false;

	private NearByDialog nearbydialog;

	private Intent destSubwayIntent;
	private long backKeyPressedTime = 0;
	private Toast appFininshToast;

	public static int categoryType = -1;

	/**
	 * =====================================================================================
	 *  단말기의 방향을 감지. - 네비게이션에 필요.
	 * =====================================================================================
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
				} else 
				{
					mNumAngles++;
				}

				// convert angles into x/y
				mAnglesRingBuffer[mRingBufferIndex][0][0] = (float) Math
						.cos(Math.toRadians(event.values[0]));
				mAnglesRingBuffer[mRingBufferIndex][0][1] = (float) Math
						.sin(Math.toRadians(event.values[0]));
				mAnglesRingBuffer[mRingBufferIndex][1][0] = (float) Math
						.cos(Math.toRadians(event.values[1]));
				mAnglesRingBuffer[mRingBufferIndex][1][1] = (float) Math
						.sin(Math.toRadians(event.values[1]));
				mAnglesRingBuffer[mRingBufferIndex][2][0] = (float) Math
						.cos(Math.toRadians(event.values[2]));
				mAnglesRingBuffer[mRingBufferIndex][2][1] = (float) Math
						.sin(Math.toRadians(event.values[2]));

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
				float azimuth = (float) Math.toDegrees(Math.atan2(
						(double) mAngles[0][1], (double) mAngles[0][0]));
				float pitch = (float) Math.toDegrees(Math.atan2(
						(double) mAngles[1][1], (double) mAngles[1][0]));
				float roll = (float) Math.toDegrees(Math.atan2(
						(double) mAngles[2][1], (double) mAngles[2][0]));
				// mCompassRenderer.setOrientation(azimuth, pitch, roll);

				//				if (azimuth < 0)
				azimuth = (360 - azimuth) % 360;

				// mHeadingView.setText(getString(R.string.heading)+": "+(int)azimuth+"°");

				float fAngle = azimuth;

				fAngle += 360;

				float absAngle = g_fLastAngle - fAngle;

				if (Math.abs(absAngle) < 2)
					return;

				g_fLastAngle = fAngle;

				try {
					if (g_sensorcallback != null)
						g_sensorcallback.UpdateDirection(fAngle);
				} catch (Exception e) {
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
			Log.e("GPS", String.format(
					"[onStatusChanged] [Provider: %s] [Status: %d]", provider,
					status));
		}

		public void onProviderEnabled(String provider) 
		{
			// GPS 사용 가능
			Log.e("GPS", String.format("[onProviderEnabled] [Provider: %s]",
					provider));
		}

		public void onProviderDisabled(String provider) 
		{
			// GPS 사용 불가능
			Log.e("GPS", String.format("[onProviderDisabled] [Provider: %s]",
					provider));
		}

		public void onLocationChanged(Location loc) 
		{
			// 바뀐 위치를 수신
			if (loc != null && m_bTrackingCurrentPosition) 
			{
				g_locationHere = loc;
				try {
					if (g_sensorcallback != null)
						g_sensorcallback.UpdateLocation(loc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * =====================================================================================
	 *  GPS init
	 * =====================================================================================
	 */
	public void userLocationInit() 
	{
		m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		m_locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// 마지막으로 기억하는 위치를 설정한다.
		m_myLocation = m_locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		g_locationHere = new Location("Here");

		if (m_myLocation == null)
			m_myLocation = m_locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// 둘다 GPS를 수신하는 리스너 변수들이다.
		m_locListenerGPS = new LocationListenerEx();
		m_locListenerNetwork = new LocationListenerEx();

		/** 130416 여기 부터 정리 시작하면 됨. */
		try {
			// 위치 정보 갱신.
			m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 1, m_locListenerGPS);
			m_locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 10, m_locListenerNetwork);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean myLocation() 
	{
		userLocationInit();
		if (m_locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| m_locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) 
		{

			if (m_myLocation != null) 
			{
				m_fLatitude = m_myLocation.getLatitude();
				m_fLongitude = m_myLocation.getLongitude();
				g_locationHere = m_myLocation;
				if(m_myLocation.getLatitude()>37.70453488762476 || m_myLocation.getLongitude()>127.17361450195312
						|| m_myLocation.getLatitude()<37.43677099171195 || m_myLocation.getLongitude()<126.76300048828125)
				{
					Log.i("INFO", "outOfseoul");
					createGPSDialog(OutOfSeoul);
				}
				Log.i("INFO", "Latitude:"+m_myLocation.getLatitude()+"Longitude :"+m_myLocation.getLongitude());
			} else 
			{
				Log.e("ERROR", "locManager.getLastKnownLocation return null");

				m_fLatitude = 37.5664700;
				m_fLongitude = 126.9779630;

				g_locationHere.setLatitude(m_fLatitude);
				g_locationHere.setLongitude(m_fLongitude);

				createGPSDialog(LocationFail);
				return false;
			}

			// g_sensorcallback에 값이 있으면 설정이지만 처음엔 해당사항이 없는듯하다.
			try {
				if (g_sensorcallback != null)
					g_sensorcallback.UpdateLocation(g_locationHere);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		} else {
			createGPSDialog(NoUseGPS);
			return false;
		}
	}

	/**
	 * =====================================================================================
	 *  Called when the activity is first created.
	 * =====================================================================================
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mapview);
		mContext = this;

		createCategoryBitmap();
		MapButtonSetting();
		summaryViewSetting();
		navigationViewSetting();
		userLocationInit();
		databaseRead();
		SearchSetting();

		blinkingactivity = BlinkingMap.this;

		/*
		 * =====================================================================================
		 *  맵 타일 다운로드 구문.
		 * =====================================================================================
		 */
		noTile = getResources().getDrawable(R.drawable.no_tile);
		Global.strMapDBFilePath = Global.GetPathWithSDCard() + Global.g_strMapDBFileName;

		/*
		 * =====================================================================================
		 * 
		 * 오픈 스트리트 맵 API 참고 http://wiki.openstreetmap.org/wiki/API_v0.6
		 * 
		 * 
		 * osmdroid의 class 타일 생성을 위한 정보의 초기화 상속 범위 : XYTileSource >
		 * OnlineTileSourceBase >BitmapTileSourceBase
		 * ("이름","네트워크 모드","맵 이미지 최소 범위",맵 이미지 최대
		 * 범위,"타일 사이즈","이미지 파일 종류","생략된듯.")
		 * 
		 * String aName, string aResourceId, int aZoomMinLevel, int
		 * aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding,
		 * String aBaseUrl
		 * =====================================================================================
		 */

		// final float scale =
		// getBaseContext().getResources().getDisplayMetrics().density;
		final int nTileSize = (int)(256*1.5);

		final XYTileSource tileSource = new XYTileSource("My Tile Source",ResourceProxy.string.offline_mode, nMinZoomLevel,
				nMaxZoomLevel, nTileSize, ".png");

		// MapView를 초기 설정하기 위한 함수 Context , TileSize , , Tile 이미지를 추출해 내기 위한
		// 전반적인 설정.-by 형철.
		m_mapView = new BoundedMapView(this, nTileSize, new DefaultResourceProxyImpl(this), new MapTileProvderEx(
				tileSource, noTile, getApplicationContext()));

		// 나도 처음엔 이해 안갔는데 OSM Droid 의 tile을 MS에서 만든 Mappoint를 사용한다.
		TileSystem.setTileSize(nTileSize);

		/*
		 * =====================================================================================
		 * 지도 회전을 위해서..
		 * =====================================================================================
		 */
		m_mapLayout = new LinearLayout(this);

		m_mapLayout.addView(m_mapView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.layout_map_view);

		m_rotateView = new RotateViewGroup(this);

		m_rotateView.addView(m_mapLayout);
		parentLayout.addView(m_rotateView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

		m_mapView.setUseDataConnection(false);
		m_mapView.setBuiltInZoomControls(false); // 줌 컨트롤 버튼 hidden
		m_mapView.setMultiTouchControls(true); // 핀치줌 가능.
		m_mapView.setClickable(true);
		m_mapView.setBackgroundColor(Color.WHITE);

		// zoom level 14 ~ 18
		m_mapView.setMaxZoomLevel(nMaxZoomLevel);
		m_mapView.setMinZoomLevel(nMinZoomLevel);

		m_mapView.getController().setZoom(zoomLevel); // 처음 화면에 보이는 Zoom level 16
		m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel, mContext);

		topLeftGpt = new GeoPoint(37604270, 126932172);
		bottomRightGpt = new GeoPoint(37522082, 127035856);

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
				m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel, mContext);

				markerRefresh();

				return false;
			}

			public boolean onScroll(ScrollEvent arg0) 
			{
				summaryviewclose = true;
				return false;
			}
		});

		m_mapView.setOnTouchListener(new OnTouchListener() 
		{			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					if(!summaryviewclose)
					{
						if(summaryView.getVisibility() == View.VISIBLE)
							summaryViewVisibleSet(summaryViewInVisible, 0);

						if(onePlaceOverlay != null)
						{
							m_mapView.getOverlays().clear();
							lineId = null;
							onePlaceOverlay = null;
							onePlaceInfo.clear();
							categoryType = -1;
							markerRefresh();
						}

						idx = null;
						markerSel(0);
					}else
					{
						markerRefresh();
					}

					if(place_fLatitude == 0)
					{
						place_fLatitude = m_mapView.getMapCenter().getLatitudeE6()/1e6;
						place_fLongitute = m_mapView.getMapCenter().getLongitudeE6()/1e6;
					}
				}
				HideKeyboard(m_mapView);
				summaryviewclose = false;
				return false;
			}
		});

		g_sensorcallback = this;
		g_bNeedToReloadActivity = true; // 초기 터치를 위해서
	} // - onCreate end..

	private OnItemClickListener onClickListItem = new OnItemClickListener() 
	{
		boolean allplace = false;
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
		{
			ListViewVisibleSetting(1);
			Log.i("INFO", "m_listview Visible : "+m_ListView.getVisibility());
			for(Entry<String, PlaceInfo> itemInfo : allplaceinfo.entrySet())
			{
				PlaceInfo info = itemInfo.getValue();
				if(info.m_strName.equals(m_Adapter.getItem(arg2)))
				{
					idx = info.m_nID;
					place_fLatitude = info.m_fLatitude;
					place_fLongitute = info.m_fLongitude;
					m_locMarkTarget = new Location("MarkTaget");
					m_locMarkTarget.setLatitude(place_fLatitude);
					m_locMarkTarget.setLongitude(place_fLongitute);
					DestinationTitle = info.m_strName;
					summaryTitle.setText(DestinationTitle);
					if (info.m_nCategoryID == 99)
					{
						summarysubTitle.setVisibility(View.INVISIBLE);
						subwaylineNumSet(idx);
					} else
					{
						summaryAddressInit();
						summarysubTitle.setVisibility(View.VISIBLE);
						summarysubTitle.setText(info.m_strAddress);
					}
					summaryViewVisibleSet(summaryViewVisible, 2);
				}
				allplace = true;
			}

			if(!allplace)
			{
				//premiumInfo의 이름이 같아 에러가 나서 수정했습니다.
				for(Entry<String, PremiumInfo> premiumItemInfo : premiumInfo.entrySet())
				{
					PremiumInfo preInfo = premiumItemInfo.getValue();
					if(preInfo.m_strName.equals(m_Adapter.getItem(arg2)))
					{
						idx = preInfo.m_nID;
						place_fLatitude = preInfo.m_fLatitude;
						place_fLongitute = preInfo.m_fLongitude;
						m_locMarkTarget = new Location("MarkTaget");
						m_locMarkTarget.setLatitude(place_fLatitude);
						m_locMarkTarget.setLongitude(place_fLongitute);
						DestinationTitle = preInfo.m_strName;
						String summaryImageName = Global.MD5Encoding(preInfo.m_strImageFilePath);
						File imagefile = new File(Global.GetPathWithSDCard()+".temp/"+summaryImageName);

						if(imagefile.exists())
							Picasso.with(mContext).load(imagefile).fit().into(summaryImage);
						else
							new summaryImageDownload().execute(preInfo.m_strImageFilePath,summaryImageName);
						summaryTitle.setText(DestinationTitle);
						summaryAddressInit();
						summarysubTitle.setVisibility(View.VISIBLE);
						summarysubTitle.setText(preInfo.m_strAddress);
						summarylikeCount.setText(""+preInfo.m_nLikeCount);
						summaryViewVisibleSet(summaryViewVisible, 1);

						summaryDetail = true;
						summaryviewclose = true;
					}
				}
			}
			categoryType = -1;
			mapCenterset(0);
			markerSel(1);
			allplace = false;
			searchEditText.setText("");
			searchEditText.setHint(R.string.message_search_word_here);
			HideKeyboard(m_ListView);
			m_Adapter.clear();
		}
	};

	//    @Override
	//    public void onBackPressed()
	//    { 
	//    	if(m_ListView.getVisibility() == View.VISIBLE)
	//    	{
	//    		searchEditText.setText("");
	//			searchEditText.setHint(R.string.message_search_word_here);
	//    		ListViewVisibleSetting(1);
	//    	}else
	//    	{
	//    		finish();
	//    	}
	//    }

	//    public void showGuide() 
	//    {
	//    	appFininshToast = Toast.makeText(this, R.string.app_finish, Toast.LENGTH_SHORT);
	//    	appFininshToast.show();
	//    }

	/**
	 * =====================================================================================
	 *  버튼 세팅
	 * =====================================================================================
	 */

	public void databaseRead()
	{
		allPlaceInfoItems = new ArrayList<OverlayItem>();
		premiumitem = new ArrayList<OverlayItem>();
		MapPlaceDataManager.getInstance(this).initDatabase();
		allplaceinfo = MapPlaceDataManager.getInstance(this).getAllPlaces();
		premiumInfo = MapPlaceDataManager.getInstance(this).getallPremium();
	}

	public void MapButtonSetting() 
	{
		btnlayout = (FrameLayout) findViewById(R.id.btn_layout);
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

	public void SearchSetting()
	{
		m_ListView = (ListView) findViewById(R.id.search_list);
		emptysearchList = (RelativeLayout) findViewById(R.id.empty_search_container);
		m_Adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		m_ListView.setAdapter(m_Adapter);
		m_ListView.setOnItemClickListener(onClickListItem);

		backBtn = (ImageButton) findViewById(R.id.back_btn);
		searchView = (LinearLayout) findViewById(R.id.search_layout);
		searchEditText = (EditText) findViewById(R.id.SearchEditText);
		searchCancel = (ImageButton) findViewById(R.id.search_cancel);

		backBtn.setOnClickListener(this);
		searchEditText.setOnEditorActionListener(this);
		searchCancel.setOnClickListener(this);
		placewatcher = new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				if(searchEditText.getText().toString().length() > 1)
				{
					if(summaryView.getVisibility() == View.VISIBLE)
						summaryViewVisibleSet(summaryViewInVisible, 0);
					if(m_Adapter != null)
					{
						m_Adapter.clear();
					}

					for(Entry<String, PlaceInfo> itemInfo : allplaceinfo.entrySet())
					{
						PlaceInfo info = itemInfo.getValue(); 
						if(info.m_strName.contains(searchEditText.getText().toString()))
						{
							m_Adapter.add(info.m_strName);
						}
					}
					
					for(Entry<String, PremiumInfo> preInfo : premiumInfo.entrySet())
					{
						PremiumInfo info2 = preInfo.getValue();
						if(info2.m_strName.contains(searchEditText.getText().toString()))
						{
							m_Adapter.add(info2.m_strName);
						}
					}

					if(m_Adapter.getCount() > 0)
					{
						if(m_ListView.getVisibility() == View.GONE)
							ListViewVisibleSetting(0);
					}else
					{
						Toast search = Toast.makeText(mContext, R.string.msg_no_search_result, Toast.LENGTH_SHORT);
						search.show();
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		searchEditText.addTextChangedListener(placewatcher);
	}

	public void summaryViewSetting() 
	{
		summaryView = (LinearLayout) findViewById(R.id.summaryView);
		summaryCloseBtn = (ImageButton) findViewById(R.id.summaryCloseBtn);
		summaryCloseBtn2 = (ImageButton) findViewById(R.id.summaryCloseBtn2);
		summaryImage = (ImageView) findViewById(R.id.summaryImage);
		summaryTextView = (LinearLayout) findViewById(R.id.summaryTextView);
		summaryTitle = (TextView) findViewById(R.id.summaryTitle);
		summarysubTitle = (TextView) findViewById(R.id.summarySubTitle);
		summaryLikeLayout = (LinearLayout) findViewById(R.id.summarylikeLayout);
		summarylikeCount = (TextView) findViewById(R.id.summarylikecount);
		subwayLayout = (RelativeLayout) findViewById(R.id.subwayLayout);
		subwayStartBtn = (LinearLayout) findViewById(R.id.subwayStartBtn);
		subwayEndBtn = (LinearLayout) findViewById(R.id.subwayEndBtn);
		naviBtn = (ImageButton) findViewById(R.id.naviBtn);

		summaryView.setClickable(true);
		subwayStartBtn.setClickable(true);
		subwayEndBtn.setClickable(true);
		summaryView.setOnClickListener(this);
		summaryCloseBtn.setOnClickListener(this);
		summaryCloseBtn2.setOnClickListener(this);
		subwayStartBtn.setOnClickListener(this);
		subwayEndBtn.setOnClickListener(this);
		naviBtn.setOnClickListener(this);

		lineNumImage1 = (ImageView) findViewById(R.id.lineNumImage1);
		lineNumImage2 = (ImageView) findViewById(R.id.lineNumImage2);
		lineNumImage3 = (ImageView) findViewById(R.id.lineNumImage3);
		lineNumImage4 = (ImageView) findViewById(R.id.lineNumImage4);

		lineNumText1 = (TextView) findViewById(R.id.lineNumText1);
		lineNumText2 = (TextView) findViewById(R.id.lineNumText2);
		lineNumText3 = (TextView) findViewById(R.id.lineNumText3);
		lineNumText4 = (TextView) findViewById(R.id.lineNumText4);

		text[0] = lineNumText1;
		text[1] = lineNumText2;
		text[2] = lineNumText3;
		text[3] = lineNumText4;
		linenum[0] = lineNumImage1;
		linenum[1] = lineNumImage2;
		linenum[2] = lineNumImage3;
		linenum[3] = lineNumImage4;
	}

	public void summaryViewVisibleSet(int type, int summaryViewID) 
	{
		Log.i("summary Info","type : "+type+" summaryViewID : "+summaryViewID);
		FrameLayout.LayoutParams btnLayoutParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		btnLayoutParams.gravity = Gravity.BOTTOM;
		Animation summaryIn = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
		Animation summaryOut = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
		if (type == summaryViewVisible) 
		{
			if(summaryViewID == 1)
			{
				summaryImage.setVisibility(View.VISIBLE);
				summaryLikeLayout.setVisibility(View.INVISIBLE);
				subwayLayout.setVisibility(View.VISIBLE);
			}else if(summaryViewID == 2)
			{
				summaryImage.setVisibility(View.GONE);
				summaryLikeLayout.setVisibility(View.INVISIBLE);
				subwayLayout.setVisibility(View.VISIBLE);
			}else if(summaryViewID == 3)
			{
				summaryImage.setVisibility(View.GONE);
				summaryLikeLayout.setVisibility(View.INVISIBLE);
				subwayLayout.setVisibility(View.GONE);
			}

			//			if(summaryView.getVisibility() == View.INVISIBLE)
			//			{
			//				summaryView.startAnimation(summaryIn);
			//				btnlayout.startAnimation(summaryIn);
			//			}
			summaryView.setVisibility(View.VISIBLE);
			summaryView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			btnLayoutParams.bottomMargin = summaryView.getMeasuredHeight() + 5;
		} else 
		{
			//			if(summaryView.getVisibility() == View.VISIBLE)
			//			{
			//				summaryView.startAnimation(summaryOut);
			//			}
			summaryView.setVisibility(View.INVISIBLE);
			btnLayoutParams.bottomMargin = 0;
		}

		btnlayout.setLayoutParams(btnLayoutParams);
	}

	public void navigationViewSetting() 
	{
		navigationView = (RelativeLayout) findViewById(R.id.navigationView);
		naviInfo01 = (TextView) findViewById(R.id.naviInfo01);
		naviInfo02 = (TextView) findViewById(R.id.naviInfo02);
		naviSubInfo = (TextView) findViewById(R.id.naviSubInfo);
		naviStopBtn = (ImageButton) findViewById(R.id.naviStopBtn);

		naviStopBtn.setOnClickListener(this);
	}

	public void ListViewVisibleSetting(int visible)
	{
		if(visible == 0)
		{
			m_ListView.setVisibility(View.VISIBLE);
			searchCancel.setVisibility(View.VISIBLE);
			m_ListView.bringToFront();
		}else
		{
			m_ListView.setVisibility(View.GONE);
			searchCancel.setVisibility(View.INVISIBLE);
		}
	}

	public void HideKeyboard(View view)
	{
		InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(view.getWindowToken(),0);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		if(searchEditText.getText().toString().length() > 1)
		{
			Toast search;
			switch (actionId) {
			case EditorInfo.IME_ACTION_SEARCH:
				if(summaryView.getVisibility() == View.VISIBLE)
					summaryViewVisibleSet(summaryViewInVisible, 0);
				if(m_Adapter != null)
				{
					m_Adapter.clear();
				}

				for(Entry<String, PlaceInfo> itemInfo : allplaceinfo.entrySet())
				{
					PlaceInfo info = itemInfo.getValue(); 
					if(info.m_strName.contains(searchEditText.getText().toString()))
					{
						m_Adapter.add(info.m_strName);
					}
				}
				
				for(Entry<String, PremiumInfo> preInfo : premiumInfo.entrySet())
				{
					PremiumInfo info2 = preInfo.getValue();
					if(info2.m_strName.contains(searchEditText.getText().toString()))
					{
						m_Adapter.add(info2.m_strName);
					}
				}

				if(m_Adapter.getCount() > 0)
				{
					if(m_ListView.getVisibility() == View.GONE)
						ListViewVisibleSetting(0);
				}
				else
				{
					search = Toast.makeText(mContext, R.string.msg_no_search_result, Toast.LENGTH_SHORT);
					search.show();
				}
				break;

			default:
				break;
			}
		}
		HideKeyboard(m_mapView);
		return false;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.myLocationBtn:
			if(myLocation() == true)
			{
				if (myLocationState == 0) 
				{
					myLocationState = 1;

					RotateMap(0, 100);
					MoveToCurrentPosition();
					ShowCurrentPosition(myLocationState,0);
					myLocationBtn.setBackgroundResource(R.drawable.btn_my_location_on);
				} else if (myLocationState == 1) 
				{
					myLocationState = 2;
					ShowCurrentPosition(myLocationState,0);

					// - 센서 매니저 등록 (gSensorEventListner,gSensorType,gDelay)
					m_sensorManager.registerListener(m_sensorListener,
							m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
							SensorManager.SENSOR_DELAY_UI);

					MoveToCurrentPosition();

					myLocationBtn.setBackgroundResource(R.drawable.btn_my_location_direction);
				} else {
					myLocationState = 0;
					if (m_curPosOverlay != null) {
						m_mapView.getOverlays().remove(m_curPosOverlay);
						m_curPosOverlay = null;
					}
					myLocationBtn.setBackgroundResource(R.drawable.btn_my_location);
					RotateMap(0, 100);
					m_sensorManager.unregisterListener(m_sensorListener);
				}
				m_mapView.invalidate();
			}
			break;

		case R.id.nearbyBtn:
			nearbydialog = new NearByDialog(mContext, categoryType);
			nearbydialog.show();
			summaryViewVisibleSet(summaryViewInVisible, 0);
			idx = null;
			markerSel(0);
			nearbydialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					Log.i("DIALOG", "categoryType : "+categoryType);
					markerRefresh();
				}
			});
			break;

		case R.id.subwayBtn:
			destSubwayIntent = new Intent(BlinkingMap.this, SubwayActivity.class);
			startActivity(destSubwayIntent);
			break;

		case R.id.zoomInbtn:
			if (zoomLevel < nMaxZoomLevel)
				zoomLevel = zoomLevel + 1;
			else
				zoomLevel = nMaxZoomLevel;
			m_mapView.getController().setZoom(zoomLevel);

			// 제한된 맵 범위 설정 및 Scroll시 이동 반경 제한 세팅.
			m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel, mContext);
			break;

		case R.id.zoomOutbtn:
			if (zoomLevel > nMinZoomLevel)
				zoomLevel = zoomLevel - 1;
			else
				zoomLevel = nMinZoomLevel;
			m_mapView.getController().setZoom(zoomLevel);

			// 제한된 맵 범위 설정 및 Scroll시 이동 반경 제한 세팅.
			m_mapView.setScrollableAreaLimit(m_boundingBox, zoomLevel, mContext);
			break;

		case R.id.summaryView:
			if(summaryDetail == true)
			{
				int i = 0;
				Intent detailPlaceIntent = new Intent(this, PlaceDetailActivity.class);
				i = Integer.parseInt(idx);
				detailPlaceIntent.putExtra("place_idx", i);
				detailPlaceIntent.putExtra("is_map", true);
				startActivity(detailPlaceIntent);
				Log.i("INFO", "place_idx : " + idx);
			}
			break;

		case R.id.summaryCloseBtn:
		case R.id.summaryCloseBtn2:
			summaryViewVisibleSet(summaryViewInVisible, 0);
			if(onePlaceOverlay != null)
				m_mapView.getOverlays().clear();

			lineId = "";
			idx = null;

			markerRefresh();

			m_mapView.invalidate();
			break;

		case R.id.subwayStartBtn:
			destSubwayIntent = new Intent(BlinkingMap.this, SubwayActivity.class);
			double[] latLon = new double[3];
			latLon[0] = place_fLatitude;
			latLon[1] = place_fLongitute;
			latLon[2] = 0;
			destSubwayIntent.putExtra("set_dest_station_lat_lon", latLon);
			startActivity(destSubwayIntent);
			break;

		case R.id.subwayEndBtn:
			destSubwayIntent = new Intent(BlinkingMap.this, SubwayActivity.class);
			double[] destLatLong = new double[3];
			destLatLong[0] = place_fLatitude;
			destLatLong[1] = place_fLongitute;
			destLatLong[2] = 1;
			destSubwayIntent.putExtra("set_dest_station_lat_lon", destLatLong);
			destSubwayIntent.putExtra("dest_name", summaryTitle.getText().toString());
			startActivity(destSubwayIntent);			
			break;

		case R.id.naviBtn:
			if(myLocation() == true)
			{
				navigationStart();
			}
			break;

		case R.id.naviStopBtn:
			navigationStop();
			break;

		case R.id.back_btn:
			finish();
			break;

		case R.id.search_cancel:
			ListViewVisibleSetting(1);
			searchEditText.setText("");
			searchEditText.setHint(R.string.message_search_word_here);
			break;
		}
	}

	/**
	 * ======================================================================================= 
	 * navigation btn 관련
	 * =======================================================================================
	 */
	public void viewSetForNavigation() 
	{
		summaryViewVisibleSet(summaryViewInVisible, 0);
		searchView.setVisibility(View.GONE);
		zoomInBtn.setVisibility(View.INVISIBLE);
		zoomOutBtn.setVisibility(View.INVISIBLE);
		btnlayout.setVisibility(View.INVISIBLE);
		navigationView.setVisibility(View.VISIBLE);
	}

	public void navigationStart() 
	{
		m_bTrackingCurrentPosition = true;
		m_mapView.getOverlays().clear();
		viewSetForNavigation();
		MoveToCurrentPosition();
		//ShowCurrentPosition(UseLocationNavi,g_fLastAngle);

		navigationCal(m_locMarkTarget);

		m_sensorManager.registerListener(m_sensorListener,m_sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	public void navigationStop() 
	{
		m_bTrackingCurrentPosition = false;
		myLocationState = 0;
		myLocationBtn.setBackgroundResource(R.drawable.btn_my_location);
		searchView.setVisibility(View.VISIBLE);
		zoomInBtn.setVisibility(View.VISIBLE);
		zoomOutBtn.setVisibility(View.VISIBLE);
		btnlayout.setVisibility(View.VISIBLE);
		navigationView.setVisibility(View.INVISIBLE);

		RotateMap(0, 100);

		m_sensorManager.unregisterListener(m_sensorListener);
		m_mapView.getOverlays().clear();
		m_mapView.getOverlays().remove(navigationOverlay);

		lineId = null;
		idx = null;
		markerRefresh();

		summaryViewVisibleSet(summaryViewInVisible, 0);
	}

	public void navigationCal(Location MarkTaget) 
	{
		naviTexts = new String[2];
		int clock = 0;

		Location locHere = g_locationHere;

		String pattern = "##.##";
		DecimalFormat dformat = new DecimalFormat(pattern);
		double distance = locHere.distanceTo(MarkTaget) * 0.001;
		double temp;
		String kmeter = dformat.format(distance);

		Log.i("INFO", "distanceto " + locHere.distanceTo(MarkTaget));

		if (distance < 1) 
		{
			String pMeter = "###";
			DecimalFormat dMeterformat = new DecimalFormat(pMeter);

			Double dMeter = Double.parseDouble(kmeter);
			String meter = dMeterformat.format(dMeter * 1000);
			int distan = (int) (distance*1000);
			if(distan<=20)
			{
				m_sensorManager.unregisterListener(m_sensorListener);
				navigationStop();
				Toast toast = Toast.makeText(mContext, R.string.msg_dest_arrived, Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			clock = (int)(distance*1000)/60;
			naviTexts[0] = meter + "m";
			if(clock <= 1)
			{
				naviTexts[1] = "1min";
			}else
			{
				naviTexts[1] = "" + clock + "min";
			}
		} else 
		{
			if(distance<3.6)
			{
				temp = distance/3.6;
				temp = temp*100;
				clock = (int)temp;
				naviTexts[1] = ""+clock+"min";
			}else
			{
				clock = (int) ((int) distance / 3.6);
				naviTexts[1] = "" + clock + "hour";
			}
			naviTexts[0] = kmeter + "km";

		}
		addNavigationOverlay(locHere, MarkTaget);
		naviInfo01.setText(naviTexts[0]);
		naviInfo02.setText(naviTexts[1]);
		String naviSubText = "到"+DestinationTitle;
		naviSubInfo.setText(naviSubText);
	}

	public void addNavigationOverlay(Location locHere, Location MarkTaget) 
	{	
		m_mapView.getOverlays().remove(navigationOverlay);
		ArrayList<OverlayItem> naviList = new ArrayList<OverlayItem>();
		OverlayItem item = new OverlayItem("", "", new GeoPoint(locHere.getLatitude(), locHere.getLongitude()));
		OverlayItem item2 = new OverlayItem("", "", new GeoPoint(MarkTaget.getLatitude(), MarkTaget.getLongitude()));
		naviList.add(item);
		naviList.add(item2);
		navigationOverlay = new NavigationOverlay(naviList,	null, new DefaultResourceProxyImpl(this), this,g_fLastAngle);

		m_mapView.getOverlays().add(navigationOverlay);
		m_mapView.invalidate();
	}

	public void createGPSDialog(int type) 
	{
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.map_notification);
		ab.setPositiveButton(R.string.map_confirm,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});

		if (type == NoUseGPS)
			ab.setMessage(R.string.map_gps_noUse);
		else if (type == LocationFail)
			ab.setMessage(R.string.map_gps_Location_fail);
		else if (type == OutOfSeoul)
			ab.setMessage(R.string.map_gps_out_of_Seoul);
		ab.show();
	}

	/**
	 * ===================================================================================== 
	 * 현재 위치 관련 로직.(마커 x, 원 표시)
	 * =====================================================================================
	 */
	private void ShowCurrentPosition(int mylocationstate,float fAngle) 
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
			m_curPosOverlay = new CurrentPositionOverlay(pList, null,
					new DefaultResourceProxyImpl(this), this, myLocationState,g_fLastAngle);

			m_mapView.getOverlays().add(m_curPosOverlay);
		}

		m_mapView.invalidate();
	}

	private void MoveToCurrentPosition() 
	{
		if (m_fLatitude > 0 && m_fLongitude > 0) 
		{
			mapCenterset(1);
		} else 
		{
			Log.d("debug", "lat = " + m_fLatitude + "  lon = " + m_fLongitude);
		}
	}

	/**
	 * =====================================================================================
	 *  Interface Callback Part!
	 * =====================================================================================
	 */

	@Override
	public void UpdateDirection(float fAngle) 
	{

		g_fLastAngle = fAngle;

		RotateMap(fAngle, 100);

		if(!m_bTrackingCurrentPosition)
			ShowCurrentPosition(UseLocationNavi,fAngle);
		else 
			navigationCal(m_locMarkTarget);
	}

	@Override
	public void UpdateLocation(Location locCur) 
	{	
		m_fLatitude = locCur.getLatitude();
		m_fLongitude = locCur.getLongitude();
		MoveToCurrentPosition();

		if(!m_bTrackingCurrentPosition)
			ShowCurrentPosition(UseLocationNavi,g_fLastAngle);
		else 
			navigationCal(m_locMarkTarget);
	}

	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		if (intent != null) 
		{
			if (intent.hasExtra("lineId"))
				lineId = intent.getStringExtra("lineId");
			if(intent.hasExtra("Latitude"))
				place_fLatitude = intent.getDoubleExtra("Latitude", 0);
			if(intent.hasExtra("Longitude"))
				place_fLongitute = intent.getDoubleExtra("Longitude", 0);

			mapCenterset(0);
		}else
			lineId="";

		Log.i("INTENT",""+ lineId);
	}

	@Override
	protected void onResume() 
	{
		super.onResume();

		Intent i = getIntent();
		if (i.hasExtra("lineId"))
			lineId = i.getStringExtra("lineId");
		if(i.hasExtra("Latitude"))
			place_fLatitude = i.getDoubleExtra("Latitude", 0);
		if(i.hasExtra("Longitude"))
			place_fLongitute = i.getDoubleExtra("Longitude", 0);

		if (allplaceOverlay != null)
			m_mapView.getOverlays().remove(allplaceOverlay);

		mapCenterset(0);

	}

	public void mapCenterset(int type)
	{
		Log.i("INFO","place_fLatitude : "+place_fLatitude + "place_fLongitute : "+ place_fLongitute);
		if(type == 0)
		{
			if (place_fLatitude == 0 || place_fLongitute == 0) 
			{
				m_mapView.getController().setCenter(new GeoPoint(37.5664700, 126.9779630));
			} else {
				m_mapView.getController().setCenter(new GeoPoint(place_fLatitude, place_fLongitute));
			}
		}else
		{
			if (m_fLatitude == 0 || m_fLongitude == 0) 
			{
				m_mapView.getController().setCenter(new GeoPoint(37.5664700, 126.9779630));
			} else {
				m_mapView.getController().setCenter(new GeoPoint(m_fLatitude, m_fLongitude));
			}
		}
		markerRefresh();
	}

	public void markerSel(int select)
	{
		if(idx != null)
		{
			if(allplaceOverlay != null&&select==0)
			{
				allplaceOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),idx);
				premiumOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),null);
			}
			if(premiumOverlay != null&&select==1)
			{
				premiumOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),idx);
				allplaceOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),null);
			}
		}else
		{
			if(allplaceOverlay != null)
				allplaceOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),null);
			if(premiumOverlay != null)
				premiumOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),null);
		}

		m_mapView.invalidate();
	}

	public void markerRefresh()
	{
		if(m_timer!=null) 
		{
			m_timer.cancel();
		}

		if(!m_bTrackingCurrentPosition) 
		{
			TimerTask onTimer = new TimerTask()
			{
				@Override
				public void run()
				{
					if (allplaceOverlay != null)
						m_mapView.getOverlays().remove(allplaceOverlay);
					if(premiumOverlay != null)
						m_mapView.getOverlays().remove(premiumOverlay);
					topLeftGpt = (GeoPoint) m_mapView.getProjection().fromPixels(0, 0);
					bottomRightGpt = (GeoPoint) m_mapView.getProjection().fromPixels(m_mapView.getWidth(),m_mapView.getHeight());

					new MarkerSetAsyncTask().execute();
				}
			};

			m_timer = new Timer();
			m_timer.schedule(onTimer, 1000);
		}
	}

	/**
	 * ===================================================================================== 
	 * 단순 계산...
	 * =====================================================================================
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
			RotateAnimation r = new RotateAnimation(fAngle, fAngle,Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			r.setDuration(duration);
			r.setFillAfter(true);

			m_mapLayout.startAnimation(r);
			m_rotateView.m_fAngle = fAngle;
			m_rotateView.invalidate();
		} else 
		{
			if (m_mapView != null)
				m_mapView.setRotation(fAngle);
		}
	}

	public int DpToPixel(int DP) 
	{
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP,
				getResources().getDisplayMetrics());

		return (int) px;
	}

	public class MarkerSetAsyncTask extends AsyncTask<Void, Integer, Void> 
	{
		ArrayList<OverlayItem> localItems = new ArrayList<OverlayItem>();
		int attractionCount = 0;
		int shoppingCount = 0;
		int restaurantCount = 0;

		@Override
		protected Void doInBackground(Void... params) 
		{
			if (lineId != null && !lineId.equals("")) 
			{
				onePlaceInfo = new ArrayList<PlaceInfo>();
				MapPlaceDataManager.getInstance(mContext).initDatabase();
				onePlaceInfo = MapPlaceDataManager.getInstance(mContext).getPlacefromIdx(lineId);
				Log.i("INFO", "onePlaceInfo size :"+onePlaceInfo.size());
				oneItems = new ArrayList<OverlayItem>();
				OverlayItem oneItem = new OverlayItem("" + onePlaceInfo.get(0).m_nCategoryID,onePlaceInfo.get(0).m_strName,
						onePlaceInfo.get(0).m_nID, new GeoPoint(onePlaceInfo.get(0).m_fLatitude,onePlaceInfo.get(0).m_fLongitude));

				oneItems.add(oneItem);
			} else 
			{
				double bottomLat = bottomRightGpt.getLatitudeE6()/1e6;
				double topLat = topLeftGpt.getLatitudeE6()/1e6;
				double bottomLng = topLeftGpt.getLongitudeE6()/1e6;
				double topLng = bottomRightGpt.getLongitudeE6()/1e6;

				localItems.clear();

				for (Entry<String, PlaceInfo> itemInfo : allplaceinfo.entrySet()) 
				{	
					PlaceInfo info = itemInfo.getValue(); 

					if(topLat >info.m_fLatitude	&& bottomLng<info.m_fLongitude && bottomLat<info.m_fLatitude && topLng>info.m_fLongitude)
					{	
						if(categoryType == 1)
						{
							if(info.m_nCategoryID == 1 || info.m_nCategoryID == 3 || info.m_nCategoryID == 4 
									|| info.m_nCategoryID == 5 || info.m_nCategoryID == 6)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}
						}else if(categoryType == 2)
						{
							if(info.m_nCategoryID == 2)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 7)
						{
							if(info.m_nCategoryID == 7)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 40)
						{
							if(info.m_nCategoryID == 40)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 99)
						{
							if(info.m_nCategoryID == 99)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 50)
						{
							if(info.m_nCategoryID == 50)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 60)
						{
							if(info.m_nCategoryID == 60)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 70)
						{
							if(info.m_nCategoryID == 70)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else if(categoryType == 80)
						{
							if(info.m_nCategoryID == 80)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}

						}else
						{
							if(m_mapView.getZoomLevel() == 17 || m_mapView.getZoomLevel() == 18)
							{
								OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
								localItems.add(item);
							}else if(m_mapView.getZoomLevel() == 16)
							{
								if(info.m_nCategoryID == 1 || info.m_nCategoryID == 2 || info.m_nCategoryID == 3 || info.m_nCategoryID == 4 
								   || info.m_nCategoryID == 5 || info.m_nCategoryID == 7 || info.m_nCategoryID == 6 || info.m_nCategoryID == 99
								   || info.m_nCategoryID == 60	)
								{
									OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
									localItems.add(item);
								}
							}else if (m_mapView.getZoomLevel() == 15)
							{
								if((info.m_nCategoryID == 1 || info.m_nCategoryID == 3 || info.m_nCategoryID == 4 
									|| info.m_nCategoryID == 5 || info.m_nCategoryID == 6) && attractionCount>10)
								{
									OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
									localItems.add(item);
									attractionCount++;
								}
								
								if(info.m_nCategoryID == 2 && shoppingCount>10)
								{
									OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
									localItems.add(item);
									shoppingCount++;
								}
								
								if(info.m_nCategoryID == 7 && restaurantCount>10)
								{
									OverlayItem item = new OverlayItem(""+ info.m_nCategoryID,info.m_strName,info.m_nID, new GeoPoint(info.m_fLatitude,info.m_fLongitude));
									localItems.add(item);
									restaurantCount++;
								}
							}
						}
					}
				}

				premiumitem.clear();

				for (Entry<String, PremiumInfo> premiumItemInfo : premiumInfo.entrySet()) 
				{
					PremiumInfo premiumInfo = premiumItemInfo.getValue();
					if(topLat >premiumInfo.m_fLatitude	&& bottomLng<premiumInfo.m_fLongitude 
							&& bottomLat< premiumInfo.m_fLatitude && topLng > premiumInfo.m_fLongitude)
					{
						if(categoryType == 1)
						{
							if(premiumInfo.m_nCategoryID == 1 || premiumInfo.m_nCategoryID == 3 || premiumInfo.m_nCategoryID == 4 
									|| premiumInfo.m_nCategoryID == 5 || premiumInfo.m_nCategoryID == 6)
							{
								OverlayItem item = new OverlayItem(""+ premiumInfo.m_nCategoryID,
										premiumInfo.m_strName, premiumInfo.m_nID,
										new GeoPoint(premiumInfo.m_fLatitude,premiumInfo.m_fLongitude));
								premiumitem.add(item);
							}
						}else if(categoryType == -1)
						{
							OverlayItem item = new OverlayItem(""+ premiumInfo.m_nCategoryID,
									premiumInfo.m_strName, premiumInfo.m_nID,
									new GeoPoint(premiumInfo.m_fLatitude,premiumInfo.m_fLongitude));
							premiumitem.add(item);
						}
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			allPlaceInfoItems = localItems;
			refreshState();
		}
	}

	// - ui
	public void refreshState() 
	{
		if (lineId != null && !lineId.equals("")) 
		{
			Log.i("INFO","refresh :"+lineId);
			onePlaceOverlay = new OnePlaceOverlay(oneItems, null, new DefaultResourceProxyImpl(mContext), mContext);
			m_mapView.getOverlays().add(onePlaceOverlay);
			summaryTitle.setText(onePlaceInfo.get(0).m_strName);

			summaryAddressInit();
			subwaylineNumSet(onePlaceInfo.get(0).m_nID);
			m_locMarkTarget = new Location("MarkTaget");
			m_locMarkTarget.setLatitude(onePlaceInfo.get(0).m_fLatitude);
			m_locMarkTarget.setLongitude(onePlaceInfo.get(0).m_fLongitude);
			DestinationTitle = onePlaceInfo.get(0).m_strName;
			summaryViewVisibleSet(summaryViewVisible, 3);
		} else 
		{
			if(premiumitem != null)
			{
				premiumOverlay = new PlaceOverlay(premiumitem, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
					@Override
					public boolean onItemLongPress(int arg0,OverlayItem arg1) 
					{
						return false;
					}

					@Override
					public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) 
					{
						PremiumInfo premiuminfo = premiumInfo.get(arg1.mDescription);
						idx = arg1.mDescription;
						String summaryImageName = Global.MD5Encoding(premiuminfo.m_strImageFilePath);
						place_fLatitude = premiuminfo.m_fLatitude;
						place_fLongitute = premiuminfo.m_fLongitude;
						m_locMarkTarget = new Location("MarkTaget");
						m_locMarkTarget.setLatitude(place_fLatitude);
						m_locMarkTarget.setLongitude(place_fLongitute);
						DestinationTitle = arg1.mTitle;
						File imagefile = new File(Global.GetPathWithSDCard()+".temp/"+summaryImageName);

						if(imagefile.exists())
							Picasso.with(mContext).load(imagefile).fit().into(summaryImage);
						else
							new summaryImageDownload().execute(premiuminfo.m_strImageFilePath,summaryImageName);
						summaryTitle.setText(DestinationTitle);	
						summaryAddressInit();
						summarysubTitle.setVisibility(View.VISIBLE);
						summarysubTitle.setText(premiuminfo.m_strAddress);
						summarylikeCount.setText(""+premiuminfo.m_nLikeCount);
						summaryViewVisibleSet(summaryViewVisible, 1);					

						summaryDetail = true;
						summaryviewclose = true;

						mapCenterset(0);

						markerSel(1);
						return false;
					}
				}, new DefaultResourceProxyImpl(mContext), mContext, categorys, true);

				m_mapView.getOverlays().add(premiumOverlay);
			}

			if(allPlaceInfoItems != null)
			{
				allplaceOverlay = new PlaceOverlay( allPlaceInfoItems, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
					@Override
					public boolean onItemLongPress(int arg0, OverlayItem arg1) 
					{
						return false;
					}

					@Override
					public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) 
					{
						PlaceInfo info = allplaceinfo.get(arg1.mDescription);
						idx = arg1.mDescription;
						place_fLatitude = info.m_fLatitude;
						place_fLongitute = info.m_fLongitude;
						m_locMarkTarget = new Location("MarkTaget");
						m_locMarkTarget.setLatitude(place_fLatitude);
						m_locMarkTarget.setLongitude(place_fLongitute);
						DestinationTitle = arg1.mTitle;
						summaryTitle.setText(DestinationTitle);
						summaryAddressInit();
						if (info.m_nCategoryID == 99) 
						{
							subwaylineNumSet(idx);
						} else 
						{
							summarysubTitle.setVisibility(View.VISIBLE);
							summarysubTitle.setText(info.m_strAddress);
						}
						summaryViewVisibleSet(summaryViewVisible, 2);
						summaryDetail = false;
						summaryviewclose = true;
						mapCenterset(0);
						markerSel(0);
						return false;
					}
				}, new DefaultResourceProxyImpl(mContext), mContext, categorys, false);
				m_mapView.getOverlays().add(allplaceOverlay);
			}
		}

		runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				if(summaryDetail == false && allplaceOverlay != null)
					allplaceOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),idx);
				else if(premiumOverlay != null)
					premiumOverlay.changeMethod((BitmapDrawable) Global.GetDrawable(mContext,R.drawable.pin_map_place),idx);
				m_mapView.invalidate();
			}
		});
	}	

	// - ui
	TextView[] text = new TextView[4];
	ImageView[] linenum = new ImageView[4];
	public void summaryAddressInit()
	{
		text[0].setVisibility(View.INVISIBLE);
		text[1].setVisibility(View.INVISIBLE);
		text[2].setVisibility(View.INVISIBLE);
		text[3].setVisibility(View.INVISIBLE);
		linenum[0].setVisibility(View.INVISIBLE);
		linenum[1].setVisibility(View.INVISIBLE);
		linenum[2].setVisibility(View.INVISIBLE);
		linenum[3].setVisibility(View.INVISIBLE);
	}

	public void subwaylineNumSet(String lineid) 
	{
		summarysubTitle.setVisibility(View.INVISIBLE);
		String[] line = lineid.split("line");

		summaryAddressInit();
		int lineLength = line.length - 1;

		for (int i = 0; lineLength > i; i++)
		{
			char a = line[i + 1].charAt(0);
			if (a == '1') {
				linenum[i].setImageResource(R.drawable.icon_subway_line1);
				text[i].setText("1号线");
			} else if (a == '2') {
				linenum[i].setImageResource(R.drawable.icon_subway_line2);
				text[i].setText("2号线");
			} else if (a == '3') {
				linenum[i].setImageResource(R.drawable.icon_subway_line3);
				text[i].setText("3号线");
			} else if (a == '4') {
				linenum[i].setImageResource(R.drawable.icon_subway_line4);
				text[i].setText("4号线");
			} else if (a == '5') {
				linenum[i].setImageResource(R.drawable.icon_subway_line5);
				text[i].setText("5号线");
			} else if (a == '6') {
				linenum[i].setImageResource(R.drawable.icon_subway_line6);
				text[i].setText("6号线");
			} else if (a == '7') {
				linenum[i].setImageResource(R.drawable.icon_subway_line7);
				text[i].setText("7号线");
			} else if (a == '8') {
				linenum[i].setImageResource(R.drawable.icon_subway_line8);
				text[i].setText("8号线");
			} else if (a == '9') {
				linenum[i].setImageResource(R.drawable.icon_subway_line9);
				text[i].setText("9号线");
			} else if (a == 'd') {
				linenum[i].setImageResource(R.drawable.icon_subway_linesb);
				text[i].setText("新盆唐线");
			} else if (a == 'p') {
				linenum[i].setImageResource(R.drawable.icon_subway_linegc);
				text[i].setText("京春线");
			} else if (a == 'a') {
				linenum[i].setImageResource(R.drawable.icon_subway_linea);
				text[i].setText("机场线");
			} else if (a == 'k') {
				char b = line[i + 1].charAt(1);
				if (b == '2') {
					linenum[i].setImageResource(R.drawable.icon_subway_lineb);
					text[i].setText("盆唐线");
				} else {
					linenum[i].setImageResource(R.drawable.icon_subway_linegj);
					text[i].setText("京义中央线");
				}
			}
			linenum[i].setVisibility(View.VISIBLE);
			text[i].setVisibility(View.VISIBLE);
		}
	}

	List<Drawable> categorys;
	Integer [] categoryResource = {
			R.drawable.icon_map_attraction,R.drawable.icon_map_shopping,
			R.drawable.icon_map_culture,R.drawable.icon_map_history,
			R.drawable.icon_map_rest,R.drawable.icon_map_experience,
			R.drawable.icon_map_restaurant,R.drawable.icon_map_cosmetic,
			R.drawable.icon_map_drugstore,R.drawable.icon_map_infocenter,
			R.drawable.icon_map_starbucks,R.drawable.icon_map_mcdonald,
			R.drawable.icon_map_subway};
	public void createCategoryBitmap()
	{
		if(categorys == null) 
		{
			categorys = new LinkedList<>();
		}

		int reSize = DpToPixel(20);

		for(int resourceId : categoryResource) 
		{
			categorys.add(Global.ResizeDrawable(mContext.getResources(), resourceId, reSize, reSize));	
		}
	}


	private class summaryImageDownload extends AsyncTask<String, Integer, Boolean>
	{
		String testurl;
		String testname;
		@Override
		protected Boolean doInBackground(String... params) 
		{
			testurl = params[0];
			testname = params[1];
			try 
			{
				Global.DownloadImage(testurl, testname);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			File file = new File(Global.GetPathWithSDCard()+".temp/"+testname);
			if(file.exists())
			{
				Picasso.with(mContext).load(file).fit().centerCrop().into(summaryImage);
			}
		}
	}
}
