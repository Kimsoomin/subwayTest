package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;

import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.fragments.trends.TrendProductimageFragment;

public class TrendProductImageViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<String> imageUrls = new ArrayList<>();
  private Context context;
  
  
  public TrendProductImageViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
    this.context = context;
  }
  
  
  public void addAll(ArrayList<String> imageUrl)
  {
    this.imageUrls.addAll(imageUrl);
    notifyDataSetChanged();
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    TrendProductimageFragment fragment = new TrendProductimageFragment();
    fragment.setImageUrl(imageUrls, imageUrls.get(position));
    return fragment;
  }
  
  
  @Override
  public int getCount()
  {
    return this.imageUrls.size();
  }
  
  
  @Override
  public CharSequence getPageTitle(int position)
  {
    return "";
  }
}