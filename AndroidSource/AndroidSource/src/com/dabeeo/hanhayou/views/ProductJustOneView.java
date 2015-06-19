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
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class ProductJustOneView extends RelativeLayout
{
  private Activity context;
  private ProductBean firstBean;
  
  
  public ProductJustOneView(Activity context)
  {
    super(context);
    this.context = context;
  }
  
  
  public void setBean(ProductBean firstBean)
  {
    this.firstBean = firstBean;
    init();
  }
  
  
  public void init()
  {
    int resId = R.layout.view_product_just_one;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    LinearLayout firstContainer = (LinearLayout) view.findViewById(R.id.conatiner_first_bean);
    ImageView firstImageView = (ImageView) view.findViewById(R.id.imageview);
    TextView firstDiscountPrice = (TextView) view.findViewById(R.id.discount_price);
    TextView firstDiscountPriceCn = (TextView) view.findViewById(R.id.discount_price_cn);
    
    //가데이터
    ImageDownloader.displayImage(context, "", firstImageView, null);
    firstContainer.setVisibility(View.VISIBLE);
    firstDiscountPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(30000));
    firstDiscountPriceCn.setText("¥ 93.00");
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
    addView(view);
  }
}
