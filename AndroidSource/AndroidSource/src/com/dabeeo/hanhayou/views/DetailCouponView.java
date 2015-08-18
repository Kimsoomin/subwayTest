package com.dabeeo.hanhayou.views;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CouponDetailBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class DetailCouponView extends RelativeLayout
{
	private Context context;
	private CouponDetailBean bean;
	private ImageView imageView;
	private TextView title, description;
	
	
	public DetailCouponView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public DetailCouponView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public DetailCouponView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setBean(CouponDetailBean bean)
	{
		this.bean = bean;
		title.setText(bean.title);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		if (bean.isNotimeLimit)
			description.setText(context.getString(R.string.term_validity_period) + " " + context.getString(R.string.term_notimelimit));
		if (bean.isExhaustion)
			description.setText(context.getString(R.string.term_validity_period) + " " + context.getString(R.string.term_exhaustion));
		if (!bean.isExhaustion && !bean.isNotimeLimit)
			description.setText(context.getString(R.string.term_validity_period) + " " + format.format(bean.endDate) + context.getString(R.string.term_enddate_retail));
		
		ImageDownloader.displayCouponImage(context, bean.couponImageUrl, imageView);
	}
	
	
	public void init()
	{
		int resId = R.layout.view_detail_coupon;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		imageView = (ImageView) view.findViewById(R.id.image_view);
		title = (TextView) view.findViewById(R.id.title);
		description = (TextView) view.findViewById(R.id.description);
		
		addView(view);
	}
}
