package com.dabeeo.hangouyou.controllers;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.SearchResultBean;

public class SearchResultAdapter extends BaseAdapter
{
	private ArrayList<SearchResultBean> items = new ArrayList<>();
	
	
	public void add(SearchResultBean bean)
	{
		items.add(bean);
	}
	
	
	public void clear()
	{
		items.clear();
	}
	
	
	public String getJsonStringForParameter(int type)
	{
		JSONArray result = new JSONArray();
		
		for (SearchResultBean bean : items)
		{
			if (bean.type == type && !bean.isTitle)
			{
				result.put(bean.toJsonObject());
			}
		}
		
		return result.toString();
	}
	
	
	@Override
	public int getCount()
	{
		return items.size();
	}
	
	
	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}
	
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		SearchResultBean bean = items.get(position);
		
		int resId = R.layout.list_item_search_result;
		if (bean.isTitle)
			resId = R.layout.list_item_search_result_title;
		convertView = LayoutInflater.from(parent.getContext()).inflate(resId, null);
		
		if (!bean.isTitle)
		{
			View seperator = (View) convertView.findViewById(R.id.seperater);
			if (position == items.size() - 1)
				seperator.setVisibility(View.GONE);
			else
				seperator.setVisibility(View.VISIBLE);
		}
		TextView title = (TextView) convertView.findViewById(android.R.id.text1);
		title.setText(bean.text);
		
		if (bean.isTitle)
		{
			ViewGroup layoutContainer = (ViewGroup) convertView.findViewById(R.id.layout_container);
			TextView moreCount = (TextView) convertView.findViewById(R.id.text_more_count);
			
			if (bean.type == SearchResultBean.TYPE_NORMAL)
			{
				layoutContainer.setClickable(true); // 왜 false일 때가 클릭이 되는걸까?
				
				if (bean.moreCount != 0)
				{
					moreCount.setText("(" + bean.moreCount + ")");
					moreCount.setVisibility(View.VISIBLE);
				}
			}
			else
			{
				if (bean.moreCount > 3)
				{
					TextView more = (TextView) convertView.findViewById(R.id.text_more);
					moreCount.setText("(" + bean.moreCount + ")");
					moreCount.setVisibility(View.VISIBLE);
					more.setVisibility(View.VISIBLE);
				}
				else
				{
					layoutContainer.setClickable(true); // 왜 false일 때가 클릭이 되는걸까?
				}
			}
		}
		
		return convertView;
	}
}
