package com.dabeeo.hangouyou.activities.trend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendKoreaBean;
import com.dabeeo.hangouyou.controllers.trend.TrendKoreaListAdapter;

public class TrendKoreaActivity extends ActionBarActivity
{
  private ListView listview;
  private TrendKoreaListAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_korea);
    
    listview = (ListView) findViewById(R.id.listview);
    adapter = new TrendKoreaListAdapter();
    listview.setAdapter(adapter);
    
    TrendKoreaBean bean = new TrendKoreaBean();
    bean.title = "한류스타들의 BEST 상품";
    bean.category = "[K STAR]";
    adapter.add(bean);
    
    bean = new TrendKoreaBean();
    bean.title = "서울 현지인이 추천하는 코스메틱선물 TOP";
    bean.category = "[BEAUTY]";
    adapter.add(bean);
    
    listview.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
      {
        TrendKoreaBean bean = (TrendKoreaBean) adapter.getItem(position);
        
        Intent i = new Intent(TrendKoreaActivity.this, TrendListActivity.class);
        startActivity(i);
      }
    });
  }
}
