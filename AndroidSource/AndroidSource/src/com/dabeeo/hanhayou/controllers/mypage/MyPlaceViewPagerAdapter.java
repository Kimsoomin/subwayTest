package com.dabeeo.hanhayou.controllers.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.beans.TitleCategoryBean;
import com.dabeeo.hanhayou.fragments.mypage.MyPlaceListFragment;

public class MyPlaceViewPagerAdapter extends FragmentPagerAdapter
{
  private List<TitleCategoryBean> items = new ArrayList<TitleCategoryBean>();
  private HashMap<Integer, MyPlaceListFragment> pageReferenceMap = new HashMap<Integer, MyPlaceListFragment>();
  
  
  public MyPlaceViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void clear()
  {
    pageReferenceMap.clear();
  }
  
  
  public void refreshItems()
  {
    for (int i = 0; i < pageReferenceMap.size(); i++)
    {
      pageReferenceMap.get(i).refresh();
    }
  }
  
  
  public void add(TitleCategoryBean bean)
  {
    items.add(bean);
    notifyDataSetChanged();
  }
  
  
  public MyPlaceListFragment getFragment(int key)
  {
    return pageReferenceMap.get(key);
  }
  
  
  public void setTitles(ArrayList<TitleCategoryBean> titles)
  {
    this.items.addAll(titles);
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = new MyPlaceListFragment();
    ((MyPlaceListFragment) fragment).setCategoryId(items.get(position).categoryId);
    pageReferenceMap.put(position, ((MyPlaceListFragment) fragment));
    return fragment;
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