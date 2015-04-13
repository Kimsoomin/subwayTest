package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.fragments.mainmenu.PlaceListFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.TravelStrategyListFragment;

public class RecommendSeoulViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<TitleCategoryBean> items = new ArrayList<>();
  
  
  public RecommendSeoulViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void add(TitleCategoryBean bean)
  {
    items.add(bean);
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    if (position == 0)
      return new TravelStrategyListFragment(items.get(position).categoryId);
    else
      return new PlaceListFragment(items.get(position).categoryId);
  }
  
  
  @Override
  public int getCount()
  {
    return items.size();
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    return items.get(position).title;
  }
}