package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendKoreaBean;

public class TrendKoreaListAdapter extends BaseAdapter
{
  private ArrayList<TrendKoreaBean> beans = new ArrayList<>();
  
  
  public void add(TrendKoreaBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<TrendKoreaBean> beans)
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
    TrendKoreaBean bean = (TrendKoreaBean) beans.get(position);
    int resId = R.layout.list_item_trend_korea;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
//    TextView category = (TextView) view.findViewById(R.id.category);
    
    title.setText(bean.title);
//    category.setText(bean.category);
    return view;
  }
}
