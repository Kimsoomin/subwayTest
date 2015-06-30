package com.dabeeo.hanhayou.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class ProductView extends RelativeLayout
{
  private Activity context;
  private ProductBean firstBean;
  private ProductBean secondBean;
  
  
  public ProductView(Activity context)
  {
    super(context);
    this.context = context;
  }
  
  
  public void setBean(ProductBean firstBean, ProductBean secondBean)
  {
    this.firstBean = firstBean;
    this.secondBean = secondBean;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_product;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    LinearLayout firstContainer = (LinearLayout) view.findViewById(R.id.conatiner_first_bean);
    ImageView firstImageView = (ImageView) view.findViewById(R.id.imageview);
    TextView firstTitle = (TextView) view.findViewById(R.id.product_title);
    TextView firstOriginalPrice = (TextView) view.findViewById(R.id.original_price);
    TextView firstDiscountPrice = (TextView) view.findViewById(R.id.discount_price);
    TextView firstDiscountPriceCn = (TextView) view.findViewById(R.id.discount_price_cn);
    
    LinearLayout secondContainer = (LinearLayout) view.findViewById(R.id.conatiner_second_bean);
    ImageView secondImageView = (ImageView) view.findViewById(R.id.imageview_second);
    TextView secondTitle = (TextView) view.findViewById(R.id.product_title_second);
    TextView secondOriginalPrice = (TextView) view.findViewById(R.id.original_price_second);
    TextView secondDiscountPrice = (TextView) view.findViewById(R.id.discount_price_second);
    TextView secondDiscountPirceCn = (TextView) view.findViewById(R.id.discount_price_second_cn);
    //가데이터
//    ImageDownloader.displayImage(context, "", firstImageView, null);
    firstImageView.setImageResource(R.drawable.sample_place_detail_shopping_list1);
    firstContainer.setVisibility(View.VISIBLE);
    firstTitle.setText("IOPE/亦博 气垫BB霜粉底霜 气垫粉饼保湿美白遮瑕SPF50+/PA+++");
    firstOriginalPrice.setText("₩ 77,000");
    firstDiscountPrice.setText("₩ 49,500");
    firstDiscountPriceCn.setText("(￥275)");
    firstOriginalPrice.setPaintFlags(firstOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    firstContainer.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
          context.startActivity(new Intent(context, TrendProductDetailActivity.class));
      }
    });
    secondContainer.setVisibility(View.VISIBLE);
//    ImageDownloader.displayImage(context, "", secondImageView, null);
    secondImageView.setImageResource(R.drawable.sample_place_detail_shopping_list2);
    secondTitle.setText("[ISA KNOX伊诺姿]365长效防晒隔离霜70ml");
    secondOriginalPrice.setText("₩ 91,000");
    secondDiscountPrice.setText("₩ 87,000");
    secondDiscountPirceCn.setText("(￥485)");
    secondOriginalPrice.setPaintFlags(secondOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    secondContainer.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
          context.startActivity(new Intent(context, TrendProductDetailActivity.class));
      }
    });
    
//    if (firstBean != null)
//    {
//      firstTitle.setText(firstBean.title);
//      firstOriginalPrice.setText("¥ " + firstBean.originalPrice);
//      firstDiscountPrice.setText("¥ " + firstBean.discountPrice);
//      firstOriginalPrice.setPaintFlags(firstOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//    }
    addView(view);
  }
}
