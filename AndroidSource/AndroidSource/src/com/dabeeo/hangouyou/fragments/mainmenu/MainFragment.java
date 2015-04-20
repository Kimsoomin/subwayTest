package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.activities.mainmenu.TravelSchedulesActivity;
import com.dabeeo.hangouyou.activities.mainmenu.TravelStrategyActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.LoginActivity;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class MainFragment extends Fragment
{
  private RelativeLayout containerStrategySeoul, containerTravelSchedule, containerShoppingMall, containerMap, containerSubway, containerTicketAndCoupon, containerReservation;
  
  
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
    
    containerStrategySeoul.setOnClickListener(menuClickListener);
    containerTravelSchedule.setOnClickListener(menuClickListener);
    containerShoppingMall.setOnClickListener(menuClickListener);
    containerMap.setOnClickListener(menuClickListener);
    containerSubway.setOnClickListener(menuClickListener);
    containerTicketAndCoupon.setOnClickListener(menuClickListener);
    containerReservation.setOnClickListener(menuClickListener);
    return view;
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
        
      }
      else if (v.getId() == containerMap.getId())
      {
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
}
