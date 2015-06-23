package com.dabeeo.hanhayou.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.map.Global;

public class MapdownloadProgressView extends Dialog
{
  public Context context;
  private ProgressBar circleProgressBar;
  private TextView msg_alert;
  
  public MapdownloadProgressView(Context context, String message)
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
    msg_alert = (TextView) findViewById(R.id.msg_alert);
    msg_alert.setText(message);
  }
  
  public void progressActive()
  {
    circleProgressBar.setIndeterminateDrawable(Global.GetDrawable(context, R.drawable.circle_progress_download_shape));
    circleProgressBar.setIndeterminate(true);
  }
  
  public void progressStop()
  {
    circleProgressBar.setIndeterminate(false);
  }
  
  public void setProgress(int progress)
  {
    circleProgressBar.setProgress(progress);
  }
}
