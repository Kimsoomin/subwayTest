package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayStationsActivity;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.managers.SubwayManager;
import com.dabeeo.hangouyou.map.BlinkingMap;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class SubwayFragment extends Fragment
{
  private static View view;
  private WebView webview;
  private Handler handler = new Handler();
  private LinearLayout stationsInfoLayout;
  private TextView stationsInfoText;
  private TextView startStationName, endStationName;
  private ImageView startStationImage, endStationImage;
  
  private ArrayList<StationBean> stations = new ArrayList<>();
  private ImageView detailStationInfo;
  private boolean isLoadEnded;
  private Runnable afterLoadSubwaysRunnable;
  private double startStationLat = -1, startStationLong = -1;
  private LinearLayout btnFindFirstStation;
  
  private double findNearByStationLat = -1, findNearByStationLon = -1;
  private double setDestFindNearStation = -1;
  
  private ProgressBar progressBar;
  private AlertDialog choiceDialog;
  private Button btnMap, btnNearByStation;
  
  private LocationManager locationManager;
  private LocationListener locationListener;
  
  private Activity activity;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    if (view == null)
    {
      int resId = R.layout.fragment_subway;
      view = inflater.inflate(resId, null);
    }
    return view;
  }
  
  
  public static void viewClear()
  {
    view = null;
  }
  
  
  public void findNearByStation(double lat, double lon)
  {
    Log.w("WARN", "지하철 찾기로 진입함");
    findNearByStationLat = lat;
    findNearByStationLon = lon;
  }
  
  
  public void findNearByStation(double lat, double lon, double type)
  {
    Log.w("WARN", "지하철을 찾고 출도착역으로 설정");
    findNearByStationLat = lat;
    findNearByStationLon = lon;
    setDestFindNearStation = type;
  }
  
  
  @Override
  public void onAttach(final Activity activity)
  {
    this.activity = activity;
    int resId = R.layout.fragment_subway;
    view = LayoutInflater.from(activity).inflate(resId, null);
    stationsInfoLayout = (LinearLayout) view.findViewById(R.id.stations_info);
    stationsInfoText = (TextView) view.findViewById(R.id.text_stations_info);
    detailStationInfo = (ImageView) view.findViewById(R.id.image_stations_info_detail);
    btnFindFirstStation = (LinearLayout) view.findViewById(R.id.layout_find_start_station);
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    
    startStationName = (TextView) view.findViewById(R.id.start_station_name);
    endStationName = (TextView) view.findViewById(R.id.end_station_name);
    startStationImage = (ImageView) view.findViewById(R.id.start_station_image);
    endStationImage = (ImageView) view.findViewById(R.id.end_station_image);
    
    webview = (WebView) view.findViewById(R.id.webview);
    
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setAllowFileAccessFromFileURLs(true);
    webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
    webview.getSettings().setUseWideViewPort(true);
    webview.getSettings().setBuiltInZoomControls(true);
    webview.getSettings().setDisplayZoomControls(false);
    webview.getSettings().setAppCacheEnabled(true);
    webview.getSettings().setDomStorageEnabled(true);
    webview.getSettings().setRenderPriority(RenderPriority.HIGH);
    webview.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);
    webview.getSettings().setAppCachePath(activity.getApplicationContext().getCacheDir().getAbsolutePath());
    webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
    
    webview.addJavascriptInterface(new JavaScriptInterface(), "myClient");
    webview.setWebViewClient(webViewClient);
    webview.setWebChromeClient(webChromeClient);
    webview.loadUrl("file:///android_asset/subway.html");
    
    handler.postDelayed(checkSubwayNativeLoadRunnable, 1000);
    
    btnMap = (Button) view.findViewById(R.id.btn_map);
    btnNearByStation = (Button) view.findViewById(R.id.btn_near_by_station);
    btnMap.setVisibility(View.GONE);
    btnNearByStation.setVisibility(View.GONE);
    
    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    locationListener = new SubwayLocationListener();
    
    btnMap.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(activity, BlinkingMap.class));
      }
    });
    btnNearByStation.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
          buildAlertMessageNoGps();
        else
        {
          progressBar.setVisibility(View.VISIBLE);
          progressBar.bringToFront();
          
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
      }
    });
    super.onAttach(activity);
  }
  
  
  private void buildAlertMessageNoGps()
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.app_name).setMessage(R.string.msg_please_gps_enable).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
      }
    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        dialog.cancel();
      }
    });
    final AlertDialog alert = builder.create();
    alert.show();
  }
  
  private class SubwayLocationListener implements LocationListener
  {
    
    @Override
    public void onLocationChanged(Location loc)
    {
      Log.w("WARN", "LocationChanged! " + loc.getLatitude() + " / " + loc.getLongitude());
      locationManager.removeUpdates(locationListener);
      webview.loadUrl("javascript:subway.setDestStation('" + loc.getLatitude() + "', '" + loc.getLongitude() + "','" + 0 + "')");
      
      progressBar.setVisibility(View.GONE);
    }
    
    
    @Override
    public void onProviderDisabled(String provider)
    {
    }
    
    
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
  }
  
  private Runnable checkSubwayNativeLoadRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      Log.w("WARN", "call checkReady");
      webview.loadUrl("javascript:checkReady()");
      handler.postDelayed(checkSubwayNativeLoadRunnable, 800);
    }
  };
  
  
  public void loadAllStations(Runnable run)
  {
    this.afterLoadSubwaysRunnable = run;
    webview.loadUrl("javascript:subway.get_all_station()");
  }
  
  
  public boolean isLoadEnded()
  {
    return isLoadEnded;
  }
  
  
  public void findStation(final String stationId, String stationName)
  {
    handler.post(new Runnable()
    {
      @Override
      public void run()
      {
        webview.loadUrl("javascript:subway.setCenterWithStationId('" + stationId + "')");
        handler.postDelayed(new Runnable()
        {
          @Override
          public void run()
          {
            showChoiceStationPopUp(stationId);
          }
        }, 500);
      }
    });
    
  }
  
  private WebViewClient webViewClient = new WebViewClient()
  {
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
      Log.w("MainActivity.java | onReceivedError", "|" + errorCode + "|" + description + "|" + failingUrl + "|");
      super.onReceivedError(view, errorCode, description, failingUrl);
    }
  };
  
  private WebChromeClient webChromeClient = new WebChromeClient()
  {
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage)
    {
      Log.w("MainActivity.java | onConsoleMessage", "" + consoleMessage + "|" + consoleMessage.message() + "|" + consoleMessage.sourceId() + "|");
      return super.onConsoleMessage(consoleMessage);
    }
    
    
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
      Log.w("MainActivity.java | onJsAlert", "" + url + "|" + message + "|" + result + "|");
      return super.onJsAlert(view, url, message, result);
    }
  };
  
  
  private void showChoiceStationPopUp(final String stationId)
  {
    try
    {
      CharSequence[] menus = new CharSequence[2];
      menus[0] = "출발역 설정";
      menus[1] = "도착역 설정";
      
      StationBean bean = SubwayManager.getInstance(activity).findStation(stationId);
      final CharSequence[] menuTitles = menus;
      
      Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle(bean.nameCn);
      builder.setItems(menus, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int whichButton)
        {
          if (menuTitles[whichButton].equals("출발역 설정"))
          {
            handler.post(new Runnable()
            {
              @Override
              public void run()
              {
                if (SubwayManager.getInstance(activity).getLatitudeWithSubwayId(stationId) != -1)
                {
                  double lat = SubwayManager.getInstance(activity).getLatitudeWithSubwayId(stationId);
                  double lon = SubwayManager.getInstance(activity).getLongitudeWithSubwayId(stationId);
                  Log.w("WARN", "출발역 경위도 : " + lat + " / " + lon);
                  startStationLat = lat;
                  startStationLong = lon;
                  
                  btnFindFirstStation.setVisibility(View.VISIBLE);
                  btnFindFirstStation.bringToFront();
                  btnFindFirstStation.setOnClickListener(new OnClickListener()
                  {
                    @Override
                    public void onClick(View arg0)
                    {
                      Intent i = new Intent(activity, BlinkingMap.class);
                      i.putExtra("lineId", stationId);
                      i.putExtra("Latitude", startStationLat);
                      i.putExtra("Longitude", startStationLong);
                      startActivity(i);
                    }
                  });
                }
                
                webview.loadUrl("javascript:subway.set_start_station('" + stationId + "')");
                
                choiceDialog = null;
              }
            });
          }
          else if (menuTitles[whichButton].equals("도착역 설정"))
          {
            handler.post(new Runnable()
            {
              @Override
              public void run()
              {
                Log.w("WARN", "StationId: " + stationId);
                webview.loadUrl("javascript:subway.set_end_station('" + stationId + "')");
                choiceDialog = null;
              }
            });
          }
        }
      });
      
      if (choiceDialog == null)
        choiceDialog = builder.create();
      
      if (!choiceDialog.isShowing())
        choiceDialog.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private class JavaScriptInterface
  {
    @JavascriptInterface
    public void completeLoadAllStation(String stationsJSONString)
    {
      Log.w("WARN", "모든 지하철역 가져와서 메모리에 저장함 : " + stationsJSONString);
      JSONArray stationsJsonArray;
      ArrayList<StationBean> stations = new ArrayList<StationBean>();
      try
      {
        stationsJsonArray = new JSONArray(stationsJSONString);
        for (int i = 0; i < stationsJsonArray.length(); i++)
        {
          StationBean bean = new StationBean();
          bean.setJSONObject(stationsJsonArray.getJSONObject(i));
          stations.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      SubwayManager.getInstance(activity).stations.clear();
      SubwayManager.getInstance(activity).stations.addAll(stations);
      afterLoadSubwaysRunnable.run();
    }
    
    
    @JavascriptInterface
    public void onSvgLoadEnded()
    {
      Log.w("WARN", "SVG 로드 완료");
      handler.removeCallbacks(checkSubwayNativeLoadRunnable);
      isLoadEnded = true;
      
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          btnMap.setVisibility(View.VISIBLE);
          btnNearByStation.setVisibility(View.VISIBLE);
          btnNearByStation.bringToFront();
          btnMap.bringToFront();
          
          if (findNearByStationLat != -1)
          {
            //가까운 지하철역을 찾아야 함
            if (setDestFindNearStation == -1)
              webview.loadUrl("javascript:subway.findNearByStation('" + findNearByStationLat + "', '" + findNearByStationLon + "')");
            else
              webview.loadUrl("javascript:subway.setDestStation('" + findNearByStationLat + "', '" + findNearByStationLon + "','" + setDestFindNearStation + "')");
          }
        }
      });
      
    }
    
    
    @JavascriptInterface
    public void completeMarkStation(final String stationsJSONString, final int time)
    {
      Log.w("WARN", "지하철 출도착 마킹 완료 : " + stationsJSONString + " ,time : " + time);
      String testNames = "";
      JSONArray stationsJsonArray;
      try
      {
        stationsJsonArray = new JSONArray(stationsJSONString);
        for (int i = 0; i < stationsJsonArray.length(); i++)
        {
          StationBean bean = new StationBean();
          bean.setJSONObject(stationsJsonArray.getJSONObject(i));
          stations.add(bean);
          testNames += " " + bean.nameKo;
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          stationsInfoLayout.setVisibility(View.VISIBLE);
          StationBean firstStationBean = stations.get(0);
          StationBean lastStationBean = stations.get(stations.size() - 1);
          
          startStationName.setText(firstStationBean.nameKo);
          endStationName.setText(lastStationBean.nameKo);
          
          startStationImage.setImageResource(SubwayManager.getInstance(activity).getSubwayLineResourceId(firstStationBean.line));
          endStationImage.setImageResource(SubwayManager.getInstance(activity).getSubwayLineResourceId(lastStationBean.line));
          
          String stationsInfoString = Integer.toString(stations.size()) + "개 역의 이동시간은 " + Integer.toString(time) + "분입니다";
          stationsInfoText.setText(stationsInfoString);
          final String infoString = stationsInfoString;
          
          stationsInfoLayout.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View arg0)
            {
              Intent i = new Intent(activity, SubwayStationsActivity.class);
              i.putExtra("stations_info", infoString);
              i.putExtra("stations_json", stationsJSONString);
              activity.startActivity(i);
            }
          });
        }
      });
    }
    
    
    @JavascriptInterface
    public void completedFindNearByStation(final String stationId)
    {
      StationBean nearByStation = SubwayManager.getInstance(activity).findStation(stationId);
      Log.w("WARN", "가까운 지하철 역 찾음:" + nearByStation.nameKo);
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          webview.loadUrl("javascript:subway.setCenterWithStationId('" + stationId + "')");
          handler.postDelayed(new Runnable()
          {
            @Override
            public void run()
            {
              showChoiceStationPopUp(stationId);
            }
          }, 500);
        }
      });
    }
    
    
    @JavascriptInterface
    public void completedSetDestStation(final String stationId, final int type)
    {
      StationBean nearByStation = SubwayManager.getInstance(activity).findStation(stationId);
      Log.w("WARN", "출도착 역 설정 완료:" + nearByStation.nameKo);
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          webview.loadUrl("javascript:subway.setCenterWithStationId('" + stationId + "')");
          progressBar.setVisibility(View.GONE);
          if (type == 0)
          {
            if (SubwayManager.getInstance(activity).getLatitudeWithSubwayId(stationId) != -1)
            {
              double lat = SubwayManager.getInstance(activity).getLatitudeWithSubwayId(stationId);
              double lon = SubwayManager.getInstance(activity).getLongitudeWithSubwayId(stationId);
              Log.w("WARN", "출발역 경위도 : " + lat + " / " + lon);
              startStationLat = lat;
              startStationLong = lon;
              
              btnFindFirstStation.setVisibility(View.VISIBLE);
              btnFindFirstStation.bringToFront();
              btnFindFirstStation.setOnClickListener(new OnClickListener()
              {
                @Override
                public void onClick(View arg0)
                {
                  Intent i = new Intent(activity, BlinkingMap.class);
                  i.putExtra("lineId", stationId);
                  i.putExtra("Latitude", startStationLat);
                  i.putExtra("Longitude", startStationLong);
                  startActivity(i);
                }
              });
            }
          }
        }
      });
    }
    
    
    @JavascriptInterface
    public void onTouchStation(final String station)
    {
      Log.w("WARN", "지하철역 터치 됨 :" + station);
      StationBean stationBean = SubwayManager.getInstance(activity).findStation(station);
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          webview.loadUrl("javascript:subway.setCenterWithStationId('" + station + "')");
          handler.postDelayed(new Runnable()
          {
            @Override
            public void run()
            {
              showChoiceStationPopUp(station);
            }
          }, 500);
        }
      });
    }
  }
  
}
