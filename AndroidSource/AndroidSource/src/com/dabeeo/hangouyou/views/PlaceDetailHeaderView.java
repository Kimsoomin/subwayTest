package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PlaceDetailHeaderView extends RelativeLayout
{
  private Context context;
  
  
  public PlaceDetailHeaderView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailHeaderView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public PlaceDetailHeaderView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_detail_header;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    @SuppressWarnings("unused")
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView imageCount = (TextView) view.findViewById(R.id.image_count);
    
    //가데이터
    imageCount.setText("10");
    addView(view);
  }
}