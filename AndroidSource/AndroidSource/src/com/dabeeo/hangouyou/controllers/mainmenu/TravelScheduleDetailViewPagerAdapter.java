package com.dabeeo.hangouyou.controllers.mainmenu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.dabeeo.hangouyou.beans.ScheduleDetailBean;
import com.dabeeo.hangouyou.fragments.mainmenu.TravelScheduleDetailFragment;

public class TravelScheduleDetailViewPagerAdapter extends FragmentPagerAdapter
{
  private ScheduleDetailBean bean = new ScheduleDetailBean();
  
  
  public TravelScheduleDetailViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void setBean(ScheduleDetailBean bean)
  {
    this.bean = bean;
    Log.w("WARN", "Days : " + bean.days.size());
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = new TravelScheduleDetailFragment();
    if (position == 0)
      ((TravelScheduleDetailFragment) fragment).setBean(position, bean, null);
    else
      ((TravelScheduleDetailFragment) fragment).setBean(position, bean, bean.days.get(position - 1));
    return fragment;
  }
  
  
  @Override
  public int getCount()
  {
    return bean.days.size();
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    if (position == 0)
      return "전체";
    else
      return Integer.toString(position) + "일";
  }
}