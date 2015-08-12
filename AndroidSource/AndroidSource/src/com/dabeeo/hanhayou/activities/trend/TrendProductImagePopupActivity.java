package com.dabeeo.hanhayou.activities.trend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

//@SuppressWarnings("deprecation")
public class TrendProductImagePopupActivity extends ActionBarActivity
{
  private WebView webView;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_product_description));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    setContentView(R.layout.activity_trend_product_image_popup_just_one);
    
    webView = (WebView) findViewById(R.id.webview);
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setDisplayZoomControls(false);
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setLoadWithOverviewMode(true);

    String imageUrl = getIntent().getStringExtra("image_url");
    
//    String data = "<html><head></head><body><center><img width=\"100%\" src=\"" + imageUrl + "\" /></center></body></html>";
//    webView.loadData(data, "text/html", null);
    webView.loadUrl(imageUrl);
    
    webView.setWebViewClient(new WebViewClient(){
      @Override
      public void onPageFinished(WebView view, String url)
      {
        super.onPageFinished(view, url);
        ((LinearLayout) findViewById(R.id.container_help)).setVisibility(View.GONE);
      }
    });
  }
  
  @SuppressLint("UseValueOf")
private int getScale(int PIC_WIDTH){
    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
    int width = display.getWidth(); 
    Double val = new Double(width)/new Double(PIC_WIDTH);
    val = val * 100d;
    return val.intValue();
}
  
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
}
