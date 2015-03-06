package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.PlaceDetailHeaderView;
import com.dabeeo.hangouyou.views.PlaceDetailTitleView;

public class MyPlaceDetailActivity extends ActionBarActivity
{
  private ScrollView scrollView;
//  private LinearLayout container;
  
  private PlaceDetailHeaderView headerView;
  private PlaceDetailTitleView titleView;
  private TextView detailTextView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_detail_place);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    scrollView = (ScrollView) findViewById(R.id.scrollview);
//    container = (LinearLayout) findViewById(R.id.content_container);
    detailTextView = (TextView) findViewById(R.id.text_detail);
    
    headerView = (PlaceDetailHeaderView) findViewById(R.id.header_view);
    titleView = (PlaceDetailTitleView) findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    FrameLayout header = (FrameLayout) findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    displayContentData();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.map)
      Log.i("MyPlaceDetailActivity.java | onOptionsItemSelected", "|" + "map" + "|");
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContentData()
  {
    detailTextView.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
        + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
        + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
        + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." + "\n\n주소\n161-1, gangnam-gu, seoul, korea\n\nTel\n+82-2-376-1234"
        + "\n\nhomepage\nwww.dabeeo.com");
  }
}
