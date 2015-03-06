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

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlacesActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MySchedulesActivity;
import com.dabeeo.hangouyou.activities.sub.LocalPhotoActivity;
import com.dabeeo.hangouyou.activities.sub.MyPageSettingActivity;

/**
 * Fragment that allows controlling the colour of lights using HSV colour wheel.
 */
public class MyPageFragment extends Fragment
{
  private Activity activity;
  private View view;
  private ImageView imageCover, imageProfile;
  private TextView textName;
  
  private Button btnSetting;
  private Button btnMySchedule, btnMyPlace, btnMyTicket, btnMyPhotoLog, btnMyBookmark, btnMyOrders;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    return view;
  }
  
  
  @Override
  public void onAttach(final Activity activity)
  {
    super.onAttach(activity);
    this.activity = activity;
    if (view == null)
    {
      int resId = R.layout.fragment_my_page;
      view = LayoutInflater.from(activity).inflate(resId, null);
    }
    
    imageCover = (ImageView) view.findViewById(R.id.image_cover);
    imageProfile = (ImageView) view.findViewById(R.id.image_profile);
    imageProfile.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent intent = new Intent(getActivity(), LocalPhotoActivity.class);
        intent.putExtra("can_select_multiple", false);
        startActivityForResult(intent, 1111);
      }
    });
    
    textName = (TextView) view.findViewById(R.id.text_name);
    btnSetting = (Button) view.findViewById(R.id.btn_setting);
    
    btnMySchedule = (Button) view.findViewById(R.id.btn_my_schedule);
    btnMyPlace = (Button) view.findViewById(R.id.btn_my_place);
    btnMyTicket = (Button) view.findViewById(R.id.btn_my_ticket_and_coupon);
    btnMyPhotoLog = (Button) view.findViewById(R.id.btn_my_photolog);
    btnMyBookmark = (Button) view.findViewById(R.id.btn_my_bookmark);
    btnMyOrders = (Button) view.findViewById(R.id.btn_my_order_and_cart);
    
    btnSetting.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(activity, MyPageSettingActivity.class));
      }
    });
    
    btnMySchedule.setOnClickListener(menuClickListener);
    btnMyPlace.setOnClickListener(menuClickListener);
    btnMyTicket.setOnClickListener(menuClickListener);
    btnMyPhotoLog.setOnClickListener(menuClickListener);
    btnMyBookmark.setOnClickListener(menuClickListener);
    btnMyOrders.setOnClickListener(menuClickListener);
  }
  
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK)
      return;
    
    if (data.hasExtra("photos"))
    {
      String[] photos = data.getStringArrayExtra("photos");
      if (photos.length == 0)
        return;
      
      imageProfile.setImageURI(Uri.fromFile(new File(photos[0])));
    }
  }
  
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnMySchedule.getId())
      {
        startActivity(new Intent(activity, MySchedulesActivity.class));
      }
      else if (v.getId() == btnMyPlace.getId())
      {
        startActivity(new Intent(activity, MyPlacesActivity.class));
      }
      else if (v.getId() == btnMyTicket.getId())
      {
        
      }
      else if (v.getId() == btnMyPhotoLog.getId())
      {
        
      }
      else if (v.getId() == btnMyBookmark.getId())
      {
        
      }
      else if (v.getId() == btnMyOrders.getId())
      {
        
      }
    }
  };
}
