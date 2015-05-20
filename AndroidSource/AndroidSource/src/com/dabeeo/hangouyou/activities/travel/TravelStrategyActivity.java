package com.dabeeo.hangouyou.activities.travel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.trend.TrendActivity;
import com.dabeeo.hangouyou.activities.trend.TrendSearchActivity;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mainmenu.RecommendSeoulViewPagerAdapter;

@SuppressWarnings("deprecation")
public class TravelStrategyActivity extends ActionBarActivity
{
	private RecommendSeoulViewPagerAdapter adapter;
	private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
	private ArrayList<TitleCategoryBean> spheres = new ArrayList<>();
	private MenuItem areaItem;
	private ViewPager viewPager;
	
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
		title.setText(getString(R.string.term_strategy_seoul));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		containerBottomTab = (LinearLayout) findViewById(R.id.container_bottom_tab);
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
		adapter = new RecommendSeoulViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		adapter.add(new TitleCategoryBean(getString(R.string.term_recommend_seoul), -1));
		adapter.add(new TitleCategoryBean(getString(R.string.term_popular_place), 9));
		adapter.add(new TitleCategoryBean(getString(R.string.term_shopping), 2));
		adapter.add(new TitleCategoryBean(getString(R.string.term_restaurant), 7));
		adapter.notifyDataSetChanged();
		
		for (int i = 0; i < adapter.getCount(); i++)
		{
			getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
		}
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				getSupportActionBar().setSelectedNavigationItem(position);
			}
			
			
			@Override
			public void onPageScrolled(int position, float arg1, int arg2)
			{
				adapter.currentPosition = position;
				invalidateOptionsMenu();
			}
			
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});
	}
	
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_recommend_seoul, menu);
		areaItem = menu.findItem(R.id.all);
		if (adapter.currentPosition == 0)
			areaItem.setVisible(true);
		else
			areaItem.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		if (item.getItemId() == R.id.all)
			showSphereDialog();
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
				Intent i = new Intent(TravelStrategyActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", MainActivity.POSITION_MY_PAGE);
				startActivity(i);
			}
			else if (v.getId() == bottomMenuWishList.getId())
			{
				Intent i = new Intent(TravelStrategyActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", MainActivity.POSITION_WISHLIST);
				startActivity(i);
			}
			else if (v.getId() == bottomMenuSearch.getId())
			{
				Intent i = new Intent(TravelStrategyActivity.this, TrendSearchActivity.class);
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
	
	
	private void showSphereDialog()
	{
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
		
		for (TitleCategoryBean bean : spheres)
		{
			arrayAdapter.add(bean.title);
		}
		
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
				String strName = arrayAdapter.getItem(which);
				Toast.makeText(getApplicationContext(), strName, Toast.LENGTH_SHORT).show();
			}
		});
		builderSingle.show();
	}
	
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
