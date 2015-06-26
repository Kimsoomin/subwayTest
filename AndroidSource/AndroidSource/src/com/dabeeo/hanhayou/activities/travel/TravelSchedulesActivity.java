package com.dabeeo.hanhayou.activities.travel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.mainmenu.TravelScheduleViewPagerAdapter;
import com.dabeeo.hanhayou.fragments.mainmenu.TravelScheduleListFragment;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

@SuppressWarnings("deprecation")
public class TravelSchedulesActivity extends ActionBarActivity
{
  private TravelScheduleViewPagerAdapter adapter;
  private ViewPager viewPager;
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private LinearLayout containerBottomTab;
  private boolean isAnimation = false;
  private float bottomTappx;
  private int lastSelectedTab = 0;
  
  public TravelScheduleListFragment scheduleListFragment;
  public TravelScheduleListFragment myScheduleListFragment;
  public TravelScheduleListFragment myBookMarkscheduleListFragment;
  
  
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
    
    scheduleListFragment = new TravelScheduleListFragment(0);
    myScheduleListFragment = new TravelScheduleListFragment(1);
    myBookMarkscheduleListFragment = new TravelScheduleListFragment(2);
    
    adapter.addFragement(scheduleListFragment);
    adapter.addFragement(myScheduleListFragment);
    adapter.addFragement(myBookMarkscheduleListFragment);
    adapter.notifyDataSetChanged();
    
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
    
    for (String title : titles)
    {
      adapter.add(title);
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(title).setTabListener(tabListener));
    }
    adapter.notifyDataSetChanged();
    
    viewPager.setCurrentItem(1);
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
    else if (item.getItemId() == R.id.my_schedule)
    {
      showStrategyDaysAlertDialog();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void showStrategyDaysAlertDialog()
  {
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
    arrayAdapter.add(getString(R.string.term_all));
    arrayAdapter.add(getString(R.string.term_one_days_schedule));
    arrayAdapter.add(getString(R.string.term_two_days_schedule));
    arrayAdapter.add(getString(R.string.term_three_days_schedule));
    arrayAdapter.add(getString(R.string.term_four_days_schedule));
    
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    
    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        if (which == 0)
        {
          scheduleListFragment.setDayCount(0);
          myScheduleListFragment.setDayCount(0);
          myBookMarkscheduleListFragment.setDayCount(0);
        }
        else
        {
          scheduleListFragment.setDayCount(which);
          myScheduleListFragment.setDayCount(which);
          myBookMarkscheduleListFragment.setDayCount(which);
        }
      }
    });
    builderSingle.show();
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
        Intent i = new Intent(TravelSchedulesActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_SEARCH);
        startActivity(i);
      }
    }
  };
  protected TabListener tabListener = new TabListener()
  {
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
      if (!SystemUtil.isConnectNetwork(getApplicationContext()) && tab.getPosition() == 2)
      {
        new AlertDialogManager(TravelSchedulesActivity.this).showDontNetworkConnectDialog();
        
        Runnable runnable = new Runnable()
        {
          @Override
          public void run()
          {
            getSupportActionBar().setSelectedNavigationItem(lastSelectedTab);
          }
        };
        Handler handler = new Handler();
        handler.post(runnable);
      }
      else
      {
        viewPager.setCurrentItem(tab.getPosition());
        lastSelectedTab = tab.getPosition();
      }
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
