package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.NoticeBean;
import com.dabeeo.hanhayou.beans.NoticeBean.NoticeContentBean;
import com.dabeeo.hanhayou.controllers.mypage.NoticeAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class NoticeActivity extends ActionBarActivity
{
	private ExpandableListView listView;
	private NoticeAdapter adapter;
	
	private ArrayList<String> titles = new ArrayList<String>();
	private HashMap<String, List<ArrayList<NoticeContentBean>>> contents = new HashMap<String, List<ArrayList<NoticeContentBean>>>();
	
	private ProgressBar progressBar;
	private ApiClient apiClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_notice));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		apiClient = new ApiClient(this);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		
		listView = (ExpandableListView) findViewById(android.R.id.list);
		adapter = new NoticeAdapter(this, titles, contents);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(getResources().getDrawable(R.drawable.transparent));
		listView.setOnGroupExpandListener(new OnGroupExpandListener()
		{
			@Override
			public void onGroupExpand(int groupPosition)
			{
				for (int i = 0; i < adapter.getGroupCount(); i++)
				{
					if (i != groupPosition)
						listView.collapseGroup(i);
				}
			}
		});
		listView.setOnScrollListener(scrollListener);
		
		loadNotices();
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
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
	
	
	private void loadNotices()
	{
		new NoticeAsyncTask().execute();
	}
	
	/**************************************************
	 * listener
	 ***************************************************/
	private OnScrollListener scrollListener = new OnScrollListener()
	{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
		}
		
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
//      if (totalItemCount > 0 && totalItemCount > offset && totalItemCount <= firstVisibleItem + visibleItemCount)
//      {
//        offset += limit;
//        load(offset);
//      }
		}
	};
	
	/**************************************************
	 * AsyncTask
	 ***************************************************/
	private class NoticeAsyncTask extends AsyncTask<String, Integer, NetworkResult>
	{
		
		@Override
		protected void onPreExecute()
		{
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		
		@Override
		protected NetworkResult doInBackground(String... params)
		{
			return apiClient.getNotices();
		}
		
		
		@Override
		protected void onPostExecute(NetworkResult result)
		{
			try
			{
				JSONObject obj = new JSONObject(result.response);
				JSONArray arr = obj.getJSONArray("notice");
				for (int i = 0; i < arr.length(); i++)
				{
					NoticeBean bean = new NoticeBean();
					bean.setJSONObject(arr.getJSONObject(i));
					
					String title = bean.title + "&&&" + bean.insertDateString;
					titles.add(title);
					if (contents.get(title) == null)
						contents.put(title, new ArrayList<ArrayList<NoticeContentBean>>());
					contents.get(title).add(bean.contents);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}
}
