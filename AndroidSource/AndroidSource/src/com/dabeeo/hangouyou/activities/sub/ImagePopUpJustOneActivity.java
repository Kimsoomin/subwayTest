package com.dabeeo.hangouyou.activities.sub;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.views.PinchImageView;

@SuppressWarnings("deprecation")
public class ImagePopUpJustOneActivity extends ActionBarActivity
{
  private RelativeLayout container;
  private ImageView imgX;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_image_popup_just_one);
    
    container = (RelativeLayout) findViewById(R.id.content);
    String imageUrl = getIntent().getStringExtra("image_url");
    
    imgX = (ImageView) findViewById(R.id.img_x);
    imgX.bringToFront();
    imgX.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    
    PinchImageView view = new PinchImageView(this, imageUrl);
    container.addView(view);
  }
  
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
  }
}
