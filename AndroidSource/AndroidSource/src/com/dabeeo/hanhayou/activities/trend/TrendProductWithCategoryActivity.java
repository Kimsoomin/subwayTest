package com.dabeeo.hanhayou.activities.trend;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.trend.TrendProductListAdapter;
import com.dabeeo.hanhayou.views.TrendProductCategoryView;

@SuppressWarnings("deprecation")
public class TrendProductWithCategoryActivity extends ActionBarActivity
{
	private GridView gridView;
	private TrendProductListAdapter adapter;
	private LinearLayout categoryContainer;
	private HashMap<String, LinearLayout> categoriesLayouts = new HashMap<>();
	
	private boolean isCategoryExist = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend_product_with_category_list);
		
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		String categoryTitle = getIntent().getStringExtra("category_title");
		title.setText(categoryTitle);
		
		isCategoryExist = getIntent().getBooleanExtra("is_exist_category", false);
		
		categoryContainer = (LinearLayout) findViewById(R.id.category_container);
		gridView = (GridView) findViewById(R.id.product_list);
		adapter = new TrendProductListAdapter(this);
		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				startActivity(new Intent(TrendProductWithCategoryActivity.this, TrendProductDetailActivity.class));
			}
		});
//		ProductBean bean = new ProductBean();
//		bean.title = "[숨]워터풀 타임리스 워터젤 크림";
//		bean.originalPrice = 80000;
//		bean.discountPrice = 452000;
//		adapter.add(bean);
//		
//		bean = new ProductBean();
//		bean.title = "[네이처리퍼블릭]수딩 맨 모이스처 알로엘 수딩젤";
//		bean.originalPrice = 80000;
//		bean.discountPrice = 452000;
//		adapter.add(bean);
		
		if (isCategoryExist)
			displayCategories();
	}
	
	
	private void displayCategories()
	{
		TrendProductCategoryView view = new TrendProductCategoryView(this);
		view.setData("스킨케어", "메이크업");
		categoriesLayouts.put("스킨케어", view.leftContainer);
		categoriesLayouts.put("메이크업", view.rightContainer);
		view.leftContainer.setOnClickListener(categoryContainerClickListener);
		view.rightContainer.setOnClickListener(categoryContainerClickListener);
		categoryContainer.addView(view);
		
		view = new TrendProductCategoryView(this);
		view.setData("헤어케어", "바디케어");
		categoriesLayouts.put("헤어케어", view.leftContainer);
		categoriesLayouts.put("바디케어", view.rightContainer);
		view.leftContainer.setOnClickListener(categoryContainerClickListener);
		view.rightContainer.setOnClickListener(categoryContainerClickListener);
		categoryContainer.addView(view);
		
		view = new TrendProductCategoryView(this);
		view.setData("남성화장품", "기타");
		categoriesLayouts.put("남성화장품", view.leftContainer);
		categoriesLayouts.put("기타", view.rightContainer);
		view.leftContainer.setOnClickListener(categoryContainerClickListener);
		view.rightContainer.setOnClickListener(categoryContainerClickListener);
		categoryContainer.addView(view);
	}
	
	private OnClickListener categoryContainerClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent i = new Intent(TrendProductWithCategoryActivity.this, TrendProductWithCategoryActivity.class);
			startActivity(i);
		}
	};
	
	
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
			finish();
		else if (id == R.id.search)
		{
			Intent i = new Intent(TrendProductWithCategoryActivity.this, TrendSearchActivity.class);
			startActivity(i);
		}
		else if (id == R.id.cart)
		{
			
		}
		return super.onOptionsItemSelected(item);
	}
}
