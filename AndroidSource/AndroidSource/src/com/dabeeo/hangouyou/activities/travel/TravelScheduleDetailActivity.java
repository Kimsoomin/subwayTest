package com.dabeeo.hangouyou.activities.travel;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.WriteReviewActivity;
import com.dabeeo.hangouyou.beans.ScheduleDetailBean;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleDetailViewPagerAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.views.SharePickView;

@SuppressWarnings("deprecation")
public class TravelScheduleDetailActivity extends ActionBarActivity
{
	private ViewPager viewPager;
	public TravelScheduleDetailViewPagerAdapter adapter;
	private ProgressBar progressBar;
	private ApiClient apiClient;
	
	private String idx;
	private ScheduleDetailBean bean;
	
	public LinearLayout containerWriteReview, containerLike, containerIsPublic;
	public Button btnIsPublic, btnLike, btnBookmark;
	private SharePickView sharePickView;
	private int currentDayNum = 1;
	private int currentPosition = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_detail);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_travel_schedule));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		idx = getIntent().getStringExtra("idx");
		apiClient = new ApiClient(this);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setOffscreenPageLimit(100);
		sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
		
		btnLike = (Button) findViewById(R.id.btn_like);
		containerLike = (LinearLayout) findViewById(R.id.container_like);
		containerWriteReview = (LinearLayout) findViewById(R.id.write_review_container);
		containerIsPublic = (LinearLayout) findViewById(R.id.container_is_public);
		btnIsPublic = (Button) findViewById(R.id.btn_is_public);
		btnBookmark = (Button) findViewById(R.id.btn_bookmark);
		
		findViewById(R.id.btn_bookmark).setOnClickListener(clickListener);
		findViewById(R.id.btn_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_like).setOnClickListener(clickListener);
		findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
		
		btnLike.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btnLike.setActivated(!btnLike.isActivated());
			}
		});
		btnBookmark.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!btnBookmark.isActivated())
					Toast.makeText(TravelScheduleDetailActivity.this, getString(R.string.msg_add_bookmark), Toast.LENGTH_LONG).show();
				btnBookmark.setActivated(!btnBookmark.isActivated());
			}
		});
		loadScheduleDetail();
	}
	
	private OnClickListener clickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.btn_bookmark)
			{
				// 북마크 토글
				if (!btnBookmark.isActivated())
					Toast.makeText(TravelScheduleDetailActivity.this, getString(R.string.msg_add_bookmark), Toast.LENGTH_LONG).show();
				btnBookmark.setActivated(!btnBookmark.isActivated());
			}
			else if (v.getId() == R.id.btn_share)
			{
				// 공유하기
				sharePickView.setVisibility(View.VISIBLE);
				sharePickView.view.setVisibility(View.VISIBLE);
				sharePickView.bringToFront();
			}
			else if (v.getId() == R.id.btn_like)
			{
				//좋아요 
			}
			else if (v.getId() == R.id.btn_write_review)
			{
				//리뷰쓰기
				Intent i = new Intent(TravelScheduleDetailActivity.this, WriteReviewActivity.class);
				i.putExtra("idx", bean.idx);
				i.putExtra("type", "place");
				startActivity(i);
			}
		}
	};
	
	
	private void loadScheduleDetail()
	{
		new LoadScheduleAsyncTask().execute();
	}
	
	private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ScheduleDetailBean>
	{
		@Override
		protected void onPreExecute()
		{
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		
		@Override
		protected ScheduleDetailBean doInBackground(String... params)
		{
			return apiClient.getTravelScheduleDetail(idx);
		}
		
		
		@Override
		protected void onPostExecute(ScheduleDetailBean result)
		{
			bean = result;
			progressBar.setVisibility(View.GONE);
			displayContent();
			super.onPostExecute(result);
		}
	}
	
	
	private void displayContent()
	{
		try
		{
			adapter = new TravelScheduleDetailViewPagerAdapter(this, getSupportFragmentManager());
			adapter.setBean(bean);
			viewPager.setAdapter(adapter);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
		viewPager.invalidate();
		
		for (int i = 0; i < adapter.getCount(); i++)
		{
			getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.map)
		{
			Intent intent = new Intent(TravelScheduleDetailActivity.this, BlinkingMap.class);
			intent.putExtra("plan", bean.days);
			
			//아래에서 쓰시면 됩니다. 
			//전체일정 dayNum 1
			//N일 일정인 경우 dayNum = currentDayNum 쓰시면 됩니다 (1일은 2, 2일은 3)
			
			try
			{
				if (currentPosition == 0)
				{
					//전체인경우
					Log.w("WARN", "경도 : " + bean.days.get(0).spots.get(0).lat);
					Log.w("WARN", "위도 : " + bean.days.get(0).spots.get(0).lng);
				}
				else
				{
					//N번쨰 일정인 경우
					Log.w("WARN", "경도 : " + bean.days.get(currentPosition - 1).spots.get(0).lat);
					Log.w("WARN", "위도 : " + bean.days.get(currentPosition - 1).spots.get(0).lng);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
//			intent.putExtra("plan_daycount", bean.dayCount);
			startActivity(intent);
		}
		else if (item.getItemId() == android.R.id.home)
		{
			finish();
			overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
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
	@SuppressWarnings("deprecation")
	protected ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener()
	{
		@Override
		public void onPageSelected(int position)
		{
			getSupportActionBar().setSelectedNavigationItem(position);
			currentPosition = position;
			currentDayNum = position + 1;
		}
	};
}
