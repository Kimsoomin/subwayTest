package com.dabeeo.hangouyou;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.activities.sub.GuideActivity;
import com.dabeeo.hangouyou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.utils.SystemUtil;
import com.dabeeo.hangouyou.views.CharacterProgressView;

public class IntroActivity extends Activity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  private Handler handler = new Handler();
  private AlertDialog tempdialog;
  private ApiClient client;
  private OfflineContentDatabaseManager contentDatabaseManager;
  
  private boolean mapdownloading = false;
  
  
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    alertManager = new AlertDialogManager(this);
    client = new ApiClient(this);
    
    progressBar.bringToFront();
    
    checkNetworkStatus();
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
  }
  
  
  @Override
  public void onBackPressed()
  {
    if (mapdownloading == true)
    {
      File file = new File(Global.GetPathWithSDCard() + Global.g_strMapDBFileName);
      if (file.exists())
        file.delete();
    }
    android.os.Process.killProcess(android.os.Process.myPid());
    super.onBackPressed();
  }
  
  
  private void checkNetworkStatus()
  {
    if (SystemUtil.isConnectNetwork(IntroActivity.this) && !SystemUtil.isConnectedWiFi(IntroActivity.this))
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
    Log.w("WARN", "다운로드 - 오프라인 컨텐츠 체크");
    //오프라인 컨텐츠 DB 있는 지 확인 
    File file = new File(OfflineContentDatabaseManager.DB_PATH + OfflineContentDatabaseManager.DB_NAME);
    
    if (file.exists())
    {
      if (SystemUtil.isConnectNetwork(IntroActivity.this))
      {
        //기존에 오프라인 컨텐츠를 만들어놓은 상태, 인터넷 연결 시 다시 오프라인컨텐츠 생성
//        file.delete();
//        makeOfflineContentDatabase();
        startMainActivity();
      }
      else
      {
        //기존에 오프라인 컨텐츠를 만들어놓은 상태,인터넷 연결이 안된 상태일 때 바로 첫화면으로 이동
        startMainActivity();
      }
    }
    else
    {
      if (SystemUtil.isConnectNetwork(IntroActivity.this))
        makeOfflineContentDatabase(); // 인터넷 연결된 상태이고 오프라인컨텐츠를 만들지 않았을 때
      else
        startMainActivity();
    }
  }
  
  
  private void makeOfflineContentDatabase()
  {
    //오프라인 컨텐츠 database를 만듬
    contentDatabaseManager = new OfflineContentDatabaseManager(this);
    try
    {
      contentDatabaseManager.createDataBase();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    new GetOfflineContentAsyncTask().execute();
  }
  
  private class GetOfflineContentAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      alertManager.showProgressDialog(getString(R.string.term_alert), getString(R.string.msg_download_travel_cotnent));
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... arg0)
    {
      Log.w("WARN", "오프라인 컨텐츠 다운로드 중");
      NetworkResult result = client.getOfflineContents();
      contentDatabaseManager.writeDatabase(result.response);
      return null;
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      Log.w("WARN", "오프라인 컨텐츠 DB화 완료");
      alertManager.hideProgressDialog();
      checkMapTemp();
      super.onPostExecute(result);
    }
  }
  
  
  private void checkMapTemp()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.msg_is_download_map);
    builder.setCancelable(false);
    
    File directory = new File(Global.GetPathWithSDCard());
    if (!directory.exists())
      directory.mkdirs();
    
    File file = new File(Global.GetPathWithSDCard() + Global.g_strMapDBFileName);
    if (!file.exists())
    {
      builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          temp3G();
        }
      });
      builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          checkMapPlaceData();
        }
      });
      tempdialog = builder.create();
      tempdialog.setCanceledOnTouchOutside(false);
      tempdialog.show();
    }
    else
      checkMapPlaceData();
  }
  
  
  private void temp3G()
  {
    if (SystemUtil.isConnectNetwork(IntroActivity.this) && !SystemUtil.isConnectedWiFi(IntroActivity.this))
    {
      //3G or LTE Mode
      alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.message_alert_lte_mode), getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
      {
        @Override
        public void onPositiveButtonClickListener()
        {
          mapdownloading = true;
          new GetMapAsyncTask().execute();
        }
        
        
        @Override
        public void onNegativeButtonClickListener()
        {
          alertManager.dismiss();
          if (!tempdialog.isShowing())
            tempdialog.show();
        }
      });
    }
    
    if (SystemUtil.isConnectedWiFi(IntroActivity.this))
    {
      if (tempdialog.isShowing())
        tempdialog.cancel();
      mapdownloading = true;
      new GetMapAsyncTask().execute();
    }
  }
  
  private class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    private Dialog dialog;
    private CharacterProgressView pView;
    
    
    @Override
    protected void onPreExecute()
    {
      if (tempdialog.isShowing())
        tempdialog.cancel();
      
      Builder builder = new AlertDialog.Builder(IntroActivity.this);
      pView = new CharacterProgressView(IntroActivity.this);
      pView.title.setText(getString(R.string.msg_map_donwload));
      pView.setCircleProgressVisible(true);
      pView.setCircleProgressVisible(true);
      builder.setView(pView);
      builder.setCancelable(false);
      dialog = builder.create();
      dialog.show();
      
      super.onPreExecute();
    }
    
    
    @Override
    protected Boolean doInBackground(String... params)
    {
      int count = 0;
      
      try
      {
        Thread.sleep(100);
        URL url = new URL("http://tourplanb.com/_map/gs_seoul.mbtiles");
        URLConnection conexion = url.openConnection();
        conexion.connect();
        
        int lenghtOfFile = conexion.getContentLength();
        Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
        
        InputStream input = new BufferedInputStream(url.openStream());
        File file = new File(Global.GetPathWithSDCard() + Global.g_strMapDBFileName);
        try
        {
          if (!file.exists())
            file.createNewFile();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        
        OutputStream output = new FileOutputStream(file);
        
        byte data[] = new byte[1024];
        
        long total = 0;
        
        while ((count = input.read(data)) != -1)
        {
          total += count;
          publishProgress((int) ((total * 100) / lenghtOfFile));
          output.write(data, 0, count);
        }
        
        output.flush();
        output.close();
        input.close();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      return null;
    }
    
    
    @Override
    protected void onProgressUpdate(Integer... progress)
    {
      pView.setProgress(progress[0]);
      super.onProgressUpdate(progress);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      if (dialog.isShowing())
        dialog.dismiss();
      
      checkMapPlaceData();
      super.onPostExecute(result);
    }
  }
  
  
  private void checkMapPlaceData()
  {
    AssetManager assetManager = getAssets();
    
    File file = new File(Global.GetPathWithSDCard() + Global.HangouyouDBFileName);
    
    if (!file.exists())
    {
      try
      {
        InputStream is = assetManager.open("hangouyou.sqlite");
        OutputStream out = new FileOutputStream(file);
        
        int size = is.available();
        
        if (size > 0)
        {
          byte[] data = new byte[size];
          is.read(data);
          
          out.write(data);
        }
        out.flush();
        out.close();
        
        is.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
//    AssetManager assetManager = getAssets();
//    InputStream inputStream = null;
//    String placeJsonString = "";
//    try
//    {
//      inputStream = assetManager.open("place_json.txt");
//      if (inputStream != null)
//      {
//        Writer writer = new StringWriter();
//        
//        char[] buffer = new char[1024];
//        try
//        {
//          Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//          int n;
//          while ((n = reader.read(buffer)) != -1)
//          {
//            writer.write(buffer, 0, n);
//          }
//        }
//        finally
//        {
//          inputStream.close();
//        }
//        placeJsonString = writer.toString();
//      }
//    }
//    catch (IOException e)
//    {
//      Log.e("message: ", e.getMessage());
//    }
//    
//    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
//    MapPlaceDataManager.getInstance(IntroActivity.this).deleteDatabase();
//    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
//    
//    JSONArray array;
//    try
//    {
//      JSONObject obj = new JSONObject(placeJsonString);
//      array = obj.getJSONArray("place");
//      for (int i = 0; i < array.length(); i++)
//      {
//        PlaceBean bean = new PlaceBean();
//        bean.setJSONObject(array.getJSONObject(i));
//        MapPlaceDataManager.getInstance(IntroActivity.this).addPlace(bean);
//      }
//      
//      ArrayList<StationBean> stations = SubwayManager.getInstance(IntroActivity.this).stations;
//      ArrayList<StationBean> afterArray = new ArrayList<StationBean>();
//      String append = "";
//      for (int i = 0; i < stations.size(); i++)
//      {
//        boolean isContain = false;
//        for (int j = 0; j < afterArray.size(); j++)
//        {
//          if (afterArray.get(j).nameKo.equals(stations.get(i).nameKo))
//            isContain = true;
//        }
//        
//        if (!isContain)
//        {
//          append += stations.get(i).nameKo + " ";
//          afterArray.add(stations.get(i));
//        }
//      }
//      for (int j = 0; j < afterArray.size(); j++)
//      {
//        MapPlaceDataManager.getInstance(IntroActivity.this).addSubway(afterArray.get(j));
//      }
//      
//      Log.w("WARN", "Stations : " + append);
//      Log.w("WARN", "Station : " + afterArray.size());
//    }
//    catch (JSONException e)
//    {
//      e.printStackTrace();
//    }
    
    checkAllowAlarm();
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
