package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.ProductDetailBean;
import com.dabeeo.hanhayou.controllers.trend.TrendProductImageViewPagerAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
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
  private LinearLayout containerProductDetailInfo, containerProductRefundInfo, containerProductDeliveryInfo;
  private View productDeliveryUnderLine, productDetailUnderLine;
  private TextView textRefundLink;
  private LinearLayout recommendProductContainer;
  private RelativeLayout progressLayout;
  
  private LinearLayout productTextContentContainer;
  private WebView productImage;
  private Button btnImageDetail, btnTop;
  private CustomScrollView scrollView;
  
  private ProductJustOneView recommendProductView;
  private RelativeLayout reviewLayout;
  private ProductReviewContainerView reviewContainerView;
  private TrendOptionAndAmountPickView optionAmountPickerView;
  private SharePickView sharePickView;
  
  private String productId;
  private boolean isWished = false;
  private String categoryId = "0";
  private int rate = 0;
  private ProductDetailBean productDetail;
  private ApiClient apiClient;
  private ArrayList<String> imageUrls;
  
  public String itemAttribute;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_product_detail);
    
    apiClient = new ApiClient(TrendProductDetailActivity.this);
    productIntentSet();
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_product_detail));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int deviceWidth = metrics.widthPixels;
    
    adapter = new TrendProductImageViewPagerAdapter(this, getSupportFragmentManager(), deviceWidth);
    ViewGroup.LayoutParams p = viewPager.getLayoutParams();
    p.width = deviceWidth;
    p.height = deviceWidth;
    viewPager.setLayoutParams(p);
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
    btnCart = (Button) findViewById(R.id.btn_my_cart);
    btnBuy = (Button) findViewById(R.id.btn_checkout);
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
    productDetailUnderLine = (View) findViewById(R.id.product_detail_info_view);
    containerProductDeliveryInfo = (LinearLayout) findViewById(R.id.product_detail_delivery_layout);
    productDeliveryUnderLine = (View) findViewById(R.id.product_detail_delivery_view);
    containerProductRefundInfo = (LinearLayout) findViewById(R.id.product_detail_refund_layout);
    
    textRefundLink = (TextView) findViewById(R.id.text_refund_link);  
    textRefundLink.setText(Html.fromHtml("<u>"+getString(R.string.term_product_detail_refund_link)+"</u>"));
    textRefundLink.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Uri uri = Uri.parse("https://www.google.co.kr/search?q=com.cklee.hashtags&oq=com.cklee.hashtags&aqs=chrome..69i57.561j0j9&sourceid=chrome&es_sm=93&ie=UTF-8#newwindow=1&q=%EB%8B%A4%EB%B9%84%EC%98%A4");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
      }
    });
    
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
    
    recommendProductView = new ProductJustOneView(TrendProductDetailActivity.this);
    
    new GetProductDetailTask().execute();
  }
  
  public void productIntentSet()
  {
    if(getIntent().hasExtra("product_idx"))
      productId = getIntent().getStringExtra("product_idx");
    
    if(getIntent().hasExtra("product_isWished"))
      isWished = getIntent().getBooleanExtra("product_isWished", false);
    
    if(getIntent().hasExtra("proudct_categoryId"))
    {  
      categoryId = getIntent().getStringExtra("proudct_categoryId");
      if (TextUtils.isEmpty(categoryId))
      {
        categoryId = "0";
      }
    }
    
    if(getIntent().hasExtra("product_rate"))
      rate = getIntent().getIntExtra("product_rate", 0);
  }
  
  @Override
  public void onBackPressed()
  {
    super.onBackPressed();
    if(optionAmountPickerView.getVisibility() == View.VISIBLE)
    {
      optionAmountPickerView.setVisibility(View.GONE);
      optionAmountPickerView.view.setVisibility(View.GONE);
      return;
    }else
    {
      finish();
    }
  }
  
  private void displayProductInfo()
  {
    if (productDetail.tempOut || productDetail.productOptionList.length() == 0)
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
      btnWishList.setVisibility(View.VISIBLE);
      optionAmountPickerView.setOptions(productDetail.productOptionArr, productDetail.productOptionList);
    }
    
    imageUrls = new ArrayList<String>();
    for(int i = 0; i <productDetail.mediaUrlArray.size(); i ++)
    {
      imageUrls.add(productDetail.mediaUrlArray.get(i).url);
    }
    adapter.addAll(imageUrls);
    
    productTitle.setText(productDetail.cnName);
    price.setText(getString(R.string.term_won) + NumberFormatter.addComma(productDetail.salePrice));
    discountPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(productDetail.discountPrice));
    String ch_price = Global.getCurrencyConvert(this, productDetail.salePrice, productDetail.currencyConvert);
    discountPriceCn.setText(ch_price);
    textDeliverySpecificDate.setVisibility(View.VISIBLE);
    discountRate.setText(productDetail.saleRate + getString(R.string.term_sale_rate));
    
    reviewContainerView = new ProductReviewContainerView(TrendProductDetailActivity.this, "product", productId, rate);
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.testLoadMore();
    
    for(int i = 0; i < productDetail.inforDescArray.size(); i++)
    {
      addDetailInfo(productDetail.inforDescArray.get(i).name, productDetail.inforDescArray.get(i).conetent);
    }
    
    final String productDetailUrl = "http://dev.hanhayou.com/_html/product.detail.desc.php?idx=" + productId;
    productImage.loadUrl(productDetailUrl);
    
    btnCart.setOnClickListener(cartClickListener);
    btnBuy.setOnClickListener(cartClickListener);
    btnWishList.setActivated(isWished);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if(PreferenceManager.getInstance(TrendProductDetailActivity.this).isLoggedIn())
        {
          new ToggleWishList().execute(productId);
        }else
        {
          new AlertDialogManager(TrendProductDetailActivity.this).showNeedLoginDialog(-1);
        }
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
        i.putExtra("image_url", productDetailUrl);
        startActivity(i);
      }
    });
    
    productDtailInfo.setOnClickListener(toggleClickListener);
    deliveryInfo.setOnClickListener(toggleClickListener);
    refundInfo.setOnClickListener(toggleClickListener);
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
      if(optionAmountPickerView.getOptionSelected())
      {
        if(PreferenceManager.getInstance(TrendProductDetailActivity.this).isLoggedIn())
        {
          setItemAttributes(optionAmountPickerView.optionColor, optionAmountPickerView.optionSize, optionAmountPickerView.amount);
          if(v.getId() == R.id.btn_my_cart)
          {
            new AddCartTask().execute();
          }else if(v.getId() == R.id.btn_checkout)
          {
            String url = "newOrderNow?_hgy_token="+PreferenceManager.getInstance(TrendProductDetailActivity.this).getUserSeq()
                + "&product_id=" + productId + "&itemAttributesList=" + itemAttribute;
            Intent i = new Intent(TrendProductDetailActivity.this, TrendCartActivity.class);
            i.putExtra("cart_parameter", url);
            startActivity(i);
            optionAmountPickerView.view.setVisibility(View.GONE);
            optionAmountPickerView.setVisibility(View.GONE);
            optionAmountPickerView.initSpinner();
          }
        }else
        {
          new AlertDialogManager(TrendProductDetailActivity.this).showNeedLoginDialog(-1);
        }
      }else
      {
        if(optionAmountPickerView.view.getVisibility() == View.VISIBLE)
        {
          new AlertDialogManager(TrendProductDetailActivity.this).showAlertDialog(getString(R.string.term_alert), getString(R.string.term_product_select_option), 
              getString(R.string.term_ok), null, new AlertListener()
          {
            
            @Override
            public void onPositiveButtonClickListener()
            { 
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
            }
          });
        }
      }
      optionAmountPickerView.setVisibility(View.VISIBLE);
      optionAmountPickerView.view.setVisibility(View.VISIBLE);
      optionAmountPickerView.bringToFront();
    }
  };
  
  
  public void setItemAttributes(String optionColor, String optionSize, int amount)
  {
    itemAttribute = "[{itemAttributes={\"" + "color" +"\"" + ":\"" + optionColor + "\"";
    if(optionSize == null)
      itemAttribute = itemAttribute + "},quantity=" + amount+ "}]";
    else
      itemAttribute = itemAttribute + ",\"" + "size" + "\"" + ":\"" + optionSize + "\"},quantity="+amount+"}]";
    BlinkingCommon.smlLibDebug("asdfasfdasfdasfdasfdas", "itemAttribute : " + itemAttribute);
  }
  
  
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
        {
          containerProductDetailInfo.setVisibility(View.VISIBLE);
          productDetailUnderLine.setVisibility(View.VISIBLE);
        }
        else
        {
          containerProductDetailInfo.setVisibility(View.GONE);
          btnImageDetail.setVisibility(View.GONE);
          btnTop.setVisibility(View.GONE);
          productDetailUnderLine.setVisibility(View.GONE);
        }
        toggleProductDetailInfo.setActivated(!toggleProductDetailInfo.isActivated());
      }
      else if (v.getId() == deliveryInfo.getId())
      {
        if (!toggleDeliveryInfo.isActivated())
        {
          containerProductDeliveryInfo.setVisibility(View.VISIBLE);
          productDeliveryUnderLine.setVisibility(View.VISIBLE);
        }
        else
        {
          containerProductDeliveryInfo.setVisibility(View.GONE);
          productDeliveryUnderLine.setVisibility(View.GONE);
        }
        toggleDeliveryInfo.setActivated(!toggleDeliveryInfo.isActivated());
      }
      else if (v.getId() == refundInfo.getId())
      {
        if (!toggleRefund.isActivated())
          containerProductRefundInfo.setVisibility(View.VISIBLE);
        else
          containerProductRefundInfo.setVisibility(View.GONE);
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
  
  
  public void createAddCartAlert(boolean addCart)
  {
    String title = getString(R.string.term_alert);
    String message = "";
    String okString = "";
    String cancelString = "";
    if(addCart)
    {
      message = getString(R.string.term_product_add_cart);
      okString = getString(R.string.term_product_cart_move);
      cancelString = getString(R.string.term_product_shopping);
      new AlertDialogManager(TrendProductDetailActivity.this).showAlertDialog(title, message, okString, cancelString, new AlertListener()
      {
        @Override
        public void onPositiveButtonClickListener()
        {
          startActivity(new Intent(TrendProductDetailActivity.this, TrendCartActivity.class));
        }
        
        @Override
        public void onNegativeButtonClickListener()
        {
        }
      });
    }
    else
    {
      message = getString(R.string.term_product_fail_cart);
      okString = getString(R.string.term_ok);
      new AlertDialogManager(TrendProductDetailActivity.this).showAlertDialog(title, message, okString, null, null);
    }      
  }
  
  /**
   * AsyncTask 
   **/
  private class GetProductDetailTask extends AsyncTask<Void, Void, ProductDetailBean>
  {
    
    @Override
    protected void onPreExecute()
    {
      progressLayout.bringToFront();
      progressLayout.setVisibility(View.VISIBLE);
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
      progressLayout.setVisibility(View.GONE);
      productDetail = result;
      
      if(productDetail != null)
      {
        displayProductInfo();
        new GetProductDetailRecomendProductTask().execute();
      }
      else
      {
        new AlertDialogManager(TrendProductDetailActivity.this).showAlertDialog(getString(R.string.term_alert), "상품 정보 오류", 
            getString(R.string.term_ok), null, new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
            finish();
          }
          
          @Override
          public void onNegativeButtonClickListener()
          { 
          }
        });
      }
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
        if(obj.getString("result").equals("INS"))
        {
          Toast.makeText(TrendProductDetailActivity.this, getString(R.string.msg_add_wishlist), Toast.LENGTH_SHORT).show();;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class AddCartTask extends AsyncTask<String, Void, JSONObject>
  {
    @Override
    protected void onPreExecute()
    {
      optionAmountPickerView.setInitSelect();
      super.onPreExecute();
    }
    
    @Override
    protected JSONObject doInBackground(String... params)
    {
      HashMap<String, String> body = new HashMap<String, String>();
      body.put("product_id", productId);
      body.put("itemAttributesList", itemAttribute);
      body.put("_hgy_token", PreferenceManager.getInstance(TrendProductDetailActivity.this).getUserSeq());
      return apiClient.addCart(body);
    }
    
    @Override
    protected void onPostExecute(JSONObject result)
    {
      super.onPostExecute(result);
      optionAmountPickerView.view.setVisibility(View.GONE);
      optionAmountPickerView.setVisibility(View.GONE);
      if(result != null)
      {
        try
        {
          if (result.has("result"))
          {
            JSONObject obj = result.getJSONObject("result");
            if(obj.getInt("status_code") == 0)
              createAddCartAlert(true);
            else
              createAddCartAlert(false);
          }
        }
        catch (Exception e)
        {
          BlinkingCommon.smlLibPrintException("TrendProductDetail", "e : " + e);
        }
      }else
        createAddCartAlert(false);
    }
  }
  
  private class GetProductDetailRecomendProductTask extends AsyncTask<Void, Void, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getCategoryProductList(categoryId, 3);
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      recommendProductContainer.removeAllViews();
      try
      {
        ArrayList<ProductBean> productList = new ArrayList<ProductBean>();
        JSONObject obj = new JSONObject(result.response);
        if(obj.has("product"))
        {
          JSONArray objInArr = obj.getJSONArray("product");
          for(int i = 0; i < objInArr.length(); i++)
          {
            ProductBean product = new ProductBean();
            JSONObject productobj = objInArr.getJSONObject(i);
            product.setJSONObject(productobj);
            productList.add(product);
          }
        }
        
        recommendProductView.setBean(productList);
        recommendProductContainer.addView(recommendProductView);
      }
      catch (Exception e)
      {
        BlinkingCommon.smlLibPrintException("TrendProductDetailActivity", "e : " + e);
      }
    }
  }
}
