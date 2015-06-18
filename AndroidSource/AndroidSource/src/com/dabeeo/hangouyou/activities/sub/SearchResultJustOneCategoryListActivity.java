package com.dabeeo.hangouyou.activities.sub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultDetailAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class SearchResultJustOneCategoryListActivity extends ActionBarActivity
{
  private SearchResultDetailAdapter adapter;
  private ApiClient apiClient;
  private ProgressBar progressBar;
  private int searchType;
  
  
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
    
    progressBar = (ProgressBar) findViewById(R.id.progressbar);
    apiClient = new ApiClient(this);
    adapter = new SearchResultDetailAdapter();
    ListView list = (ListView) findViewById(android.R.id.list);
    list.setOnItemClickListener(itemClickListener);
    list.setAdapter(adapter);
    
    TextView searchTitle = (TextView) findViewById(R.id.title);
    TextView moreCount = (TextView) findViewById(R.id.text_more_count);
    searchTitle.setText(getIntent().getStringExtra("title"));
    moreCount.setText(" (" + getIntent().getIntExtra("more_count", 0) + ")");
    
    searchType = getIntent().getIntExtra("search_type", SearchResultBean.TYPE_PLACE);
    String searchText = getIntent().getStringExtra("search_text");
    Log.w("WARN", "Search Type : " + searchType);
    new SearchTask().execute(searchText);
  }
  
  /**************************************************
   * AsyncTask
   ***************************************************/
  private class SearchTask extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      adapter.clear();
      progressBar.setVisibility(View.VISIBLE);
      progressBar.bringToFront();
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.searchResult(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      String status = "";
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
        
        if (status.equals("OK"))
        {
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          
          for (int i = 0; i < jsonArray.length(); i++)
          {
            SearchResultBean bean = new SearchResultBean();
            JSONObject obj = jsonArray.getJSONObject(i);
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            bean.setLogType(obj.getString("logType"));
            
            if (bean.type == searchType)
              adapter.add(bean);
          }
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
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
      Intent i = null;
      switch (bean.type)
      {
        case SearchResultBean.TYPE_PLACE:
          i = new Intent(SearchResultJustOneCategoryListActivity.this, PlaceDetailActivity.class);
          i.putExtra("place_idx", bean.idx);
          startActivity(i);
          break;
        
        case SearchResultBean.TYPE_PRODUCT:
          startActivity(new Intent(SearchResultJustOneCategoryListActivity.this, TrendProductDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_RECOMMEND_SEOUL:
          i = new Intent(SearchResultJustOneCategoryListActivity.this, TravelStrategyDetailActivity.class);
          i.putExtra("place_idx", bean.idx);
          startActivity(i);
          break;
        
        case SearchResultBean.TYPE_SCHEDULE:
          i = new Intent(SearchResultJustOneCategoryListActivity.this, TravelScheduleDetailActivity.class);
          i.putExtra("idx", bean.idx);
          startActivity(i);
          break;
      }
    }
  };
}
