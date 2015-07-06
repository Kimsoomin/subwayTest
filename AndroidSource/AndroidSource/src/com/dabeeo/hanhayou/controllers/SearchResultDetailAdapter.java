package com.dabeeo.hanhayou.controllers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.SearchResultBean;

public class SearchResultDetailAdapter extends BaseAdapter
{
  private ArrayList<SearchResultBean> items = new ArrayList<>();
  
  
  public void add(SearchResultBean bean)
  {
    items.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<SearchResultBean> bean)
  {
    items.addAll(bean);
    notifyDataSetChanged();
  }
  
  
  public void clear()
  {
    items.clear();
    notifyDataSetChanged();
  }
  
  
  @Override
  public int getCount()
  {
    return items.size();
  }
  
  
  @Override
  public Object getItem(int position)
  {
    return items.get(position);
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
    SearchResultBean bean = items.get(position);
    
    int resId = R.layout.list_item_search_result;
    convertView = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    TextView title = (TextView) convertView.findViewById(android.R.id.text1);
    title.setText(bean.text);
    
    View seperator = (View) convertView.findViewById(R.id.seperater);
    if (items.size() - 1 == position)
      seperator.setVisibility(View.GONE);
    else
      seperator.setVisibility(View.VISIBLE);
    return convertView;
  }
}
