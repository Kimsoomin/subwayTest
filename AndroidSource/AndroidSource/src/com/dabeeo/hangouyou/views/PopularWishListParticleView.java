package com.dabeeo.hangouyou.views;

import java.util.Random;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;

public class PopularWishListParticleView extends RelativeLayout
{
	
	private Context context;
	private String firstBean;
	private String secondBean;
	private int position;
	
	
	public PopularWishListParticleView(Context context)
	{
		super(context);
		this.context = context;
	}
	
	
	public void setBean(int position, String firstBean, String secondBean)
	{
		this.position = position;
		this.firstBean = firstBean;
		this.secondBean = secondBean;
		init();
	}
	
	
	public void init()
	{
		int resId = R.layout.view_popular_wishlist_view;
		View view = LayoutInflater.from(context).inflate(resId, null);
		
		TextView leftText = (TextView) view.findViewById(R.id.text_left);
		TextView rightText = (TextView) view.findViewById(R.id.text_right);
		
		leftText.setText(firstBean);
		rightText.setText(secondBean);
		
		if (position % 2 == 0)
		{
			leftText.setBackgroundResource(R.drawable.yellow_rectangle);
			rightText.setBackgroundResource(R.drawable.blue_rectangle);
		}
		else
		{
			leftText.setBackgroundResource(R.drawable.blue_rectangle);
			rightText.setBackgroundResource(R.drawable.red_rectangle);
		}
		
		addView(view);
	}
}
