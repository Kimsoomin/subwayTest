package com.dabeeo.hangouyou.activities.mypage.sub;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mypage.MyPlaceViewPagerAdapter;
import com.dabeeo.hangouyou.fragments.mypage.MyPlaceListFragment;

@SuppressWarnings("deprecation")
public class MyPlaceActivity extends ActionBarActivity
{
	private ViewPager viewPager;
	private MenuItem editMenuItem, closeMenuItem;
	public boolean isEditMode = false;
	private MyPlaceViewPagerAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_place);
		
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_my_place));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setOffscreenPageLimit(100);
		
		adapter = new MyPlaceViewPagerAdapter(this, getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		displayTitles();
	}
	
	
	private void displayTitles()
	{
		adapter.add(new TitleCategoryBean(getString(R.string.term_all), -1));
		adapter.add(new TitleCategoryBean(getString(R.string.term_popular_place), 9));
		adapter.add(new TitleCategoryBean(getString(R.string.term_shopping), 2));
		adapter.add(new TitleCategoryBean(getString(R.string.term_restaurant), 7));
		
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
      getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			int index = viewPager.getCurrentItem();
			MyPlaceListFragment fragment = adapter.getFragment(index);
			fragment.setEditMode(isEditMode);
			invalidateOptionsMenu();
		}
		else if (item.getItemId() == closeMenuItem.getItemId())
		{
			isEditMode = false;
			int index = viewPager.getCurrentItem();
			MyPlaceListFragment fragment = adapter.getFragment(index);
			fragment.setEditMode(isEditMode);
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
			isEditMode = false;
			invalidateOptionsMenu();
		}
	};
}
