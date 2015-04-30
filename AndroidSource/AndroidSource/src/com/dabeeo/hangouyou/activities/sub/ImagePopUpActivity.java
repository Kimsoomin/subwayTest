package com.dabeeo.hangouyou.activities.sub;

import android.app.DownloadManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    
    Display display = getWindowManager().getDefaultDisplay();
    int width = display.getWidth();
    int height = display.getHeight();
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
    imageView.setLayoutParams(layoutParams);
    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    
    ImageDownloader.displayImage(this, imageUrl, imageView, new ImageLoadingListener()
    {
      @Override
      public void onLoadingStarted(String arg0, View arg1)
      {
      }
      
      
      @Override
      public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
      {
      }
      
      
      @Override
      public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
      {
        usingSimpleImage(imageView);
      }
      
      
      @Override
      public void onLoadingCancelled(String arg0, View arg1)
      {
      }
    });
  }
  
  
  public void usingSimpleImage(ImageView imageView)
  {
    ImageAttacher attacher = new ImageAttacher(imageView);
    ImageAttacher.MAX_ZOOM = 4.0f;
    ImageAttacher.MIN_ZOOM = 1.0f;
    MatrixChangeListener matrixListener = new MatrixChangeListener();
    attacher.setOnMatrixChangeListener(matrixListener);
  }
  
  private class MatrixChangeListener implements OnMatrixChangedListener
  {
    @Override
    public void onMatrixChanged(RectF rect)
    {
      
    }
  }
  
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
  }
}
