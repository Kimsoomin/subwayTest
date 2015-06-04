package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PlaceDetailHeaderView extends RelativeLayout
{
  private Context context;
  private TextView imageCount;
  private ImageView imageView;
  
  
  public PlaceDetailHeaderView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailHeaderView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailHeaderView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setBean(final PlaceDetailBean bean)
  {
    if (bean != null)
    {
      if (bean.imageUrls.size() > 1)
        imageCount.setText("+" + Integer.toString(bean.imageUrls.size()));
      
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, new ImageLoadingListener()
      {
        @Override
        public void onLoadingStarted(String arg0, View arg1)
        {
          imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        
        
        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
        {
          imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        
        
        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
        {
          if (bitmap == null)
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          else
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        
        
        @Override
        public void onLoadingCancelled(String arg0, View arg1)
        {
          imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
      });
      
      imageView.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent i = new Intent(context, ImagePopUpActivity.class);
          i.putExtra("ImageUrls", bean.imageUrls);
          i.putExtra("ImageUrl", bean.imageUrl);
          context.startActivity(i);
        }
      });
    }
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageView = (ImageView) view.findViewById(R.id.imageview);
    imageCount = (TextView) view.findViewById(R.id.image_count);
    imageCount.setVisibility(View.GONE);
    
    addView(view);
  }
}
