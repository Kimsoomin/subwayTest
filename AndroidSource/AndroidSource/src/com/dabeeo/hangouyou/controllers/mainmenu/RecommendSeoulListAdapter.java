package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PremiumBean;
import com.squareup.picasso.Picasso;

public class RecommendSeoulListAdapter extends BaseAdapter
{
	private ArrayList<PremiumBean> beans = new ArrayList<>();
	
	
	public void add(PremiumBean bean)
	{
		this.beans.add(bean);
		notifyDataSetChanged();
	}
	
	
	public void addAll(ArrayList<PremiumBean> beans)
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
	
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		PremiumBean bean = (PremiumBean) beans.get(position);
		int resId = R.layout.list_item_recommend_seoul;
		View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView category = (TextView) view.findViewById(R.id.category);
		TextView likeCount = (TextView) view.findViewById(R.id.like_count);
		
		title.setText(bean.title);
//    category.setText(bean.category);
		likeCount.setText(Integer.toString(bean.likeCount));
		
		Picasso.with(parent.getContext()).load(bean.imageUrl).fit().centerCrop().into(imageView);
		return view;
	}
}
