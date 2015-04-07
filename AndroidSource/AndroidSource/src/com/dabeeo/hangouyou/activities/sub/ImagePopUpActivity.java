package com.dabeeo.hangouyou.activities.sub;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.ImageView;

import com.dabeeo.hangouyou.R;
import com.squareup.picasso.Picasso;

public class ImagePopUpActivity extends ActionBarActivity
{
  private ImageView imageView;
  private ImageView btnTurnOff;
  private String couponId;
  private String imageUrl;
  private DownloadManager downloadManager;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_image_popup);
    imageUrl = getIntent().getStringExtra("imageUrl");
    imageView = (ImageView) findViewById(R.id.image_view);
    
    Picasso.with(this).load(imageUrl).into(imageView);
  }
  
}
