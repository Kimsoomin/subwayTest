package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.SystemUtil;

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
    if (bean == null)
      return;
    
    if (bean.imageUrls.size() > 1)
      imageCount.setText("+" + Integer.toString(bean.imageUrls.size()));
    else
      imageCount.setVisibility(View.GONE);
    
    if (SystemUtil.isConnectNetwork(context))
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    else
    {
      if (!TextUtils.isEmpty(bean.imageUrl))
      {
        String filename = Global.MD5Encoding(bean.imageUrl);
        ImageDownloader.displayImage(context, "file:///" + Global.GetImageFilePath() + filename, imageView, null);
      }
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
