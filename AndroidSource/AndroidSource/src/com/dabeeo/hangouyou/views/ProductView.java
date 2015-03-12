package com.dabeeo.hangouyou.views;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;

public class ProductView extends RelativeLayout
{
  private Context context;
  private ProductBean bean;
  
  
  public ProductView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public void setBean(ProductBean bean)
  {
    this.bean = bean;
    init();
  }
  
  
  @SuppressWarnings("unused")
  public void init()
  {
    int resId = R.layout.view_product;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.product_title);
    TextView originalPrice = (TextView) view.findViewById(R.id.original_price);
    TextView discountPrice = (TextView) view.findViewById(R.id.discount_price);
    Button btnWishList = (Button) view.findViewById(R.id.btn_wishlist);
    
    //가데이터
    title.setText("수분크림");
    originalPrice.setText("¥ 150.00");
    discountPrice.setText("¥ 93.00");
    originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    
    if (bean != null)
    {
      title.setText(bean.title);
      originalPrice.setText("¥ " + bean.originalCount);
      discountPrice.setText("¥ " + bean.discountCount);
      originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
    
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        
      }
    });
    addView(view);
  }
}
