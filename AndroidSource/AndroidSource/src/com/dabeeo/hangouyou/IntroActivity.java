package com.dabeeo.hangouyou;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.activities.sub.GuideActivity;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class IntroActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  private WebView webview;
  private Handler handler = new Handler();
  
  
  @SuppressWarnings("deprecation")
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    alertManager = new AlertDialogManager(this);
    
    webview = (WebView) findViewById(R.id.webview);
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setAllowFileAccessFromFileURLs(true);
    webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
    webview.getSettings().setUseWideViewPort(true);
    webview.getSettings().setBuiltInZoomControls(true);
    webview.getSettings().setDisplayZoomControls(false);
    webview.getSettings().setDomStorageEnabled(true);
    webview.getSettings().setAppCacheEnabled(true);
    webview.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);
    webview.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
    webview.addJavascriptInterface(new JavaScriptInterface(), "myClient");
  }
  
  
  @Override
  protected void onDestroy()
  {
    handler.removeCallbacksAndMessages(null);
    super.onDestroy();
  }
  
  
  @Override
  protected void onResume()
  {
    super.onResume();
    progressBar.bringToFront();
    webview.loadUrl("file:///android_asset/subway.html");
    handler.postDelayed(checkSubwayNativeLoadRunnable, 800);
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
  
  
  private void checkDownloadInfo()
  {
    //지도정보, 상품정보, 지하철정보 다운로드
    Log.w("WARN", "다운로드 정보 체크");
    
    //만약 다운로드 된 정보가 없거나, 혹은 업데이트가 있는 경우 
    alertManager.showProgressDialog(getString(R.string.term_alert), getString(R.string.message_alert_download_seoul_info));
    
    //추후 아랫부분 삭제 후 네트워크 연결
    // memory leak warning 제거 
    Runnable runn = new Runnable()
    {
      @Override
      public void run()
      {
        alertManager.hideProgressDialog();
        checkAllowAlarm();
      }
    };
    Handler handler = new Handler();
    handler.postDelayed(runn, 1000);
  }
  
  
  private void checkAllowAlarm()
  {
    if (PreferenceManager.getInstance(this).getIsFirst())
    {
      alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_allow_notification), getString(R.string.term_ok), getString(R.string.term_cancel),
          new AlertListener()
          {
            @Override
            public void onPositiveButtonClickListener()
            {
              PreferenceManager.getInstance(IntroActivity.this).setAllowPopup(true);
              startGuideActivity();
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
              PreferenceManager.getInstance(IntroActivity.this).setAllowPopup(false);
              startGuideActivity();
            }
          });
    }
    else
      startMainActivity();
  }
  
  
  private void startGuideActivity()
  {
    PreferenceManager.getInstance(this).setIsFirst(false);
    startActivity(new Intent(IntroActivity.this, GuideActivity.class));
  }
  
  
  private void startMainActivity()
  {
    startActivity(new Intent(IntroActivity.this, MainActivity.class));
    finish();
  }
  
  /**
   * Subway pre loading
   */
  private class JavaScriptInterface
  {
    @JavascriptInterface
    public void onSvgLoadEnded()
    {
      Log.w("WARN", "onSVGLoadEnded");
      handler.removeCallbacks(checkSubwayNativeLoadRunnable);
//      checkDownloadInfo();
      if (SystemUtil.isConnectNetwork(IntroActivity.this) && !SystemUtil.isConnectedWiFi(IntroActivity.this))
      {
        //3G or LTE Mode
        alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_lte_mode), getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            checkDownloadInfo();
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            finish();
          }
        });
      }
      else
        checkDownloadInfo();
    }
    
    
    @JavascriptInterface
    public void completeMarkStation(String stationsJSONString)
    {
    }
    
    
    @JavascriptInterface
    public void onTouchStation(final String station)
    {
    }
  }
}
