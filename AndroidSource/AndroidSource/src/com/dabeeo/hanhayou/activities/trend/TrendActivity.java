package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.dabeeo.hanhayou.controllers.trend.TrendKoreaListAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class TrendActivity extends ActionBarActivity
{
	private ListView listview;
	private TrendKoreaListAdapter adapter;
	
	private LinearLayout containerBottomTab;
	private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuWishList, bottomMenuSearch;
	private int lastVisibleItem = 0;
	
	private boolean isAnimation = false;
	private float bottomTappx;
	
	private int imageWidth = 0;
	private int imageheight = 0;
	
	public ApiClient apiClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend_korea);
		
		apiClient = new ApiClient(TrendActivity.this);
		
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_trend_korea));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		containerBottomTab = (LinearLayout) findViewById(R.id.container_bottom_tab);
		bottomMenuHome = (LinearLayout) findViewById(R.id.container_menu_home);
		bottomMenuMyPage = (LinearLayout) findViewById(R.id.container_menu_mypage);
		bottomMenuWishList = (LinearLayout) findViewById(R.id.container_menu_wishlist);
		bottomMenuSearch = (LinearLayout) findViewById(R.id.container_menu_search);
		bottomMenuHome.setOnClickListener(bottomMenuClickListener);
		bottomMenuMyPage.setOnClickListener(bottomMenuClickListener);
		bottomMenuWishList.setOnClickListener(bottomMenuClickListener);
		bottomMenuSearch.setOnClickListener(bottomMenuClickListener);
		
		listview = (ListView) findViewById(R.id.listview);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		imageWidth = metrics.widthPixels;
		imageheight = (int) (imageWidth * 0.43);
		
		adapter = new TrendKoreaListAdapter(this, imageWidth, imageheight);
		listview.setAdapter(adapter);
		
		float density = getResources().getDisplayMetrics().density;
		bottomTappx = 65 * density;
		
		loadThemeList();
		
		listview.setOnScrollListener(new OnScrollListener()
		{
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (isAnimation)
					return;
				if (firstVisibleItem > lastVisibleItem)
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
			
			
			public void onScrollStateChanged(AbsListView listView, int scrollState)
			{
				
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
			{
				if (!SystemUtil.isConnectNetwork(TrendActivity.this))
				{
					new AlertDialogManager(TrendActivity.this).showDontNetworkConnectDialog();
					return;
				}
				
				TrendKoreaBean bean = (TrendKoreaBean) adapter.getItem(position);
				
				Intent i = new Intent(TrendActivity.this, TrendExhibitionActivity.class);
				i.putExtra("themeIdx", bean.idx);
				i.putExtra("themeTitle", bean.title);
				i.putExtra("themeImageUrl", bean.imageUrl);
				startActivity(i);
			}
		});
	}
	
	
	public void loadThemeList()
	{
		new GetTrendyKoreaList().execute();
	}
	
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
				Intent i = new Intent(TrendActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", MainActivity.POSITION_MY_PAGE);
				startActivity(i);
			}
			else if (v.getId() == bottomMenuWishList.getId())
			{
				Intent i = new Intent(TrendActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", MainActivity.POSITION_WISHLIST);
				startActivity(i);
			}
			else if (v.getId() == bottomMenuSearch.getId())
			{
				Intent i = new Intent(TrendActivity.this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("position", MainActivity.POSITION_SEARCH);
				startActivity(i);
			}
		}
	};
	
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_search, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == android.R.id.home)
		{
			finish();
			overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
		}
		else if (id == R.id.search)
		{
			Intent i = new Intent(TrendActivity.this, TrendSearchActivity.class);
			startActivity(i);
		}
		else if (id == R.id.cart)
		{
			if (PreferenceManager.getInstance(TrendActivity.this).isLoggedIn())
			{
				Intent i = new Intent(TrendActivity.this, TrendCartActivity.class);
				startActivity(i);
			}
			else
			{
				new AlertDialogManager(TrendActivity.this).showNeedLoginDialog(-1);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class GetTrendyKoreaList extends AsyncTask<Void, Integer, ArrayList<TrendKoreaBean>>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		
		
		@Override
		protected ArrayList<TrendKoreaBean> doInBackground(Void... params)
		{
			ArrayList<TrendKoreaBean> result = null;
			result = apiClient.getThemeList(0);
			return result;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<TrendKoreaBean> result)
		{
			super.onPostExecute(result);
			adapter.addAll(result);
			
			if (adapter.getCount() == 0)
			{
				listview.setVisibility(View.GONE);
			}
			else
			{
				listview.setVisibility(View.VISIBLE);
			}
		}
	}
}
