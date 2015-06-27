package com.dabeeo.hanhayou.activities.sub;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.squareup.picasso.Picasso;

public class PromotionActivity extends Activity
{
	private ImageView imageView;
	private Button btnShowDetail;
	private TextView textCloseAday;
	private ImageView btnClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		imageView = (ImageView) findViewById(R.id.imageview);
		btnShowDetail = (Button) findViewById(R.id.btn_show_detail);
		btnShowDetail.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String url = "http://tourplanb.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		textCloseAday = (TextView) findViewById(R.id.text_close_a_day);
		btnClose = (ImageView) findViewById(R.id.btn_close);
		btnClose.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		textCloseAday.setOnClickListener(new OnClickListener()
		{
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v)
			{
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
				PreferenceManager.getInstance(PromotionActivity.this).setDontShowPopupDate(formatter.format(date));
				finish();
			}
		});
		
		Picasso.with(PromotionActivity.this).load("https://ununsplash.imgix.net/photo-1428976343495-f2c66e701b2b?q=75&fm=jpg&w=1080&fit=max&s=9c3b5137c75d9d462fe2958edd646cd5").into(imageView);
	}
}
