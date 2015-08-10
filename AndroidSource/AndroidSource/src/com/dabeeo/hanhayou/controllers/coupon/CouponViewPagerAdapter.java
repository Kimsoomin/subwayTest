package com.dabeeo.hanhayou.controllers.coupon;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.fragments.coupon.CouponListFragment;
import com.dabeeo.hanhayou.fragments.coupon.DownloadedCouponListFragment;

public class CouponViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<String> titles = new ArrayList<String>();
  private CouponListFragment couponListFragment;
  private DownloadedCouponListFragment downloadCouponListFragment;
  
  
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
    {
      if (couponListFragment == null)
        couponListFragment = new CouponListFragment();
      return couponListFragment;
    }
    else
    {
      if (downloadCouponListFragment == null)
        downloadCouponListFragment = new DownloadedCouponListFragment();
      return downloadCouponListFragment;
    }
  }
  
  
  @Override
  public int getCount()
  {
    return titles.size();
  }
}