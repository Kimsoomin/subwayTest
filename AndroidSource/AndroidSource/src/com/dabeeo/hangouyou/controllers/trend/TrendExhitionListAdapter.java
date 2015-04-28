package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendExhibitionBean;
import com.dabeeo.hangouyou.beans.TrendKoreaBean;

public class TrendExhitionListAdapter extends BaseAdapter
{
  private ArrayList<TrendExhibitionBean> beans = new ArrayList<>();
  
  
  public void add(TrendExhibitionBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<TrendExhibitionBean> beans)
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
    TrendExhibitionBean bean = (TrendExhibitionBean) beans.get(position);
    int resId = R.layout.list_item_trend_exhibition;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView discountPrice = (TextView) view.findViewById(R.id.discound_price);
    TextView price = (TextView) view.findViewById(R.id.price);
    Button btnWishList = (Button) view.findViewById(R.id.btn_wish_list);
    
    title.setText(bean.title);
    discountPrice.setText(Integer.toString(bean.discountPrice));
    price.setText(Integer.toString(bean.price));
    
    return view;
  }
}
