package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.fragments.mainmenu.PlaceListFragment;

public class FamousPlaceViewPagerAdapter extends FragmentPagerAdapter
{
  public static int pos = 0;
  private ArrayList<TitleCategoryBean> items = new ArrayList<>();
  
  
  public FamousPlaceViewPagerAdapter(Context context, FragmentManager fm)
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