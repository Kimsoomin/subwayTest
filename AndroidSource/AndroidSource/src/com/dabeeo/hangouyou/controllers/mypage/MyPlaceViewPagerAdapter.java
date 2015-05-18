package com.dabeeo.hangouyou.controllers.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.fragments.mypage.MyPlaceListFragment;

public class MyPlaceViewPagerAdapter extends FragmentPagerAdapter
{
  private List<String> titles = new ArrayList<String>();
  private HashMap<Integer, MyPlaceListFragment> pageReferenceMap = new HashMap<Integer, MyPlaceListFragment>();
  
  
  public MyPlaceViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public MyPlaceListFragment getFragment(int key)
  {
    return pageReferenceMap.get(key);
  }
  
  
  public void setTitles(ArrayList<String> titles)
  {
    this.titles.addAll(titles);
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = new MyPlaceListFragment();
    ((MyPlaceListFragment) fragment).setCategoryId(-1);
    pageReferenceMap.put(position, ((MyPlaceListFragment) fragment));
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