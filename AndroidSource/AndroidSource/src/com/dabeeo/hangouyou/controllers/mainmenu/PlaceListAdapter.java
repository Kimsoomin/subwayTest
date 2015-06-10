package com.dabeeo.hangouyou.controllers.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.managers.CategoryManager;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class PlaceListAdapter extends BaseAdapter
{
  private ArrayList<PlaceBean> beans = new ArrayList<>();
  private Context context;
  private int lastPosition;
  
  
  public PlaceListAdapter(Context context)
  {
    this.context = context;
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
  
  
  @SuppressLint({ "ViewHolder", "SimpleDateFormat" })
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    PlaceBean bean = (PlaceBean) beans.get(position);
    int resId = R.layout.list_item_place;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    final ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView isCoupon = (TextView) view.findViewById(R.id.text_coupon);
    TextView createdAt = (TextView) view.findViewById(R.id.created_at);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView category = (TextView) view.findViewById(R.id.category);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    TextView reviewCount = (TextView) view.findViewById(R.id.review_count);
    TextView textRanking = (TextView) view.findViewById(R.id.text_ranking);
    RelativeLayout recommendRanking = (RelativeLayout) view.findViewById(R.id.recommend_container);
    if (position == 0)
    {
      recommendRanking.setVisibility(View.VISIBLE);
      textRanking.setVisibility(View.GONE);
    }
    else
    {
      recommendRanking.setVisibility(View.GONE);
      textRanking.setVisibility(View.VISIBLE);
      textRanking.setText(Integer.toString(position));
    }
    
    if (position % 2 == 1)
      isCoupon.setVisibility(View.VISIBLE);
    else
      isCoupon.setVisibility(View.GONE);
    
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
    
    if (SystemUtil.isConnectNetwork(context))
      ImageDownloader.displayImage(context, bean.imageUrl, imageView, null);
    else
    {
      if (!TextUtils.isEmpty(bean.imageUrl))
      {
        String filename = Global.MD5Encoding(bean.imageUrl);
        ImageDownloader.displayImage(context, "file:///" + Global.GetImageFilePath() + filename, imageView, null);
      }
    }
    
    title.setText(bean.title);
    category.setText(CategoryManager.getInstance(context).getCategoryName(bean.categoryId));
    if (TextUtils.isEmpty(category.getText().toString()))
      category.setVisibility(View.GONE);
    likeCount.setText(Integer.toString(bean.likeCount));
    reviewCount.setText(Integer.toString(bean.reviewCount));
    
    if (SystemUtil.isConnectNetwork(context))
    {
      Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
      view.startAnimation(animation);
      lastPosition = position;
    }
    return view;
  }
}
