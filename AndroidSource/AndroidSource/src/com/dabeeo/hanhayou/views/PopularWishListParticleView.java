package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;

public class PopularWishListParticleView extends RelativeLayout
{
  
  private Context context;
  private String firstBean;
  private String secondBean;
  private int position;
  
  
  public PopularWishListParticleView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public void setBean(int position, String firstBean, String secondBean)
  {
    this.position = position;
    this.firstBean = firstBean;
    this.secondBean = secondBean;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_popular_wishlist_view;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    TextView leftText = (TextView) view.findViewById(R.id.text_left);
    TextView rightText = (TextView) view.findViewById(R.id.text_right);
    
    leftText.setText(firstBean);
    if(!TextUtils.isEmpty(secondBean))
      rightText.setText(secondBean);
    else
      rightText.setVisibility(View.GONE);
    
    if (position % 2 == 0)
    {
      leftText.setBackgroundResource(R.drawable.yellow_rectangle);
      rightText.setBackgroundResource(R.drawable.green_rectangle);
    }
    else
    {
      leftText.setBackgroundResource(R.drawable.blue_rectangle);
      rightText.setBackgroundResource(R.drawable.red_rectangle);
    }
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Toast.makeText(context, context.getString(R.string.msg_add_wishlist), Toast.LENGTH_LONG).show();
      }
    });
    
    addView(view);
  }
}
