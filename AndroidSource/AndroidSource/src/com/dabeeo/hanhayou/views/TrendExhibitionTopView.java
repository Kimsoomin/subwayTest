package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
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
  
  
  public void setBean(String themeTitle, String imageUrl)
  {
    init(themeTitle, imageUrl);
  }
  
  
  public void init(String themetitle, String imageUrl)
  {
    int resId = R.layout.view_trend_exhibition_top_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    LinearLayout textLayout = (LinearLayout) view.findViewById(R.id.trendy_text_layout);
    RelativeLayout.LayoutParams layparam = (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
    layparam.height = (int)(imageheight*0.8);
    textLayout.setLayoutParams(layparam);
    TextView title = (TextView) view.findViewById(R.id.title);
    
    Picasso.with(context).load(imageUrl).resize(imagewidth, imageheight).centerCrop().into(imageView);
    title.setText(themetitle);
    addView(view);
  }
}
