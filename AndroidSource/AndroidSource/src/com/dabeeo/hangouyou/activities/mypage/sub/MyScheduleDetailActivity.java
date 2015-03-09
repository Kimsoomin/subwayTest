package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;

public class MyScheduleDetailActivity extends ActionBarActivity
{
  private ScrollView scrollView;
  private LinearLayout container;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_schedule_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    scrollView = (ScrollView) findViewById(R.id.scrollview);
    container = (LinearLayout) findViewById(R.id.content_container);
    
    headerView = (ScheduleDetailHeaderView) findViewById(R.id.header_view);
    titleView = (ScheduleDetailTitleView) findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    FrameLayout header = (FrameLayout) findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    displayContentData();
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContentData()
  {
    container.removeAllViews();
    
    ProductView productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
    productView = new ProductView(this);
    container.addView(productView);
  }
}
