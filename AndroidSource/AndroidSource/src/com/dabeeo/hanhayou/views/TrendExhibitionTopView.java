package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.squareup.picasso.Picasso;

public class TrendExhibitionTopView extends RelativeLayout
{
  private Context context;
  
  
  public TrendExhibitionTopView(Context context)
  {
    super(context);
    this.context = context;
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
  
  
  public void setBean(ReviewBean bean)
  {
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_trend_exhibition_top_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    
    Picasso.with(context).load("http://image.gsshop.com/mi09/deal/dealno/166/20150520154548882748.jpg").centerCrop().fit().into(imageview);
    title.setText("[BEAUTY]\n서울 현지인이 추천하는 코스메틱선물 TOP");
    addView(view);
  }
}
