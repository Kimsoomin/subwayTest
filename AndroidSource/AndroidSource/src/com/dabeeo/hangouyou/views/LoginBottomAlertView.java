package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class LoginBottomAlertView extends RelativeLayout
{
  private Context context;
  private LinearLayout alertContainer;
  private TextView alertMessage;
  
  
  public LoginBottomAlertView(Context context)
  {
    super(context);
    this.context = context;
    init();
  }
  
  
  public LoginBottomAlertView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public LoginBottomAlertView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setAlert(final String msg)
  {
    alertContainer.setVisibility(View.VISIBLE);
    alertMessage.setText(msg);
    
    new Handler().postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        alertContainer.setVisibility(View.GONE);
      }
    }, 2000);
  }
  
  
  public void init()
  {
    int resId = R.layout.view_login_bottom_alert;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    alertContainer = (LinearLayout) view.findViewById(R.id.container);
    alertMessage = (TextView) view.findViewById(R.id.text_alert);
    
    alertContainer.setVisibility(View.GONE);
    
    addView(view);
  }
}
