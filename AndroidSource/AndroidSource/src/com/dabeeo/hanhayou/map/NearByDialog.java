package com.dabeeo.hanhayou.map;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;

public class NearByDialog extends Dialog implements android.view.View.OnClickListener, OnTouchListener
{

	int type;
	RelativeLayout nearby_popup;
	LinearLayout category_all;
	LinearLayout attraction;
	LinearLayout shopping;
	LinearLayout restaurant;
	LinearLayout subway;
	LinearLayout cosmetic;
	LinearLayout tourinfo;
	LinearLayout drugstore;
	LinearLayout mcdonalds;
	LinearLayout starbucks;
	
	int click = 0;

	public NearByDialog(Context context, int categoryType) 
	{
		super(context);
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.4f;
		getWindow().setAttributes(lpWindow);
		type = categoryType;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nearby_dialog);
		setLayout();
	}

	@Override
	public void dismiss() 
	{
		super.dismiss();
	}

	private void setLayout()
	{
		nearby_popup = (RelativeLayout) findViewById(R.id.nearby_popup);
		category_all = (LinearLayout) findViewById(R.id.category_all);
		attraction = (LinearLayout) findViewById(R.id.attraction_btn);
		shopping = (LinearLayout) findViewById(R.id.shopping_btn);
		restaurant = (LinearLayout) findViewById(R.id.restaurant_btn);
		subway = (LinearLayout) findViewById(R.id.subway_btn);
		cosmetic = (LinearLayout) findViewById(R.id.cosmetic_btn);
		tourinfo = (LinearLayout) findViewById(R.id.tourinfo_btn);
		drugstore = (LinearLayout) findViewById(R.id.drugstore_btn);
		mcdonalds = (LinearLayout) findViewById(R.id.mcdonalds_btn);
		starbucks = (LinearLayout) findViewById(R.id.starbucks_btn);

		category_all.setOnClickListener(this);
		attraction.setOnClickListener(this);
		shopping.setOnClickListener(this);
		restaurant.setOnClickListener(this);
		subway.setOnClickListener(this);
		cosmetic.setOnClickListener(this);
		tourinfo.setOnClickListener(this);
		drugstore.setOnClickListener(this);
		mcdonalds.setOnClickListener(this);
		starbucks.setOnClickListener(this);
		
		category_all.setOnTouchListener(this);
		attraction.setOnTouchListener(this);
		shopping.setOnTouchListener(this);
		restaurant.setOnTouchListener(this);
		subway.setOnTouchListener(this);
		cosmetic.setOnTouchListener(this);
		tourinfo.setOnTouchListener(this);
		drugstore.setOnTouchListener(this);
		mcdonalds.setOnTouchListener(this);
		starbucks.setOnTouchListener(this);

		if(type == -1)
			category_all.setSelected(true);
		else if(type == 1)
			attraction.setSelected(true);
		else if(type == 2)
			shopping.setSelected(true);
		else if(type == 7)
			restaurant.setSelected(true);
		else if(type == 99)
			subway.setSelected(true);
		else if(type == 40)
			cosmetic.setSelected(true);
		else if(type == 60)
			tourinfo.setSelected(true);
		else if(type == 50)
			drugstore.setSelected(true);
		else if(type == 80)
			mcdonalds.setSelected(true);
		else if(type == 70)
			starbucks.setSelected(true);
	}

	private void setlayout()
	{
		category_all.setSelected(false);
		attraction.setSelected(false);
		shopping.setSelected(false);
		restaurant.setSelected(false);
		subway.setSelected(false);
		cosmetic.setSelected(false);
		tourinfo.setSelected(false);
		drugstore.setSelected(false);
		mcdonalds.setSelected(false);
		starbucks.setSelected(false);
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(click == 0)
		{
			setlayout();

			switch (v.getId()) {
			case R.id.category_all:
				BlinkingMap.categoryType = -1;
				break;
			case R.id.attraction_btn:
				BlinkingMap.categoryType = 1;
				break;
			case R.id.shopping_btn:
				BlinkingMap.categoryType = 2;
				break;
			case R.id.restaurant_btn:
				BlinkingMap.categoryType = 7;
				break;
			case R.id.subway_btn:
				BlinkingMap.categoryType = 99;
				break;
			case R.id.cosmetic_btn:
				BlinkingMap.categoryType = 40;
				break;
			case R.id.drugstore_btn:
				BlinkingMap.categoryType = 50;
				break;
			case R.id.tourinfo_btn:
				BlinkingMap.categoryType = 60;
				break;
			case R.id.mcdonalds_btn:
				BlinkingMap.categoryType = 80;
				break;
			case R.id.starbucks_btn:
				BlinkingMap.categoryType = 70;
				break;
			}

			dismiss();
			v.setSelected(true);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			click += event.getPointerCount();
			
		}else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			click -= event.getPointerCount();
		}
		
		if(click >1)
			return true;
		else
			return false;
	}
}
