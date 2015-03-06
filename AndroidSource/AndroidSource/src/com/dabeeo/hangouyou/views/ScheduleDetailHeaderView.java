package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class ScheduleDetailHeaderView extends RelativeLayout
{
  private Context context;
  
  
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
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    
    //가데이터
    title.setText("서울 투어");
    month.setText("3월");
    addView(view);
  }
}
