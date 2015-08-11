package com.dabeeo.hanhayou.fragments.mainmenu;

import java.io.File;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.coupon.CouponActivity;
import com.dabeeo.hanhayou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hanhayou.activities.travel.TravelSchedulesActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hanhayou.activities.trend.TrendActivity;
import com.dabeeo.hanhayou.controllers.OfflineContentDatabaseManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.utils.SystemUtil;

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
//    drawable = (GradientDrawable) containerTicket.getBackground();
//    drawable.setColor(Color.parseColor("#9584f8"));
//    drawable = (GradientDrawable) containerCoupon.getBackground();
//    drawable.setColor(Color.parseColor("#ae7ff3"));
    
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
        MapCheckUtil.checkMapExist(getActivity(), new Runnable()
        {
          @Override
          public void run()
          {
            Intent mapIntent = new Intent(getActivity(), BlinkingMap.class);
            mapIntent.putExtra("bannerVisible", true);
            startActivity(mapIntent);
            getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
          }
        });
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
        
        new AlertDialogManager(getActivity()).showAlertDialog(getString(R.string.term_alert), "준비중입니다.", getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
        {
          
          @Override
          public void onPositiveButtonClickListener()
          {
            Global.useDevUrl = false;
          }
          
          
          @Override
          public void onNegativeButtonClickListener()
          {
            Global.useDevUrl = true;
          }
        });
//        new AlertDialogManager(getActivity()).showAlertDialog(getString(R.string.term_alert), "준비중입니다.", getString(R.string.term_ok), null, null);
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
        startActivity(new Intent(getActivity(), CouponActivity.class));
      }
    }
  };
}
