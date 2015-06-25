package com.dabeeo.hanhayou.services;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.dabeeo.hanhayou.beans.OfflineBehaviorBean;
import com.dabeeo.hanhayou.controllers.NetworkBraodCastReceiver;
import com.dabeeo.hanhayou.controllers.OfflineDeleteManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class BackService extends Service
{
  private Handler handler = new Handler();
  private ApiClient apiClient;
  private OfflineDeleteManager offlineDatabaseManager;
  
  
  @Override
  public IBinder onBind(Intent arg0)
  {
    return null;
  }
  
  
  @Override
  public void onCreate()
  {
    super.onCreate();
    Log.w("WARN", "서비스 시작");
    apiClient = new ApiClient(this);
    offlineDatabaseManager = new OfflineDeleteManager(this);
    
    unRegisterRestart();
    registerNetworkOnOff();
  }
  
  
  @Override
  public void onDestroy()
  {
    registerRestart();
    unRegisterNetworkOnOff();
    super.onDestroy();
  }
  
  
  void registerRestart()
  {
    Intent kintent = new Intent(BackService.this, RestartBroadCastReceiver.class);
    kintent.setAction("com.dabeeo.restart");
    PendingIntent kSender = PendingIntent.getBroadcast(BackService.this, 0, kintent, 0);
    long firstTime = SystemClock.elapsedRealtime();
    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, 0, kSender);
  }
  
  
  void unRegisterRestart()
  {
    Intent intent = new Intent(BackService.this, RestartBroadCastReceiver.class);
    intent.setAction("com.dabeeo.restart");
    PendingIntent kSender = PendingIntent.getBroadcast(BackService.this, 0, intent, 0);
    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    am.cancel(kSender);
  }
  
  
  private void registerNetworkOnOff()
  {
    IntentFilter filter = new IntentFilter(NetworkBraodCastReceiver.ACTION_NETWORK_STATUS_CHANGE);
    registerReceiver(receiver, filter);
  }
  
  
  private void unRegisterNetworkOnOff()
  {
    unregisterReceiver(receiver);
  }
  
  //Network On/Off status change receiver
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      boolean isNetworkConnected = intent.getBooleanExtra("is_network_connected", false);
      if (isNetworkConnected)
      {
        //온라인 연결 시 SQLite 가져와서 동기화 처리하기
        Log.w("WARN", "백단 서비스 온라인 연결 동기화 시작 ");
        
        ArrayList<OfflineBehaviorBean> behaviors = offlineDatabaseManager.getAllBehavior();
        Log.w("WARN", "백단 서비스 온라인 : 오프라인 행동의 갯수 : " + behaviors.size());
        
        for (int i = 0; i < behaviors.size(); i++)
        {
          offlineDatabaseManager.deleteBehavior(behaviors.get(i).id);
          new OnlineSyncAsyncTask(behaviors.get(i)).execute();
        }
      }
    }
  };
  
  private class OnlineSyncAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    private OfflineBehaviorBean bean;
    
    
    public OnlineSyncAsyncTask(OfflineBehaviorBean bean)
    {
      this.bean = bean;
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      if (bean.behavior.equals(OfflineBehaviorBean.BEHAVIOR_DELETE_MY_PLAN))
        return apiClient.deleteMyPlan(bean.idx, bean.userSeq);
      else
        return apiClient.deleteMyPlace(bean.idx, bean.userSeq);
    }
  };
}
