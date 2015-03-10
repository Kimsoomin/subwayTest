package com.dabeeo.hangouyou.views;

import android.annotation.SuppressLint;
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
  
  
  @SuppressLint("InflateParams")
  public void init()
  {
    View view = LayoutInflater.from(context).inflate(R.layout.view_schedule_title, null);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    
    //가데이터
    title.setText("Day 1 (2015. 1. 1)");
    addView(view);
  }
}
