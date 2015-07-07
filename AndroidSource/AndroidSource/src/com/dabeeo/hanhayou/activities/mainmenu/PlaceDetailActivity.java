package com.dabeeo.hanhayou.activities.mainmenu;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.coupon.CouponDetailActivity;
import com.dabeeo.hanhayou.activities.ticket.TicketDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hanhayou.beans.CouponBean;
import com.dabeeo.hanhayou.beans.PlaceDetailBean;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.TicketBean;
import com.dabeeo.hanhayou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.CustomScrollView;
import com.dabeeo.hanhayou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hanhayou.views.DetailCouponView;
import com.dabeeo.hanhayou.views.DetailTicketView;
import com.dabeeo.hanhayou.views.PlaceDetailHeaderView;
import com.dabeeo.hanhayou.views.PlaceDetailTitleView;
import com.dabeeo.hanhayou.views.ProductView;
import com.dabeeo.hanhayou.views.ReviewContainerView;
import com.dabeeo.hanhayou.views.SharePickView;

public class PlaceDetailActivity extends ActionBarActivity
{
  private CustomScrollView scrollView;
  
  private PlaceDetailHeaderView headerView;
  private PlaceDetailTitleView titleView;
  private TextView textDetail;
  private TextView textRate;
  
  private LinearLayout containerProduct;
  
  private RelativeLayout reviewLayout;
  private ReviewContainerView reviewContainerView;
  
  public Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  private ProgressBar progressBar;
  
  private ApiClient apiClient;
  private String placeIdx;
  private String premiumIdx;
  private PlaceDetailBean bean;
  private ViewGroup layoutDetailPlaceInfo;
  private LinearLayout containerTicketAndCoupon;
  public LinearLayout containerWriteReview;
  private LinearLayout layoutRecommendSeoul;
  private Button btnRecommendSeoul;
  
  private boolean isEnterMap = false;
  private boolean isPremium = false;
  private Button btnLike, btnBookmark;
  private SharePickView sharePickView;
  private LinearLayout rateLayout;
  
  private ViewGroup layoutRecommendProduct;
  
  int rate = 0;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_place));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    isEnterMap = getIntent().getBooleanExtra("is_map", false);
    isPremium = getIntent().getBooleanExtra("isPremium", false);
    apiClient = new ApiClient(this);
    placeIdx = getIntent().getStringExtra("place_idx");
    if (getIntent().hasExtra("premium_Idx"))
    {
      premiumIdx = getIntent().getStringExtra("premium_Idx");
      if (TextUtils.isEmpty(premiumIdx))
        premiumIdx = "null";
    }
    else
      premiumIdx = "null";
    
    BlinkingCommon.smlLibDebug("PlaceDetail", "premiumIdx : " + premiumIdx);
    
    layoutRecommendSeoul = (LinearLayout) findViewById(R.id.container_recommend_by_expect);
    if (premiumIdx.equals("null") || isPremium)
    {
      layoutRecommendSeoul.setVisibility(View.GONE);
    }
    else
    {
      layoutRecommendSeoul.setVisibility(View.VISIBLE);
    }
    
    rateLayout = (LinearLayout) findViewById(R.id.rate_layout);
    rateLayout.setOnClickListener(rateClickListener);
    btnRecommendSeoul = (Button) findViewById(R.id.btn_recommend_by_expert);
    btnRecommendSeoul.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
          new AlertDialogManager(PlaceDetailActivity.this).showDontNetworkConnectDialog();
        else
        {
          Intent i = new Intent(PlaceDetailActivity.this, TravelStrategyDetailActivity.class);
          i.putExtra("place_idx", premiumIdx);
          i.putExtra("is_map", isEnterMap);
          BlinkingCommon.smlLibDebug("PlaceDetail", "premiumIdx : " + premiumIdx);
          startActivity(i);
        }
      }
    });
    containerWriteReview = (LinearLayout) findViewById(R.id.write_review_container);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    containerTicketAndCoupon = (LinearLayout) findViewById(R.id.container_coupon_and_ticket);
    
//    textDetail = (TextView) findViewById(R.id.text_detail);
    textRate = (TextView) findViewById(R.id.text_rate);
    sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
    
    btnReviewBest = (Button) findViewById(R.id.btn_review_best);
    btnReviewSoso = (Button) findViewById(R.id.btn_review_soso);
    btnReviewWorst = (Button) findViewById(R.id.btn_review_worst);
    btnReviewBest.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    btnReviewWorst.setOnClickListener(rateClickListener);
    
    containerProduct = (LinearLayout) findViewById(R.id.container_product);
    reviewLayout = (RelativeLayout) findViewById(R.id.review_layout);
    
    headerView = (PlaceDetailHeaderView) findViewById(R.id.header_view);
    titleView = (PlaceDetailTitleView) findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    final float density = getResources().getDisplayMetrics().density;
    scrollView = (CustomScrollView) findViewById(R.id.scrollview);
    scrollView.setScrollViewListener(new ScrollViewListener()
    {
      @Override
      public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy)
      {
        if (scrollView.getScrollY() > 150 * density)
          titleView.title.setVisibility(View.INVISIBLE);
        else
          titleView.title.setVisibility(View.VISIBLE);
        
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        
        if (diff == 0 && reviewContainerView != null && (reviewContainerView.getVisibility() == View.VISIBLE))
          reviewContainerView.loadMore();
      }
    });
    
    btnBookmark = (Button) findViewById(R.id.btn_bookmark);
    btnBookmark.setOnClickListener(clickListener);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    btnLike = (Button) findViewById(R.id.btn_like);
    btnLike.setOnClickListener(clickListener);
    findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
    
    FrameLayout header = (FrameLayout) findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    if (!SystemUtil.isConnectNetwork(this))
    {
      headerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0));
      header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 78, r.getDisplayMetrics())));
    }
    layoutRecommendProduct = (ViewGroup) findViewById(R.id.layout_recommend_product);
    loadPlaceDetail();
  }
  
  
  @Override
  protected void onResume()
  {
    if (reviewContainerView != null)
      reviewContainerView.reload();
    super.onResume();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    if (!isEnterMap)
      getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
    else
      getMenuInflater().inflate(R.menu.menu_empty, menu);
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
      MapCheckUtil.checkMapExist(PlaceDetailActivity.this, new Runnable()
      {
        @Override
        public void run()
        {
          Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
          i.putExtra("placeIdx", bean.idx);
          startActivity(i);
        }
      });
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void loadPlaceDetail()
  {
    progressBar.setVisibility(View.VISIBLE);
    progressBar.bringToFront();
    new GetPlaceDetailAsyncTask().execute();
  }
  
  private class GetPlaceDetailAsyncTask extends AsyncTask<String, Integer, PlaceDetailBean>
  {
    
    @Override
    protected PlaceDetailBean doInBackground(String... params)
    {
      return apiClient.getPlaceDetail(placeIdx);
    }
    
    
    @Override
    protected void onPostExecute(PlaceDetailBean result)
    {
      bean = result;
      btnBookmark.setActivated(bean.isBookmarked);
      btnLike.setActivated(bean.isLiked);
      displayContentData();
      headerView.setBean(bean);
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContentData()
  {
    rateBtnSet();
    //TEST BEAN
    TicketBean ticketBean = new TicketBean();
    ticketBean.title = "경복궁 동반1인 무료 입장";
    DetailTicketView view = new DetailTicketView(PlaceDetailActivity.this);
    view.setBean(ticketBean);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(PlaceDetailActivity.this))
          new AlertDialogManager(PlaceDetailActivity.this).showDontNetworkConnectDialog();
        else
          startActivity(new Intent(PlaceDetailActivity.this, TicketDetailActivity.class));
      }
    });
    containerTicketAndCoupon.addView(view);
    
    CouponBean couponBean = new CouponBean();
    couponBean.title = "서울 시티투어 버스 20% 할인";
    DetailCouponView couponView = new DetailCouponView(PlaceDetailActivity.this);
    couponView.setBean(couponBean);
    couponView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(PlaceDetailActivity.this))
          new AlertDialogManager(PlaceDetailActivity.this).showDontNetworkConnectDialog();
        else
          startActivity(new Intent(PlaceDetailActivity.this, CouponDetailActivity.class));
      }
    });
    containerTicketAndCoupon.addView(couponView);
    
    if (SystemUtil.isConnectNetwork(getApplicationContext()))
    {
      layoutRecommendProduct.setVisibility(View.VISIBLE);
      containerProduct.removeAllViews();
      ProductView productView = new ProductView(PlaceDetailActivity.this);
      ProductBean productBean = new ProductBean();
      productBean.title = "XXX 수분크림";
      productBean.originalPrice = 150;
      productBean.discountPrice = 93;
      productView.setBean(productBean, productBean);
      containerProduct.addView(productView);
    }
    else
    {
      layoutRecommendProduct.setVisibility(View.GONE);
    }
    
    if (bean == null)
      return;
    
    reviewContainerView = new ReviewContainerView(PlaceDetailActivity.this, "place", bean.idx);
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.loadMore();
    
    textRate.setText(Float.toString(bean.rate));
//    textDetail.setText(bean.contents);
    
    addDetailInfo(getString(R.string.term_place_detail_info), bean.contents);
    addDetailInfo(getString(R.string.term_address), bean.address);
    addDetailInfo(getString(R.string.term_phone), bean.contact);
    addDetailInfo(getString(R.string.term_homepage), bean.homepage);
    addDetailInfo(getString(R.string.term_working_time), bean.businessHours);
    addDetailInfo(getString(R.string.term_price_info), bean.priceInfo);
    addDetailInfo(getString(R.string.term_traffic), bean.trafficInfo);
    
    titleView.setBean(bean);
  }
  
  
  private void addDetailInfo(String title, final String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(R.id.text_title);
      TextView textView = (TextView) view.findViewById(R.id.text_description);
      titleView.setText(title);
      textView.setText(text);
      
      ImageView btnCall = (ImageView) view.findViewById(R.id.btn_call);
      ImageView btnAddress = (ImageView) view.findViewById(R.id.btn_address);
      if (title.equals(getString(R.string.term_address)) && !isEnterMap)
      {
        btnAddress.setVisibility(View.VISIBLE);
        btnAddress.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            MapCheckUtil.checkMapExist(PlaceDetailActivity.this, new Runnable()
            {
              @Override
              public void run()
              {
                Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
                i.putExtra("placeIdx", bean.idx);
                startActivity(i);
              }
            });
          }
        });
      }
      else if (title.equals(getString(R.string.term_phone)))
      {
        btnCall.setVisibility(View.VISIBLE);
        btnCall.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + text));
            startActivity(callIntent);
          }
        });
      }
      layoutDetailPlaceInfo.addView(view);
    }
  }
  
  public void rateBtnSet()
  {
    if(bean.myLastRate == 1)
    {
      btnReviewWorst.setSelected(true);
    }else if(bean.myLastRate == 3)
    {
      btnReviewSoso.setSelected(true);
    }else if(bean.myLastRate == 5)
    {
      btnReviewBest.setSelected(true);
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
      btnReviewBest.setSelected(false);
      btnReviewSoso.setSelected(false);
      btnReviewWorst.setSelected(false);
      
      if (!PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
      {
        new AlertDialogManager(PlaceDetailActivity.this).showNeedLoginDialog(-1);
        return;
      }else
      {
        if(!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(PlaceDetailActivity.this).showDontNetworkConnectDialog();
          return;
        }
      }
      
      if (v.getId() == btnReviewBest.getId())
      {
        btnReviewBest.setSelected(true);
        rate = 5;
      }
      else if (v.getId() == btnReviewSoso.getId())
      {
        btnReviewSoso.setSelected(true);
        rate = 3;
      }
      else if (v.getId() == btnReviewWorst.getId())
      {
        btnReviewWorst.setSelected(true);
        rate = 1;
      }
      
      AlertDialogManager alert = new AlertDialogManager(PlaceDetailActivity.this);
      alert.showAlertDialog(getString(R.string.term_alert), getString(R.string.msg_write_review), getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
      {
        public void onPositiveButtonClickListener()
        {
          Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
          i.putExtra("idx", bean.idx);
          i.putExtra("type", "place");
          i.putExtra("rate", rate);
          startActivity(i);
        }
        
        public void onNegativeButtonClickListener()
        {
          new postRateTask().execute();
        }
      });
    }
  };
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if(!SystemUtil.isConnectNetwork(PlaceDetailActivity.this))
      {
        new AlertDialogManager(PlaceDetailActivity.this).showDontNetworkConnectDialog();
        return;
      }
      if (v.getId() == R.id.btn_share)
      {
        // 공유하기
        sharePickView.setVisibility(View.VISIBLE);
        sharePickView.view.setVisibility(View.VISIBLE);
        sharePickView.bringToFront();
      }
      else 
      {
        if (PreferenceManager.getInstance(PlaceDetailActivity.this).isLoggedIn())
        {
          if (v.getId() == R.id.btn_bookmark)
          {
            new ToggleBookmarkTask().execute();
          }
          else if (v.getId() == R.id.btn_like)
          {
            //좋아요 
            new ToggleLikeTask().execute();
          }
          else if (v.getId() == R.id.btn_write_review)
          {
            //리뷰쓰기
            Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
            if (bean != null)
              i.putExtra("idx", bean.idx);
            i.putExtra("type", "place");
            startActivity(i);
          }
        }
        else
          new AlertDialogManager(PlaceDetailActivity.this).showNeedLoginDialog(-1);
      }
    }
  };
  
  
  /**************************************************
   * async task
   ***************************************************/
  /**
   * post rate AsyncTask
   */
  private class postRateTask extends AsyncTask<Void, Void, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.postReviewRate("palce", placeIdx, PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), rate, null);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
    }
  }
  
  private class ToggleBookmarkTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), bean.idx, "place", "B");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        
        if (obj.getString("result").equals("INS"))
        {
          btnBookmark.setActivated(true);
          Toast.makeText(PlaceDetailActivity.this, getString(R.string.msg_add_bookmark), Toast.LENGTH_LONG).show();
          bean.bookmarkCount++;
        }
        else
        {
          bean.bookmarkCount--;
          btnBookmark.setActivated(false);
        }
        titleView.reloadBookmarkCount(bean.bookmarkCount);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class ToggleLikeTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), bean.idx, "place", "L");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        btnLike.setActivated(obj.getString("result").equals("INS"));
        if (obj.getString("result").equals("INS"))
          bean.likeCount++;
        else
        {
          bean.likeCount--;
        }
        titleView.reloadLikeCount(bean.likeCount);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
