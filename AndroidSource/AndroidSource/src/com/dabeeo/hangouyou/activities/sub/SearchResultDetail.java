package com.dabeeo.hangouyou.activities.sub;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPhotoLogDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultDetailAdapter;

public class SearchResultDetail extends ActionBarActivity
{
  private SearchResultDetailAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_search_result_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    adapter = new SearchResultDetailAdapter();
    ListView list = (ListView) findViewById(android.R.id.list);
    list.setOnItemClickListener(itemClickListener);
    list.setAdapter(adapter);
    
    setTitle(getIntent().getStringExtra("title"));
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
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
          startActivity(new Intent(SearchResultDetail.this, PlaceDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_PRODUCT:
          Log.i("SearchResultFragment.java | detail", "|" + "상품 상세화면으로 가기" + "|"); 
          break;
        
        case SearchResultBean.TYPE_RECOMMEND_SEOUL:
          startActivity(new Intent(SearchResultDetail.this, TravelStrategyDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_SCHEDULE:
          startActivity(new Intent(SearchResultDetail.this, MyScheduleDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_PHOTO_LOG:
          startActivity(new Intent(SearchResultDetail.this, MyPhotoLogDetailActivity.class));
          break;
      }
    }
  };
}
