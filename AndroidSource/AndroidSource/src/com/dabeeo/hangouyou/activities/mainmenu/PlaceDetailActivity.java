package com.dabeeo.hangouyou.activities.mainmenu;

import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.views.PlaceDetailHeaderView;
import com.dabeeo.hangouyou.views.PlaceDetailTitleView;
import com.dabeeo.hangouyou.views.ProductView;
import com.squareup.picasso.Picasso;

public class PlaceDetailActivity extends ActionBarActivity
{
  private ScrollView scrollView;
  
  private PlaceDetailHeaderView headerView;
  private PlaceDetailTitleView titleView;
  private TextView textDetail;
  private TextView textRate;
  
  private LinearLayout containerProduct, containerReview;
  private Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  private ProgressBar progressBar;
  
  private ApiClient apiClient;
  private int placeIdx = -1;
  private PlaceDetailBean bean;
  private ViewGroup layoutDetailPlaceInfo;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    apiClient = new ApiClient(this);
    placeIdx = getIntent().getIntExtra("place_idx", -1);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    scrollView = (ScrollView) findViewById(R.id.scrollview);
    textDetail = (TextView) findViewById(R.id.text_detail);
    textRate = (TextView) findViewById(R.id.text_rate);
    
    btnReviewBest = (Button) findViewById(R.id.btn_review_best);
    btnReviewSoso = (Button) findViewById(R.id.btn_review_soso);
    btnReviewSoso = (Button) findViewById(R.id.btn_review_worst);
    btnReviewBest.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    
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
    findViewById(R.id.btn_like).setOnClickListener(clickListener);
    findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    loadPlaceDetail();
  }
  
  
  private void loadPlaceDetail()
  {
    progressBar.setVisibility(View.VISIBLE);
    progressBar.bringToFront();
    new GetPlaceDetailAsyncTask().execute();
  }
  
  private class GetPlaceDetailAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getPlaceDetail(placeIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new PlaceDetailBean();
          bean.setJSONObject(obj.getJSONObject("place"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        displayContentData();
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
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
    {
      Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
      i.putExtra("idx", bean.idx);
      startActivity(i);
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContentData()
  {
    if (bean == null)
      return;
    
    setTitle(bean.title);
    textRate.setText(Integer.toString(bean.rate));
    textDetail.setText(bean.contents);
    
    addDetailInfo(getString(R.string.term_address), bean.address);
    addDetailInfo(getString(R.string.term_phone), bean.contact);
    addDetailInfo(getString(R.string.term_homepage), bean.homepage);
    
    titleView.setBean(bean);
    
    containerProduct.removeAllViews();
    ProductView productView = new ProductView(PlaceDetailActivity.this);
    ProductBean bean = new ProductBean();
    bean.title = "XXX 수분크림";
    bean.originalPrice = 150;
    bean.discountPrice = 93;
    productView.setBean(bean);
    containerProduct.addView(productView);
    
//    reviewView = new ReviewView(PlaceDetailActivity.this);
//    reviewBean = new ReviewBean();
//    reviewBean.userName = "planB";
//    reviewBean.content = "좋네요!";
//    reviewView.setBean(reviewBean);
//    containerReview.addView(reviewView);
  }
  
  
  private void addDetailInfo(String title, String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(android.R.id.text1);
      TextView textView = (TextView) view.findViewById(android.R.id.text2);
      titleView.setText(title);
      textView.setText(text);
      layoutDetailPlaceInfo.addView(view);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener rateClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnReviewBest.getId())
      {
        
      }
      else if (v.getId() == btnReviewSoso.getId())
      {
        
      }
      else if (v.getId() == btnReviewWorst.getId())
      {
        
      }
    }
  };
  
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
      else if (v.getId() == R.id.btn_like)
      {
        //좋아요 
      }
      else if (v.getId() == R.id.btn_write_review)
      {
        //리뷰쓰기
        Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
        i.putExtra("idx", bean.idx);
        i.putExtra("type", "place");
        startActivity(i);
      }
    }
  };
}
