package com.dabeeo.hanhayou.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.dabeeo.hanhayou.R;

public class MapdownloadProgressView extends Dialog
{
  public Context context;
  private ProgressBar circleProgressBar;
  
  public MapdownloadProgressView(Context context)
  {
    super(context);
    this.context = context;
    
    WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
    lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    lpWindow.dimAmount = 0.4f;
    getWindow().setAttributes(lpWindow);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.view_map_progress);
    circleProgressBar = (ProgressBar) findViewById(R.id.circle_progress);
  }
  
  public void setProgress(int progress)
  {
    circleProgressBar.setProgress(progress);
  }
}
