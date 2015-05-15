package com.dabeeo.hangouyou.views;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.utils.ImageDownloader;

public class ScheduleDetailHeaderView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView textTitle;
  private ImageView budgetIcon;
  private TextView textDayCount;
  private TextView textBudget;
  
  
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
  
  
  public void setData(String imageUrl, String title, int dayCount, String budget)
  {
    ImageDownloader.displayImage(context, imageUrl, imageView, null);
    textTitle.setText(title);
    textDayCount.setText(Integer.toString(dayCount));
    textBudget.setText(budget);
    
    if (Locale.getDefault().getLanguage().contains("ko"))
      budgetIcon.setImageResource(R.drawable.icon_budget_kr);
    else
      budgetIcon.setImageResource(R.drawable.icon_budget_cn);
    
    ImageDownloader.displayImage(context, imageUrl, imageView, null);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageView = (ImageView) view.findViewById(R.id.imageview);
    textTitle = (TextView) view.findViewById(R.id.title);
    budgetIcon = (ImageView) view.findViewById(R.id.icon_budget);
    textDayCount = (TextView) view.findViewById(R.id.text_day_count);
    textBudget = (TextView) view.findViewById(R.id.text_budget);
    
    addView(view);
  }
}
