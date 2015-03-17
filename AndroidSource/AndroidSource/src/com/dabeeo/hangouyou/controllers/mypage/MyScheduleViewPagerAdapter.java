package com.dabeeo.hangouyou.controllers.mypage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.fragments.mypage.MyScheduleDetailFragment;

public class MyScheduleViewPagerAdapter extends FragmentPagerAdapter
{
  public static int pos = 0;
  private List<String> titles = new ArrayList<String>();
  
  
  public MyScheduleViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
    
  }
  
  
  public void setTitles(ArrayList<String> titles)
  {
    this.titles.addAll(titles);
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = new MyScheduleDetailFragment();
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
    setPos(position);
    String PageTitle = titles.get(position);
    return PageTitle;
  }
  
  
  public static int getPos()
  {
    return pos;
  }
  
  
  public static void setPos(int pos)
  {
    MyScheduleViewPagerAdapter.pos = pos;
  }
}