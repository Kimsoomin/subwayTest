package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class PlaceDetailHeaderView extends RelativeLayout
{
  private Context context;
  private TextView imageCount;
  private ImageView imageView;
  private LinearLayout imageCountContainer;
  
  
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
    if (bean == null)
      return;
    
    if (bean.imageUrls.size() > 1)
    {
      imageCount.setText("+" + Integer.toString(bean.imageUrls.size() - 1));
      imageCount.setVisibility(View.VISIBLE);
      imageCountContainer.setVisibility(View.VISIBLE);
    }
    else
    {
      imageCount.setVisibility(View.GONE);
      imageCountContainer.setVisibility(View.GONE);
    }
    
    String imageUrl = null;
    
    if (!TextUtils.isEmpty(bean.imageUrl))
    {
      if (SystemUtil.isConnectNetwork(context))
      {
        imageUrl = bean.imageUrl;
      }
      else
      {
        String filename = Global.MD5Encoding(bean.imageUrl);
        imageUrl = "file:///" + Global.GetImageFilePath() + filename;
      }
    }
    
    ImageDownloader.displayImage(context, imageUrl, imageView, null);
    
    final String finalImageUrl = imageUrl;
    imageView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, ImagePopUpActivity.class);
        i.putExtra("imageUrls", bean.imageUrls);
        i.putExtra("imageUrl", finalImageUrl);
        context.startActivity(i);
      }
    });
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageCountContainer = (LinearLayout) view.findViewById(R.id.image_count_container);
    imageView = (ImageView) view.findViewById(R.id.imageview);
    imageCount = (TextView) view.findViewById(R.id.image_count);
    imageCount.setVisibility(View.GONE);
    
    addView(view);
  }
}
