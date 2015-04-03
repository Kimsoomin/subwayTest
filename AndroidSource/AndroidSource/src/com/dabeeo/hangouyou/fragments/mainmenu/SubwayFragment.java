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
import android.os.Build;
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
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayStationsActivity;
import com.dabeeo.hangouyou.beans.StationBean;

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
  
  
  @Override
  public void onAttach(Activity activity)
  {
//    if (view == null)
//    {
    int resId = R.layout.fragment_subway;
    view = LayoutInflater.from(activity).inflate(resId, null);
    stationsInfoLayout = (LinearLayout) view.findViewById(R.id.stations_info);
    stationsInfoText = (TextView) view.findViewById(R.id.text_stations_info);
    detailStationInfo = (ImageView) view.findViewById(R.id.image_stations_info_detail);
    
    webview = (WebView) view.findViewById(R.id.webview);
//    if (Build.VERSION.SDK_INT >= 19)
//      webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    
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
    
    webview.addJavascriptInterface(new JavaScriptInterface(), "myClient");
    webview.setWebViewClient(webViewClient);
    webview.setWebChromeClient(webChromeClient);
    webview.loadUrl("file:///android_asset/subway.html");
    
    handler.postDelayed(checkSubwayNativeLoadRunnable, 200);
//    }
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
  
  
  public boolean isLoadEnded()
  {
    return isLoadEnded;
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
      Log.w("MainActivity.java | onConsoleMessage", "111111111|" + consoleMessage + "|" + consoleMessage.message() + "|" + consoleMessage.sourceId() + "|");
      return super.onConsoleMessage(consoleMessage);
    }
    
    
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
      Log.w("MainActivity.java | onJsAlert", "222222222|" + url + "|" + message + "|" + result + "|");
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
  
  private class JavaScriptInterface
  {
    @JavascriptInterface
    public void onSvgLoadEnded()
    {
      handler.removeCallbacks(checkSubwayNativeLoadRunnable);
      isLoadEnded = true;
    }
    
    
    @JavascriptInterface
    public void completeMarkStation(final String stationsJSONString, final int time)
    {
      Log.w("WARN", "stations Json String : " + stationsJSONString);
      Log.w("WARN", "Time is : " + time);
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
    public void onTouchStation(final String station)
    {
      Log.w("MainActivity.java | bbb", "33333333|" + station + "|");
      CharSequence[] menus = new CharSequence[2];
      menus[0] = "출발역 설정";
      menus[1] = "도착역 설정";
      
      final CharSequence[] menuTitles = menus;
      Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("출도착역 설정");
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
                webview.loadUrl("javascript:subway.set_start_station('" + station + "')");
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
                webview.loadUrl("javascript:subway.set_end_station('" + station + "')");
              }
            });
            
          }
        }
      });
      builder.create().show();
    }
  }
  
}
