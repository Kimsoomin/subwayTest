package com.dabeeo.hanhayou.controllers.mypage;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.MyPhotoLogBean;

public class EditPhotoLogListAdapter extends BaseAdapter
{
  private ArrayList<MyPhotoLogBean> beans = new ArrayList<>();
  private boolean isEditMode = false;
  private ImageView photo;
  private ImageButton btnLocalPhoto;
  
  
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
    int resId = R.layout.view_new_photo_log;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    photo = (ImageView) view.findViewById(R.id.photo);
//    photo.setOnClickListener(clickListener);
    btnLocalPhoto = (ImageButton) view.findViewById(R.id.btn_local_photo);
//    btnLocalPhoto.setOnClickListener(clickListener);
    
    return view;
  }
}
