package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.beans.SpotBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class ScheduleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView title;
  private TextView position, hours;
  private RelativeLayout nextKmAndMinConatiner;
  private TextView nextKmAndMin, memo;
  private LinearLayout memoContainer, planTitleLayout;
  private View line, memoLine;
  
  private RelativeLayout planMemoConatiner;
  private TextView planMemoText, planMemoTextMoneyAndTime;
  private SpotBean bean;
  private boolean isThumbnailInvisible = false;
  
  
  public ScheduleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public void setThumbnailInvisible()
  {
    isThumbnailInvisible = true;
  }
  
  public void setData(int position, SpotBean bean)
  {
    this.bean = bean;
    if(bean.type == 3)
    {
      nextKmAndMinConatiner.setVisibility(View.GONE);
      planTitleLayout.setVisibility(View.GONE);
      line.setVisibility(View.GONE);
      memoContainer.setVisibility(View.VISIBLE);
      this.memo.setText(bean.memo);
      memoLine.setVisibility(View.VISIBLE);
    }else
    {
      planTitleLayout.setVisibility(View.VISIBLE);
      line.setVisibility(View.VISIBLE);
      title.setText(bean.title);
      if (TextUtils.isEmpty(bean.imageUrl))
        imageView.setVisibility(View.GONE);
      else
      {
        if (!isThumbnailInvisible)
        {
          imageView.setVisibility(View.VISIBLE);
          ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
        }
        else
          imageView.setVisibility(View.GONE);
      }
      
      
      if (!TextUtils.isEmpty(bean.distance))
      {
        nextKmAndMinConatiner.setVisibility(View.VISIBLE);
        nextKmAndMin.setText(bean.distance);
      }
      else
      {
        nextKmAndMinConatiner.setVisibility(View.GONE);
      }
      this.position.setText(Integer.toString(position));
      hours.setText(NumberFormatter.getTimeString(bean.time));
      
      if (TextUtils.isEmpty(bean.memo))
      {
        planMemoConatiner.setVisibility(View.GONE);
        memoLine.setVisibility(View.GONE);
      }
      else
      {
        planMemoConatiner.setVisibility(View.VISIBLE);
        planMemoText.setText(bean.memo);
        memoLine.setVisibility(View.VISIBLE);
      }
    }
  }
  
  
  public void setFinalView()
  {
    line.setVisibility(View.GONE);
    memoLine.setVisibility(View.GONE);
    nextKmAndMinConatiner.setVisibility(View.GONE);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    planMemoConatiner = (RelativeLayout) view.findViewById(R.id.plan_memo_conatiner);
    planMemoText = (TextView) view.findViewById(R.id.plan_memo);
    planMemoTextMoneyAndTime = (TextView) view.findViewById(R.id.plan_memo_time_and_money);
    
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
    
    planTitleLayout = (LinearLayout) view.findViewById(R.id.plan_title_layout);
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(context, PlaceDetailActivity.class);
        i.putExtra("place_idx", bean.idx);
        context.startActivity(i);
      }
    });
    
    addView(view);
  }
}
