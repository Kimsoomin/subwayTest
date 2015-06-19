package com.dabeeo.hanhayou.controllers.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.fragments.mainmenu.MyBookmarkPlaceListFragment;
import com.dabeeo.hanhayou.fragments.mainmenu.MyBookmarkTravelScheduleListFragment;

public class MyBookmarkViewPagerAdapter extends FragmentPagerAdapter
{
  private List<String> titles = new ArrayList<String>();
  private HashMap<Integer, Fragment> pageReferenceMap = new HashMap<Integer, Fragment>();
  
  
  public MyBookmarkViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void setTitles(ArrayList<String> titles)
  {
    this.titles.addAll(titles);
    notifyDataSetChanged();
  }
  
  
  public Fragment getFragment(int key)
  {
    return pageReferenceMap.get(key);
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = null;
    if (position == 0)
      fragment = new MyBookmarkPlaceListFragment();
    else
      fragment = new MyBookmarkTravelScheduleListFragment();
    pageReferenceMap.put(position, fragment);
    return fragment;
  }
  
  
  @Override
  public int getCount()
  {
    return titles.size();
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    return titles.get(position);
  }
}