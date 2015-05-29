package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.fragments.trends.TrendProductimageFragment;

public class TrendProductImageViewPagerAdapter extends FragmentPagerAdapter
{
	private ArrayList<String> imageUrls = new ArrayList<>();
	
	
	public TrendProductImageViewPagerAdapter(Context context, FragmentManager fm)
	{
		super(fm);
	}
	
	
	public void addAll(ArrayList<String> imageUrl)
	{
		this.imageUrls.addAll(imageUrl);
		notifyDataSetChanged();
	}
	
	
	@Override
	public Fragment getItem(int position)
	{
		TrendProductimageFragment fragment = new TrendProductimageFragment();
		fragment.setImageUrl(imageUrls.get(position));
		return fragment;
	}
	
	
	@Override
	public int getCount()
	{
		return this.imageUrls.size();
	}
	
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		return "";
	}
}