package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MySchedulesActivity;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleViewPagerAdapter;

public class TravelSchedulesActivity extends ActionBarActivity implements TabListener
{
  private ProgressBar progressBar;
  private ViewPager viewPager;
  private TravelScheduleViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_travel_schedules);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new TravelScheduleViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(100);
    
    displayTitles();
  }
  
  
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add("전체");
    titles.add("1일 일정");
    titles.add("2일 일정");
    titles.add("3일 일정");
    titles.add("4일 일정");
    titles.add("5일 일정");
    titles.add("6일 일정");
    adapter.setTitles(titles);
    
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
    {
      @Override
      public void onPageSelected(int position)
      {
        invalidateOptionsMenu();
        getSupportActionBar().setSelectedNavigationItem(position);
      }
    });
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(this));
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_travel_schedules, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    else if (item.getItemId() == R.id.my_schedule)
      startActivity(new Intent(getApplicationContext(), MySchedulesActivity.class));
    return super.onOptionsItemSelected(item);
  }
  
  
  @Override
  public void onTabReselected(Tab arg0, FragmentTransaction arg1)
  {
  }
  
  
  @Override
  public void onTabSelected(Tab tab, FragmentTransaction arg1)
  {
    viewPager.setCurrentItem(tab.getPosition());
  }
  
  
  @Override
  public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
  {
  }
}
