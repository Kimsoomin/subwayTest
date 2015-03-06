package com.dabeeo.hangouyou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class IntroActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    alertManager = new AlertDialogManager(this);
  }
  
  
  @Override
  protected void onResume()
  {
    super.onResume();
    progressBar.bringToFront();
    if (SystemUtil.isConnectNetwork(this) && !SystemUtil.isConnectedWiFi(this))
    {
      //3G or LTE Mode
      alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_lte_mode), getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
      {
        @Override
        public void onPositiveButtonClickListener()
        {
          checkDownloadInfo();
        }
        
        
        @Override
        public void onNegativeButtonClickListener()
        {
          finish();
        }
      });
    }
    else
      checkDownloadInfo();
  }
  
  
  private void checkDownloadInfo()
  {
    //지도정보, 상품정보, 지하철정보 다운로드
    Log.w("WARN", "다운로드 정보 체크");
    
    //만약 다운로드 된 정보가 없거나, 혹은 업데이트가 있는 경우 
    alertManager.showProgressDialog(getString(R.string.term_alert), getString(R.string.message_alert_download_seoul_info));
    
    //추후 아랫부분 삭제 후 네트워크 연결
    // memory leak warning 제거 
    Runnable runn = new Runnable()
    {
      @Override
      public void run()
      {
        alertManager.hideProgressDialog();
        checkAllowAlarm();
      }
    };
    Handler handler = new Handler();
    handler.postDelayed(runn, 1000);
  }
  
  
  private void checkAllowAlarm()
  {
    if (PreferenceManager.getInstance(this).getIsFirst())
    {
      alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_allow_notification), getString(R.string.term_ok), getString(R.string.term_cancel),
          new AlertListener()
          {
            @Override
            public void onPositiveButtonClickListener()
            {
              PreferenceManager.getInstance(IntroActivity.this).setAllowPopup(true);
              startMainActivity();
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
              PreferenceManager.getInstance(IntroActivity.this).setAllowPopup(false);
              startMainActivity();
            }
          });
    }
    else
      startMainActivity();
  }
  
  
  private void startMainActivity()
  {
    Intent i = new Intent(IntroActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }
}
