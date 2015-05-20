package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class TrendProductCategoryView extends RelativeLayout
{
	private Context context;
	
	private TextView leftTitle, rightTitle;
	public LinearLayout leftContainer, rightContainer;
	
	
	public TrendProductCategoryView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public TrendProductCategoryView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public TrendProductCategoryView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setData(String categoryTitle, String categoryTitle2)
	{
		leftTitle.setText(categoryTitle);
		rightTitle.setText(categoryTitle2);
	}
	
	
	public void init()
	{
		int resId = R.layout.view_product_category;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		leftTitle = (TextView) view.findViewById(R.id.left_title);
		rightTitle = (TextView) view.findViewById(R.id.right_title);
		leftContainer = (LinearLayout) view.findViewById(R.id.container_left);
		rightContainer = (LinearLayout) view.findViewById(R.id.container_right);
		
		addView(view);
	}
}
