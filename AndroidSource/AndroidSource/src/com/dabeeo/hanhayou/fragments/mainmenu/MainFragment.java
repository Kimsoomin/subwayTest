package com.dabeeo.hanhayou.fragments.mainmenu;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hanhayou.activities.travel.TravelSchedulesActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hanhayou.activities.trend.TrendActivity;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.CharacterProgressView;

public class MainFragment extends Fragment
{
  private RelativeLayout containerStrategySeoul, containerTravelSchedule, containerShoppingMall, containerMap, containerSubway, containerTicket, containerCoupon;
  private LinearLayout containerMsgDownloadMap;
  private ImageView badgeStrategySeoul, badgeTravelSchedule, badgeTrend, badgeSubway, badgeTicket, badgeCoupon;
  private OfflineContentDatabaseManager contentDatabaseManager;
  private ApiClient client;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_main_menu;
    View view = inflater.inflate(resId, null);
    
    client = new ApiClient(getActivity());
    containerStrategySeoul = (RelativeLayout) view.findViewById(R.id.container_strategy_seoul);
    containerTravelSchedule = (RelativeLayout) view.findViewById(R.id.container_travel_schedule);
    containerShoppingMall = (RelativeLayout) view.findViewById(R.id.container_shopping_mall);
    containerMap = (RelativeLayout) view.findViewById(R.id.container_map);
    containerSubway = (RelativeLayout) view.findViewById(R.id.container_subway);
    containerTicket = (RelativeLayout) view.findViewById(R.id.container_ticket);
    containerCoupon = (RelativeLayout) view.findViewById(R.id.container_coupon);
    containerMsgDownloadMap = (LinearLayout) view.findViewById(R.id.container_download_map);
    
    badgeStrategySeoul = (ImageView) view.findViewById(R.id.badge_main_place);
    badgeTravelSchedule = (ImageView) view.findViewById(R.id.badge_main_plan);
    badgeTravelSchedule.setVisibility(View.VISIBLE);
    badgeTrend = (ImageView) view.findViewById(R.id.badge_main_trend);
    badgeSubway = (ImageView) view.findViewById(R.id.badge_main_subway);
    badgeTicket = (ImageView) view.findViewById(R.id.badge_main_ticket);
    badgeCoupon = (ImageView) view.findViewById(R.id.badge_main_coupon);
    
    GradientDrawable drawable = (GradientDrawable) containerStrategySeoul.getBackground();
    drawable.setColor(Color.parseColor("#ffb55d"));
    drawable = (GradientDrawable) containerTravelSchedule.getBackground();
    drawable.setColor(Color.parseColor("#ffcb68"));
    drawable = (GradientDrawable) containerShoppingMall.getBackground();
    drawable.setColor(Color.parseColor("#ff6978"));
    drawable = (GradientDrawable) containerMap.getBackground();
    drawable.setColor(Color.parseColor("#6a8bf1"));
    drawable = (GradientDrawable) containerSubway.getBackground();
    drawable.setColor(Color.parseColor("#69a2f3"));
    drawable = (GradientDrawable) containerTicket.getBackground();
    drawable.setColor(Color.parseColor("#9584f8"));
    drawable = (GradientDrawable) containerCoupon.getBackground();
    drawable.setColor(Color.parseColor("#ae7ff3"));
    
    containerStrategySeoul.setOnClickListener(menuClickListener);
    containerTravelSchedule.setOnClickListener(menuClickListener);
    containerShoppingMall.setOnClickListener(menuClickListener);
    containerMap.setOnClickListener(menuClickListener);
    containerSubway.setOnClickListener(menuClickListener);
    containerTicket.setOnClickListener(menuClickListener);
    containerCoupon.setOnClickListener(menuClickListener);
    return view;
  }
  
  
  @Override
  public void onResume()
  {
    File directory = new File(Global.GetDatabaseFilePath());
    if (!directory.exists())
      directory.mkdirs();
    
    File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
    if (!file.exists())
      containerMsgDownloadMap.setVisibility(View.VISIBLE);
    else
      containerMsgDownloadMap.setVisibility(View.GONE);
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
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == containerTravelSchedule.getId())
      {
        startActivity(new Intent(getActivity(), TravelSchedulesActivity.class));
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == containerShoppingMall.getId())
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
        {
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
          return;
        }
        startActivity(new Intent(getActivity(), TrendActivity.class));
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == containerMap.getId())
      {
        File directory = new File(Global.GetDatabaseFilePath());
        if (!directory.exists())
          directory.mkdirs();
        
        File file = new File(Global.GetDatabaseFilePath() + Global.g_strMapDBFileName);
        if (!file.exists())
        {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setTitle(R.string.term_alert).setMessage(R.string.message_alert_lte_mode).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog, int whichButton)
            {
              dialog.cancel();
              if (SystemUtil.isConnectedWiFi(getActivity()))
                new GetMapAsyncTask().execute();
              else
              {
                final AlertDialogManager alertManager = new AlertDialogManager(getActivity());
                alertManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.msg_is_download_map), getString(R.string.term_ok), getString(R.string.term_cancel),
                    new AlertListener()
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
          startActivity(new Intent(getActivity(), BlinkingMap.class));
          getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
      }
      else if (v.getId() == containerSubway.getId())
      {
        Intent i = new Intent(getActivity(), SubwayActivity.class);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == containerTicket.getId())
      {
        //temp
        new AlertDialogManager(getActivity()).showAlertDialog(getString(R.string.term_alert), "준비중입니다.", getString(R.string.term_ok), null, null);
//        if (PreferenceManager.getInstance(getActivity()).isLoggedIn())
//        {
//          startActivity(new Intent(getActivity(), TicketActivity.class));
//        }
//        else
//        {
//          new AlertDialogManager(getActivity()).showNeedLoginDialog();
//        }
      }
      else if (v.getId() == containerCoupon.getId())
      {
        
        //temp alert
       new AlertDialogManager(getActivity()).showAlertDialog(getString(R.string.term_alert), "준비중입니다.", getString(R.string.term_ok), null, null);
//        //네트워크 연결 체크 후 연결했을 때만 실행
//        if (!SystemUtil.isConnectNetwork(getActivity()))
//        {
//          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
//          return;
//        }
//        
//        if (PreferenceManager.getInstance(getActivity()).isLoggedIn())
//        {
//          startActivity(new Intent(getActivity(), CouponActivity.class));
//        }
//        else
//        {
//          new AlertDialogManager(getActivity()).showNeedLoginDialog();
//        }
      }
    }
  };
  
  private class GetMapAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    private AlertDialog dialog;
    private CharacterProgressView pView;
    
    
    @Override
    protected void onPreExecute()
    {
      Builder builder = new AlertDialog.Builder(getActivity());
      pView = new CharacterProgressView(getActivity());
      pView.title.setText(getString(R.string.msg_map_donwload));
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
      File file = new File(Global.GetDatabaseFilePath() + OfflineContentDatabaseManager.DB_NAME);
      
      if (!file.exists())
        makeOfflineContentDatabase();
      
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
      if (dialog.isShowing())
        dialog.dismiss();
      containerMsgDownloadMap.setVisibility(View.GONE);
      startActivity(new Intent(getActivity(), BlinkingMap.class));
      getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      super.onPostExecute(result);
    }
  }
  
  
  private void makeOfflineContentDatabase()
  {
    //오프라인 컨텐츠 database를 만듬
    contentDatabaseManager = new OfflineContentDatabaseManager(getActivity());
    try
    {
      contentDatabaseManager.createDataBase();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    NetworkResult result = client.getOfflineContents();
    contentDatabaseManager.writeDatabase(result.response);
  }
}