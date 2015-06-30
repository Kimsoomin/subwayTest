package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.external.libraries.RoundedImageView;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class ScheduleDetailTitleView extends RelativeLayout
{
  private Context context;
  private RoundedImageView imageView;
  private TextView name;
  private TextView time;
  private TextView likeCount;
  private TextView reviewCount;
  public RelativeLayout container;
  public TextView title, textMoney, textDays;
  public LinearLayout infoContainer;
  public LinearLayout likeAndBookmarkContainer, userContainer;
  public View titleDivider;
  
  
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
  
  
  @SuppressLint("SimpleDateFormat")
  public void setBean(ScheduleDetailBean bean)
  {
    name.setText(bean.userName);
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    try
    {
      time.setText(format.format(bean.updateDate));
    }
    catch (Exception e)
    {
    }
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    title.setText(bean.title);
    try
    {
      textMoney.setText(NumberFormatter.addComma(bean.budgetTotal));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    textDays.setText(Integer.toString(bean.dayCount));
    ImageDownloader.displayProfileImage(context, bean.mfidx, imageView);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_schedule_detail_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    userContainer = (LinearLayout) view.findViewById(R.id.user_container);
    likeAndBookmarkContainer = (LinearLayout) view.findViewById(R.id.like_and_bookmark_count_container);
    container = (RelativeLayout) view.findViewById(R.id.container);
    infoContainer = (LinearLayout) view.findViewById(R.id.total_container);
    imageView = (RoundedImageView) view.findViewById(R.id.imageview);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    likeCount = (TextView) view.findViewById(R.id.like_count);
    reviewCount = (TextView) view.findViewById(R.id.review_count);
    title = (TextView) view.findViewById(R.id.text_title);
    textMoney = (TextView) view.findViewById(R.id.text_money);
    textDays = (TextView) view.findViewById(R.id.text_days);
    titleDivider = (View) view.findViewById(R.id.schedule_title_divider);
    addView(view);
  }
  
  public void initDayTitle()
  {
    title.setVisibility(View.GONE);
    infoContainer.setVisibility(View.GONE);
  }
  
  
}
