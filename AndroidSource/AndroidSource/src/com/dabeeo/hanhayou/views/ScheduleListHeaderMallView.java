package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.squareup.picasso.Picasso;

public class ScheduleListHeaderMallView extends RelativeLayout
{
  private Context context;
  private int imageWidth = 0;
  private int imageHeight = 0;
  private TrendKoreaBean bean;
  
  public ScheduleListHeaderMallView(Context context, int imageWidth, int imageHeight)
  {
    super(context);
    this.context = context;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
  }
  
  
  public ScheduleListHeaderMallView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public ScheduleListHeaderMallView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setBean(TrendKoreaBean bean)
  {
    this.bean = bean;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_header_mall_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
    
    imageview.getLayoutParams().width = imageWidth;
    imageview.getLayoutParams().height = imageHeight;
    
    Picasso.with(context).load(bean.imageUrl).resize(imageWidth, imageHeight).centerCrop().into(imageview);
    addView(view);
  }
}
