package com.dabeeo.hangouyou.controllers;

import java.util.ArrayList;

import android.R.attr;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.SearchResultBean;

public class SearchResultAdapter extends BaseAdapter
{
  private ArrayList<SearchResultBean> items = new ArrayList<>();
  
  
  public void add(SearchResultBean bean)
  {
    items.add(bean);
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
    if (bean.isTitle)
    {
      title.setClickable(true); // 왜 false일 때가 클릭이 되는걸까?
      title.setTextAppearance(parent.getContext(), android.R.style.TextAppearance_Large);
      
      TextView seperater = (TextView) convertView.findViewById(R.id.seperater);
      seperater.setVisibility(View.GONE);
    }
    
    return convertView;
  }
}
