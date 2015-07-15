package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class ScheduleDetailHeaderView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  
  
  public ScheduleDetailHeaderView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public ScheduleDetailHeaderView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public ScheduleDetailHeaderView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setData(final String imageUrl, String title, int dayCount, String budget)
  {
    if (SystemUtil.isConnectNetwork(context))
      ImageDownloader.displayImage(context, imageUrl, imageView, null);
    else
      imageView.setVisibility(View.GONE);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageView = (ImageView) view.findViewById(R.id.imageview);
    addView(view);
  }
}
