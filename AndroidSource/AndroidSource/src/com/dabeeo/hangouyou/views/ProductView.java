package com.dabeeo.hangouyou.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class ProductView extends RelativeLayout
{
	private Activity context;
	private ProductBean firstBean;
	private ProductBean secondBean;
	
	
	public ProductView(Activity context)
	{
		super(context);
		this.context = context;
	}
	
	
	public void setBean(ProductBean firstBean, ProductBean secondBean)
	{
		this.firstBean = firstBean;
		this.secondBean = secondBean;
		init();
	}
	
	
	public void init()
	{
		int resId = R.layout.view_product;
		View view = LayoutInflater.from(context).inflate(resId, null);
		
		LinearLayout firstContainer = (LinearLayout) view.findViewById(R.id.conatiner_first_bean);
		ImageView firstImageView = (ImageView) view.findViewById(R.id.imageview);
		TextView firstTitle = (TextView) view.findViewById(R.id.product_title);
		TextView firstOriginalPrice = (TextView) view.findViewById(R.id.original_price);
		TextView firstDiscountPrice = (TextView) view.findViewById(R.id.discount_price);
		
		LinearLayout secondContainer = (LinearLayout) view.findViewById(R.id.conatiner_second_bean);
		ImageView secondImageView = (ImageView) view.findViewById(R.id.imageview_second);
		TextView secondTitle = (TextView) view.findViewById(R.id.product_title_second);
		TextView secondOriginalPrice = (TextView) view.findViewById(R.id.original_price_second);
		TextView secondDiscountPrice = (TextView) view.findViewById(R.id.discount_price_second);
		
		//가데이터
		ImageDownloader.displayImage(context, "", firstImageView, null);
		firstContainer.setVisibility(View.VISIBLE);
		firstTitle.setText("수분크림");
		firstOriginalPrice.setText("¥ 150.00");
		firstDiscountPrice.setText("¥ 93.00");
		firstOriginalPrice.setPaintFlags(firstOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		firstContainer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemUtil.isConnectNetwork(context))
					new AlertDialogManager(context).showDontNetworkConnectDialog();
				else
					context.startActivity(new Intent(context, TrendProductDetailActivity.class));
			}
		});
		secondContainer.setVisibility(View.VISIBLE);
		ImageDownloader.displayImage(context, "", secondImageView, null);
		secondTitle.setText("수분크림");
		secondOriginalPrice.setText("¥ 150.00");
		secondDiscountPrice.setText("¥ 93.00");
		secondOriginalPrice.setPaintFlags(secondOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		secondContainer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (SystemUtil.isConnectNetwork(context))
					new AlertDialogManager(context).showDontNetworkConnectDialog();
				else
					context.startActivity(new Intent(context, TrendProductDetailActivity.class));
			}
		});
		
//    if (firstBean != null)
//    {
//      firstTitle.setText(firstBean.title);
//      firstOriginalPrice.setText("¥ " + firstBean.originalPrice);
//      firstDiscountPrice.setText("¥ " + firstBean.discountPrice);
//      firstOriginalPrice.setPaintFlags(firstOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//    }
		addView(view);
	}
}
