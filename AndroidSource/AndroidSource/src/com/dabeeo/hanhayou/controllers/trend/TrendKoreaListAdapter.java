package com.dabeeo.hanhayou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.squareup.picasso.Picasso;

public class TrendKoreaListAdapter extends BaseAdapter
{
  private ArrayList<TrendKoreaBean> beans = new ArrayList<>();
  private int lastPosition;
  private Context context;
  
  
  public TrendKoreaListAdapter(Context context)
  {
    this.context = context;
  }
  
  
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
    
    Picasso.with(context).load(bean.imageUrl).centerCrop().fit().into(imageView);
    Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
    view.startAnimation(animation);
    lastPosition = position;
    return view;
  }
}