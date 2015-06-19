package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

public class RecommendScheduleBottomView extends RelativeLayout
{
  private Context context;
  private TextView title;
  
  
  public RecommendScheduleBottomView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public RecommendScheduleBottomView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public RecommendScheduleBottomView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setBean(String titleString)
  {
    title.setText(titleString);
  }
  
  
  public String getTitle()
  {
    return title.getText().toString();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_recommend_schedule_bottom;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    title = (TextView) view.findViewById(R.id.title);
    
    addView(view);
  }
}
