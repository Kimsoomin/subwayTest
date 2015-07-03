package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.fragments.mainmenu.TravelScheduleDetailFragment;

public class TravelScheduleDetailViewPagerAdapter extends FragmentPagerAdapter
{
  private ScheduleDetailBean bean = new ScheduleDetailBean();
  private Context context;
  private boolean isMySchedule = false;
  private boolean isRecommendSchedule = false;
  
  private HashMap<Integer, Fragment> framgents = new HashMap<Integer, Fragment>();
  
  
  public TravelScheduleDetailViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
    this.context = context;
  }
  
  
  public void setLikeCountUpAndDown(boolean isUp)
  {
    if (isUp)
      this.bean.likeCount++;
    else
      this.bean.likeCount--;
    
    for (Entry<Integer, Fragment> entry : framgents.entrySet())
    {
      Fragment value = entry.getValue();
      ((TravelScheduleDetailFragment) value).titleView.reloadLikeCount(this.bean.likeCount);
    }
    notifyDataSetChanged();
  }
  
  
  public void setIsMySchedule(boolean isMySchedule)
  {
    this.isMySchedule = isMySchedule;
  }
  
  
  public void setIsRecommendSchedule(boolean isRecommendSchedule)
  {
    this.isRecommendSchedule = isRecommendSchedule;
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
      ((TravelScheduleDetailFragment) fragment).setBean(position, this.bean, null, isMySchedule, isRecommendSchedule);
    else
      ((TravelScheduleDetailFragment) fragment).setBean(position, this.bean, bean.days.get(position - 1), isMySchedule, isRecommendSchedule);
    
    framgents.put(position, fragment);
    return fragment;
  }
  
  
  @Override
  public int getCount()
  {
    try
    {
      return bean.days.size() + 1;
    }
    catch (Exception e)
    {
      return 0;
    }
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    if (position == 0)
      return context.getString(R.string.term_all);
    else
    {
      if (Locale.getDefault().getLanguage().contains("ko"))
        return Integer.toString(position) + context.getString(R.string.term_after_day);
      else
      {
        String dayString = context.getString(R.string.term_after_day);
        dayString = dayString.replace("#1", Integer.toString(position));
        return dayString;
      }
      
    }
  }
}