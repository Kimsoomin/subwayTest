package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PlaceDetailTitleView extends RelativeLayout
{
  private Context context;
  
  
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
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_title;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView name = (TextView) view.findViewById(R.id.name);
    TextView time = (TextView) view.findViewById(R.id.time);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    //가데이터
    name.setText("planb");
    time.setText("2015.01.01 16:53");
    likeCount.setText("970");
    reviewCount.setText("321");
    
    addView(view);
  }
}
