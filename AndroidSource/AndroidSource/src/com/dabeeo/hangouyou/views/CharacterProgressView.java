package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;

public class CharacterProgressView extends RelativeLayout
{
  
  private Context context;
  public ImageView imageView;
  public TextView title;
  private Handler handler;
  private ProgressBar circleProgressBar;
  
  private int animatingFlag = 0;
  
  
  public CharacterProgressView(Context context)
  {
    super(context);
    this.context = context;
    handler = new Handler();
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_character_progress;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageView = (ImageView) view.findViewById(R.id.character_image);
    title = (TextView) view.findViewById(R.id.title);
    circleProgressBar = (ProgressBar) view.findViewById(R.id.circle_progress);
    handler.post(characterAnimate);
    addView(view);
  }
  
  
  public void setCircleProgressVisible(boolean isVisible)
  {
//    if (isVisible)
//      circleProgressBar.setVisibility(View.VISIBLE);
//    else
//      circleProgressBar.setVisibility(View.GONE);
  }
  
  Runnable characterAnimate = new Runnable()
  {
    @Override
    public void run()
    {
      if (animatingFlag == 0)
      {
        imageView.setImageResource(R.drawable.icon_popup_making1);
        animatingFlag = 1;
      }
      else
      {
        imageView.setImageResource(R.drawable.icon_popup_making2);
        animatingFlag = 0;
      }
      
      handler.postDelayed(characterAnimate, 500);
    }
  };
}
