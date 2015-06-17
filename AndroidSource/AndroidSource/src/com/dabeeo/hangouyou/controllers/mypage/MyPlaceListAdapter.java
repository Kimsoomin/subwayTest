package com.dabeeo.hangouyou.controllers.mypage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.managers.CategoryManager;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MyPlaceListAdapter extends BaseAdapter
{
  private ArrayList<PlaceBean> beans = new ArrayList<>();
  private boolean isEditMode = false;
  private Context context;
  
  
  public MyPlaceListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void setAllCheck(boolean isCheck)
  {
    for (int i = 0; i < beans.size(); i++)
    {
      beans.get(i).isChecked = isCheck;
    }
    notifyDataSetChanged();
  }
  
  
  public void setEditMode(boolean isEditMode)
  {
    this.isEditMode = isEditMode;
    notifyDataSetChanged();
  }
  
  
  public void add(PlaceBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<PlaceBean> beans)
  {
    this.beans.addAll(beans);
    notifyDataSetChanged();
  }
  
  
  public void removeCheckedItem()
  {
    for (int i = beans.size() - 1; i >= 0; i--)
    {
      PlaceBean bean = beans.get(i);
      if (bean.isChecked)
        beans.remove(i);
    }
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
  
  
  public ArrayList<PlaceBean> getCheckedArrayList()
  {
    ArrayList<PlaceBean> checkedLists = new ArrayList<>();
    for (int i = 0; i < beans.size(); i++)
    {
      if (beans.get(i).isChecked)
        checkedLists.add(beans.get(i));
    }
    return checkedLists;
  }
  
  
  @SuppressLint("ViewHolder")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent)
  {
    PlaceBean bean = (PlaceBean) beans.get(position);
    int resId = R.layout.list_item_my_place;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
    if (isEditMode)
    {
      checkBox.setVisibility(View.VISIBLE);
      checkBox.bringToFront();
    }
    else
      checkBox.setVisibility(View.GONE);
    
    checkBox.setChecked(bean.isChecked);
    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        ((PlaceBean) beans.get(position)).isChecked = isChecked;
      }
    });
    
    final ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView createdAt = (TextView) view.findViewById(R.id.created_at);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView category = (TextView) view.findViewById(R.id.category);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    ImageView imagePrivate = (ImageView) view.findViewById(R.id.image_private);
    
    ImageDownloader.displayImage(context, bean.imageUrl, imageView, new ImageLoadingListener()
    {
      @Override
      public void onLoadingStarted(String arg0, View arg1)
      {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      }
      
      
      @Override
      public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
      {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      }
      
      
      @Override
      public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap)
      {
        if (bitmap == null)
          imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        else
          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      }
      
      
      @Override
      public void onLoadingCancelled(String arg0, View arg1)
      {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      }
    });
    
    if (position % 2 == 1)
      imagePrivate.setVisibility(View.VISIBLE);
    else
      imagePrivate.setVisibility(View.GONE);
    try
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
      String insertDateString = format.format(bean.insertDate);
      createdAt.setText(insertDateString);
    }
    catch (Exception e)
    {
      createdAt.setVisibility(View.GONE);
    }
    
    title.setText(bean.title);
    category.setText(CategoryManager.getInstance(parent.getContext()).getCategoryName(bean.categoryId));
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    return view;
  }
}
