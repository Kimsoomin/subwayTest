package com.dabeeo.hangouyou.controllers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabeeo.hangouyou.R; 
import com.dabeeo.hangouyou.beans.StationBean;

public class SubwayListAdapter extends BaseAdapter
{
  private ArrayList<StationBean> items = new ArrayList<>();
  
  
  public void addAll(ArrayList<StationBean> beans)
  {
    items.clear();
    items.addAll(beans);
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
    StationBean bean = (StationBean) items.get(position);
    int resId = R.layout.list_item_station;
    convertView = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    TextView line = (TextView) convertView.findViewById(R.id.text_line);
    TextView stationName = (TextView) convertView.findViewById(R.id.text_station_name);
    
    line.setText(bean.line);
    stationName.setText(bean.nameKo);
    return convertView;
  }
}
