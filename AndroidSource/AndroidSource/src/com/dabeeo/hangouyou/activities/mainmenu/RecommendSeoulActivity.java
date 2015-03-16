package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.mainmenu.RecommendSeoulViewPagerAdapter;

public class RecommendSeoulActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private RecommendSeoulViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_seoul);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new RecommendSeoulViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(100);
    
    displayTitles();
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add("전체");
    titles.add("홍대");
    titles.add("명동");
    titles.add("종로");
    titles.add("인사동");
    titles.add("삼청동");
    titles.add("압구정");
    titles.add("강남");
    titles.add("한류지역");
    adapter.setTitles(titles);
    
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    viewPager.setOnPageChangeListener(pageChangeListener);
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private TabListener tabListener = new TabListener()
  {
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {
    }
    
    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
      viewPager.setCurrentItem(tab.getPosition());
    }
    
    
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {
    }
  };
  
  private ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener()
  {
    @Override
    public void onPageSelected(int position)
    {
      getSupportActionBar().setSelectedNavigationItem(position);
    }
  };
}
