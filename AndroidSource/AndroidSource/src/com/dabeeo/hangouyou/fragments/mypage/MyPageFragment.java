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

public class MyPageFragment extends Fragment
{
  private final static int REQUEST_CODE = 120;
  private ImageView imageCover;
  private RoundedImageView imageProfile;
  private TextView textName;
  
  private Button btnSetting;
  private Button btnMySchedule, btnMyPlace, btnMyTicket, btnMyPhotoLog, btnMyBookmark, btnMyOrders;
  
  private boolean isChangeBackground = false;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_page;
    View view = inflater.inflate(resId, null);
    
    imageCover = (ImageView) view.findViewById(R.id.image_cover);
    imageProfile = (RoundedImageView) view.findViewById(R.id.image_profile);
    imageProfile.setOnClickListener(clickListener);
    
    textName = (TextView) view.findViewById(R.id.text_name);
    btnSetting = (Button) view.findViewById(R.id.btn_setting);
    btnSetting.setOnClickListener(clickListener);
    
    btnMySchedule = (Button) view.findViewById(R.id.btn_my_schedule);
    btnMyPlace = (Button) view.findViewById(R.id.btn_my_place);
    btnMyTicket = (Button) view.findViewById(R.id.btn_my_ticket_and_coupon);
    btnMyPhotoLog = (Button) view.findViewById(R.id.btn_my_photolog);
    btnMyBookmark = (Button) view.findViewById(R.id.btn_my_bookmark);
    btnMyOrders = (Button) view.findViewById(R.id.btn_my_order_and_cart);
    
    btnMySchedule.setOnClickListener(menuClickListener);
    btnMyPlace.setOnClickListener(menuClickListener);
    btnMyTicket.setOnClickListener(menuClickListener);
    btnMyPhotoLog.setOnClickListener(menuClickListener);
    btnMyPhotoLog.setVisibility(View.GONE);
    btnMyBookmark.setOnClickListener(menuClickListener);
    btnMyOrders.setOnClickListener(menuClickListener);
    
    return view;
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
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnMySchedule.getId())
      {
        startActivity(new Intent(getActivity(), MySchedulesActivity.class));
      }
      else if (v.getId() == btnMyPlace.getId())
      {
        startActivity(new Intent(getActivity(), MyPlaceActivity.class));
      }
      else if (v.getId() == btnMyTicket.getId())
      {
        Toast.makeText(getActivity(), "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == btnMyPhotoLog.getId())
      {
        startActivity(new Intent(getActivity(), MyPhotoLogListActivity.class));
      }
      else if (v.getId() == btnMyBookmark.getId())
      {
        startActivity(new Intent(getActivity(), MyBookmarkActivity.class));
      }
      else if (v.getId() == btnMyOrders.getId())
      {
        Toast.makeText(getActivity(), "준비중입니다", Toast.LENGTH_LONG).show();
      }
    }
  };
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == imageProfile.getId())
      {
        CharSequence[] menus = new CharSequence[2];
        menus[0] = getString(R.string.term_change_background);
        menus[1] = getString(R.string.term_change_profile);
        
        Builder builder = new AlertDialog.Builder(getActivity());
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
      else if (v.getId() == btnSetting.getId())
      {
        startActivity(new Intent(getActivity(), MyPageSettingActivity.class));
      }
    }
  };
}
