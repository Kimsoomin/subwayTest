/******************************************************************************
 * Copyright (C) Cambridge Silicon Radio Limited 2014 This software is provided
 * to the customer for evaluation purposes only and, as such early feedback on
 * performance and operation is anticipated. The software source code is subject
 * to change and not intended for production. Use of developmental release
 * software is at the user's own risk. This software is provided "as is," and
 * CSR cautions users to determine for themselves the suitability of using the
 * beta release version of this software. CSR makes no warranty or
 * representation whatsoever of merchantability or fitness of the product for
 * any particular purpose or use. In no event shall CSR be liable for any
 * consequential, incidental or special damages whatsoever arising out of the
 * use of or inability to use this software, even if the user has advised CSR of
 * the possibility of such damages.
 ******************************************************************************/

package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Activity;
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

/**
 * Fragment that allows controlling the colour of lights using HSV colour wheel.
 */
public class MainFragment extends Fragment
{
  private View view;
  private RelativeLayout containerRecommandSeoul, containerFamousPlace, containerTravelSchedule, containerShoppingMall, containerMap, containerSubway, containerPhotolog, containerTicketAndCoupon,
      containerReservationHotel;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    
    return view;
  }
  
  
  @Override
  public void onAttach(final Activity activity)
  {
    super.onAttach(activity);
    if (view == null)
    {
      int resId = R.layout.fragment_main_menu;
      view = LayoutInflater.from(activity).inflate(resId, null);
    }
    
    containerRecommandSeoul = (RelativeLayout) view.findViewById(R.id.container_recommend_seoul);
    containerFamousPlace = (RelativeLayout) view.findViewById(R.id.container_famous_place);
    containerTravelSchedule = (RelativeLayout) view.findViewById(R.id.container_travel_schedule);
    containerShoppingMall = (RelativeLayout) view.findViewById(R.id.container_shopping_mall);
    containerMap = (RelativeLayout) view.findViewById(R.id.container_map);
    containerSubway = (RelativeLayout) view.findViewById(R.id.container_subway);
    containerPhotolog = (RelativeLayout) view.findViewById(R.id.container_photo_log);
    containerTicketAndCoupon = (RelativeLayout) view.findViewById(R.id.container_ticket_and_coupon);
    containerReservationHotel = (RelativeLayout) view.findViewById(R.id.container_reservation_hotel);
    
    containerRecommandSeoul.setOnClickListener(menuClickListener);
    containerFamousPlace.setOnClickListener(menuClickListener);
    containerTravelSchedule.setOnClickListener(menuClickListener);
    containerShoppingMall.setOnClickListener(menuClickListener);
    containerMap.setOnClickListener(menuClickListener);
    containerSubway.setOnClickListener(menuClickListener);
    containerPhotolog.setOnClickListener(menuClickListener);
    containerTicketAndCoupon.setOnClickListener(menuClickListener);
    containerReservationHotel.setOnClickListener(menuClickListener);
  }
  
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == containerRecommandSeoul.getId())
      {
        
      }
      else if (v.getId() == containerFamousPlace.getId())
      {
        startActivity(new Intent(getActivity(), FamousPlaceActivity.class));
      }
      else if (v.getId() == containerTravelSchedule.getId())
      {
        
      }
      else if (v.getId() == containerShoppingMall.getId())
      {
        
      }
      else if (v.getId() == containerMap.getId())
      {
        
      }
      else if (v.getId() == containerSubway.getId())
      {
        
      }
      else if (v.getId() == containerPhotolog.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행 
      }
      else if (v.getId() == containerTicketAndCoupon.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행 
      }
      else if (v.getId() == containerReservationHotel.getId())
      {
        //네트워크 연결 체크 후 연결했을 때만 실행 
      }
    }
  };
}
