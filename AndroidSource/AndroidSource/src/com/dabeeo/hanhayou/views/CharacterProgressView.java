package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

public class CharacterProgressView extends RelativeLayout
{
  
  private Context context;
  public ImageView imageView;
  public TextView title, noticeTitle;
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
    noticeTitle = (TextView) view.findViewById(R.id.text_notice);
    circleProgressBar = (ProgressBar) view.findViewById(R.id.circle_progress);
    handler.post(characterAnimate);
    addView(view);
  }
  
  
  public void setDownloadOfflinePopup()
  {
    imageView.setImageResource(R.drawable.icon_popup_download);
    handler.removeCallbacks(characterAnimate);
    title.setText(context.getString(R.string.msg_download_travel_cotnent));
    noticeTitle.setVisibility(View.VISIBLE);
  }
  
  
  public void setCircleProgressVisible(boolean isVisible)
  {
    if (isVisible)
      circleProgressBar.setVisibility(View.VISIBLE);
    else
      circleProgressBar.setVisibility(View.GONE);
  }
  
  
  public void setProgress(int progress)
  {
    circleProgressBar.setProgress(progress);
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
