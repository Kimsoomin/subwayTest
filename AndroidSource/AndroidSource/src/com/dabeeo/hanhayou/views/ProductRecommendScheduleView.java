package com.dabeeo.hanhayou.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class ProductRecommendScheduleView extends RelativeLayout
{
	private Context context;
	private ProductBean bean;
	private ImageView imageView;
	private TextView title, price, discountPrice, discountPriceCn, btnWishList;
	
	
	public ProductRecommendScheduleView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public ProductRecommendScheduleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public ProductRecommendScheduleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setBean(ProductBean bean)
	{
		this.bean = bean;
		imageView.setImageResource(R.drawable.sample_plan_detail_shopping_list3);
		title.setText(bean.name);
		price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceSale));
		discountPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceDiscount));
		discountPriceCn.setText("("+context.getString(R.string.term_yuan)+"500"+")");
		price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		btnWishList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btnWishList.setActivated(!btnWishList.isActivated());
			}
		});
	}
	
	
	public void init()
	{
		int resId = R.layout.view_product_recommend_in_schedule;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		imageView = (ImageView) view.findViewById(R.id.imageview);
		title = (TextView) view.findViewById(R.id.text_title);
		price = (TextView) view.findViewById(R.id.price);
		discountPrice = (TextView) view.findViewById(R.id.discount_price);
		discountPriceCn = (TextView) view.findViewById(R.id.discount_china_currency);
		btnWishList = (TextView) view.findViewById(R.id.btn_wish_list);
		
		addView(view);
	}
}
