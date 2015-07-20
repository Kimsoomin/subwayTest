package com.dabeeo.hanhayou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;

public class TrendSearchListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<ProductBean>();
  private Activity context;
    
  public TrendSearchListAdapter(Activity context)
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
    int resId = R.layout.list_item_trend_search_list;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    title.setText(bean.name);
    return view;
  }
}
