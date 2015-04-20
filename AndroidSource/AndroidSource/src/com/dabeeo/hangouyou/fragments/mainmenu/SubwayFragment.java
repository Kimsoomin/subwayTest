package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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
  private ArrayList<StationBean> stations = new ArrayList<>();
  private ImageView detailStationInfo;
  private boolean isLoadEnded;
  private Runnable afterLoadSubwaysRunnable;
  private double startStationLat = -1, startStationLong = -1;
  private Button btnFindFirstStation;
  
  private double findNearByStationLat = -1, findNearByStationLon = -1;
  private double setDestFindNearStation = -1;
  
  private AlertDialog choiceDialog;
  
  
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
  public void onAttach(Activity activity)
  {
    int resId = R.layout.fragment_subway;
    view = LayoutInflater.from(activity).inflate(resId, null);
    stationsInfoLayout = (LinearLayout) view.findViewById(R.id.stations_info);
    stationsInfoText = (TextView) view.findViewById(R.id.text_stations_info);
    detailStationInfo = (ImageView) view.findViewById(R.id.image_stations_info_detail);
    btnFindFirstStation = (Button) view.findViewById(R.id.btn_find_start_station);
    
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
    webview.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
    webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
    
    webview.addJavascriptInterface(new JavaScriptInterface(), "myClient");
    webview.setWebViewClient(webViewClient);
    webview.setWebChromeClient(webChromeClient);
    webview.loadUrl("file:///android_asset/subway.html");
    
    handler.postDelayed(checkSubwayNativeLoadRunnable, 1000);
    super.onAttach(activity);
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
  
  @SuppressWarnings("unused")
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      Log.w("MainActivity.java | onClick", "|" + "call javascript function" + "|");
      webview.loadUrl("javascript:ccc(" + "'aaa'" + ")");
    }
  };
  
  
  private void showChoiceStationPopUp(final String stationId)
  {
    CharSequence[] menus = new CharSequence[2];
    menus[0] = "출발역 설정";
    menus[1] = "도착역 설정";
    
    StationBean bean = SubwayManager.getInstance(getActivity()).findStation(stationId);
    final CharSequence[] menuTitles = menus;
    Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(bean.nameKo);
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
              if (SubwayManager.getInstance(getActivity()).getLatitudeWithSubwayId(stationId) != -1)
              {
                double lat = SubwayManager.getInstance(getActivity()).getLatitudeWithSubwayId(stationId);
                double lon = SubwayManager.getInstance(getActivity()).getLongitudeWithSubwayId(stationId);
                Log.w("WARN", "출발역 경위도 : " + lat + " / " + lon);
                startStationLat = lat;
                startStationLong = lon;
                
                btnFindFirstStation.setVisibility(View.VISIBLE);
                btnFindFirstStation.setOnClickListener(new OnClickListener()
                {
                  @Override
                  public void onClick(View arg0)
                  {
                    Intent i = new Intent(getActivity(), BlinkingMap.class);
                    i.putExtra("lineId", stationId);
                    i.putExtra("Latitude", startStationLat);
                    i.putExtra("Longitude", startStationLong);
                    startActivity(i);
                  }
                });
              }
              
              webview.loadUrl("javascript:subway.set_start_station('" + stationId + "')");
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
              webview.loadUrl("javascript:subway.set_end_station('" + stationId + "')");
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
      SubwayManager.getInstance(getActivity()).stations.clear();
      SubwayManager.getInstance(getActivity()).stations.addAll(stations);
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
      Toast.makeText(getActivity(), testNames, Toast.LENGTH_LONG).show();
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          stationsInfoLayout.setVisibility(View.VISIBLE);
          StationBean firstStationBean = stations.get(0);
          StationBean lastStationBean = stations.get(stations.size() - 1);
          String stationsInfoString = firstStationBean.nameKo + " (" + firstStationBean.line + ") > ";
          stationsInfoString += lastStationBean.nameKo + " (" + lastStationBean.line + ")\n";
          stationsInfoString += Integer.toString(stations.size()) + "개 역의 이동시간은 " + Integer.toString(time) + "분입니다";
          stationsInfoText.setText(stationsInfoString);
          final String infoString = stationsInfoString;
          stationsInfoLayout.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View arg0)
            {
              Intent i = new Intent(getActivity(), SubwayStationsActivity.class);
              i.putExtra("stations_info", infoString);
              i.putExtra("stations_json", stationsJSONString);
              getActivity().startActivity(i);
            }
          });
        }
      });
    }
    
    
    @JavascriptInterface
    public void completedFindNearByStation(final String stationId)
    {
      StationBean nearByStation = SubwayManager.getInstance(getActivity()).findStation(stationId);
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
    public void completedSetDestStation(final String stationId)
    {
      StationBean nearByStation = SubwayManager.getInstance(getActivity()).findStation(stationId);
      Log.w("WARN", "출도착 역 설정 완료:" + nearByStation.nameKo);
      handler.post(new Runnable()
      {
        @Override
        public void run()
        {
          webview.loadUrl("javascript:subway.setCenterWithStationId('" + stationId + "')");
        }
      });
    }
    
    
    @JavascriptInterface
    public void onTouchStation(final String station)
    {
      Log.w("WARN", "지하철역 터치 됨 :" + station);
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
