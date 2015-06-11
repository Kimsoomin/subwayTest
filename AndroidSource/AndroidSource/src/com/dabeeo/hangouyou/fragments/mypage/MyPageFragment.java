package com.dabeeo.hangouyou.fragments.mypage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyBookmarkActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MySchedulesActivity;
import com.dabeeo.hangouyou.activities.sub.PhotoSelectActivity;
import com.dabeeo.hangouyou.activities.sub.SettingActivity;
import com.dabeeo.hangouyou.controllers.NetworkBraodCastReceiver;
import com.dabeeo.hangouyou.external.libraries.RoundedImageView;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class MyPageFragment extends Fragment
{
  private final static int REQUEST_CODE = 120;
  private final static int REQUEST_CODE_SETTING_WHEN_LOGOUT = 121;
//  private ImageView imageCover;
  private RoundedImageView imageProfile;
  private TextView textName;
  private ImageView imageBookmark, imageOrder, imageCart;
  private LinearLayout conatinerMySchedule, conatinerMyPlace, conatinerMySetting, conatinerMyBookmark, conatinerMyOrders, conatinerMyCart;
  
  private boolean isChangeBackground = false;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_page;
    View view = inflater.inflate(resId, null);
    
//    imageCover = (ImageView) view.findViewById(R.id.image_cover);
    imageProfile = (RoundedImageView) view.findViewById(R.id.image_profile);
    imageProfile.setOnClickListener(clickListener);
    
    textName = (TextView) view.findViewById(R.id.text_name);
    
    conatinerMySchedule = (LinearLayout) view.findViewById(R.id.container_my_schedule);
    conatinerMyPlace = (LinearLayout) view.findViewById(R.id.container_my_place);
    conatinerMySetting = (LinearLayout) view.findViewById(R.id.container_my_setting);
    conatinerMyBookmark = (LinearLayout) view.findViewById(R.id.container_my_bookmark);
    conatinerMyOrders = (LinearLayout) view.findViewById(R.id.container_my_order);
    conatinerMyCart = (LinearLayout) view.findViewById(R.id.container_my_cart);
    
    imageBookmark = (ImageView) view.findViewById(R.id.image_my_bookmark);
    imageOrder = (ImageView) view.findViewById(R.id.image_my_order);
    imageCart = (ImageView) view.findViewById(R.id.image_my_cart);
    
    conatinerMySetting.setOnClickListener(clickListener);
    conatinerMySchedule.setOnClickListener(menuClickListener);
    conatinerMyPlace.setOnClickListener(menuClickListener);
    conatinerMyBookmark.setOnClickListener(menuClickListener);
    conatinerMyOrders.setOnClickListener(menuClickListener);
    conatinerMyCart.setOnClickListener(menuClickListener);
    
    return view;
  }
  
  
  @Override
  public void onResume()
  {
    setNetworkOnOffDisplay(SystemUtil.isConnectNetwork(getActivity()));
    
    IntentFilter filter = new IntentFilter(NetworkBraodCastReceiver.ACTION_NETWORK_STATUS_CHANGE);
    getActivity().registerReceiver(receiver, filter);
    super.onResume();
  }
  
  
  @Override
  public void onPause()
  {
    getActivity().unregisterReceiver(receiver);
    super.onPause();
  }
  
  //Network On/Off status change receiver
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      boolean isNetworkConnected = intent.getBooleanExtra("is_network_connected", false);
      setNetworkOnOffDisplay(isNetworkConnected);
    }
  };
  
  
  private void setNetworkOnOffDisplay(boolean isConnected)
  {
    imageBookmark.setImageResource(isConnected ? R.drawable.btn_mypage_bookmark : R.drawable.btn_mypage_bookmark_offline);
    imageOrder.setImageResource(isConnected ? R.drawable.btn_mypage_buylist : R.drawable.btn_mypage_buylist_offline);
    imageCart.setImageResource(isConnected ? R.drawable.btn_mypage_cart : R.drawable.btn_mypage_cart_offline);
  }
  
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK)
      return;
    
    if (requestCode == REQUEST_CODE)
    {
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
//        if (isChangeBackground)
//          imageCover.setImageBitmap(bitmap);
//        else
          if (!isChangeBackground)
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
    else if (requestCode == REQUEST_CODE_SETTING_WHEN_LOGOUT)
    {
      // 로그아웃 하면 홈 탭 선택하기
      ((MainActivity) getActivity()).setFragments(0);
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
      if (v.getId() == conatinerMySchedule.getId())
      {
        startActivity(new Intent(getActivity(), MySchedulesActivity.class));
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == conatinerMyPlace.getId())
      {
        startActivity(new Intent(getActivity(), MyPlaceActivity.class));
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
      else if (v.getId() == conatinerMyBookmark.getId())
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
        else
        {
          startActivity(new Intent(getActivity(), MyBookmarkActivity.class));
          getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
      }
      else if (v.getId() == conatinerMyOrders.getId())
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
        else
          Toast.makeText(getActivity(), "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == conatinerMyCart.getId())
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
        else
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
//        CharSequence[] menus = new CharSequence[2];
//        menus[0] = getString(R.string.term_change_background);
//        menus[1] = getString(R.string.term_change_profile);
//        
//        Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setItems(menus, new DialogInterface.OnClickListener()
//        {
//          public void onClick(DialogInterface dialog, int whichButton)
//          {
//            if (whichButton == 0)
//            {
//              //배경화면 변경
//              isChangeBackground = true;
//              Intent intent = new Intent(getActivity(), PhotoSelectActivity.class);
//              intent.putExtra("can_select_multiple", false);
//              startActivityForResult(intent, REQUEST_CODE);
//            }
//            else if (whichButton == 1)
//            {
//              //프로필사진 변경
//              isChangeBackground = false;
//              Intent intent = new Intent(getActivity(), PhotoSelectActivity.class);
//              intent.putExtra("can_select_multiple", false);
//              startActivityForResult(intent, REQUEST_CODE);
//            }
//          }
//        });
//        builder.create().show();
        isChangeBackground = false;
        Intent intent = new Intent(getActivity(), PhotoSelectActivity.class);
        intent.putExtra("can_select_multiple", false);
        startActivityForResult(intent, REQUEST_CODE);
      }
      else if (v.getId() == conatinerMySetting.getId())
      {
        startActivityForResult(new Intent(getActivity(), SettingActivity.class), REQUEST_CODE_SETTING_WHEN_LOGOUT);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
      }
    }
  };
}
