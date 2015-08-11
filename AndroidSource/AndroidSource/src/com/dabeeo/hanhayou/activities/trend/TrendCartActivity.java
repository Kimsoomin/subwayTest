package com.dabeeo.hanhayou.activities.trend;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.map.BlinkingCommon;

public class TrendCartActivity extends ActionBarActivity
{
  private WebView trendCart;
  private String url = "https://devm.hanhayou.com/ecom/cart/";
  
//  private String productId = "";
//  private String itemAttributesList = "";
  
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_cart);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_my_cart));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    trendCart = (WebView) findViewById(R.id.trend_cart_webview);    
    trendCart.getSettings().setJavaScriptEnabled(true);
    
    if(getIntent().hasExtra("cart_parameter"))
    {
      url = url + getIntent().getStringExtra("cart_parameter");
    }else
    {
      url = url + "list?_hgy_token=" + PreferenceManager.getInstance(TrendCartActivity.this).getUserSeq();
    }
    
    BlinkingCommon.smlLibDebug("TrendCartActivity", "URL : " + url);
    
//    Map<String, String> headers = new HashMap<String, String>();
//    headers.put("_hgy_token", hgyToken);
//    headers.put("product_id", productId);
//    headers.put("itemAttributesList", itemAttributesList);
    
//    trendCart.postUrl(url, EncodingUtils.getBytes(hgyToken, "base64"));
//    trendCart.loadUrl("https://devshop.hanhayou.com/ecom/cart/newOrderNow", headers);
    trendCart.loadUrl(url);
    
    
    
    trendCart.setWebViewClient(new WebViewClientClass());  
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
    if ((keyCode == KeyEvent.KEYCODE_BACK) && trendCart.canGoBack()) 
    { 
      trendCart.goBack(); 
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
    
    @Override
    public void onPageFinished(WebView view, String url)
    {
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      int deviceWidth = metrics.widthPixels;
      BlinkingCommon.smlLibDebug("TrendCartActivity", "deviceWidth : " + deviceWidth);
      BlinkingCommon.smlLibDebug("TrendCartActivity", "getWidth : " + trendCart.getWidth());
      super.onPageFinished(view, url);
    }
  }
}
