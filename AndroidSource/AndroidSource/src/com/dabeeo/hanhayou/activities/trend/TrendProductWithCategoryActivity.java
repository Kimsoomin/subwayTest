package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.TrendProductCategoryView;

public class TrendProductWithCategoryActivity extends ActionBarActivity
{
	private GridView gridView;
	private TrendProductListAdapter adapter;
	private LinearLayout categoryContainer;
	private HashMap<String, LinearLayout> categoriesLayouts = new HashMap<>();
	
	public ApiClient apiClient;
	public String categoryId;
	public ArrayList<ProductBean> ProductArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend_product_with_category_list);
		
		apiClient = new ApiClient(TrendProductWithCategoryActivity.this);
		
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		String categoryTitle = getIntent().getStringExtra("category_title");
		title.setText(categoryTitle);
		
		categoryId = getIntent().getStringExtra("categoryId");
		
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
		
		loadCategoryItems();
//		displayCategories();
	}
	
	private void loadCategoryItems()
	{
	  new GetCategoryProductList().execute();
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
	
	private class GetCategoryProductList extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }

    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getCategryProductList(categoryId);
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      responseParser(result.response);
    }
  }
  
  public void responseParser(String result)
  {
    try
    {
      JSONObject obj = new JSONObject(result);
      ProductArray = new ArrayList<ProductBean>();
      JSONArray productArr = obj.getJSONArray("product");
      for (int i = 0; i < productArr.length(); i++)
      {
        ProductBean product = new ProductBean();
        JSONObject objInArr = productArr.getJSONObject(i);
        product.setJSONObject(objInArr);
        ProductArray.add(product);
      }
      adapter.addAll(ProductArray);
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    
  }
}
