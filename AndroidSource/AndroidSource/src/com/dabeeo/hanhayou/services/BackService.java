package com.dabeeo.hanhayou.services;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.activities.trend.TrendProductPopupActivity;
import com.dabeeo.hanhayou.beans.OfflineBehaviorBean;
import com.dabeeo.hanhayou.beans.PushBean;
import com.dabeeo.hanhayou.controllers.NetworkBraodCastReceiver;
import com.dabeeo.hanhayou.controllers.OfflineDeleteManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class BackService extends Service
{
  private Handler handler = new Handler();
  private ApiClient apiClient;
  private OfflineDeleteManager offlineDatabaseManager;
  private LocationManager manager;
  private BackLocationListener listener;
  private double currentLat, currentLon;
  
  private long minTime = (1000 * 60) * 10;
  private long notificationRequestTime = (1000 * 60) * 60;
  private int notificationId = 12342;
  
  private boolean isFirstTime = true;
  
  
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
    
    registerLocationListener();
    unRegisterRestart();
    registerNetworkOnOff();
    
    handler.post(pushNotificationRunnable);
  }
  
  Runnable pushNotificationRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      Log.w("WARN", "Push Notification 요청 lat :" + currentLat + " / lon:" + currentLon);
      //TODO 실제 서버에 currentLat/Lng 보내고 받은 내용으로 팝업띄우기
      //현재는 테스트용으로 60분마다 로그인 (자동로그인 O) / 설정의 알림팝업이 켜져있는 경우 뜨도록 
      if (PreferenceManager.getInstance(BackService.this).getIsAutoLogin())
      {
        if (PreferenceManager.getInstance(BackService.this).isLoggedIn() && PreferenceManager.getInstance(BackService.this).getAllowPopup())
        {
          if (currentLat != 0)
            new PushNotificationAPIAsyncTask().execute();
        }
      }
      
      handler.postDelayed(pushNotificationRunnable, notificationRequestTime);
    }
  };
  
  private class PushNotificationAPIAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.reqeustPushNotification(currentLat, currentLon);
    }
    
    
    @Override
    protected void onPostExecute(final NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        PushBean bean = new PushBean();
        bean.setJSONObject(new JSONObject(result.response));
        
        if (bean.status.equals("NO_PUSH"))
        {
          Log.w("WARN", "No Push feed");
        }
        else
        {
          final Intent emptyIntent = new Intent(BackService.this, TrendProductDetailActivity.class);
          emptyIntent.putExtra("idx", bean.productId);
          
          String won = getString(R.string.term_yuan);
          if (bean.productCurrency.equals("KRW"))
            won = getString(R.string.term_won);
          PendingIntent pendingIntent = PendingIntent.getActivity(BackService.this, notificationId, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
          NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BackService.this).setSmallIcon(R.drawable.hanhayou).setLargeIcon(
              BitmapFactory.decodeResource(getResources(), R.drawable.hanhayou)).setContentTitle(bean.productTitle).setContentText(bean.productDiscount + won).setContentIntent(pendingIntent);
          NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          notificationManager.notify(101, mBuilder.build());
          
          handler.postDelayed(new Runnable()
          {
            @Override
            public void run()
            {
              Intent i = new Intent(BackService.this, TrendProductPopupActivity.class);
              i.putExtra("json_string", result.response);
              i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
            }
          }, 3000);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  
  @Override
  public void onDestroy()
  {
    registerRestart();
    unRegisterNetworkOnOff();
    unRegisterLocationListener();
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
  
  
  private void registerLocationListener()
  {
    if (manager == null)
      manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    float minDistance = 0;
    
    listener = new BackLocationListener();
    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
  }
  
  
  private void unRegisterLocationListener()
  {
    manager.removeUpdates(listener);
  }
  
  public class BackLocationListener implements LocationListener
  {
    @Override
    public void onLocationChanged(Location location)
    {
      currentLat = location.getLatitude();
      currentLon = location.getLongitude();
      Log.w("WARN", "current Lat : " + currentLat + " / current Lon : " + currentLon);
      
      if (isFirstTime)
      {
        handler.removeCallbacks(pushNotificationRunnable);
        handler.post(pushNotificationRunnable);
        isFirstTime = false;
      }
    }
    
    
    @Override
    public void onProviderDisabled(String provider)
    {
    }
    
    
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
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
  
  //AsyncTask
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
