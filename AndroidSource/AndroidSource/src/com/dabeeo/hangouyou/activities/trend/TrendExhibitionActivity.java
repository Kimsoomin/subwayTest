package com.dabeeo.hangouyou.activities.trend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.controllers.trend.TrendProductListAdapter;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hangouyou.views.TrendExhibitionTopView;

@SuppressWarnings("deprecation")
public class TrendExhibitionActivity extends ActionBarActivity
{
  private GridViewWithHeaderAndFooter gridView;
  private TrendProductListAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_exhition);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_exhibitions));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);
    TrendExhibitionTopView view = new TrendExhibitionTopView(this);
    view.setBean(null);
    gridView.addHeaderView(view);
    
    adapter = new TrendProductListAdapter(this);
    gridView.setAdapter(adapter);
    
    ProductBean bean = new ProductBean();
    bean.title = "[숨]워터풀 타임리스 워터젤 크림";
    bean.originalPrice = 80000;
    bean.discountPrice = 452000;
    adapter.add(bean);
    
    bean = new ProductBean();
    bean.title = "[네이처리퍼블릭]수딩 맨 모이스처 알로엘 수딩젤";
    bean.originalPrice = 80000;
    bean.discountPrice = 452000;
    adapter.add(bean);
    
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
}
