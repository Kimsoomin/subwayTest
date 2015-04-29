package com.dabeeo.hangouyou.activities.travel;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleViewPagerAdapter;

public class TravelSchedulesActivity extends BaseNavigationTabActivity
{
  private TravelScheduleViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new TravelScheduleViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_popular_schedule));
    titles.add(getString(R.string.term_registered_schedule));
    titles.add(getString(R.string.term_bookmarked_schedule));
    
    adapter.setTitles(titles);
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
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
    return super.onOptionsItemSelected(item);
  }
}
