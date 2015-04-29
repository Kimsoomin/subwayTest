package com.dabeeo.hangouyou.activities.trend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendProductBean;
import com.dabeeo.hangouyou.controllers.trend.TrendExhitionListAdapter;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hangouyou.views.TrendExhibitionTopView;

public class TrendExhibitionActivity extends ActionBarActivity
{
  private GridViewWithHeaderAndFooter gridView;
  private TrendExhitionListAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_exhition);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);
    TrendExhibitionTopView view = new TrendExhibitionTopView(this);
    view.setBean(null);
    gridView.addHeaderView(view);
    
    adapter = new TrendExhitionListAdapter(this);
    gridView.setAdapter(adapter);
    
    TrendProductBean bean = new TrendProductBean();
    bean.title = "[숨]워터풀 타임리스 워터젤 크림";
    bean.price = 80000;
    bean.discountPrice = 452000;
    adapter.add(bean);
    
    bean = new TrendProductBean();
    bean.title = "[네이처리퍼블릭]수딩 맨 모이스처 알로엘 수딩젤";
    bean.price = 80000;
    bean.discountPrice = 452000;
    adapter.add(bean);
    
    gridView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
      {
        Intent i = new Intent(TrendExhibitionActivity.this, TrendProductDetailActivity.class);
        startActivity(i);
      }
    });
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
}
