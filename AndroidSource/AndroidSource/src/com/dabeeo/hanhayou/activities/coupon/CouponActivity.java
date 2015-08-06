package com.dabeeo.hanhayou.activities.coupon;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.travel.TravelSchedulesActivity;
import com.dabeeo.hanhayou.bases.BaseNavigationTabActivity;
import com.dabeeo.hanhayou.controllers.coupon.CouponViewPagerAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;

public class CouponActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private CouponViewPagerAdapter adapter;
  
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private LinearLayout containerBottomTab;
  
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coupon);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_coupon));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    
    containerBottomTab = (LinearLayout) findViewById(R.id.container_bottom_tab);
    bottomMenuHome = (LinearLayout) findViewById(R.id.container_menu_home);
    bottomMenuMyPage = (LinearLayout) findViewById(R.id.container_menu_mypage);
    bottomMenuPhotolog = (LinearLayout) findViewById(R.id.container_menu_photolog);
    bottomMenuWishList = (LinearLayout) findViewById(R.id.container_menu_wishlist);
    bottomMenuSearch = (LinearLayout) findViewById(R.id.container_menu_search);
    bottomMenuHome.setOnClickListener(bottomMenuClickListener);
    bottomMenuMyPage.setOnClickListener(bottomMenuClickListener);
    bottomMenuPhotolog.setOnClickListener(bottomMenuClickListener);
    bottomMenuWishList.setOnClickListener(bottomMenuClickListener);
    bottomMenuSearch.setOnClickListener(bottomMenuClickListener);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setOffscreenPageLimit(100);
    
    adapter = new CouponViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  public void setViewPagerPosition(int position)
  {
    viewPager.setCurrentItem(position);
    getSupportActionBar().setSelectedNavigationItem(position);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener bottomMenuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == bottomMenuHome.getId())
      {
        finish();
      }
      else if (v.getId() == bottomMenuMyPage.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(CouponActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_MY_PAGE);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(CouponActivity.this).showNeedLoginDialog(1);
        }
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(CouponActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_WISHLIST);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(CouponActivity.this).showNeedLoginDialog(2);
        }
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        Intent i = new Intent(CouponActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_SEARCH);
        startActivity(i);
      }
    }
  };
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_all_coupons));
    titles.add(getString(R.string.term_downloaded_coupon));
    
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
    
    if (resultCode == RESULT_OK)
      getSupportActionBar().setSelectedNavigationItem(1);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  protected TabListener tabListener = new TabListener()
  {
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
      viewPager.setCurrentItem(tab.getPosition());
    }
    
    
    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
    
    
    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
  };
  
  protected ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener()
  {
    @Override
    public void onPageSelected(int position)
    {
      getSupportActionBar().setSelectedNavigationItem(position);
    }
  };
}
