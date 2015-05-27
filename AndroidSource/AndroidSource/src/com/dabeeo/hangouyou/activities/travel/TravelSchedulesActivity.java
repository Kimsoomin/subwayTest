package com.dabeeo.hangouyou.activities.travel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.trend.TrendSearchActivity;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleViewPagerAdapter;

@SuppressWarnings("deprecation")
public class TravelSchedulesActivity extends ActionBarActivity
{
  private TravelScheduleViewPagerAdapter adapter;
  private ViewPager viewPager;
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private LinearLayout containerBottomTab;
  private boolean isAnimation = false;
  private float bottomTappx;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_travel_strategy);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_travel_schedule));
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
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setOffscreenPageLimit(100);
    adapter = new TravelScheduleViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  @Override
  public void onBackPressed()
  {
    super.onBackPressed();
    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
  }
  
  
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
    if (item.getItemId() == android.R.id.home)
    {
      finish();
      overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
    return super.onOptionsItemSelected(item);
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
        Intent i = new Intent(TravelSchedulesActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_MY_PAGE);
        startActivity(i);
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        Intent i = new Intent(TravelSchedulesActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_WISHLIST);
        startActivity(i);
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        Intent i = new Intent(TravelSchedulesActivity.this, TrendSearchActivity.class);
        startActivity(i);
      }
    }
  };
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
  
  
  /**
   * Animation Float
   */
  public void showBottomTab(boolean isShow)
  {
    if (isAnimation)
      return;
    
    float density = getResources().getDisplayMetrics().density;
    bottomTappx = 65 * density;
    
    if (isShow)
    {
      //Showing
      if (containerBottomTab.getVisibility() == View.GONE)
      {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, bottomTappx);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new AnimationListener()
        {
          @Override
          public void onAnimationStart(Animation animation)
          {
            containerBottomTab.setVisibility(View.VISIBLE);
            isAnimation = true;
          }
          
          
          @Override
          public void onAnimationRepeat(Animation animation)
          {
            
          }
          
          
          @Override
          public void onAnimationEnd(Animation arg0)
          {
            isAnimation = false;
          }
        });
        
        containerBottomTab.startAnimation(animation);
      }
    }
    else
    {
      //GONE
      if (containerBottomTab.getVisibility() == View.VISIBLE)
      {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, bottomTappx, 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new AnimationListener()
        {
          @Override
          public void onAnimationStart(Animation animation)
          {
            isAnimation = true;
          }
          
          
          @Override
          public void onAnimationRepeat(Animation animation)
          {
            
          }
          
          
          @Override
          public void onAnimationEnd(Animation arg0)
          {
            isAnimation = false;
            containerBottomTab.setVisibility(View.GONE);
          }
        });
        containerBottomTab.startAnimation(animation);
      }
    }
  }
}
