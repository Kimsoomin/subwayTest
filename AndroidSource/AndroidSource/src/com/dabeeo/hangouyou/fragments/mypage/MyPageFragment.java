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

package com.dabeeo.hangouyou.fragments.mypage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyBookmarkActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPhotoLogListActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MySchedulesActivity;
import com.dabeeo.hangouyou.activities.sub.MyPageSettingActivity;
import com.dabeeo.hangouyou.activities.sub.PhotoSelectActivity;
import com.dabeeo.hangouyou.external.libraries.RoundedImageView;

/**
 * Fragment that allows controlling the colour of lights using HSV colour wheel.
 */
public class MyPageFragment extends Fragment
{
  private final static int REQUEST_CODE = 120;
  private Activity activity;
  private View view;
  private ImageView imageCover;
  private RoundedImageView imageProfile;
  private TextView textName;
  
  private Button btnSetting;
  private Button btnMySchedule, btnMyPlace, btnMyTicket, btnMyPhotoLog, btnMyBookmark, btnMyOrders;
  
  private boolean isChangeBackground = false;
  
  
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
    imageProfile = (RoundedImageView) view.findViewById(R.id.image_profile);
    imageProfile.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        CharSequence[] menus = new CharSequence[2];
        menus[0] = activity.getString(R.string.term_change_background);
        menus[1] = activity.getString(R.string.term_change_profile);
        
        Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(menus, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            if (whichButton == 0)
            {
              //배경화면 변경
              isChangeBackground = true;
              Intent intent = new Intent(getActivity(), PhotoSelectActivity.class);
              intent.putExtra("can_select_multiple", false);
              startActivityForResult(intent, REQUEST_CODE);
            }
            else if (whichButton == 1)
            {
              //프로필사진 변경
              isChangeBackground = false;
              Intent intent = new Intent(getActivity(), PhotoSelectActivity.class);
              intent.putExtra("can_select_multiple", false);
              startActivityForResult(intent, REQUEST_CODE);
            }
          }
        });
        builder.create().show();
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
    Log.w("WARN", "ActivityResult");
    if (data != null && data.hasExtra("photos"))
    {
      String[] photos = data.getStringArrayExtra("photos");
      if (photos.length == 0)
        return;
      
      try
      {
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is = null;
        is = new FileInputStream(new File(photos[0]));
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        is = new FileInputStream(new File(photos[0]));
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        if (isChangeBackground)
          imageCover.setImageBitmap(bitmap);
        else
          imageProfile.setImageBitmap(bitmap);
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      
      //Network 통해 서버의 사진 변경
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
        startActivity(new Intent(activity, MyPlaceActivity.class));
      }
      else if (v.getId() == btnMyTicket.getId())
      {
        Toast.makeText(activity, "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == btnMyPhotoLog.getId())
      {
        startActivity(new Intent(activity, MyPhotoLogListActivity.class));
      }
      else if (v.getId() == btnMyBookmark.getId())
      {
        startActivity(new Intent(activity, MyBookmarkActivity.class));
      }
      else if (v.getId() == btnMyOrders.getId())
      {
        Toast.makeText(activity, "준비중입니다", Toast.LENGTH_LONG).show();
      }
    }
  };
}
