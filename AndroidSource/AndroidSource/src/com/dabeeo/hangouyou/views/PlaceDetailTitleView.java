package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;

public class PlaceDetailTitleView extends RelativeLayout
{
  private Context context;
  public LinearLayout container;
  private ImageView imageView;
  private TextView title, name, time, likeCount, reviewCount;
  
  
  public PlaceDetailTitleView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailTitleView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailTitleView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setBean(PlaceDetailBean bean)
  {
    title.setText(bean.title);
    name.setText(bean.userName);
    time.setText(bean.updateDateString);
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    container = (LinearLayout) view.findViewById(R.id.container);
    title = (TextView) view.findViewById(R.id.text_title);
    imageView = (ImageView) view.findViewById(R.id.imageview);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    likeCount = (TextView) view.findViewById(R.id.like_count);
    reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    addView(view);
  }
}
