package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.fragments.mainmenu.CouponListFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.MyCouponListFragment;

public class CouponViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<String> titles = new ArrayList<String>();
  
  
  public CouponViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void add(String title)
  {
    titles.add(title);
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    if (position == 0)
      return new CouponListFragment();
    else
      return new MyCouponListFragment();
  }
  
  
  @Override
  public int getCount()
  {
    return titles.size();
  }
}