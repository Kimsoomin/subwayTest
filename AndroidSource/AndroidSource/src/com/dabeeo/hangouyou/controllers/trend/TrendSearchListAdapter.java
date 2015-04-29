package com.dabeeo.hangouyou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TrendSearchListBean;

public class TrendSearchListAdapter extends BaseAdapter
{
  private ArrayList<TrendSearchListBean> beans = new ArrayList<TrendSearchListBean>();
  
  
  public void add(TrendSearchListBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<TrendSearchListBean> beans)
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
    TrendSearchListBean bean = (TrendSearchListBean) beans.get(position);
    int resId = R.layout.list_item_trend_search_list;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    title.setText(bean.title);
    return view;
  }
}
