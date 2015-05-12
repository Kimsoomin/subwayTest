package com.dabeeo.hangouyou.controllers.mypage;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ScheduleBean;

public class MySchedulesListAdapter extends BaseAdapter
{
  private ArrayList<ScheduleBean> beans = new ArrayList<>();
  private boolean isEditMode = false;
  
  
  public void setEditMode(boolean isEditMode)
  {
    this.isEditMode = isEditMode;
    notifyDataSetChanged();
  }
  
  
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
  
  
  public void delete()
  {
    for (int i = beans.size() - 1; i >= 0; i--)
    {
      ScheduleBean bean = beans.get(i);
      if (bean.isChecked)
        beans.remove(i);
    }
    
    notifyDataSetChanged();
  }
  
  
  public ArrayList<ScheduleBean> getCheckedArrayList()
  {
    ArrayList<ScheduleBean> checkedLists = new ArrayList<>();
    for (int i = 0; i < beans.size(); i++)
    {
      if (beans.get(i).isChecked)
        checkedLists.add(beans.get(i));
    }
    return checkedLists;
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
  public View getView(final int position, View convertView, ViewGroup parent)
  {
    ScheduleBean bean = (ScheduleBean) beans.get(position);
    int resId = R.layout.list_item_my_schedule;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
    if (isEditMode)
      checkBox.setVisibility(View.VISIBLE);
    else
      checkBox.setVisibility(View.GONE);
    
    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        ((ScheduleBean) beans.get(position)).isChecked = isChecked;
      }
    });
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView month = (TextView) view.findViewById(R.id.month);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    
    title.setText(bean.title);
    month.setText(Integer.toString(bean.dayCount) + parent.getContext().getString(R.string.term_month));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    return view;
  }
}
