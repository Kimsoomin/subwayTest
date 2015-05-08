package com.dabeeo.hangouyou.controllers.mypage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.LocalPhotoBean;
import com.dabeeo.hangouyou.views.SquareImageView;
import com.squareup.picasso.Picasso;

public class LocalPhotoAdapter extends BaseAdapter
{
  private ArrayList<LocalPhotoBean> items = new ArrayList<>();
  private int selectIndex = 1;
  private boolean canSelectMultiple;
  private Activity context;
  
  
  public LocalPhotoAdapter(Activity context, boolean canMultipleSelect)
  {
    this.context = context;
    this.canSelectMultiple = canMultipleSelect;
  }
  
  
  public void add(String path, int index)
  {
    LocalPhotoBean bean = new LocalPhotoBean();
    bean.path = path;
    
    if (index == -1)
      items.add(bean);
    else
      items.add(index, bean);
  }
  
  
  public void clear()
  {
    items.clear();
    
    // 카메라 아이콘용
    LocalPhotoBean bean = new LocalPhotoBean();
    bean.isCamera = true;
    items.add(bean);
  }
  
  
  public String[] selectedPhotos()
  {
    Comparator<LocalPhotoBean> compare = new Comparator<LocalPhotoBean>()
    {
      @Override
      public int compare(LocalPhotoBean lhs, LocalPhotoBean rhs)
      {
        return lhs.selectIndex > rhs.selectIndex ? 1 : -1;
      }
    };
    
    Collections.sort(items, compare);
    
    ArrayList<String> result = new ArrayList<>();
    for (LocalPhotoBean bean : items)
    {
      if (bean.isSelected)
        result.add(bean.path);
    }
    return result.toArray(new String[result.size()]);
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
    final LocalPhotoBean bean = items.get(position);
    
    int resId = R.layout.list_item_local_photo;
    convertView = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container);
    
    Display display = context.getWindowManager().getDefaultDisplay();
    int width = display.getWidth();
    int photoSize = width / 4;
    container.setLayoutParams(new ViewGroup.LayoutParams(photoSize, photoSize));
    
    SquareImageView photo = (SquareImageView) convertView.findViewById(R.id.photo);
    final View imageSelection = (View) convertView.findViewById(R.id.image_selection);
    final View textSelection = (View) convertView.findViewById(R.id.text_selection);
    
    if (position == 0)
    {
      photo.setImageResource(R.drawable.btn_gallery_camera);
      photo.setBackgroundColor(Color.parseColor("#969b9c"));
      photo.setScaleType(ScaleType.CENTER_INSIDE);
    }
    else
    {
      photo.setBackgroundColor(Color.parseColor("#00000000"));
      Picasso.with(parent.getContext()).load(Uri.fromFile(new File(bean.path)).toString()).resize(100, 100).centerCrop().into(photo);
      
      convertView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          if (bean.isSelected)
          {
            imageSelection.setVisibility(View.GONE);
            textSelection.setVisibility(View.GONE);
            
            if (canSelectMultiple)
            {
              for (LocalPhotoBean photoBean : items)
              {
                if (photoBean.selectIndex > bean.selectIndex)
                {
                  photoBean.selectIndex--;
                }
              }
              selectIndex--;
              bean.selectIndex = -1;
            }
            
            bean.isSelected = false;
          }
          else
          {
            imageSelection.setVisibility(View.VISIBLE);
            textSelection.setVisibility(View.VISIBLE);
            
            if (canSelectMultiple)
            {
              bean.isSelected = true;
              bean.selectIndex = selectIndex;
              selectIndex++;
            }
            else
            {
              for (LocalPhotoBean photoBean : items)
              {
                photoBean.isSelected = false;
              }
              
              bean.isSelected = true;
            }
          }
          
          notifyDataSetChanged();
        }
      });
    }
    
    imageSelection.setVisibility(View.GONE);
    textSelection.setVisibility(View.GONE);
    if (bean.isSelected)
    {
      imageSelection.setVisibility(View.VISIBLE);
      textSelection.setVisibility(View.VISIBLE);
      
//      if (canSelectMultiple)
//        textSelection.setText(bean.selectIndex + "");
//      else
//        textSelection.setText("V");
    }
    
    return convertView;
  }
}
