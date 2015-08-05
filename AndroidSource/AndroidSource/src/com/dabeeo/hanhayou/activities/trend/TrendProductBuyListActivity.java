package com.dabeeo.hanhayou.activities.trend;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dabeeo.hanhayou.R;

public class TrendProductBuyListActivity extends ActionBarActivity
{
  
  private WebView trendOrder;
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_buy_list);
    
    getSupportActionBar().setTitle(getString(R.string.term_my_order));
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    trendOrder = (WebView) findViewById(R.id.trend_buy_list_webview);    
    trendOrder.getSettings().setJavaScriptEnabled(true);
    trendOrder.loadUrl("https://devm.hanhayou.com/ecom/account/orders/1?_hgy_token=9723");
//    trendCart.postUrl("https://", EncodingUtils.getBytes("", "utf-8"));
    trendOrder.setWebViewClient(new WebViewClientClass());  
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) 
  { 
    if ((keyCode == KeyEvent.KEYCODE_BACK) && trendOrder.canGoBack()) 
    { 
      trendOrder.goBack(); 
      return true; 
    } 
    return super.onKeyDown(keyCode, event);
  }
  
  private class WebViewClientClass extends WebViewClient { 
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
      view.loadUrl(url); 
      return true; 
    } 
    
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
      super.onReceivedSslError(view, handler, error);
      handler.proceed();
    }
  }
}
