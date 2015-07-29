package com.dabeeo.hanhayou.views;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.squareup.picasso.Picasso;

public class ProductRecommendScheduleView extends RelativeLayout
{
  private Context context;
  private ImageView imageView;
  private TextView title, price, chinaPrice, saleRate, btnWishList;
  
  private ApiClient apiClient;
  
  
  public ProductRecommendScheduleView(Context context)
  {
    super(context);
    this.context = context;
    apiClient = new ApiClient(context);
    init();
  }
  
  
  public ProductRecommendScheduleView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init();
  }
  
  
  public ProductRecommendScheduleView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init();
  }
  
  
  public void setBean(ProductBean bean)
  {
    Picasso.with(context).load(bean.imageUrl).fit().centerCrop().into(imageView);
    title.setText(bean.name);
    price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceSale));
    String ch_price = "";
    int calChPrice = (int)(Integer.parseInt(bean.priceSale)/Float.parseFloat(bean.currencyConvert));
    ch_price = "(대략 "+ context.getString(R.string.term_yuan) + ""+ NumberFormatter.addComma(calChPrice) + ")";
    chinaPrice.setText(""+ch_price);
    saleRate.setText(bean.saleRate + context.getString(R.string.term_sale_rate));
    final String id = bean.id;
    btnWishList.setActivated(bean.isWished);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new ToggleWishList().execute(id);
      }
    });
  }
  
  
  public void init()
  {
    int resId = R.layout.view_product_recommend_in_schedule;
    View view = LayoutInflater.from(context).inflate(resId, null);
    view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    
    imageView = (ImageView) view.findViewById(R.id.imageview);
    title = (TextView) view.findViewById(R.id.text_title);
    price = (TextView) view.findViewById(R.id.price);
    chinaPrice = (TextView) view.findViewById(R.id.discount_price);
    saleRate = (TextView) view.findViewById(R.id.discount_china_currency);
    btnWishList = (TextView) view.findViewById(R.id.btn_wish_list);
    
    addView(view);
  }
  
  private class ToggleWishList extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(context).getUserSeq(), params[0], "product", "W");
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
