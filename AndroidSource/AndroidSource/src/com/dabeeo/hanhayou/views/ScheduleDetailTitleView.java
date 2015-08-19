package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class ScheduleDetailTitleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView name;
  private TextView time;
  private TextView likeCount;
  private TextView bookmarkCount;
  public RelativeLayout container;
  public TextView title, textMoney, textDays;
  public LinearLayout titleLayout;
  public LinearLayout infoContainer;
  public LinearLayout likeAndBookmarkContainer, userContainer;
  public View titleDivider;
  public ImageView isPublic;
  
  
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
  
  
  public void reloadLikeCount(int count)
  {
    if (count != -1)
      likeCount.setText(Integer.toString(count));
  }
  
  public void reloadBookmarkCount(int count)
  {
    if (count != -1)
      bookmarkCount.setText(Integer.toString(count));
  }
  
  
  public void setTitle(String titleStr)
  {
    title.setText(titleStr);
  }
  
  
  @SuppressLint("SimpleDateFormat")
  public void setBean(ScheduleDetailBean bean)
  {
    name.setText(bean.userName);
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    try
    {
      time.setText(format.format(bean.insertDate));
    }
    catch (Exception e)
    {
    }
    likeCount.setText(Integer.toString(bean.likeCount));
    bookmarkCount.setText(Integer.toString(bean.bookmarkCount));
    title.setText(bean.title);
    try
    {
      textMoney.setText(NumberFormatter.addComma(bean.budgetTotal));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (Locale.getDefault().getLanguage().contains("ko"))
      textDays.setText(Integer.toString(bean.dayCount) + "일");
    else
    {
      textDays.setText(Integer.toString(bean.dayCount) + "日游");
    }
    
    if (!bean.isOpen)
      isPublic.setVisibility(View.VISIBLE);
    
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
    imageView = (ImageView) view.findViewById(R.id.imageview);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    likeCount = (TextView) view.findViewById(R.id.like_count);
    bookmarkCount = (TextView) view.findViewById(R.id.bookmark_count);
    titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
    title = (TextView) view.findViewById(R.id.text_title);
    textMoney = (TextView) view.findViewById(R.id.text_money);
    textDays = (TextView) view.findViewById(R.id.text_days);
    titleDivider = (View) view.findViewById(R.id.schedule_title_divider);
    isPublic = (ImageView) view.findViewById(R.id.is_public);
    addView(view);
  }
  
  
  public void initDayTitle()
  {
    titleLayout.setVisibility(View.GONE);
    infoContainer.setVisibility(View.GONE);
  }
  
}
