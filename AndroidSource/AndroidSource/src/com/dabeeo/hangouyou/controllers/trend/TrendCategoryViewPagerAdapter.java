package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hangouyou.beans.TrendSubCategoryBean;
import com.dabeeo.hangouyou.fragments.trends.TrendCategoryListFragment;

public class TrendCategoryViewPagerAdapter extends FragmentPagerAdapter
{
  private List<TrendSubCategoryBean> beans = new ArrayList<TrendSubCategoryBean>();
  
  
  public TrendCategoryViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
    
  }
  
  
  public void setBeans(ArrayList<TrendSubCategoryBean> beans)
  {
    this.beans.addAll(beans);
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = new TrendCategoryListFragment();
    return fragment;
  }
  
  
  @Override
  public int getCount()
  {
    return beans.size();
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    return beans.get(position).title;
  }
  
}