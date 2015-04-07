package com.dabeeo.hangouyou;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.activities.sub.GuideActivity;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class IntroActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  private Handler handler = new Handler();
  
  
  @SuppressWarnings("static-access")
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    alertManager = new AlertDialogManager(this);
    
    if (MainActivity.subwayFrament != null)
      MainActivity.subwayFrament.viewClear();
    MainActivity.subwayFrament = new SubwayFragment();
    FragmentManager fragmentManager = getFragmentManager();
    if (MainActivity.subwayFrament != null)
    {
      FragmentTransaction ft = fragmentManager.beginTransaction();
      ft.replace(R.id.content, MainActivity.subwayFrament);
      ft.commit();
    }
  }
  
  
  @Override
  protected void onDestroy()
  {
    handler.removeCallbacksAndMessages(null);
    super.onDestroy();
  }
  
  
  @Override
  protected void onResume()
  {
    super.onResume();
    progressBar.bringToFront();
    handler.postDelayed(checkSubwayNativeLoadRunnable, 800);
  }
  
  private Runnable checkSubwayNativeLoadRunnable = new Runnable()
  {
    @Override
    public void run()
    {
//      Log.w("WARN", "call checkReady");
      if (MainActivity.subwayFrament.isLoadEnded())
      {
        MainActivity.subwayFrament.loadAllStations(new Runnable()
        {
          @Override
          public void run()
          {
            if (SystemUtil.isConnectNetwork(IntroActivity.this) && !SystemUtil.isConnectedWiFi(IntroActivity.this))
            {
              //3G or LTE Mode
              alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_lte_mode), getString(R.string.term_ok), getString(R.string.term_cancel),
                  new AlertListener()
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
        });
        
      }
      else
        handler.postDelayed(checkSubwayNativeLoadRunnable, 800);
    }
  };
  
  
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
              startGuideActivity();
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
              PreferenceManager.getInstance(IntroActivity.this).setAllowPopup(false);
              startGuideActivity();
            }
          });
    }
    else
      startMainActivity();
  }
  
  
  private void startGuideActivity()
  {
    PreferenceManager.getInstance(this).setIsFirst(false);
    startActivity(new Intent(IntroActivity.this, GuideActivity.class));
  }
  
  
  private void startMainActivity()
  {
    startActivity(new Intent(IntroActivity.this, MainActivity.class));
    finish();
  }
  
}
