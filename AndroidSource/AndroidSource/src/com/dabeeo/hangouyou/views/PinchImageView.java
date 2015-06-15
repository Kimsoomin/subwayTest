package com.dabeeo.hangouyou.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PinchImageView extends LinearLayout
{
  private Activity context;
  
  
  public PinchImageView(Activity context, String url)
  {
    super(context);
    this.context = context;
    init(url);
  }
  
  
  public PinchImageView(Activity context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  @SuppressWarnings("deprecation")
  private void init(final String url)
  {
    LayoutInflater inflater = LayoutInflater.from(context);
    int resId = R.layout.view_pinch_image;
    View view = inflater.inflate(resId, null);
    
    final ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
    
//    Display display = context.getWindowManager().getDefaultDisplay();
//    int width = display.getWidth();
//    int height = display.getHeight();
//    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
//    imageView.setLayoutParams(layoutParams);
    
//    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//    Display display = wm.getDefaultDisplay();
//    @SuppressWarnings("deprecation")
//    int width = display.getWidth();
//    
//    int imageHeight = (int) (width * 0.75);
//    ((RelativeLayout) view.findViewById(R.id.container)).getLayoutParams().height = imageHeight;
    
    ImageDownloader.displayImage(context, url, imageView, new ImageLoadingListener()
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
    addView(view);
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
  
}
