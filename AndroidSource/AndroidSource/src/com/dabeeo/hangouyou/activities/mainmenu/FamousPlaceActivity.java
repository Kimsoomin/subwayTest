package com.dabeeo.hangouyou.activities.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceActivity;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mainmenu.FamousPlaceViewPagerAdapter;

public class FamousPlaceActivity extends BaseNavigationTabActivity
{
  private FamousPlaceViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    adapter = new FamousPlaceViewPagerAdapter(this, getSupportFragmentManager());
    TitleCategoryBean bean = new TitleCategoryBean("전체", -1);
    adapter.add(bean);
    viewPager.setAdapter(adapter);
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_my_place, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.my_place)
      startActivity(new Intent(FamousPlaceActivity.this, MyPlaceActivity.class));
    return super.onOptionsItemSelected(item);
  }
  
}
