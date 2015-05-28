package com.dabeeo.hangouyou.activities.sub;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultDetailAdapter;

@SuppressWarnings("deprecation")
public class SearchResultJustOneCategoryListActivity extends ActionBarActivity
{
	private SearchResultDetailAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search_in_category_result_detail);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_search_result));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		adapter = new SearchResultDetailAdapter();
		ListView list = (ListView) findViewById(android.R.id.list);
		list.setOnItemClickListener(itemClickListener);
		list.setAdapter(adapter);
		
		TextView searchTitle = (TextView) findViewById(R.id.title);
		TextView moreCount = (TextView) findViewById(R.id.text_more_count);
		searchTitle.setText(getIntent().getStringExtra("title"));
		moreCount.setText("(50)");
		
		String json = getIntent().getStringExtra("results");
		parse(json);
	}
	
	
	private void parse(String json)
	{
		try
		{
			JSONArray array = new JSONArray(json);
			
			for (int i = 0; i < array.length(); i++)
			{
				adapter.add(new SearchResultBean(array.getString(i)));
			}
			
			adapter.notifyDataSetChanged();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
	
	/**************************************************
	 * listener
	 ***************************************************/
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			SearchResultBean bean = (SearchResultBean) adapter.getItem(position);
			
			switch (bean.type)
			{
				case SearchResultBean.TYPE_PLACE:
					startActivity(new Intent(SearchResultJustOneCategoryListActivity.this, PlaceDetailActivity.class));
					break;
				
				case SearchResultBean.TYPE_PRODUCT:
					Log.i("SearchResultFragment.java | detail", "|" + "상품 상세화면으로 가기" + "|");
					break;
				
				case SearchResultBean.TYPE_RECOMMEND_SEOUL:
					startActivity(new Intent(SearchResultJustOneCategoryListActivity.this, TravelStrategyDetailActivity.class));
					break;
				
				case SearchResultBean.TYPE_SCHEDULE:
					startActivity(new Intent(SearchResultJustOneCategoryListActivity.this, MyScheduleDetailActivity.class));
					break;
			}
		}
	};
}
