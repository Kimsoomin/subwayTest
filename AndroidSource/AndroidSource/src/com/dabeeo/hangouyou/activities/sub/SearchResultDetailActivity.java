package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;

@SuppressWarnings("deprecation")
public class SearchResultDetailActivity extends ActionBarActivity
{
	private EditText inputWord;
	private SearchResultAdapter adapter;
	private ImageView imageX;
	private ListView searchListView;
	private LinearLayout emptyContainer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_search_result));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		searchListView = (ListView) findViewById(R.id.search_list);
		emptyContainer = (LinearLayout) findViewById(R.id.empty_container);
		
		inputWord = (EditText) findViewById(R.id.edit_search);
		inputWord.setOnEditorActionListener(editorActionListener);
		inputWord.setText(getIntent().getStringExtra("keyword"));
		
		imageX = (ImageView) findViewById(R.id.search_cancel);
		imageX.setVisibility(View.GONE);
		imageX.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				inputWord.setText("");
				search("");
			}
		});
		
		adapter = new SearchResultAdapter();
		searchListView = (ListView) findViewById(R.id.search_list);
		searchListView.setOnItemClickListener(itemClickListener);
		searchListView.setAdapter(adapter);
		
		TextWatcher watcher = new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s)
			{
			}
			
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (inputWord.getText().toString().length() > 1)
				{
					searchListView.setVisibility(View.VISIBLE);
					emptyContainer.setVisibility(View.GONE);
					imageX.setVisibility(View.VISIBLE);
					search(inputWord.getText().toString());
				}
				else
				{
					imageX.setVisibility(View.GONE);
					searchListView.setVisibility(View.GONE);
					emptyContainer.setVisibility(View.VISIBLE);
				}
			}
			
		};
		inputWord.addTextChangedListener(watcher);
		
		if (!TextUtils.isEmpty(inputWord.getText().toString()))
		{
			imageX.setVisibility(View.VISIBLE);
			inputWord.setSelection(inputWord.getText().toString().length());
			search(inputWord.getText().toString());
		}
	}
	
	
	private void search(String text)
	{
		adapter.clear();
		
		if (TextUtils.isEmpty(text))
		{
			emptyContainer.setVisibility(View.VISIBLE);
			searchListView.setVisibility(View.GONE);
			return;
		}
		else
		{
			searchListView.setVisibility(View.VISIBLE);
			emptyContainer.setVisibility(View.GONE);
		}
		
		//TEST Beans
		SearchResultBean resultBean = new SearchResultBean();
		resultBean.addNormalTitle(getString(R.string.term_search_result), 300);
		adapter.add(resultBean);
		
		SearchResultBean locationBean = new SearchResultBean();
		locationBean.addPlaceTitle(getString(R.string.term_place), 100);
		adapter.add(locationBean);
		
		for (int i = 0; i < 3; i++)
		{
			SearchResultBean bean = new SearchResultBean();
			bean.addText(text + " " + getString(R.string.term_place) + i, SearchResultBean.TYPE_PLACE);
			adapter.add(bean);
		}
		
		SearchResultBean productBean = new SearchResultBean();
		productBean.addProductTitle(getString(R.string.term_product), 3);
		adapter.add(productBean);
		
		for (int i = 0; i < 3; i++)
		{
			SearchResultBean bean = new SearchResultBean();
			bean.addText(text + " " + getString(R.string.term_product) + i, SearchResultBean.TYPE_PRODUCT);
			adapter.add(bean);
		}
		
		SearchResultBean recommendSeoulBean = new SearchResultBean();
		recommendSeoulBean.addRecommendSeoulTitle(getString(R.string.term_strategy_seoul), 100);
		adapter.add(recommendSeoulBean);
		
		for (int i = 0; i < 3; i++)
		{
			SearchResultBean bean = new SearchResultBean();
			bean.addText(text + " " + getString(R.string.term_strategy_seoul) + i, SearchResultBean.TYPE_RECOMMEND_SEOUL);
			adapter.add(bean);
		}
		
		SearchResultBean scheduleBean = new SearchResultBean();
		scheduleBean.addScheduleTitle(getString(R.string.term_travel_schedule), 100);
		adapter.add(scheduleBean);
		
		for (int i = 0; i < 3; i++)
		{
			SearchResultBean bean = new SearchResultBean();
			bean.addText(text + " " + getString(R.string.term_travel_schedule) + i, SearchResultBean.TYPE_SCHEDULE);
			adapter.add(bean);
		}
		
		adapter.notifyDataSetChanged();
	}
	
	
	private void detail(SearchResultBean searchResultBean)
	{
		if (searchResultBean.isTitle)
		{
			Intent intent = new Intent(SearchResultDetailActivity.this, SearchResultJustOneCategoryListActivity.class);
			intent.putExtra("title", searchResultBean.text + " " + getString(R.string.term_search_result));
			intent.putExtra("results", adapter.getJsonStringForParameter(searchResultBean.type));
			startActivity(intent);
		}
		else
		{
			switch (searchResultBean.type)
			{
				case SearchResultBean.TYPE_PLACE:
					startActivity(new Intent(SearchResultDetailActivity.this, PlaceDetailActivity.class));
					break;
				
				case SearchResultBean.TYPE_PRODUCT:
					startActivity(new Intent(SearchResultDetailActivity.this, TrendProductDetailActivity.class));
					break;
				
				case SearchResultBean.TYPE_RECOMMEND_SEOUL:
					startActivity(new Intent(SearchResultDetailActivity.this, TravelStrategyDetailActivity.class));
					break;
				
				case SearchResultBean.TYPE_SCHEDULE:
					startActivity(new Intent(SearchResultDetailActivity.this, MyScheduleDetailActivity.class));
					break;
				
				default:
					inputWord.setText(searchResultBean.text);
					search(searchResultBean.text);
					break;
			}
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
	private OnEditorActionListener editorActionListener = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
				search(v.getText().toString());
			return false;
		}
	};
	
	private OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			detail((SearchResultBean) adapter.getItem(position));
		}
	};
}
