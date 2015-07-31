package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.ProductDetailBean;
import com.dabeeo.hanhayou.controllers.trend.TrendProductImageViewPagerAdapter;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.views.CustomScrollView;
import com.dabeeo.hanhayou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hanhayou.views.ProductJustOneView;
import com.dabeeo.hanhayou.views.ProductReviewContainerView;
import com.dabeeo.hanhayou.views.SharePickView;
import com.dabeeo.hanhayou.views.TrendOptionAndAmountPickView;

public class TrendProductDetailActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private TrendProductImageViewPagerAdapter adapter;
  
  private TextView productTitle, price, discountPrice, discountPriceCn, textDeliverySpecificDate;
  private TextView discountRate;
  private Button btnWishList, btnShare;
  private Button btnCart, btnBuy, btnSoldOut;
  private LinearLayout productDtailInfo, deliveryInfo, refundInfo;
  private ImageView toggleProductDetailInfo, toggleDeliveryInfo, toggleRefund;
  private LinearLayout containerProductDetailInfo;
  private TextView textDeliveryInfo, textRefundInfo;
  private LinearLayout recommendProductContainer;
  private ProgressBar progressBar;
  
  private LinearLayout productTextContentContainer;
  private WebView productImage;
  private Button btnImageDetail, btnTop;
  private CustomScrollView scrollView;
  
  private RelativeLayout reviewLayout;
  private ProductReviewContainerView reviewContainerView;
  private TrendOptionAndAmountPickView optionAmountPickerView;
  private SharePickView sharePickView;
  
  private String productId;
  private ProductDetailBean productDetail;
  private ApiClient apiClient;
  private ArrayList<String> imageUrls;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_product_detail);
    
    apiClient = new ApiClient(TrendProductDetailActivity.this);
    productId = getIntent().getStringExtra("product_idx");
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_product_detail));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new TrendProductImageViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
    optionAmountPickerView = (TrendOptionAndAmountPickView) findViewById(R.id.option_picker_view);
    reviewLayout = (RelativeLayout) findViewById(R.id.review_layout);
    scrollView = (CustomScrollView) findViewById(R.id.scrollview);
    productTitle = (TextView) findViewById(R.id.text_title);
    price = (TextView) findViewById(R.id.price);
    discountPrice = (TextView) findViewById(R.id.discount_price);
    discountPriceCn = (TextView) findViewById(R.id.discount_china_currency);
    textDeliverySpecificDate = (TextView) findViewById(R.id.text_delivery_specific_date);
    discountRate = (TextView) findViewById(R.id.text_discount_rate);
    
    btnWishList = (Button) findViewById(R.id.btn_wishlist);
    btnShare = (Button) findViewById(R.id.btn_share);
    btnCart = (Button) findViewById(R.id.btn_checkout);
    btnBuy = (Button) findViewById(R.id.btn_my_cart);
    btnImageDetail = (Button) findViewById(R.id.btn_image_detail);
    btnTop = (Button) findViewById(R.id.btn_top);
    btnSoldOut = (Button) findViewById(R.id.btn_soldout);
    
    productDtailInfo = (LinearLayout) findViewById(R.id.product_detail_info);
    toggleProductDetailInfo = (ImageView) findViewById(R.id.toggle_product_detail_info);
    deliveryInfo = (LinearLayout) findViewById(R.id.product_detail_delivery_info);
    toggleDeliveryInfo = (ImageView) findViewById(R.id.toggle_product_detail_delivery_info);
    refundInfo = (LinearLayout) findViewById(R.id.product_detail_refund);
    toggleRefund = (ImageView) findViewById(R.id.toggle_product_detail_refund);
    containerProductDetailInfo = (LinearLayout) findViewById(R.id.container_product_detail_info);
    textDeliveryInfo = (TextView) findViewById(R.id.text_product_detail_delivery_info);
    textRefundInfo = (TextView) findViewById(R.id.text_product_detail_refund);
    
    recommendProductContainer = (LinearLayout) findViewById(R.id.container_product);
    
    productImage = (WebView) findViewById(R.id.image_product_content);
    WebSettings settings = productImage.getSettings();
    settings.setLoadWithOverviewMode(true);
    settings.setUseWideViewPort(true);
    
    productTextContentContainer = (LinearLayout) findViewById(R.id.container_product_detail_text_info);
    
    scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
    {
      
      @Override
      public void onScrollChanged()
      {
        int scrollY = scrollView.getScrollY();
        if (scrollY < 200)
          btnTop.setVisibility(View.GONE);
        
        if (scrollY > 200 && containerProductDetailInfo.getVisibility() == View.VISIBLE)
          btnTop.setVisibility(View.VISIBLE);
        
        if (containerProductDetailInfo.getVisibility() == View.VISIBLE && scrollY < containerProductDetailInfo.getHeight() + 200 && scrollY > 200)
          btnImageDetail.setVisibility(View.VISIBLE);
        else
          btnImageDetail.setVisibility(View.GONE);
      }
    });
    scrollView.setScrollViewListener(new ScrollViewListener()
    {
      @Override
      public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy)
      {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        
        if (diff == 0 && reviewContainerView != null && (reviewContainerView.getVisibility() == View.VISIBLE))
          reviewContainerView.testLoadMore();
      }
    });
    
    new GetProductDetailTask().execute();
    
  }
  
  
  private void displayProductInfo()
  {
    if (productDetail.tempOut)
    {
      btnSoldOut.setVisibility(View.VISIBLE);
      btnCart.setVisibility(View.GONE);
      btnBuy.setVisibility(View.GONE);
      btnWishList.setVisibility(View.GONE);
    }
    else
    {
      btnSoldOut.setVisibility(View.GONE);
      btnCart.setVisibility(View.VISIBLE);
      btnBuy.setVisibility(View.VISIBLE);
    }
    
    imageUrls = new ArrayList<String>();
    for(int i = 0; i <productDetail.mediaUrlArray.size(); i ++)
    {
      imageUrls.add(productDetail.mediaUrlArray.get(i).url);
    }
    adapter.addAll(imageUrls);
    
    productTitle.setText(productDetail.cnName);
    price.setText(getString(R.string.term_won) + " "+ NumberFormatter.addComma(productDetail.salePrice));
    discountPrice.setText(getString(R.string.term_won) + " " + NumberFormatter.addComma(productDetail.discountPrice));
    String ch_price = Global.getCurrencyConvert(this, productDetail.salePrice, productDetail.currencyConvert);
    discountPriceCn.setText(ch_price);
    textDeliverySpecificDate.setVisibility(View.VISIBLE);
    discountRate.setText(productDetail.saleRate + getString(R.string.term_sale_rate));
    textRefundInfo.setText("- 自顾客收到所订购商品之日起(以签收日期为准), 7日之内提供退换货服务。退换货时仅限于同类 产品、同一颜色、同一型号。\n- 以下情况将不提供退换货服务:\n- 商品外包装(包括附带赠品)发生破损现象,并且 影响二次销售时;\n- 商品表面及内部出现使用过的痕迹(包括附带赠 品)或者商品本身破损时;\n- 衣物类商品经过洗涤时;\n- 商品附件、说明书、保修单、标签等有缺失。 若商品有吊牌,吊牌被剪掉或损坏时;");
    textDeliveryInfo.setText("- 本购物商城员工将会按照顾客所提交的订单中的 期望收货时间,按时配送到顾客赴韩后所下榻的酒 店,宾馆等、如顾客外出或暂时不在酒店、宾馆时, 则会委托所下榻酒店或宾馆的工作人员转交给顾客。 - 本购物商城会竭尽全力使整个配送流程可以快 速·准确·顺利完成,如因一些不可抗力(如火山爆 发、台风、地震、海啸等)或韩国公休日等情况下, 不能完全按照顾客期望收货时间配送货物时,敬请 谅解。- 顾客亦可提早在赴韩之前在本购物商城订购商品, 我们会根据顾客所提供的赴韩时间及预计下榻酒店、 宾馆进行配送货物。");
    
    reviewContainerView = new ProductReviewContainerView(TrendProductDetailActivity.this, "place", "");
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.testLoadMore();
    
    for(int i = 0; i < productDetail.inforDescArray.size(); i++)
    {
      addDetailInfo(productDetail.inforDescArray.get(i).name, productDetail.inforDescArray.get(i).conetent);
    }
    
    String productDetailUrl = "https://dev.hanhayou.com/_html/product_detail_desc.php?idx=" + productId;
    productImage.loadUrl(productDetailUrl);
    
    btnCart.setOnClickListener(cartClickListener);
    btnBuy.setOnClickListener(cartClickListener);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        new ToggleWishList().execute(productId);
      }
    });
    btnShare.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        sharePickView.setVisibility(View.VISIBLE);
        sharePickView.view.setVisibility(View.VISIBLE);
        sharePickView.bringToFront();
      }
    });
    btnTop.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
      }
    });
    btnImageDetail.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TrendProductDetailActivity.this, TrendProductImagePopupActivity.class);
        i.putExtra("image_url", "http://gaengs.co.kr/web/2015/g0522/22.jpg");
        startActivity(i);
      }
    });
    productDtailInfo.setOnClickListener(toggleClickListener);
    deliveryInfo.setOnClickListener(toggleClickListener);
    refundInfo.setOnClickListener(toggleClickListener);
    
    recommendProductContainer.removeAllViews();
    for (int i = 0; i < 7; i++)
    {
      ProductJustOneView view = new ProductJustOneView(TrendProductDetailActivity.this);
      view.setBean(new ProductBean());
      recommendProductContainer.addView(view);
    }
  }
  
  
  private void addDetailInfo(String title, final String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_trend_product_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(R.id.text_title);
      TextView textView = (TextView) view.findViewById(R.id.text_description);
      titleView.setText(title);
      textView.setText(text);
      productTextContentContainer.addView(view);
    }
  }
  
  private OnClickListener cartClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      optionAmountPickerView.setVisibility(View.VISIBLE);
      optionAmountPickerView.view.setVisibility(View.VISIBLE);
      optionAmountPickerView.setOptions(null);
      optionAmountPickerView.bringToFront();
    }
  };
  
  private OnClickListener toggleClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == productDtailInfo.getId())
      {
        btnTop.setVisibility(View.VISIBLE);
        btnImageDetail.setVisibility(View.VISIBLE);
        if (!toggleProductDetailInfo.isActivated())
          containerProductDetailInfo.setVisibility(View.VISIBLE);
        else{
          containerProductDetailInfo.setVisibility(View.GONE);
          btnImageDetail.setVisibility(View.GONE);
          btnTop.setVisibility(View.GONE);
        }
        toggleProductDetailInfo.setActivated(!toggleProductDetailInfo.isActivated());
      }
      else if (v.getId() == deliveryInfo.getId())
      {
        if (!toggleDeliveryInfo.isActivated())
          textDeliveryInfo.setVisibility(View.VISIBLE);
        else
          textDeliveryInfo.setVisibility(View.GONE);
        toggleDeliveryInfo.setActivated(!toggleDeliveryInfo.isActivated());
      }
      else if (v.getId() == refundInfo.getId())
      {
        if (!toggleRefund.isActivated())
          textRefundInfo.setVisibility(View.VISIBLE);
        else
          textRefundInfo.setVisibility(View.GONE);
        toggleRefund.setActivated(!toggleRefund.isActivated());
      }
    }
  };
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  /**
   * AsyncTask 
   **/
  private class GetProductDetailTask extends AsyncTask<Void, Void, ProductDetailBean>
  {
    
    @Override
    protected void onPreExecute()
    {
      progressBar.bringToFront();
//      getWindow().setDimAmount(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    @Override
    protected ProductDetailBean doInBackground(Void... params)
    {
      return apiClient.getProductDetail(productId);
    }
    
    @Override
    protected void onPostExecute(ProductDetailBean result)
    {
      super.onPostExecute(result);
      progressBar.setVisibility(View.GONE);
      productDetail = result;
      
      displayProductInfo();
    }
    
  }
  
  private class ToggleWishList extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(TrendProductDetailActivity.this).getUserSeq(), params[0], "product", "W");
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        btnWishList.setActivated(obj.getString("result").equals("INS"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  
}
