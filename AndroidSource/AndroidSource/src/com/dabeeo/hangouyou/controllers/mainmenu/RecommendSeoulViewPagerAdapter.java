package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RecommendSeoulViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<Fragment> items = new ArrayList<>();
  public int currentPosition = 0;
  
  
  public RecommendSeoulViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void add(Fragment fragment)
  {
    items.add(fragment);
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    return items.get(position);
//    if (position == 0)
//      return new TravelStrategyListFragment(items.get(position).categoryId);
//    else
//      return new PlaceListFragment(items.get(position).categoryId);
  }
  
  
  @Override
  public int getCount()
  {
    return items.size();
  }
}