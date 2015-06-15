package com.dabeeo.hangouyou.activities.mypage.sub;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.mypage.MyBookmarkViewPagerAdapter;
import com.dabeeo.hangouyou.fragments.mainmenu.MyBookmarkPlaceListFragment;
import com.dabeeo.hangouyou.fragments.mainmenu.MyBookmarkTravelScheduleListFragment;

@SuppressWarnings("deprecation")
public class MyBookmarkActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private MenuItem editMenuItem, closeMenuItem;
  public boolean isEditMode = false;
  private MyBookmarkViewPagerAdapter adapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bookmark);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_my_bookmark));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new MyBookmarkViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setOffscreenPageLimit(100);
    
    displayTitles();
  }
  
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_place));
    titles.add(getString(R.string.term_schedule));
    adapter.setTitles(titles);
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
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
      
      int index = viewPager.getCurrentItem();
      Fragment fragment = adapter.getFragment(index);
      if (index == 0)
        ((MyBookmarkPlaceListFragment) fragment).setEditMode(isEditMode);
      else
        ((MyBookmarkTravelScheduleListFragment) fragment).setEditMode(isEditMode);
      getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
      invalidateOptionsMenu();
    }
    else if (item.getItemId() == closeMenuItem.getItemId())
    {
      isEditMode = false;
      int index = viewPager.getCurrentItem();
      Fragment fragment = adapter.getFragment(index);
      if (index == 0)
        ((MyBookmarkPlaceListFragment) fragment).setEditMode(isEditMode);
      else
        ((MyBookmarkTravelScheduleListFragment) fragment).setEditMode(isEditMode);
      getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      invalidateOptionsMenu();
    }
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
