package com.dabeeo.hanhayou.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.LoginActivity;

public class AlertDialogManager
{
  public AlertDialog dialog;
  private ProgressDialog progressDialog;
  private Activity activity;
  
  
  public AlertDialogManager(Activity activity)
  {
    this.activity = activity;
  }
  
  
  public void dismiss()
  {
    dialog.dismiss();
  }
  
  
  /**
   * 네트웍 연결 시 사용이 가능합니다.
   */
  public void showDontNetworkConnectDialog()
  {
    showAlertDialog(activity.getString(R.string.term_alert), activity.getString(R.string.msg_dont_connect_network), activity.getString(android.R.string.ok), null, null);
  }
  
  
  /**
   * 로그인이 필요합니다.
   */
  public void showNeedLoginDialog(final int mainFragmentPosition)
  {
    showAlertDialog(activity.getString(R.string.term_alert), activity.getString(R.string.msg_require_login), activity.getString(android.R.string.ok), activity.getString(android.R.string.cancel),
        new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            Intent i = new Intent(activity, LoginActivity.class);
            i.putExtra("mainFragmentPostion", mainFragmentPosition);
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            
          }
        });
  }
  
  
  public void showAlertDialog(String title, String message, String okString, String cancelString, final AlertListener listener)
  {
    if (dialog != null)
    {
      if (dialog.isShowing())
        dialog.dismiss();
      
      dialog = null;
    }
    Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setCancelable(false);
    builder.setPositiveButton(okString, new OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        if (listener != null)
          listener.onPositiveButtonClickListener();
      }
    });
    
    if (!TextUtils.isEmpty(cancelString))
    {
      builder.setNegativeButton(cancelString, new OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          if (listener != null)
            listener.onNegativeButtonClickListener();
        }
      });
    }
    
    dialog = builder.create();
    dialog.show();
  }
  
  
  public void showProgressDialog(String title, String message)
  {
    if (progressDialog == null)
    {
      progressDialog = new ProgressDialog(activity);
      progressDialog.setTitle(title);
      progressDialog.setIndeterminate(false);
      progressDialog.setCancelable(false);
    }
    progressDialog.setMessage(message);
    
    try
    {
      if (!progressDialog.isShowing())
        progressDialog.show();
    }
    catch (Exception e)
    {
      
    }
  }
  
  
  public void hideProgressDialog()
  {
    if (progressDialog != null && progressDialog.isShowing())
    {
      try
      {
        progressDialog.dismiss();
        progressDialog = null;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public interface AlertListener
  {
    public void onPositiveButtonClickListener();
    
    
    public void onNegativeButtonClickListener();
  }
}
