package com.dabeeo.hanhayou.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.utils.ImageDownloader;
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
    
    Display display = context.getWindowManager().getDefaultDisplay();
    int width = display.getWidth();
    int height = display.getHeight();
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
    imageView.setLayoutParams(layoutParams);
    
    ImageDownloader.displayImagePinchZoom(context, url, imageView, null);
    addView(view);
  }
  
}
