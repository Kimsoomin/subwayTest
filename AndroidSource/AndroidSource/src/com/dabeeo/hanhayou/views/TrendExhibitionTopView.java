package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.squareup.picasso.Picasso;

public class TrendExhibitionTopView extends RelativeLayout
{
  private Context context;
  
  public int imagewidth = 0;
  public int imageheight = 0;  
  
  public TrendExhibitionTopView(Context context, int imageWidth, int imageHeight)
  {
    super(context);
    this.context = context;
    this.imagewidth = imageWidth;
    this.imageheight = imageHeight;
  }
  
  
  public TrendExhibitionTopView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public TrendExhibitionTopView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setBean(String imageUrl)
  {
    init(imageUrl);
  }
  
  
  public void init(String imageUrl)
  {
    int resId = R.layout.view_trend_exhibition_top_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    
    Picasso.with(context).load(imageUrl).resize(imagewidth, imageheight).centerCrop().into(imageView);
    addView(view);
  }
}
