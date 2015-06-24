package com.dabeeo.hanhayou.activities.mypage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class MyScheduleDetailActivity extends TravelScheduleDetailActivity
{
  private LinearLayout containerIsPublicPopup;
  private RelativeLayout containerPublic, containerPrivate;
  private View background;
  private TextView btnCancelIsPublic;
  private ApiClient apiClient;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
//		adapter.setIsMySchedule(true);
    
    super.isMySchedule = true;
    apiClient = new ApiClient(this);
    containerIsPublicPopup = (LinearLayout) findViewById(R.id.container_set_public);
    containerPublic = (RelativeLayout) findViewById(R.id.container_public);
    containerPrivate = (RelativeLayout) findViewById(R.id.container_private);
    background = (View) findViewById(R.id.background);
    btnCancelIsPublic = (TextView) findViewById(R.id.btn_cancel_set_is_public);
    containerPublic.setActivated(true);
  }
  
  
  @Override
  protected void onResume()
  {
    super.containerWriteReview.setVisibility(View.GONE);
    super.containerLike.setVisibility(View.GONE);
    super.containerIsPublic.setVisibility(View.VISIBLE);
    super.btnIsPublic.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(MyScheduleDetailActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        containerIsPublicPopup.setVisibility(View.VISIBLE);
        containerIsPublicPopup.bringToFront();
        containerPublic.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            containerIsPublicPopup.setVisibility(View.GONE);
            containerPublic.setActivated(true);
            containerPrivate.setActivated(false);
            btnIsPublic.setActivated(true);
            //공개로
            new OpenAsyncTask().execute("1");
          }
        });
        containerPrivate.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            containerIsPublicPopup.setVisibility(View.GONE);
            containerPublic.setActivated(false);
            containerPrivate.setActivated(true);
            btnIsPublic.setActivated(false);
            //비공개로
            new OpenAsyncTask().execute("0");
          }
        });
        background.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            containerIsPublicPopup.setVisibility(View.GONE);
          }
        });
        btnCancelIsPublic.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            containerIsPublicPopup.setVisibility(View.GONE);
          }
        });
      }
    });
    super.onResume();
  }
  
  /**
   * AsyncTask
   */
  private class OpenAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.requestOpenMySchedule(bean.idx, params[0]);
    }
  }
}
