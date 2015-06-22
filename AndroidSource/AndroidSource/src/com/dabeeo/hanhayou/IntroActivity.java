package com.dabeeo.hanhayou;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.dabeeo.hanhayou.activities.sub.GuideActivity;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.CharacterProgressView;

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
      File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
      if (file.exists())
        file.delete();
    }
    android.os.Process.killProcess(android.os.Process.myPid());
    super.onBackPressed();
  }
  
  
  private void checkNetworkStatus()
  {
    if (!SystemUtil.isConnectedWiFi(IntroActivity.this))
    {
      if (SystemUtil.isConnectNetwork(IntroActivity.this))
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
            checkAllowAlarm();
          }
        });
      }
      else
      {
        checkAllowAlarm();
      }
    }
    else
    {
      checkDownloadInfo();
    }
  }
  
  
  private void checkDownloadInfo()
  {
    Log.w("WARN", "다운로드 - 오프라인 컨텐츠 체크");
    //오프라인 컨텐츠 DB 있는 지 확인 
    File dir = new File(Global.GetDatabaseFilePath());
    if (!dir.exists())
    {
      dir.mkdirs();
    }
    
    File file = new File(Global.GetDatabaseFilePath() + OfflineContentDatabaseManager.DB_NAME);
    
    if (file.exists())
    {
      if (SystemUtil.isConnectNetwork(IntroActivity.this))
      {
        upateCheckOfflineContent();
      }
      else
      {
        //기존에 오프라인 컨텐츠를 만들어놓은 상태,인터넷 연결이 안된 상태일 때 바로 첫화면으로 이동
        checkAllowAlarm();
      }
    }
    else
    {
      if (SystemUtil.isConnectNetwork(IntroActivity.this))
      {
        makeOfflineContentDatabase(); // 인터넷 연결된 상태이고 오프라인컨텐츠를 만들지 않았을 때
      }
      else
      {
        checkAllowAlarm();
      }
    }
  }
  
  
  private void upateCheckOfflineContent()
  {
    //To-do : updateContent check - 오프라인 컨텐츠 업데이트 관련(추후 동기화 시나리오 적용)
    new UpdateCheckOfflineContentAsyncTask().execute();
  }
  
  private class UpdateCheckOfflineContentAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      alertManager.showProgressDialog(getString(R.string.term_alert), getString(R.string.msg_update_travel_content));
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      NetworkResult result = client.updateOfflineContents();
      try
      {
        Thread.sleep(2000);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      return null;
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      alertManager.hideProgressDialog();
      checkAllowAlarm();
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
    private Dialog dialog;
    private CharacterProgressView pView;
    
    
    @Override
    protected void onPreExecute()
    {
      Builder builder = new AlertDialog.Builder(IntroActivity.this);
      pView = new CharacterProgressView(IntroActivity.this);
      pView.title.setText(getString(R.string.msg_map_donwload));
      pView.setCircleProgressVisible(true);
      pView.setDownloadOfflinePopup();
      builder.setView(pView);
      builder.setCancelable(false);
      dialog = builder.create();
      dialog.show();
      
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... arg0)
    {
      Log.w("WARN", "오프라인 컨텐츠 다운로드 중");
      NetworkResult result = client.getOfflineContents();
      File dbFile = new File(Global.GetDatabaseFilePath() + OfflineContentDatabaseManager.DB_NAME);
      if (dbFile.exists())
        contentDatabaseManager.writeDatabase(result.response);
      
      publishProgress(10);
      try
      {
        File directory = new File(Global.GetImageFilePath());
        
        if (!directory.exists())
          directory.mkdirs();
        
        JSONObject obj = new JSONObject(result.response);
        File outputFile = new File(Global.GetImageFilePath() + "place_image.zip");
        
        if (!outputFile.exists())
        {
          outputFile.createNewFile();
          
          try
          {
            URL u = new URL(obj.getString("zip"));
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();
            
            DataInputStream stream = new DataInputStream(u.openStream());
            
            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();
            publishProgress(40);
            
            Log.w("WARN", "이미지 압축파일 다운로드 중");
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
          }
          catch (FileNotFoundException e)
          {
            e.printStackTrace();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
          
          publishProgress(80);
          unpackZip(outputFile.getAbsolutePath());
          
          outputFile.delete();
        }
      }
      catch (Exception e)
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
    protected void onPostExecute(NetworkResult result)
    {
      Log.w("WARN", "오프라인 컨텐츠 DB화 완료");
      if (dialog.isShowing())
        dialog.dismiss();
      checkMapDownload();
      super.onPostExecute(result);
    }
  }
  
  
  private boolean unpackZip(String path)
  {
    InputStream is;
    ZipInputStream zis;
    try
    {
      is = new FileInputStream(path);
      zis = new ZipInputStream(new BufferedInputStream(is));
      ZipEntry ze;
      
      while ((ze = zis.getNextEntry()) != null)
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        
        String filename = Global.MD5Encoding(ze.getName());
        FileOutputStream fout = new FileOutputStream(Global.GetImageFilePath() + filename);
        
        // reading and writing
        while ((count = zis.read(buffer)) != -1)
        {
          baos.write(buffer, 0, count);
          byte[] bytes = baos.toByteArray();
          fout.write(bytes);
          baos.reset();
        }
        
        fout.close();
        zis.closeEntry();
      }
      
      zis.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return false;
    }
    
    return true;
  }
  
  
  private void checkMapDownload()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.term_alert);
    builder.setMessage(R.string.msg_is_download_map);
    builder.setCancelable(false);
    
    File directory = new File(Global.GetDatabaseFilePath());
    if (!directory.exists())
      directory.mkdirs();
    
    File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
    if (!file.exists())
    {
      builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          mapdownloading = true;
          new GetMapAsyncTask().execute();
        }
      });
      builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          checkAllowAlarm();
        }
      });
      tempdialog = builder.create();
      tempdialog.setCanceledOnTouchOutside(false);
      tempdialog.show();
    }
    else
    {
      upateCheckOfflineContent();
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
        File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
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
      checkAllowAlarm();
      super.onPostExecute(result);
    }
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
    {
      startMainActivity();
    }
  }
  
  
  private void startGuideActivity()
  {
    PreferenceManager.getInstance(this).setIsFirst(false);
    startActivity(new Intent(IntroActivity.this, GuideActivity.class));
    finish();
  }
  
  
  private void startMainActivity()
  {
    startActivity(new Intent(IntroActivity.this, MainActivity.class));
    finish();
  }
}
