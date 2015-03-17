package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleDetailViewPagerAdapter;

@SuppressWarnings("deprecation")
public class TravelScheduleDetailActivity extends ActionBarActivity implements TabListener
{
  private ViewPager viewPager;
  private TravelScheduleDetailViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_travel_schedule_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new TravelScheduleDetailViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(100);
    
    displayTitles();
  }
  
  
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add("1日");
    titles.add("2日");
    titles.add("3日");
    titles.add("4日");
    titles.add("5日");
    titles.add("6日");
    titles.add("7日");
    titles.add("8日");
    titles.add("9日");
    titles.add("10日");
    titles.add("11日");
    titles.add("12日");
    
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
    getMenuInflater().inflate(R.menu.menu_map, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    if (item.getItemId() == R.id.map)
      Toast.makeText(this, "준비 중입니다", Toast.LENGTH_LONG).show();
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
