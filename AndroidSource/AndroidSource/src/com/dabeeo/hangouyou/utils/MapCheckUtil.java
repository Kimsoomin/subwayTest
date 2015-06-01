package com.dabeeo.hangouyou.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.views.CharacterProgressView;

public class MapCheckUtil
{
	private static Activity context;
	private static Runnable run;
	
	
	public static void checkMapExist(Activity contextp, Runnable runp)
	{
		context = contextp;
		run = runp;
		
		File directory = new File(Global.GetPathWithSDCard());
		if (!directory.exists())
			directory.mkdirs();
		
		File file = new File(Global.GetPathWithSDCard() + Global.g_strMapDBFileName);
		if (!file.exists())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.app_name).setMessage(R.string.msg_is_download_map).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					dialog.cancel();
					if (SystemUtil.isConnectedWiFi(context))
						new GetMapAsyncTask().execute();
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
		private AlertDialog dialog;
		private CharacterProgressView pView;
		
		
		@Override
		protected void onPreExecute()
		{
			Builder builder = new AlertDialog.Builder(context);
			pView = new CharacterProgressView(context);
			pView.title.setText(context.getString(R.string.msg_map_donwload));
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
				} catch (IOException e)
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
				
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (IOException e)
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
	
	private  static void checkMapPlaceData()
	  {
	    AssetManager assetManager = context.getAssets();
	    
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
//	    AssetManager assetManager = getAssets();
//	    InputStream inputStream = null;
//	    String placeJsonString = "";
//	    try
//	    {
//	      inputStream = assetManager.open("place_json.txt");
//	      if (inputStream != null)
//	      {
//	        Writer writer = new StringWriter();
//	        
//	        char[] buffer = new char[1024];
//	        try
//	        {
//	          Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//	          int n;
//	          while ((n = reader.read(buffer)) != -1)
//	          {
//	            writer.write(buffer, 0, n);
//	          }
//	        }
//	        finally
//	        {
//	          inputStream.close();
//	        }
//	        placeJsonString = writer.toString();
//	      }
//	    }
//	    catch (IOException e)
//	    {
//	      Log.e("message: ", e.getMessage());
//	    }
	//    
//	    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
//	    MapPlaceDataManager.getInstance(IntroActivity.this).deleteDatabase();
//	    MapPlaceDataManager.getInstance(IntroActivity.this).initDatabase();
	//    
//	    JSONArray array;
//	    try
//	    {
//	      JSONObject obj = new JSONObject(placeJsonString);
//	      array = obj.getJSONArray("place");
//	      for (int i = 0; i < array.length(); i++)
//	      {
//	        PlaceBean bean = new PlaceBean();
//	        bean.setJSONObject(array.getJSONObject(i));
//	        MapPlaceDataManager.getInstance(IntroActivity.this).addPlace(bean);
//	      }
//	      
//	      ArrayList<StationBean> stations = SubwayManager.getInstance(IntroActivity.this).stations;
//	      ArrayList<StationBean> afterArray = new ArrayList<StationBean>();
//	      String append = "";
//	      for (int i = 0; i < stations.size(); i++)
//	      {
//	        boolean isContain = false;
//	        for (int j = 0; j < afterArray.size(); j++)
//	        {
//	          if (afterArray.get(j).nameKo.equals(stations.get(i).nameKo))
//	            isContain = true;
//	        }
//	        
//	        if (!isContain)
//	        {
//	          append += stations.get(i).nameKo + " ";
//	          afterArray.add(stations.get(i));
//	        }
//	      }
//	      for (int j = 0; j < afterArray.size(); j++)
//	      {
//	        MapPlaceDataManager.getInstance(IntroActivity.this).addSubway(afterArray.get(j));
//	      }
//	      
//	      Log.w("WARN", "Stations : " + append);
//	      Log.w("WARN", "Station : " + afterArray.size());
//	    }
//	    catch (JSONException e)
//	    {
//	      e.printStackTrace();
//	    }
	    
	    if (run != null)
			run.run();
	  }
	  
}