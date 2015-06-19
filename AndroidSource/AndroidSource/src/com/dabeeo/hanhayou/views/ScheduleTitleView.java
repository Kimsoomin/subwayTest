package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

public class ScheduleTitleView extends RelativeLayout
{
  private Context context;
  private TextView title, date;
  
  
  public ScheduleTitleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  @SuppressLint("SimpleDateFormat")
  public void setData(String title, Date date)
  {
    this.title.setText(title);
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    this.date.setText(format.format(date));
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    title = (TextView) view.findViewById(R.id.title);
    date = (TextView) view.findViewById(R.id.date);
    
    addView(view);
  }
}
