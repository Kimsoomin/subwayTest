package com.dabeeo.hangouyou.controllers.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.managers.CategoryManager;

public class PlaceListAdapter extends BaseAdapter
{
	private ArrayList<PlaceBean> beans = new ArrayList<>();
	private Context context;
	
	
	public PlaceListAdapter(Context context)
	{
		this.context = context;
	}
	
	
	public void add(PlaceBean bean)
	{
		this.beans.add(bean);
		notifyDataSetChanged();
	}
	
	
	public void addAll(ArrayList<PlaceBean> beans)
	{
		this.beans.addAll(beans);
		notifyDataSetChanged();
	}
	
	
	public void clear()
	{
		this.beans.clear();
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount()
	{
		return beans.size();
	}
	
	
	@Override
	public Object getItem(int position)
	{
		return beans.get(position);
	}
	
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	
	@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PlaceBean bean = (PlaceBean) beans.get(position);
		int resId = R.layout.list_item_place;
		View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView isCoupon = (TextView) view.findViewById(R.id.text_coupon);
		TextView createdAt = (TextView) view.findViewById(R.id.created_at);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView category = (TextView) view.findViewById(R.id.category);
		TextView likeCount = (TextView) view.findViewById(R.id.like_count);
		TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
		
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
			String insertDateString = format.format(bean.insertDate);
			createdAt.setText(insertDateString);
		} catch (Exception e)
		{
			createdAt.setVisibility(View.GONE);
		}
		
		title.setText(bean.title);
		category.setText(CategoryManager.getInstance(context).getCategoryName(bean.categoryId));
		likeCount.setText(Integer.toString(bean.likeCount));
		reviewCount.setText(Integer.toString(bean.reviewCount));
		return view;
	}
}
