package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;

public class ListFooterProgressView extends RelativeLayout
{
  private Context context;
  private Handler handler;
  public ImageView imageView;
  private int animatingFlag = 0;
  
  
  public ListFooterProgressView(Context context)
  {
    super(context);
    this.context = context;
    handler = new Handler();
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_list_footer_progress;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    imageView = (ImageView) view.findViewById(R.id.character_image);
    handler.post(characterAnimate);
    addView(view);
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
