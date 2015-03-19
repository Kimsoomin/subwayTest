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
import com.dabeeo.hangouyou.beans.MyPhotoLogBean;

public class MyPhotoLogListAdapter extends BaseAdapter
{
  private ArrayList<MyPhotoLogBean> beans = new ArrayList<>();
  private boolean isEditMode = false;
  
  
  public void setEditMode(boolean isEditMode)
  {
    this.isEditMode = isEditMode;
    notifyDataSetChanged();
  }
  
  
  public void add(MyPhotoLogBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<MyPhotoLogBean> beans)
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
      MyPhotoLogBean bean = beans.get(i);
      if (bean.isChecked)
        beans.remove(i);
    }
    
    notifyDataSetChanged();
  }
  
  
  public ArrayList<MyPhotoLogBean> getCheckedArrayList()
  {
    ArrayList<MyPhotoLogBean> checkedLists = new ArrayList<>();
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
    MyPhotoLogBean bean = (MyPhotoLogBean) beans.get(position);
    int resId = R.layout.list_item_my_photo_log;
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
        ((MyPhotoLogBean) beans.get(position)).isChecked = isChecked;
      }
    });
    ImageView photo = (ImageView) view.findViewById(R.id.photo);
    TextView photoCount = (TextView) view.findViewById(R.id.photo_count);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView days = (TextView) view.findViewById(R.id.days);
    TextView date = (TextView) view.findViewById(R.id.date);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    
    photoCount.setText(Integer.toString(bean.photoCount));
    title.setText(bean.title);
    days.setText(bean.days);
    date.setText(bean.date);
    likeCount.setText(Integer.toString(bean.likeCount));
    
    return view;
  }
}
