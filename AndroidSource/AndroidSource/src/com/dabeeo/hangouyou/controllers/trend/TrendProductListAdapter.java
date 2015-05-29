package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.utils.NumberFormatter;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class TrendProductListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<>();
  private Activity context;
  
  
  public TrendProductListAdapter(Activity context)
  {
    this.context = context;
  }
  
  
  public void add(ProductBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<ProductBean> beans)
  {
    this.beans.addAll(beans);
    notifyDataSetChanged();
  }
  
  
  public void clear()
  {
    this.beans.clear();
    notifyDataSetChanged();
  }
  
  
  @Override
  public int getCount()
  {
    return beans.size();
  }
  
  
  @Override
  public Object getItem(int position)
  {
    return beans.get(position);
  }
  
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  
  @SuppressLint("ViewHolder")
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    ProductBean bean = (ProductBean) beans.get(position);
    int resId = R.layout.list_item_product;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView price = (TextView) view.findViewById(R.id.price);
    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    TextView discountPrice = (TextView) view.findViewById(R.id.discount_price);
    TextView chinaPrice = (TextView) view.findViewById(R.id.discount_china_currency);
    TextView month = (TextView) view.findViewById(R.id.month);
    
    final Button btnWishList = (Button) view.findViewById(R.id.btn_wish_list);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        btnWishList.setActivated(!btnWishList.isActivated());
      }
    });
    title.setText(bean.title);
    discountPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.toString(bean.discountPrice)));
    price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.toString(bean.originalPrice)));
    month.setText("7월");
    chinaPrice.setText("(500￥)");
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
        {
          Intent i = new Intent(context, TrendProductDetailActivity.class);
          context.startActivity(i);
        }
      }
    });
    return view;
  }
}
