package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.FamousPlaceActivity;
import com.dabeeo.hangouyou.activities.mainmenu.RecommendSeoulActivity;
import com.dabeeo.hangouyou.activities.mainmenu.SubwayActivity;
import com.dabeeo.hangouyou.activities.mainmenu.TravelSchedulesActivity;

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
        startActivity(new Intent(getActivity(), RecommendSeoulActivity.class));
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
        
      }
      else if (v.getId() == containerSubway.getId())
      {
        startActivity(new Intent(getActivity(), SubwayActivity.class));
      }
      else if (v.getId() == containerTicketAndCoupon.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행 
      }
      else if (v.getId() == containerReservation.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행 
      }
    }
  };
}
