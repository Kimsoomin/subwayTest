package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ScheduleBean;

public class TravelScheduleListAdapter extends BaseAdapter
{
  private ArrayList<ScheduleBean> beans = new ArrayList<>();
  
  
  public void add(ScheduleBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<ScheduleBean> beans)
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
    ScheduleBean bean = (ScheduleBean) beans.get(position);
    int resId = R.layout.list_item_travel_schedule;
    
//    if (position == 0)
//      resId = R.layout.list_item_travel_schedule_first_row;
    
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    title.setText(bean.title);
    month.setText(Integer.toString(bean.month) + parent.getContext().getString(R.string.term_month));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    return view;
  }
}
