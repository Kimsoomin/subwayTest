package com.dabeeo.hangouyou.activities.coupon;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.controllers.coupon.CouponViewPagerAdapter;

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
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    
    // 쿠폰 상세에서 다운로드 버튼 누른 다음에 나오는 팝업창에서 쿠폰 내역보기를 선택했을 때 할 일
    getSupportActionBar().setSelectedNavigationItem(1);
  }
}
