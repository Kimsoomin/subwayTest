package com.dabeeo.hangouyou.activities.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.controllers.trend.TrendProductImageViewPagerAdapter;

@SuppressWarnings("deprecation")
public class TrendProductDetailActivity extends ActionBarActivity
{
	private ViewPager viewPager;
	private TrendProductImageViewPagerAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend_product_detail);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_product_detail));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		Log.w("WARN", "Product Detail Activity");
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new TrendProductImageViewPagerAdapter(this, getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		ArrayList<String> imageUrls = new ArrayList<>();
		imageUrls.add("http://image.gsshop.com/image/16/54/16545567_O1.jpg");
		imageUrls.add("http://image.gsshop.com/image/16/68/16680935_O1.jpg");
		imageUrls.add("http://image.gsshop.com/image/16/36/16368125_O1.jpg");
		adapter.addAll(imageUrls);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_empty, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
}
