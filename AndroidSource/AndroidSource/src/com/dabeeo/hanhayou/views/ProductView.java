package com.dabeeo.hanhayou.views;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.squareup.picasso.Picasso;

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
    TextView firstCnPrice = (TextView) view.findViewById(R.id.cn_price);
    TextView firstDiscountRate = (TextView) view.findViewById(R.id.discount_rate);
    
    LinearLayout secondContainer = (LinearLayout) view.findViewById(R.id.conatiner_second_bean);
    ImageView secondImageView = (ImageView) view.findViewById(R.id.imageview_second);
    TextView secondTitle = (TextView) view.findViewById(R.id.product_title_second);
    TextView secondOriginalPrice = (TextView) view.findViewById(R.id.original_price_second);
    TextView secondCnPrice = (TextView) view.findViewById(R.id.cn_price_second);
    TextView secondDiscountRate = (TextView) view.findViewById(R.id.discount_rate_second);
    
    String ch_price = "";
    if(firstBean != null)
    {
      firstContainer.setVisibility(View.VISIBLE);
      Picasso.with(context).load(firstBean.imageUrl).fit().centerCrop().into(firstImageView);
      firstImageView.setImageResource(R.drawable.sample_place_detail_shopping_list1);
      firstContainer.setVisibility(View.VISIBLE);
      firstTitle.setText(firstBean.name);
      firstOriginalPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(firstBean.priceSale)));
      ch_price = Global.getCurrencyConvert(context, Integer.parseInt(firstBean.priceSale), Float.parseFloat(firstBean.currencyConvert));
      firstCnPrice.setText(ch_price);
      firstDiscountRate.setText(firstBean.saleRate + context.getString(R.string.term_sale_rate));
      firstContainer.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent i = new Intent(context, TrendProductDetailActivity.class);
          i.putExtra("product_idx", firstBean.id);
          i.putExtra("product_isWished", firstBean.isWished);
          i.putExtra("proudct_categoryId", firstBean.categoryId);
          i.putExtra("product_rate", firstBean.rate);
          context.startActivity(i);
        }
      });
    }else
    {
      firstContainer.setVisibility(View.GONE);
    }
    
    if(secondBean != null)
    {
      secondContainer.setVisibility(View.VISIBLE);
      Picasso.with(context).load(secondBean.imageUrl).fit().centerCrop().into(secondImageView);
      secondTitle.setText(secondBean.name);
      secondOriginalPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(secondBean.priceSale)));
      ch_price = Global.getCurrencyConvert(context, Integer.parseInt(secondBean.priceSale), Float.parseFloat(secondBean.currencyConvert));
      secondCnPrice.setText(ch_price);
      secondDiscountRate.setText(secondBean.saleRate + context.getString(R.string.term_sale_rate));
      secondContainer.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent i = new Intent(context, TrendProductDetailActivity.class);
          i.putExtra("product_idx", secondBean.id);
          i.putExtra("product_isWished", secondBean.isWished);
          i.putExtra("proudct_categoryId", secondBean.categoryId);
          i.putExtra("product_rate", secondBean.rate);
          context.startActivity(i);
        }
      });
    }else
    {
      secondContainer.setVisibility(View.GONE);
    }
    
    addView(view);
  }
}
