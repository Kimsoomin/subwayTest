package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.fragments.mainmenu.TravelScheduleListFragment;

public class TravelScheduleViewPagerAdapter extends FragmentPagerAdapter
{
  private List<String> titles = new ArrayList<String>();
  private ArrayList<Fragment> items = new ArrayList<>();
  
  public TravelScheduleViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void add(String title)
  {
    titles.add(title);
  }
  
  public void addFragement(Fragment fragment)
  {
    items.add(fragment);
  }
  
  @Override
  public Fragment getItem(int position)
  {
    items.get(position);
    if (position == 0)
      ((TravelScheduleListFragment) items.get(position)).setType(TravelScheduleListFragment.SCHEDULE_TYPE_POPULAR);
    else if (position == 1)
      ((TravelScheduleListFragment) items.get(position)).setType(TravelScheduleListFragment.SCHEDULE_TYPE_MY);
    else
      ((TravelScheduleListFragment) items.get(position)).setType(TravelScheduleListFragment.SCHEDULE_TYPE_BOOKMARK);
    return items.get(position);
  }
  
  
  @Override
  public int getCount()
  {
    return titles.size();
  }
}