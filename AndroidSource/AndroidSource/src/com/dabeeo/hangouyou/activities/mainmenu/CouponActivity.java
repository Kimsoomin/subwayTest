package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.os.Bundle;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.controllers.mainmenu.CouponViewPagerAdapter;

public class CouponActivity extends BaseNavigationTabActivity
{
  private CouponViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new CouponViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_all_coupons));
    titles.add(getString(R.string.term_my_coupon));
    
    for (String title : titles)
    {
      adapter.add(title);
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(title).setTabListener(tabListener));
    }
    
    adapter.notifyDataSetChanged();
  }
}
