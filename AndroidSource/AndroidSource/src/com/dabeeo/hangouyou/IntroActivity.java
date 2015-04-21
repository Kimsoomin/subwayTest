package com.dabeeo.hangouyou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.activities.sub.GuideActivity;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.SubwayManager;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.map.MapPlaceDataManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class IntroActivity extends ActionBarActivity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  private Handler handler = new Handler();
  private ProgressDialog mapDialog;
  
  
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
        checkMap();
      }
      else
        handler.postDelayed(checkSubwayNativeLoadRunnable, 800);
    }
  };
  
  
  private void checkMap()
  {
    if (mapDialog == null)
    {
      mapDialog = new ProgressDialog(IntroActivity.this);
      mapDialog.setTitle(getString(R.string.app_name));
      mapDialog.setMessage(getString(R.string.msg_map_donwload));
    }
    
    if (!mapDialog.isShowing())
      mapDialog.show();
    Log.w("WARN", "CheckMap");
    new GetMapAsyncTask().execute();
  }
  
  private class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    @Override
    protected Boolean doInBackground(String... params)
    {
      File directory = new File(Global.GetPathWithSDCard("/BlinkingMap/"));
      if (!directory.exists())
        directory.mkdirs();
      
      File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
      if (!file.exists())
      {
        try
        {
          file.createNewFile();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
        
        Log.w("WARN", "Map Download");
        AssetManager assetManager = getAssets();
        
        InputStream in = null;
        OutputStream out = null;
        try
        {
          in = assetManager.open(Global.g_strMapDBFileName);
          String newFileName = Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName);
          out = new FileOutputStream(newFileName);
          
          byte[] buffer = new byte[1024];
          int read;
          while ((read = in.read(buffer)) != -1)
          {
            out.write(buffer, 0, read);
          }
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        }
        catch (Exception e)
        {
          Log.w("WARN", "File copy error");
          e.printStackTrace();
        }
        return null;
      }
      else
        return null;
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      if (mapDialog.isShowing())
        mapDialog.dismiss();
      
      MainActivity.subwayFrament.loadAllStations(new Runnable()
      {
        @Override
        public void run()
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
      });
      super.onPostExecute(result);
    }
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
        checkMapPlaceData();
      }
    };
    Handler handler = new Handler();
    handler.postDelayed(runn, 1000);
  }
  
  
  private void checkMapPlaceData()
  {
    AssetManager assetManager = getAssets();
    InputStream inputStream = null;
    String placeJsonString = "";
    try
    {
      inputStream = assetManager.open("place_json.txt");
      if (inputStream != null)
      {
        Writer writer = new StringWriter();
        
        char[] buffer = new char[1024];
        try
        {
          Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
          int n;
          while ((n = reader.read(buffer)) != -1)
          {
            writer.write(buffer, 0, n);
          }
        }
        finally
        {
          inputStream.close();
        }
        placeJsonString = writer.toString();
      }
    }
    catch (IOException e)
    {
      Log.e("message: ", e.getMessage());
    }
    
    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
    MapPlaceDataManager.getInstance(IntroActivity.this).deleteDatabase();
    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
    
    JSONArray array;
    try
    {
      JSONObject obj = new JSONObject(placeJsonString);
      array = obj.getJSONArray("place");
      for (int i = 0; i < array.length(); i++)
      {
        PlaceBean bean = new PlaceBean();
        bean.setJSONObject(array.getJSONObject(i));
        MapPlaceDataManager.getInstance(IntroActivity.this).addPlace(bean);
      }
      
      ArrayList<StationBean> stations = SubwayManager.getInstance(IntroActivity.this).stations;
      ArrayList<StationBean> afterArray = new ArrayList<StationBean>();
      String append = "";
      for (int i = 0; i < stations.size(); i++)
      {
        boolean isContain = false;
        for (int j = 0; j < afterArray.size(); j++)
        {
          if (afterArray.get(j).nameKo.equals(stations.get(i).nameKo))
            isContain = true;
        }
        
        if (!isContain)
        {
          append += stations.get(i).nameKo + " ";
          MapPlaceDataManager.getInstance(IntroActivity.this).addSubway(stations.get(i));
        }
        afterArray.add(stations.get(i));
      }
      Log.w("WARN", "Stations : " + append);
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    
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
