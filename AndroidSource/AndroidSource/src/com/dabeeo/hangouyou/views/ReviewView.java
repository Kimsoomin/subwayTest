package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ReviewBean;

public class ReviewView extends RelativeLayout
{
	private Context context;
	private ReviewBean bean;
	
	
	public ReviewView(Context context)
	{
		super(context);
		this.context = context;
	}
	
	
	public ReviewView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	
	public ReviewView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
	}
	
	
	public void setBean(ReviewBean bean)
	{
		this.bean = bean;
		init();
	}
	
	
	public void init()
	{
		int resId = R.layout.view_place_review;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView time = (TextView) view.findViewById(R.id.time);
		TextView content = (TextView) view.findViewById(R.id.content);
		TextView reviewScore = (TextView) view.findViewById(R.id.text_review_score);
		ImageView btnMore = (ImageView) view.findViewById(R.id.btn_review_list_more);
		
		name.setText("planb");
		time.setText("2015.01.01 16:53");
		reviewScore.setText("5.0");
		content.setText("좋네요");
//		if (bean != null)
//		{
//			name.setText(bean.userName);
//			content.setText(bean.content);
//		}
		addView(view);
	}
}
