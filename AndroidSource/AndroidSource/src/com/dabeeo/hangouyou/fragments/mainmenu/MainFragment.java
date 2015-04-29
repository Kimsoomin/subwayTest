package com.dabeeo.hangouyou.fragments.mainmenu;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.IntroActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.LoginActivity;
import com.dabeeo.hangouyou.activities.travel.TravelSchedulesActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hangouyou.activities.trend.TrendActivity;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class MainFragment extends Fragment
{
  private RelativeLayout containerStrategySeoul, containerTravelSchedule, containerShoppingMall, containerMap, containerSubway, containerTicketAndCoupon, containerReservation;
  private TextView textDownloadMap;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_main_menu;
    View view = inflater.inflate(resId, null);
    
    containerStrategySeoul = (RelativeLayout) view.findViewById(R.id.container_strategy_seoul);
    containerTravelSchedule = (RelativeLayout) view.findViewById(R.id.container_travel_schedule);
    containerShoppingMall = (RelativeLayout) view.findViewById(R.id.container_shopping_mall);
    containerMap = (RelativeLayout) view.findViewById(R.id.container_map);
    containerSubway = (RelativeLayout) view.findViewById(R.id.container_subway);
    containerTicketAndCoupon = (RelativeLayout) view.findViewById(R.id.container_ticket_and_coupon);
    containerReservation = (RelativeLayout) view.findViewById(R.id.container_reservation);
    textDownloadMap = (TextView) view.findViewById(R.id.text_download_map);
    
    containerStrategySeoul.setOnClickListener(menuClickListener);
    containerTravelSchedule.setOnClickListener(menuClickListener);
    containerShoppingMall.setOnClickListener(menuClickListener);
    containerMap.setOnClickListener(menuClickListener);
    containerSubway.setOnClickListener(menuClickListener);
    containerTicketAndCoupon.setOnClickListener(menuClickListener);
    containerReservation.setOnClickListener(menuClickListener);
    return view;
  }
  
  
  @Override
  public void onResume()
  {
    File directory = new File(Global.GetPathWithSDCard("/BlinkingMap/"));
    if (!directory.exists())
      directory.mkdirs();
    
    File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
    if (!file.exists())
      textDownloadMap.setVisibility(View.VISIBLE);
    else
      textDownloadMap.setVisibility(View.GONE);
    super.onResume();
  }
  
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == containerStrategySeoul.getId())
      {
        startActivity(new Intent(getActivity(), TravelStrategyActivity.class));
      }
      else if (v.getId() == containerTravelSchedule.getId())
      {
        startActivity(new Intent(getActivity(), TravelSchedulesActivity.class));
      }
      else if (v.getId() == containerShoppingMall.getId())
      {
        startActivity(new Intent(getActivity(), TrendActivity.class));
      }
      else if (v.getId() == containerMap.getId())
      {
        File directory = new File(Global.GetPathWithSDCard("/BlinkingMap/"));
        if (!directory.exists())
          directory.mkdirs();
        
        File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
        if (!file.exists())
        {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setTitle(R.string.app_name).setMessage(R.string.msg_is_download_map).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int whichButton)
            {
              dialog.cancel();
              new GetMapAsyncTask().execute();
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
          startActivity(new Intent(getActivity(), BlinkingMap.class));
      }
      else if (v.getId() == containerSubway.getId())
      {
        //가까운 지하철 역 찾기
//        Intent i = new Intent(getActivity(), SubwayActivity.class);
//        double[] latLon = new double[2];
//        latLon[0] = 38;
//        latLon[1] = 128;
//        i.putExtra("near_by_station_lat_lon", latLon);
//        startActivity(i);
        
        //해당 지하철역 출발역으로 지정
//        Intent i = new Intent(getActivity(), SubwayActivity.class);
//        double[] latLon = new double[3];
//        latLon[0] = 38;
//        latLon[1] = 128;
//        latLon[2] = 1;
//        i.putExtra("set_dest_station_lat_lon", latLon);
//        startActivity(i);
        Intent i = new Intent(getActivity(), SubwayActivity.class);
        startActivity(i);
      }
      else if (v.getId() == containerTicketAndCoupon.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행
        if (!SystemUtil.isConnectNetwork(getActivity()))
        {
          Toast.makeText(getActivity(), getString(R.string.msg_not_connect_network), Toast.LENGTH_LONG).show();
          return;
        }
        
        if (TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getUserSeq()))
          startActivity(new Intent(getActivity(), LoginActivity.class));
        else
          Toast.makeText(getActivity(), "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerReservation.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행
        if (!SystemUtil.isConnectNetwork(getActivity()))
        {
          Toast.makeText(getActivity(), getString(R.string.msg_not_connect_network), Toast.LENGTH_LONG).show();
          return;
        }
        
        if (TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getUserSeq()))
          startActivity(new Intent(getActivity(), LoginActivity.class));
        else
          Toast.makeText(getActivity(), "준비중입니다", Toast.LENGTH_LONG).show();
      }
    }
  };
  
  private class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    private ProgressDialog dialog;
    
    
    @Override
    protected void onPreExecute()
    {
      dialog = new ProgressDialog(getActivity());
      dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      dialog.setMessage(getString(R.string.msg_map_donwload));
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
        File file = new File(Global.GetPathWithSDCard("/BlinkingMap/" + Global.g_strMapDBFileName));
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
      dialog.setProgress(progress[0]);
      super.onProgressUpdate(progress);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      textDownloadMap.setVisibility(View.GONE);
      startActivity(new Intent(getActivity(), BlinkingMap.class));
      super.onPostExecute(result);
    }
  }
  
}
