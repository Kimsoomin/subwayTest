package com.dabeeo.hangouyou.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.beans.SpotBean;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.NumberFormatter;

public class ScheduleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView title;
  private TextView position, hours;
  private RelativeLayout nextKmAndMinConatiner;
  private TextView nextKmAndMin, memo;
  private LinearLayout memoContainer;
  private View line, memoLine;
  
  private RelativeLayout planMemoConatiner;
  private TextView planMemoText;
  private SpotBean bean;
  
  
  public ScheduleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  @SuppressLint("SimpleDateFormat")
  public void setData(int position, SpotBean bean)
  {
    this.bean = bean;
    title.setText(bean.title);
    if (TextUtils.isEmpty(bean.imageUrl))
      imageView.setVisibility(View.GONE);
    else
    {
      imageView.setVisibility(View.VISIBLE);
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    }
    
    String nextKmAndDistance = "";
    nextKmAndDistance += bean.distance + " ";
    try
    {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
      Date startTime = simpleDateFormat.parse(bean.startTime);
      Date endTime = simpleDateFormat.parse(bean.endTime);
      
      long difference = endTime.getTime() - startTime.getTime();
      long x = difference / 1000;
      int seconds = (int) x % 60;
      x /= 60;
      int minutes = (int) x % 60;
      x /= 60;
      int hours = (int) x % 24;
      x /= 24;
      int days = (int) x;
      
      if (hours != 0)
        nextKmAndDistance += Integer.toString(hours) + "hour ";
      if (minutes != 0)
        nextKmAndDistance += Integer.toString(minutes) + "min";
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (!TextUtils.isEmpty(nextKmAndDistance))
    {
      nextKmAndMinConatiner.setVisibility(View.VISIBLE);
      nextKmAndMin.setText(nextKmAndDistance);
    }
    else
    {
      nextKmAndMinConatiner.setVisibility(View.GONE);
    }
    this.position.setText(Integer.toString(position));
    hours.setText(NumberFormatter.getTimeString(bean.time));
    
    if (TextUtils.isEmpty(bean.memo))
    {
      memoContainer.setVisibility(View.GONE);
      memoLine.setVisibility(View.GONE);
    }
    else
    {
      memoContainer.setVisibility(View.VISIBLE);
      this.memo.setText(bean.memo);
      memoLine.setVisibility(View.VISIBLE);
    }
    
  }
  
  
  public void setFinalView(String planMemo)
  {
    line.setVisibility(View.GONE);
    memoLine.setVisibility(View.GONE);
    nextKmAndMinConatiner.setVisibility(View.GONE);
    
    if (!TextUtils.isEmpty(planMemo))
    {
      planMemoConatiner.setVisibility(View.VISIBLE);
      planMemoText.setText(planMemo);
    }
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    planMemoConatiner = (RelativeLayout) view.findViewById(R.id.plan_memo_conatiner);
    planMemoText = (TextView) view.findViewById(R.id.plan_memo);
    
    nextKmAndMinConatiner = (RelativeLayout) view.findViewById(R.id.container_next_km_and_min);
    line = (View) view.findViewById(R.id.line);
    memoLine = (View) view.findViewById(R.id.memo_line);
    imageView = (ImageView) view.findViewById(R.id.imageview);
    title = (TextView) view.findViewById(R.id.text_title);
    position = (TextView) view.findViewById(R.id.position);
    hours = (TextView) view.findViewById(R.id.text_hours);
    nextKmAndMin = (TextView) view.findViewById(R.id.text_next_km_and_min);
    memo = (TextView) view.findViewById(R.id.memo);
    memoContainer = (LinearLayout) view.findViewById(R.id.memo_container);
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, PlaceDetailActivity.class);
        i.putExtra("place_idx", bean.contentsIdx);
        context.startActivity(i);
      }
    });
    
    addView(view);
  }
}
