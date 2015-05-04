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
import com.dabeeo.hangouyou.beans.TicketBean;

public class DetailTicketView extends RelativeLayout
{
	private Context context;
	private TicketBean bean;
	private ImageView imageView;
	private TextView title, description;
	
	
	public DetailTicketView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public DetailTicketView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public DetailTicketView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setBean(TicketBean bean)
	{
		this.bean = bean;
		title.setText(bean.title);
	}
	
	
	public void init()
	{
		int resId = R.layout.view_detail_ticket;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		imageView = (ImageView) view.findViewById(R.id.image_view);
		title = (TextView) view.findViewById(R.id.title);
		description = (TextView) view.findViewById(R.id.description);
		
		addView(view);
	}
}
