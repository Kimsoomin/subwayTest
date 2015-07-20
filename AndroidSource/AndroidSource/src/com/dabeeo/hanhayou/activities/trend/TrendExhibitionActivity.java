package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.dabeeo.hanhayou.controllers.trend.TrendProductListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.TrendExhibitionTopView;

public class TrendExhibitionActivity extends ActionBarActivity
{
  private GridViewWithHeaderAndFooter gridView;
  private TrendProductListAdapter adapter;
  
  public String idx;
  public String themeTitle;
  public String themeUrl;
  public ApiClient apiClient;  
  public TrendExhibitionTopView TopView;
  public ArrayList<ProductBean> ProductArray;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_exhition);
    
    apiClient = new ApiClient(TrendExhibitionActivity.this);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_exhibitions));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    idx = getIntent().getStringExtra("themeIdx");
    themeTitle = getIntent().getStringExtra("themeTitle");
    themeUrl = getIntent().getStringExtra("themeImageUrl");
    
    gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);
    
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int imageWidth = metrics.widthPixels;
    int imageHeight = (int) (imageWidth*0.43);
    
    TopView = new TrendExhibitionTopView(this, imageWidth, imageHeight);
    
    adapter = new TrendProductListAdapter(this);
    gridView.setAdapter(adapter);
    
    loadThemeItem();
  }
  
  public void loadThemeItem()
  {
    new GetTrendyProductList().execute();
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
      finish();
    else if (id == R.id.search)
    {
      Intent i = new Intent(TrendExhibitionActivity.this, TrendSearchActivity.class);
      startActivity(i);
    }
    else if (id == R.id.cart)
    {
      
    }
    return super.onOptionsItemSelected(item);
  }
  
  private class GetTrendyProductList extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }

    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getThemeItem(idx);
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
      

      TopView.setBean(themeTitle, themeUrl);
      gridView.addHeaderView(TopView);
      
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
