package com.dabeeo.hangouyou.activities.mainmenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dabeeo.hangouyou.R;

public class SubwayActivity extends ActionBarActivity
{
  private WebView webview;
  private Handler handler = new Handler();
  
  
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway);
    setTitle(getString(R.string.term_subway));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    webview = (WebView) findViewById(R.id.webview);
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setAllowFileAccessFromFileURLs(true);
    webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
    webview.getSettings().setUseWideViewPort(true);
    webview.getSettings().setBuiltInZoomControls(true);
    webview.getSettings().setDisplayZoomControls(false);
    
    webview.addJavascriptInterface(new JavaScriptInterface(), "myClient");
    webview.setWebViewClient(webViewClient);
    webview.setWebChromeClient(webChromeClient);
    webview.loadUrl("file:///android_asset/subway.html");
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
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
    public void onTouchStation(final String station)
    {
      Log.w("MainActivity.java | bbb", "33333333|" + station + "|");
      CharSequence[] menus = new CharSequence[2];
      menus[0] = "출발역 설정";
      menus[1] = "도착역 설정";
      
      final CharSequence[] menuTitles = menus;
      Builder builder = new AlertDialog.Builder(SubwayActivity.this);
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
