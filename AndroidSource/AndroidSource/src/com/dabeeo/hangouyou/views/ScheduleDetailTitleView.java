package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class ScheduleDetailTitleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView name;
  private TextView time;
  private TextView likeCount;
  private TextView reviewCount;
  public RelativeLayout container;
  
  
  public ScheduleDetailTitleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public ScheduleDetailTitleView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public ScheduleDetailTitleView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setData(int likeCount, int reviewCount)
  {
    this.name.setText("planb");
    this.time.setText("2015.01.01 16:53");
    try
    {
      this.likeCount.setText(Integer.toString(likeCount));
      this.reviewCount.setText(Integer.toString(reviewCount));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_detail_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    container = (RelativeLayout) view.findViewById(R.id.container);
    imageView = (ImageView) view.findViewById(R.id.imageview);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    likeCount = (TextView) view.findViewById(R.id.like_count);
    reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    addView(view);
  }
}
