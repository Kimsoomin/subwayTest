package com.dabeeo.hangouyou.controllers.mainmenu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ScheduleDetailBean;
import com.dabeeo.hangouyou.fragments.mainmenu.TravelScheduleDetailFragment;

public class TravelScheduleDetailViewPagerAdapter extends FragmentPagerAdapter
{
	private ScheduleDetailBean bean = new ScheduleDetailBean();
	private Context context;
	private boolean isMySchedule = false;
	
	
	public TravelScheduleDetailViewPagerAdapter(Context context, FragmentManager fm)
	{
		super(fm);
		this.context = context;
	}
	
	
	public void setIsMySchedule(boolean isMySchedule)
	{
		this.isMySchedule = isMySchedule;
	}
	
	
	public void setBean(ScheduleDetailBean bean)
	{
		this.bean = bean;
		notifyDataSetChanged();
	}
	
	
	@Override
	public Fragment getItem(int position)
	{
		Fragment fragment = new TravelScheduleDetailFragment();
		if (position == 0)
			((TravelScheduleDetailFragment) fragment).setBean(position, this.bean, null, isMySchedule);
		else
			((TravelScheduleDetailFragment) fragment).setBean(position, this.bean, bean.days.get(position - 1), isMySchedule);
		return fragment;
	}
	
	
	@Override
	public int getCount()
	{
		return bean.days.size() + 1;
	}
	
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		if (position == 0)
			return context.getString(R.string.term_all);
		else
			return Integer.toString(position) + context.getString(R.string.term_after_day);
	}
}