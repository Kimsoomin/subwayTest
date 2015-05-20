package com.dabeeo.hangouyou.activities.trend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.controllers.trend.TrendProductListAdapter;

public class TrendProductWithCategoryActivity extends ActionBarActivity
{
  private GridView gridView;
  private TrendProductListAdapter adapter;
  
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
    
    gridView = (GridView) findViewById(R.id.product_list);
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
}
