package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class RecommendProductView extends RelativeLayout
{
  private Context context;
  
  
  public RecommendProductView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public RecommendProductView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public RecommendProductView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_recommend_product;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    ImageView image1 = (ImageView) view.findViewById(R.id.product1_image);
    TextView title1 = (TextView) view.findViewById(R.id.product1_title);
    TextView originalPrice1 = (TextView) view.findViewById(R.id.original1_price);
    TextView discountPrice1 = (TextView) view.findViewById(R.id.discount1_price);
    
    ImageView image2 = (ImageView) view.findViewById(R.id.product2_image);
    TextView title2 = (TextView) view.findViewById(R.id.product2_title);
    TextView originalPrice2 = (TextView) view.findViewById(R.id.original2_price);
    TextView discountPrice2 = (TextView) view.findViewById(R.id.discount2_price);
    
    title1.setText("상품명1");
    originalPrice1.setText("¥ 150.00");
    discountPrice1.setText("¥ 93.00");
    originalPrice1.setPaintFlags(originalPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    
    title2.setText("상품명2");
    originalPrice2.setText("¥ 150.00");
    discountPrice2.setText("¥ 93.00");
    originalPrice2.setPaintFlags(originalPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    
    addView(view);
  }
}
