package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class ScheduleTitleView extends RelativeLayout
{
  private Context context;
  
  
  public ScheduleTitleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    
    //가데이터
    title.setText("Day 1 (2015. 1. 1)");
    addView(view);
  }
}
