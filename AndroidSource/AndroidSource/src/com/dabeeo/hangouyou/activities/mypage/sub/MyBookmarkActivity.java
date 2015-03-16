package com.dabeeo.hangouyou.activities.mypage.sub;

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
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.mypage.MyBookmarkViewPagerAdapter;

public class MyBookmarkActivity extends ActionBarActivity implements TabListener
{
  private ProgressBar progressBar;
  private MenuItem editMenuItem, closeMenuItem;
  private boolean isEditMode = false;
  private ViewPager viewPager;
  private MyBookmarkViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_bookmark);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new MyBookmarkViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(100);
    
    displayTitles();
  }
  
  
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add("장소");
    titles.add("일정");
    titles.add("포토로그");
    titles.add("추천서울");
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
    getMenuInflater().inflate(R.menu.menu_edit, menu);
    editMenuItem = menu.findItem(R.id.edit);
    closeMenuItem = menu.findItem(R.id.close);
    editMenuItem.setVisible(!isEditMode);
    closeMenuItem.setVisible(isEditMode);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    else if (item.getItemId() == editMenuItem.getItemId())
    {
      isEditMode = true;
      invalidateOptionsMenu();
    }
    else if (item.getItemId() == closeMenuItem.getItemId())
    {
      isEditMode = false;
      invalidateOptionsMenu();
    }
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
