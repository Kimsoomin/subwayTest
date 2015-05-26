package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;

public class SharePickView extends RelativeLayout
{
	private Context context;
	
	public ImageView btnWechat, btnQQ, btnWeibo;
	
	
	public SharePickView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public SharePickView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public SharePickView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void init()
	{
		int resId = R.layout.view_share;
		View view = LayoutInflater.from(context).inflate(resId, null);
		
		btnQQ = (ImageView) view.findViewById(R.id.btn_qq);
		btnWechat = (ImageView) view.findViewById(R.id.btn_wechat);
		btnWeibo = (ImageView) view.findViewById(R.id.btn_weibo);
		addView(view);
	}
}
