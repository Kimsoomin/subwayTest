package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class PopularKeywordView extends RelativeLayout
{
	private Context context;
	private TextView position, keyword;
	private View bottomLine;
	
	
	public PopularKeywordView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}
	
	
	public PopularKeywordView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	
	public PopularKeywordView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	public void setData(int position, String keyword)
	{
		this.position.setText(Integer.toString(position));
		this.keyword.setText(keyword);
		
		if (position == 3)
			bottomLine.setVisibility(View.GONE);
	}
	
	
	public void init()
	{
		int resId = R.layout.view_popular_keyword;
		View view = LayoutInflater.from(context).inflate(resId, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		keyword = (TextView) view.findViewById(R.id.text_keyword);
		position = (TextView) view.findViewById(R.id.text_position);
		bottomLine = (View) view.findViewById(R.id.bottom_line);
		addView(view);
	}
}
