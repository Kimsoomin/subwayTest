package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.PlaceDetailHeaderView;
import com.dabeeo.hangouyou.views.PlaceDetailTitleView;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ReviewView;

public class MyPlaceDetailActivity extends ActionBarActivity
{
  private ScrollView scrollView;
  
  private PlaceDetailHeaderView headerView;
  private PlaceDetailTitleView titleView;
  private TextView textDetail;
  private TextView textRate;
  
  private LinearLayout containerProduct, containerReview;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_place_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    scrollView = (ScrollView) findViewById(R.id.scrollview);
    textDetail = (TextView) findViewById(R.id.text_detail);
    textRate = (TextView) findViewById(R.id.text_rate);
    containerProduct = (LinearLayout) findViewById(R.id.container_product);
    containerReview = (LinearLayout) findViewById(R.id.container_review);
    
    headerView = (PlaceDetailHeaderView) findViewById(R.id.header_view);
    titleView = (PlaceDetailTitleView) findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    FrameLayout header = (FrameLayout) findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    findViewById(R.id.btn_bookmark).setOnClickListener(clickListener);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    
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
      Toast.makeText(this, "준비 중입니다", Toast.LENGTH_LONG).show();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContentData()
  {
    setTitle("왓슨스");
    textRate.setText("5.0");
    textDetail.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
        + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
        + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
        + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." + "\n\n주소\n161-1, gangnam-gu, seoul, korea\n\nTel\n+82-2-376-1234"
        + "\n\nhomepage\nwww.dabeeo.com");
    
    containerProduct.removeAllViews();
    ProductView productView = new ProductView(MyPlaceDetailActivity.this);
    ProductBean bean = new ProductBean();
    bean.title = "XXX 수분크림";
    bean.originalPrice = 150;
    bean.discountPrice = 93;
    productView.setBean(bean, bean);
    containerProduct.addView(productView);
    
    containerReview.removeAllViews();
    ReviewView reviewView = new ReviewView(MyPlaceDetailActivity.this);
    ReviewBean reviewBean = new ReviewBean();
    reviewBean.userName = "planB";
    reviewBean.content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do";
    reviewView.setBean(reviewBean);
    containerReview.addView(reviewView);
    
    reviewView = new ReviewView(MyPlaceDetailActivity.this);
    reviewBean = new ReviewBean();
    reviewBean.userName = "planB";
    reviewBean.content = "좋네요!";
    reviewView.setBean(reviewBean);
    containerReview.addView(reviewView);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_bookmark)
      {
        // 북마크 토글
      }
      else if (v.getId() == R.id.btn_share)
      {
        // 공유하기
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
      }
    }
  };
}
