package com.dabeeo.hanhayou.views;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.squareup.picasso.Picasso;



public class ProductJustOneView extends RelativeLayout
{
  private Activity context;
  private ProductBean firstProduct, secondProduct, thirdProduct;
  private LinearLayout firstProductLayout, secondProductLayout, thirdProductLayout;
  private ImageView firstProudctImage, secondProductImage, thirdProductImage;
  private TextView firstPrice, secondPrice, thirdPrice;
  private TextView firstChinaPrice, secondChinaPrice, thirdChinaPrice;
    
  public ProductJustOneView(Activity context)
  {
    super(context);
    this.context = context;
  }
  
  
  public void setBean(ArrayList<ProductBean> productList)
  {
    for(int i = 0; i < productList.size(); i++)
    {
      if(i == 0)
        firstProduct = productList.get(i);
      if(i == 1)
        secondProduct = productList.get(i);
      if(i == 2)
        thirdProduct = productList.get(i);
    }
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_product_just_one;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    firstProductLayout = (LinearLayout) view.findViewById(R.id.first_product_layout);
    firstProudctImage = (ImageView) view.findViewById(R.id.first_product_image);
    firstPrice = (TextView) view.findViewById(R.id.first_price);
    firstChinaPrice = (TextView) view.findViewById(R.id.first_china_price);
    
    secondProductLayout = (LinearLayout) view.findViewById(R.id.second_product_layout);
    secondProductImage = (ImageView) view.findViewById(R.id.second_product_image);
    secondPrice = (TextView) view.findViewById(R.id.second_price);
    secondChinaPrice = (TextView) view.findViewById(R.id.second_china_price);
    
    thirdProductLayout = (LinearLayout) view.findViewById(R.id.third_product_layout);
    thirdProductImage = (ImageView) view.findViewById(R.id.third_product_image);
    thirdPrice = (TextView) view.findViewById(R.id.third_price);
    thirdChinaPrice = (TextView) view.findViewById(R.id.third_china_price);
    
    if(firstProduct != null)
    {
      Picasso.with(context).load(firstProduct.imageUrl).fit().centerCrop().into(firstProudctImage);
      firstPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(firstProduct.priceSale)));
      firstChinaPrice.setText(Global.getCurrencyConvert(context, Integer.parseInt(firstProduct.priceSale), Float.parseFloat(firstProduct.currencyConvert)));
    }else
    {
      firstProductLayout.setVisibility(View.GONE);
    }
    
    if(secondProduct != null)
    {
      Picasso.with(context).load(secondProduct.imageUrl).fit().centerCrop().into(secondProductImage);
      secondPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(secondProduct.priceSale)));
      secondChinaPrice.setText(Global.getCurrencyConvert(context, Integer.parseInt(secondProduct.priceSale), Float.parseFloat(secondProduct.currencyConvert)));
    }else
    {
      secondProductLayout.setVisibility(View.GONE);
    }
    
    if(thirdProduct != null)
    {
      Picasso.with(context).load(thirdProduct.imageUrl).fit().centerCrop().into(thirdProductImage);
      thirdPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(thirdProduct.priceSale)));
      thirdChinaPrice.setText(Global.getCurrencyConvert(context, Integer.parseInt(thirdProduct.priceSale), Float.parseFloat(thirdProduct.currencyConvert)));
    }else
    {
      thirdProductLayout.setVisibility(View.GONE);
    }
    
    addView(view);
  }
}
