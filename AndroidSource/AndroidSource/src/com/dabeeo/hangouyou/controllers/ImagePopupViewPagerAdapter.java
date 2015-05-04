package com.dabeeo.hangouyou.controllers;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dabeeo.hangouyou.views.PinchImageView;

public class ImagePopupViewPagerAdapter extends PagerAdapter
{
  private Activity context;
  private ArrayList<String> pictures = new ArrayList<String>();
  
  
  public ImagePopupViewPagerAdapter(Activity c)
  {
    super();
    this.context = c;
  }
  
  
  public ImagePopupViewPagerAdapter(Activity c, boolean isNameVisible)
  {
    super();
    this.context = c;
  }
  
  
  public void clear()
  {
    pictures.clear();
  }
  
  
  public void addItem(ArrayList<String> data)
  {
    pictures.clear();
    pictures.addAll(data);
    notifyDataSetChanged();
  }
  
  
  @Override
  public int getCount()
  {
    return pictures.size();
  }
  
  
  @Override
  public int getItemPosition(Object object)
  {
    return POSITION_NONE;
  }
  
  
  @Override
  public Object instantiateItem(View pager, final int position)
  {
    View view = new PinchImageView(context, pictures.get(position));
    ((ViewPager) pager).addView(view, 0);
    return view;
  }
  
  
  @Override
  public void destroyItem(View pager, int position, Object view)
  {
    ((ViewPager) pager).removeView((View) view);
  }
  
  
  @Override
  public boolean isViewFromObject(View pager, Object obj)
  {
    return pager == obj;
  }
  
  
  @Override
  public void restoreState(Parcelable arg0, ClassLoader arg1)
  {
  }
  
  
  @Override
  public Parcelable saveState()
  {
    return null;
  }
  
  
  @Override
  public void startUpdate(View arg0)
  {
  }
  
  
  @Override
  public void finishUpdate(View arg0)
  {
  }
}
