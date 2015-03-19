package com.dabeeo.hangouyou.activities.mypage.sub;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.controllers.mypage.MyPlaceViewPagerAdapter;

public class MyPlaceActivity extends BaseNavigationTabActivity
{
  private MenuItem editMenuItem, closeMenuItem;
  private boolean isEditMode = false;
  private MyPlaceViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new MyPlaceViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add("전체");
    titles.add("명소");
    titles.add("쇼핑");
    titles.add("레스토랑");
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
    if (item.getItemId() == editMenuItem.getItemId())
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
}
