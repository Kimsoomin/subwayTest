package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ReviewBean;

public class ReviewView extends RelativeLayout
{
  private Context context;
  private ReviewBean bean;
  
  private ImageView icon;
  private TextView name;
  private TextView time;
  private TextView content;
  private TextView reviewScore;
  private ImageView btnMore;
  
  
  public ReviewView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public ReviewView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public ReviewView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setBean(ReviewBean bean)
  {
    this.bean = bean;
    init();
    
    name.setText(bean.userName);
    time.setText(bean.insertDateString);
    reviewScore.setText(Integer.toString(bean.rate));
    content.setText(bean.content);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    icon = (ImageView) view.findViewById(R.id.icon);
    name = (TextView) view.findViewById(R.id.name);
    time = (TextView) view.findViewById(R.id.time);
    content = (TextView) view.findViewById(R.id.content);
    reviewScore = (TextView) view.findViewById(R.id.text_review_score);
    btnMore = (ImageView) view.findViewById(R.id.btn_review_list_more);
    
    btnMore.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        
        
      }
    });
    addView(view);
  }
}
