package com.dabeeo.hangouyou.activities.mypage.sub;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;

public class MyScheduleDetailActivity extends TravelScheduleDetailActivity
{
	private LinearLayout containerIsPublicPopup;
	private RelativeLayout containerPublic, containerPrivate;
	private View background;
	private TextView btnCancelIsPublic;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		adapter.setIsMySchedule(true);
		
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
	
}
