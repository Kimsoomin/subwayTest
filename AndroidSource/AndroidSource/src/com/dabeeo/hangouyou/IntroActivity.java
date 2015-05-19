package com.dabeeo.hangouyou;

import java.io.BufferedInputStream;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.dabeeo.hangouyou.views.CharacterProgressView;

public class IntroActivity extends Activity
{
  private ProgressBar progressBar;
  private AlertDialogManager alertManager;
  private Handler handler = new Handler();
  private AlertDialog tempdialog;
  
  private boolean mapdownloading = false;
  
  
  @SuppressWarnings("static-access")
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    handler.postDelayed(checkSubwayNativeLoadRunnable, 100);
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
  
  private Runnable checkSubwayNativeLoadRunnable = new Runnable()
  {
    @Override
    public void run()
    {
//      Log.w("WARN", "call checkReady");
      if (MainActivity.subwayFrament.isLoadEnded())
      {
//        checkMap();
        checkMapTemp();
      }
      else
        handler.postDelayed(checkSubwayNativeLoadRunnable, 100);
    }
  };
  
  
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
      tempdialog = builder.create();
      tempdialog.setCanceledOnTouchOutside(false);
      tempdialog.show();
    }
    else
      getAllStations();
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
  
//  private void checkMap()
//  {
//    Log.w("WARN", "CheckMap");
//    
//    File directory = new File(Global.GetPathWithSDCard("/BlinkingMap/"));
//    if (!directory.exists())
//      directory.mkdirs();
//    
//    File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
//    if (!file.exists())
//    {
//      AlertDialog.Builder builder = new AlertDialog.Builder(this);
//      builder.setTitle(R.string.app_name).setMessage(R.string.msg_is_download_map).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
//      {
//        public void onClick(DialogInterface dialog, int whichButton)
//        {
//          dialog.cancel();
//          new GetMapAsyncTask().execute();
//        }
//      }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
//      {
//        public void onClick(DialogInterface dialog, int whichButton)
//        {
//          dialog.cancel();
//          getAllStations();
//        }
//      });
//      
//      AlertDialog dialog = builder.create();
//      dialog.show();
//    }
//    else
//      getAllStations();
//  }
  
  private class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    private Dialog dialog;
    
    
    @Override
    protected void onPreExecute()
    {
      if (tempdialog.isShowing())
        tempdialog.cancel();
      
      Builder builder = new AlertDialog.Builder(IntroActivity.this);
      CharacterProgressView pView = new CharacterProgressView(IntroActivity.this);
      pView.title.setText(getString(R.string.msg_map_donwload));
      pView.setCircleProgressVisible(true);
      builder.setView(pView);
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
//        File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
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
//      dialog.setProgress(progress[0]);
      super.onProgressUpdate(progress);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      if (dialog.isShowing())
        dialog.dismiss();
      
      getAllStations();
      super.onPostExecute(result);
    }
  }
  
  
  private void getAllStations()
  {
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
		
		File file = new File(Global.GetPathWithSDCard()+Global.HangouyouDBFileName);
		
		if(!file.exists())
		{
			try {
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
			} catch (Exception e) {
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
