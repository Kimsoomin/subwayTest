package com.dabeeo.hangouyou.views;

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
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceDetailActivity;
import com.dabeeo.hangouyou.beans.SpotBean;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.NumberFormatter;

public class ScheduleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView title;
  private TextView position, hours;
  private TextView nextKmAndMin, memo;
  private LinearLayout memoContainer;
  private View line, memoLine;
  
  
  public ScheduleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public void setData(int position, SpotBean bean)
  {
    title.setText(bean.title);
    if (TextUtils.isEmpty(bean.imageUrl))
      imageView.setVisibility(View.GONE);
    else
    {
      imageView.setVisibility(View.VISIBLE);
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    }
    
    this.position.setText(Integer.toString(position));
    hours.setText(NumberFormatter.getTimeString(bean.time));
    
    if (TextUtils.isEmpty(bean.memo))
      memoContainer.setVisibility(View.GONE);
    else
    {
      memoContainer.setVisibility(View.VISIBLE);
      this.memo.setText(bean.memo);
    }
  }
  
  
  public void setFinalView()
  {
    line.setVisibility(View.GONE);
    memoLine.setVisibility(View.GONE);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
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
        Intent i = new Intent(context, MyPlaceDetailActivity.class);
        context.startActivity(i);
      }
    });
    
    addView(view);
  }
}
