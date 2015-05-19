package com.dabeeo.hangouyou.controllers.mypage;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.managers.SubwayManager;

public class SubwaySearchListAdapter extends BaseAdapter
{
  private ArrayList<StationBean> beans = new ArrayList<>();
  private Context context;
  
  
  public SubwaySearchListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(StationBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<StationBean> beans)
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
    StationBean bean = (StationBean) beans.get(position);
    
    int resId = R.layout.list_item_subway_search;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    LinearLayout imageContainer = (LinearLayout) view.findViewById(R.id.line_images);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    
    if (Locale.getDefault().getLanguage().contains("ko"))
      title.setText(bean.nameKo);
    else
      title.setText(bean.nameCn);
    
    imageContainer.removeAllViews();
    
    ImageView imageView = new ImageView(context);
    imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
    imageView.setImageResource(SubwayManager.getInstance(context).getSubwayLineResourceId(bean.line));
    imageContainer.addView(imageView);
    
    for (int i = 0; i < bean.lines.size(); i++)
    {
      imageView = new ImageView(context);
      imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
      imageView.setImageResource(SubwayManager.getInstance(context).getSubwayLineResourceId(bean.lines.get(i)));
      imageContainer.addView(imageView);
    }
    return view;
  }
}
