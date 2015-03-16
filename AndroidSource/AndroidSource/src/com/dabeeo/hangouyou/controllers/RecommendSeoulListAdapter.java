package com.dabeeo.hangouyou.controllers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.RecommendSeoulBean;

public class RecommendSeoulListAdapter extends BaseAdapter
{
  private ArrayList<RecommendSeoulBean> beans = new ArrayList<>();
  private Context context;
  
  
  public RecommendSeoulListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(RecommendSeoulBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<RecommendSeoulBean> beans)
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
    RecommendSeoulBean bean = (RecommendSeoulBean) beans.get(position);
    int resId = R.layout.list_item_place;
    View view = LayoutInflater.from(context).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView category = (TextView) view.findViewById(R.id.category);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    title.setText(bean.title);
    category.setText(bean.category);
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    return view;
  }
}
