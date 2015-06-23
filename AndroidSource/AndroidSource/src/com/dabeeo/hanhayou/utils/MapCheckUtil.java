package com.dabeeo.hanhayou.utils;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.views.MapdownloadProgressView;

public class MapCheckUtil
{
  private static Activity context;
  private static Runnable run;
  private static OfflineContentDatabaseManager contentDatabaseManager;
  
  
  public static void checkMapExist(Activity contextp, Runnable runp)
  {
    context = contextp;
    run = runp;
    
    File directory = new File(Global.GetPathWithSDCard());
    if (!directory.exists())
      directory.mkdirs();
    
    File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
    if (!file.exists())
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle(R.string.app_name).setMessage(R.string.msg_is_download_map).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int whichButton)
        {
          dialog.cancel();
          if(SystemUtil.isConnectNetwork(context))
          {
            if (SystemUtil.isConnectedWiFi(context))
            {
              new GetMapAsyncTask().execute();
            }
            else
            {
              final AlertDialogManager alertManager = new AlertDialogManager(context);
              alertManager.showAlertDialog(context.getString(R.string.term_alert), context.getString(R.string.message_alert_lte_mode), context.getString(R.string.term_ok),
                  context.getString(R.string.term_cancel), new AlertListener()
              {
                @Override
                public void onPositiveButtonClickListener()
                {
                  alertManager.dismiss();
                  new GetMapAsyncTask().execute();
                }
                
                
                @Override
                public void onNegativeButtonClickListener()
                {
                  alertManager.dismiss();
                }
              });
            }
          }else
          {
            final AlertDialogManager alertManager = new AlertDialogManager(context);
            alertManager.showDontNetworkConnectDialog();
          }
        }
      }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int whichButton)
        {
          dialog.cancel();
        }
      });
      
      AlertDialog dialog = builder.create();
      dialog.show();
    }
    else
    {
      if (run != null)
        run.run();
    }
  }
  
  private static class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    private MapdownloadProgressView pView;
    
    @Override
    protected void onPreExecute()
    {
      pView = new MapdownloadProgressView(context, context.getString(R.string.msg_update_travel_content));
      pView.setCanceledOnTouchOutside(false);
      pView.progressActive();
      pView.setCancelable(false);
      pView.show();
      
      super.onPreExecute();
    }
    
    
    @Override
    protected Boolean doInBackground(String... params)
    {
      File file = new File(Global.GetDatabaseFilePath() + OfflineContentDatabaseManager.DB_NAME);
      
      if (!file.exists())
        makeOfflineContentDatabase();
      
      int count = 0;
      
      pView.progressStop();
      
      try
      {
        Thread.sleep(100);
        URL url = new URL("http://tourplanb.com/_map/gs_seoul.mbtiles");
        URLConnection conexion = url.openConnection();
        conexion.connect();
        
        int lenghtOfFile = conexion.getContentLength();
        Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
        
        InputStream input = new BufferedInputStream(url.openStream());
        file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
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
      if (pView.isShowing())
        pView.dismiss();
      
      checkMapPlaceData();
      super.onPostExecute(result);
    }
  }
  
  private static void makeOfflineContentDatabase()
  {
    //오프라인 컨텐츠 database를 만듬
    contentDatabaseManager = new OfflineContentDatabaseManager(context);
    try
    {
      contentDatabaseManager.createDataBase();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    NetworkResult result = new ApiClient(context).getOfflineContents();
    
    File dbFile = new File(Global.GetDatabaseFilePath() + OfflineContentDatabaseManager.DB_NAME);
    if (dbFile.exists())
      contentDatabaseManager.writeDatabase(result.response);
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
        
        unpackZip(outputFile.getAbsolutePath());
        
        outputFile.delete();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static boolean unpackZip(String path)
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
  
  private static void checkMapPlaceData()
  {
    AssetManager assetManager = context.getAssets();
    
    File file = new File(Global.GetDatabaseFilePath() + Global.HangouyouDBFileName);
    
    if (!file.exists())
    {
      try
      {
        InputStream is = assetManager.open("hanhayou.sqlite");
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
    
    if (run != null)
      run.run();
  }
  
}
