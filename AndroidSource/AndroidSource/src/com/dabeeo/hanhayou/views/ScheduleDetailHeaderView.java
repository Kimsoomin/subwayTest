package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpJustOneActivity;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class ScheduleDetailHeaderView extends RelativeLayout
{
	private Context context;
	private ImageView imageView;
	
	
	public ScheduleDetailHeaderView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public ScheduleDetailHeaderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public ScheduleDetailHeaderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setData(final String imageUrl, String title, int dayCount, String budget)
	{
		ImageDownloader.displayImage(context, imageUrl, imageView, null);
//		imageView.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(context, ImagePopUpJustOneActivity.class);
//				i.putExtra("image_url", imageUrl);
//				context.startActivity(i);
//			}
//		});
		
		ImageDownloader.displayImage(context, imageUrl, imageView, null);
	}
	
	
	public void init()
	{
		int resId = R.layout.view_schedule_detail_header;
		View view = LayoutInflater.from(context).inflate(resId, null);
		
		imageView = (ImageView) view.findViewById(R.id.imageview);
		addView(view);
	}
}
