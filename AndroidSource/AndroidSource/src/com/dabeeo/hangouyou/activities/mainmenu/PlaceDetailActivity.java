package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.coupon.CouponDetailActivity;
import com.dabeeo.hangouyou.activities.ticket.TicketDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.CouponBean;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.utils.SystemUtil;
import com.dabeeo.hangouyou.views.CustomScrollView;
import com.dabeeo.hangouyou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hangouyou.views.DetailCouponView;
import com.dabeeo.hangouyou.views.DetailTicketView;
import com.dabeeo.hangouyou.views.PlaceDetailHeaderView;
import com.dabeeo.hangouyou.views.PlaceDetailTitleView;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ReviewContainerView;
import com.dabeeo.hangouyou.views.SharePickView;

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
  private PlaceDetailBean bean;
  private ViewGroup layoutDetailPlaceInfo;
  private LinearLayout containerTicketAndCoupon;
  public LinearLayout containerWriteReview;
  private Dialog sharedialog;
  private Button btnRecommendSeoul;
  
  private boolean isEnterMap = false;
  private Button btnLike, btnBookmark;
  
  
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
    apiClient = new ApiClient(this);
    placeIdx = getIntent().getStringExtra("place_idx");
    
    btnRecommendSeoul = (Button) findViewById(R.id.btn_recommend_by_expert);
    btnRecommendSeoul.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(PlaceDetailActivity.this, TravelStrategyDetailActivity.class));
      }
    });
    containerWriteReview = (LinearLayout) findViewById(R.id.write_review_container);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    containerTicketAndCoupon = (LinearLayout) findViewById(R.id.container_coupon_and_ticket);
    
    textDetail = (TextView) findViewById(R.id.text_detail);
    textRate = (TextView) findViewById(R.id.text_rate);
    
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
    
    scrollView = (CustomScrollView) findViewById(R.id.scrollview);
    scrollView.setScrollViewListener(new ScrollViewListener()
    {
      @Override
      public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy)
      {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        
        if (diff == 0 && reviewContainerView != null && (reviewContainerView.getVisibility() == View.VISIBLE))
          reviewContainerView.loadMore();
      }
    });
    
    FrameLayout header = (FrameLayout) findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    btnBookmark = (Button) findViewById(R.id.btn_bookmark);
    btnBookmark.setOnClickListener(clickListener);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    btnLike = (Button) findViewById(R.id.btn_like);
    btnLike.setOnClickListener(clickListener);
    findViewById(R.id.btn_write_review).setOnClickListener(clickListener);
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    loadPlaceDetail();
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
      Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
      i.putExtra("idx", bean.idx);
      startActivity(i);
    }
    return super.onOptionsItemSelected(item);
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
        headerView.setBean(bean);
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContentData()
  {
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
    
    containerProduct.removeAllViews();
    ProductView productView = new ProductView(PlaceDetailActivity.this);
    ProductBean productBean = new ProductBean();
    productBean.title = "XXX 수분크림";
    productBean.originalPrice = 150;
    productBean.discountPrice = 93;
    productView.setBean(productBean, productBean);
    containerProduct.addView(productView);
    
    if (bean == null)
      return;
    
    reviewContainerView = new ReviewContainerView(PlaceDetailActivity.this, "place", bean.idx);
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.loadMore();
    
    textRate.setText(Integer.toString(bean.rate));
    textDetail.setText(bean.contents);
    
    addDetailInfo(getString(R.string.term_address), bean.address);
//		addDetailInfo(getString(R.string.term_phone), bean.contact);
    addDetailInfo(getString(R.string.term_phone), "02-000-0000");
    addDetailInfo(getString(R.string.term_homepage), bean.homepage);
    
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
      if (title.equals(getString(R.string.term_address)))
      {
        btnAddress.setVisibility(View.VISIBLE);
        btnAddress.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent i = new Intent(PlaceDetailActivity.this, BlinkingMap.class);
            startActivity(i);
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
      
      int rate = 3;
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
      
      Intent i = new Intent(PlaceDetailActivity.this, WriteReviewActivity.class);
      i.putExtra("rate", rate);
      startActivity(i);
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
        Toast.makeText(PlaceDetailActivity.this, getString(R.string.msg_add_bookmark), Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == R.id.btn_share)
      {
        // 공유하기
        Builder builder = new AlertDialog.Builder(PlaceDetailActivity.this);
        View view = new SharePickView(PlaceDetailActivity.this);
        builder.setView(view);
        
        ((SharePickView) view).btnQQ.setOnClickListener(shareClickListener);
        ((SharePickView) view).btnWechat.setOnClickListener(shareClickListener);
        ((SharePickView) view).btnWeibo.setOnClickListener(shareClickListener);
        
        sharedialog = builder.create();
        if (!sharedialog.isShowing())
          sharedialog.show();
      }
      else if (v.getId() == R.id.btn_like)
      {
        //좋아요 
        btnLike.setActivated(!btnLike.isActivated());
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
  };
  
  private OnClickListener shareClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      String url = "";
      
      if (v.getId() == R.id.btn_qq)
      {
        if (isAppInstalled("com.tencent.mobileqq"))
        {
          share("com.tencent.mobileqq");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.tencent.mobileqq";
      }
      else if (v.getId() == R.id.btn_wechat)
      {
        if (isAppInstalled("com.tencent.mm"))
        {
          share("com.tencent.mm");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.tencent.mm";
      }
      else if (v.getId() == R.id.btn_weibo)
      {
        if (isAppInstalled("com.sina.weibo"))
        {
          share("com.sina.weibo");
          return;
        }
        else
          url = "https://play.google.com/store/apps/details?id=com.sina.weibo";
      }
      
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      startActivity(i);
      
      if (sharedialog.isShowing())
        sharedialog.dismiss();
    }
  };
  
  
  @SuppressLint("DefaultLocale")
  private void share(String sharePackageName)
  {
    boolean found = false;
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    
//		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//		File file = new File(extStorageDirectory, "ic_launcher.png");
//		
//		try
//		{
//			if (!file.exists())
//				file.createNewFile();
//		} catch (IOException e1)
//		{
//			e1.printStackTrace();
//		}
//		FileOutputStream outStream;
//		try
//		{
//			outStream = new FileOutputStream(file);
//			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//			outStream.flush();
//			outStream.close();
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
//
//		intent.setType("image/jpeg");
    //Weibo는 Image+Text가능, WeChat/QQ는 Text만 가능 
    
    intent.putExtra(Intent.EXTRA_TEXT, "[Hanhayou] Download Hanhayou! http://dabeeo.com");
    intent.setType("text/plain");
    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
    if (!resInfo.isEmpty())
    {
      for (ResolveInfo info : resInfo)
      {
        if (info.activityInfo.packageName.toLowerCase().equals(sharePackageName))
        {
          intent.setPackage(info.activityInfo.packageName);
          found = true;
          break;
        }
      }
      if (!found)
        return;
      
      startActivity(Intent.createChooser(intent, "Share"));
    }
  }
  
  
  private boolean isAppInstalled(String packageName)
  {
    try
    {
      getPackageManager().getApplicationInfo(packageName, 0);
      return true;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      return false;
    }
  }
}
