package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PlaceReviewView extends RelativeLayout
{
  private Context context;
  
  
  public PlaceReviewView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public PlaceReviewView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context; 
    init();
  }
  
  
  public PlaceReviewView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_place_review;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView icon = (ImageView) view.findViewById(R.id.icon);
    TextView name = (TextView) view.findViewById(R.id.name);
    TextView time = (TextView) view.findViewById(R.id.time);
    
    name.setText("planb");
    time.setText("2015.01.01 16:53");
    
    addView(view);
  }
}
