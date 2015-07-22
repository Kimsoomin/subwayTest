package com.dabeeo.hanhayou.activities.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.trend.TrendProductImageViewPagerAdapter;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.views.CustomScrollView;
import com.dabeeo.hanhayou.views.ProductJustOneView;
import com.dabeeo.hanhayou.views.ProductReviewContainerView;
import com.dabeeo.hanhayou.views.SharePickView;
import com.dabeeo.hanhayou.views.TrendOptionAndAmountPickView;
import com.dabeeo.hanhayou.views.CustomScrollView.ScrollViewListener;

public class TrendProductDetailActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private TrendProductImageViewPagerAdapter adapter;
  
  private TextView productTitle, price, discountPrice, discountPriceCn, textDeliverySpecificDate;
  private TextView discountRate;
  private Button btnWishList, btnShare;
  private Button btnCart, btnBuy, btnSoldOut;
  private ImageView toggleProductDetailInfo, toggleDeliveryInfo, toggleRefund;
  private LinearLayout containerProductDetailInfo;
  private TextView textDeliveryInfo, textRefundInfo;
  private LinearLayout recommendProductContainer;
  
  private LinearLayout productTextContentContainer;
  private WebView productImage;
  private Button btnImageDetail, btnTop;
  private CustomScrollView scrollView;
  
  private RelativeLayout reviewLayout;
  private ProductReviewContainerView reviewContainerView;
  private TrendOptionAndAmountPickView optionAmountPickerView;
  private SharePickView sharePickView;
  
  //품절 처리 추후 API연동되면 빠져야 함
  private boolean isSoldOut = false;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_product_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_product_detail));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    isSoldOut = getIntent().getBooleanExtra("is_sold_out", false);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new TrendProductImageViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    ArrayList<String> imageUrls = new ArrayList<>();
    imageUrls.add("http://image.gsshop.com/image/16/54/16545567_O1.jpg");
    imageUrls.add("http://image.gsshop.com/image/16/68/16680935_O1.jpg");
    imageUrls.add("http://image.gsshop.com/image/16/36/16368125_O1.jpg");
    adapter.addAll(imageUrls);
    
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
    
    toggleProductDetailInfo = (ImageView) findViewById(R.id.toggle_product_detail_info);
    toggleDeliveryInfo = (ImageView) findViewById(R.id.toggle_product_detail_delivery_info);
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
    displayProductInfo();
  }
  
  
  private void displayProductInfo()
  {
    if (isSoldOut)
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
    productTitle.setText("[헤라]천송이(전지현)립스틱-세럼인텐스립스 립스틱세럼");
    price.setText(getString(R.string.term_won) + NumberFormatter.addComma(15000));
    discountPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(12000));
    discountPriceCn.setText("(" + getString(R.string.term_yuan) + "500)");
    textDeliverySpecificDate.setVisibility(View.VISIBLE);
    discountRate.setText("6월");
    textRefundInfo.setText("- 自顾客收到所订购商品之日起(以签收日期为准), 7日之内提供退换货服务。退换货时仅限于同类 产品、同一颜色、同一型号。\n- 以下情况将不提供退换货服务:\n- 商品外包装(包括附带赠品)发生破损现象,并且 影响二次销售时;\n- 商品表面及内部出现使用过的痕迹(包括附带赠 品)或者商品本身破损时;\n- 衣物类商品经过洗涤时;\n- 商品附件、说明书、保修单、标签等有缺失。 若商品有吊牌,吊牌被剪掉或损坏时;");
    textDeliveryInfo.setText("- 本购物商城员工将会按照顾客所提交的订单中的 期望收货时间,按时配送到顾客赴韩后所下榻的酒 店,宾馆等、如顾客外出或暂时不在酒店、宾馆时, 则会委托所下榻酒店或宾馆的工作人员转交给顾客。 - 本购物商城会竭尽全力使整个配送流程可以快 速·准确·顺利完成,如因一些不可抗力(如火山爆 发、台风、地震、海啸等)或韩国公休日等情况下, 不能完全按照顾客期望收货时间配送货物时,敬请 谅解。- 顾客亦可提早在赴韩之前在本购物商城订购商品, 我们会根据顾客所提供的赴韩时间及预计下榻酒店、 宾馆进行配送货物。");
    
    reviewContainerView = new ProductReviewContainerView(TrendProductDetailActivity.this, "place", "");
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.testLoadMore();
    
    addDetailInfo("상품코드", "8213124515");
    addDetailInfo("제품사양", "350g");
    addDetailInfo("사용방법", "페이스 메이크업 후");
    addDetailInfo("제품/유통", "한국/아모레퍼시픽");
    
    String data = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" "
        + "name=\"viewport\" content=\"width=device-width, user-scalable=no\"/></head>"
        + "<body><table width=\"700\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n <tbody>\n  <tr> \n   <td colspan=\"3\" height=\"20\"></td>"
        + " \n  </tr> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\" align=\"center\">&nbsp;</td> \n   "
        + "<td width=\"50\"></td> \n  </tr> \n  <tr> \n   <td colspan=\"3\" height=\"20\"></td> \n  </tr> \n  <tr> \n   "
        + "<td colspan=\"3\" align=\"center\" valign=\"top\"> \n    <table width=\"660\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" "
        + "bgcolor=\"#dfdede\"> \n     <tbody>\n      <tr> \n       <td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, "
        + "20px\"><font color=\"black\"><span style=\"font-size:10pt;\">&nbsp;</span></font><span class=\"style10\" style=\"font-size:10pt;\">"
        + "<font color=\"black\">기본구성</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\">"
        + "<font color=\"black\" face=\"굴림\"><span style=\"font-size:10pt;\">&nbsp;가방</span></font><span class=\"style19\" style=\"font-size:10pt;\">"
        + "<font color=\"black\" face=\"굴림\"> 1종, 더스트백</font></span></td> \n      </tr> \n      <tr> \n       <td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, 20px\">"
        + "<font color=\"black\"><span style=\"font-size:10pt;\">&nbsp;</span></font><span class=\"style10\" style=\"font-size:10pt;\">"
        + "<font color=\"black\">원산지</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\">"
        + "<font color=\"black\" face=\"굴림\"><span style=\"font-size:10pt;\">&nbsp;ITALY</span></font>"
        + "</td> \n      </tr> \n      <tr> \n       <td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, 20px\">"
        + "<font color=\"black\"><span style=\"font-size:10pt;\">&nbsp;</span></font><span class=\"style10\" style=\"font-size:10pt;\">"
        + "<font color=\"black\">제조사</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\">"
        + "<font color=\"black\" face=\"굴림\"><span style=\"font-size:10pt;\">&nbsp;</span></font>"
        + "<span style=\"font-size:10pt;\">살바토레 페라가모(Salvatore Ferragamo)</span></td> \n      </tr> \n      <tr> \n       "
        + "<td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, 20px\"><font color=\"black\"><span style=\"font-size:10pt;\">&nbsp;</span></font><span class=\"style10\" style=\"font-size:10pt;\"><font color=\"black\">색상</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\"><font color=\"black\" face=\"굴림\"><span style=\"font-size:10pt;\">&nbsp;</span></font><span style=\"font-size:10pt;\">블랙, 브라운, 다크그린, 퍼플</span></td> \n      </tr> \n      <tr> \n       <td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, 20px\"><span class=\"style10\" style=\"font-size:10pt;\"><font color=\"black\">사이즈 / 크기</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\"> <p><span style=\"font-size:10pt;\">&nbsp;가로 40cm * 세로 36cm * 폭 14cm / 손잡이 높이 17cm)</span></p> </td> \n      </tr> \n      <tr> \n       <td width=\"120\" valign=\"top\" bgcolor=\"#f6f6f6\" style=\"padding:8px, 20px\"><span class=\"style10\" style=\"font-size:10pt;\"><font color=\"black\">재질/소재</font></span></td> \n       <td width=\"540\" valign=\"top\" bgcolor=\"#ffffff\" style=\"padding:8px, 20px\"> <p><span style=\"font-size:10pt;\">&nbsp;카프 스킨(송아지 가죽)</span></p> </td> \n      </tr> \n     </tbody>\n    </table> </td> \n  </tr> \n  <tr> \n   <td colspan=\"3\" height=\"50\"></td> \n  </tr> \n  <!--상세이미지 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"> \n    <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n     <tbody>\n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n      </tr> \n      <tr> \n       <td width=\"10\" bgcolor=\"#ffc8ce\"></td> \n       <td width=\"550\" valign=\"top\"> <p align=\"center\"><img src=\"http://image.gsshop.com/image/11/86/11869565_1378819351093.jpg\" align=\"bottom\" width=\"364\" height=\"550\" border=\"0\" alt=\"사본 - L1016072.jpg\" /></p></td> \n       <td width=\"40\" valign=\"top\" bgcolor=\"#ffc8ce\">&nbsp; <p>&nbsp;</p> </td> \n      </tr> \n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n      </tr> \n     </tbody>\n    </table> </td> \n   <td width=\"50\">&nbsp;</td> \n  </tr> \n  <!--상세이미지 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"20\"> <p>&nbsp;</p> </td> \n  </tr> \n  <!--상세설명 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"> \n    <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n     <tbody>\n      <tr> \n       <td width=\"10\" bgcolor=\"#ffc8ce\"></td> \n       <td width=\"550\" valign=\"top\"><img src=\"http://image.gsshop.com/image/11/86/11869565_1378819351453.jpg\" align=\"bottom\" width=\"550\" height=\"507\" border=\"0\" alt=\"사본 -파니사블랙.jpg\" /></td> \n       <td width=\"40\" valign=\"top\" bgcolor=\"#ffc8ce\">&nbsp; <p>&nbsp;</p> </td> \n      </tr> \n     </tbody>\n    </table> </td> \n   <td width=\"50\"></td> \n  </tr> \n  <tr> \n   <td colspan=\"3\" height=\"10\"></td> \n  </tr> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"><font size=\"3\" color=\"#c3566d\"><strong>블랙</strong></font></td> \n   <td width=\"50\"></td> \n  </tr> \n  <!--상세설명 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"50\"></td> \n  </tr> \n  <!--상세이미지 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"> \n    <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n     <tbody>\n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n      </tr> \n      <tr> \n       <td width=\"10\" bgcolor=\"#ffc8ce\"></td> \n       <td width=\"550\" valign=\"top\"> <p><img src=\"http://image.gsshop.com/image/11/86/11869565_1378819351781.jpg\" align=\"bottom\" width=\"550\" height=\"509\" border=\"0\" alt=\"사본 -파니사브라운.jpg\" /></p> </td> \n       <td width=\"40\" valign=\"top\" bgcolor=\"#ffc8ce\">&nbsp;</td> \n      </tr> \n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n      </tr> \n     </tbody>\n    </table> </td> \n   <td width=\"50\"></td> \n  </tr> \n  <!--상세이미지 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"20\"></td> \n  </tr> \n  <!--상세설명 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"><font size=\"3\" color=\"#c3566d\"><strong>브라운</strong></font></td> \n   <td width=\"50\"></td> \n  </tr> \n  <tr> \n   <td colspan=\"3\" height=\"10\"></td> \n  </tr> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\">&nbsp;</td> \n   <td width=\"50\"></td> \n  </tr> \n  <!--상세설명 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"50\"></td> \n  </tr> \n  <!--상세이미지 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"> \n    <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n     <tbody>\n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"> <p>&nbsp;</p> </td> \n      </tr> \n      <tr> \n       <td width=\"10\" bgcolor=\"#ffc8ce\"></td> \n       <td width=\"550\" valign=\"top\"> <p><img src=\"http://image.gsshop.com/image/11/86/11869565_1378819406281.jpg\" align=\"bottom\" width=\"550\" height=\"484\" border=\"0\" alt=\"사본 -파니사그린.jpg\" /></p> </td> \n       <td width=\"40\" valign=\"top\" bgcolor=\"#ffc8ce\">&nbsp;</td> \n      </tr> \n      <tr> \n       <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n      </tr> \n     </tbody>\n    </table> </td> \n   <td width=\"50\"></td> \n  </tr> \n  <!--상세이미지 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"20\"></td> \n  </tr> \n  <!--상세설명 시작--> \n  <tr> \n   <td width=\"50\"></td> \n   <td width=\"600\" valign=\"top\"><font size=\"3\" color=\"#c3566d\"><strong>다크그린</strong></font></td> \n   <td width=\"50\"></td> \n  </tr> \n  <tr> \n   <td colspan=\"3\" height=\"59\"></td> \n  </tr> \n  <!--상세설명 끝--> \n  <tr> \n   <td colspan=\"3\" height=\"50\">&nbsp; \n    <table width=\"700\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n     <!--상세설명 끝--> \n     <tbody>\n      <tr> \n       <td colspan=\"3\" height=\"50\"></td> \n      </tr> \n      <!--상세이미지 시작--> \n      <tr> \n       <td width=\"50\"></td> \n       <td width=\"600\" valign=\"top\"> \n        <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"> \n         <tbody>\n          <tr> \n           <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"> <p>&nbsp;</p> </td> \n          </tr> \n          <tr> \n           <td width=\"10\" bgcolor=\"#ffc8ce\"></td> \n           <td width=\"550\" valign=\"top\"> <p><img src=\"http://image.gsshop.com/image/11/86/11869565_1378819406609.jpg\" align=\"bottom\" width=\"550\" height=\"491\" border=\"0\" alt=\"사본 -파니사퍼플.jpg\" /></p> </td> \n           <td width=\"40\" valign=\"top\" bgcolor=\"#ffc8ce\">&nbsp;</td> \n          </tr> \n          <tr> \n           <td colspan=\"3\" height=\"10\" bgcolor=\"#ffc8ce\"></td> \n          </tr> \n         </tbody>\n        </table> </td> \n       <td width=\"50\"></td> \n      </tr> \n      <!--상세이미지 끝--> \n      <tr> \n       <td colspan=\"3\" height=\"20\"></td> \n      </tr> \n      <!--상세설명 시작--> \n      <tr> \n       <td width=\"50\"></td> \n       <td width=\"600\" valign=\"top\"><font size=\"3\" color=\"#c3566d\"><strong>퍼플</strong></font></td> \n       <td width=\"50\"></td> \n      </tr> \n     </tbody>\n    </table> </td> \n  </tr> \n </tbody>\n</table></body></html>";
    productImage.loadData(data, "text/html", "utf-8");
    
    btnCart.setOnClickListener(cartClickListener);
    btnBuy.setOnClickListener(cartClickListener);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        btnWishList.setActivated(!btnWishList.isActivated());
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
    toggleProductDetailInfo.setOnClickListener(toggleClickListener);
    toggleDeliveryInfo.setOnClickListener(toggleClickListener);
    toggleRefund.setOnClickListener(toggleClickListener);
    
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
      if (v.getId() == toggleProductDetailInfo.getId())
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
      else if (v.getId() == toggleDeliveryInfo.getId())
      {
        if (!toggleDeliveryInfo.isActivated())
          textDeliveryInfo.setVisibility(View.VISIBLE);
        else
          textDeliveryInfo.setVisibility(View.GONE);
        toggleDeliveryInfo.setActivated(!toggleDeliveryInfo.isActivated());
      }
      else if (v.getId() == toggleRefund.getId())
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
}
