package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PremiumBean;
import com.squareup.picasso.Picasso;

public class RecommendSeoulListAdapter extends BaseAdapter
{
  private ArrayList<PremiumBean> beans = new ArrayList<>();
  private int lastPosition;
  private Context context;
  
  
  public RecommendSeoulListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(PremiumBean bean)
  {
    this.beans.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void addAll(ArrayList<PremiumBean> beans)
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
    PremiumBean bean = (PremiumBean) beans.get(position);
    int resId = R.layout.list_item_recommend_seoul;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
    TextView likeCount = (TextView) view.findViewById(R.id.like_count);
    
    SpannableStringBuilder style = new SpannableStringBuilder(bean.title);
    style.setSpan(new BackgroundColorSpan(Color.parseColor("#ffffff")), 0, bean.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    title.setText(style);
    subtitle.setText(bean.placeTitle);
    likeCount.setText(Integer.toString(bean.likeCount));
    
    Log.w("WARN", "bean.imageUrl : " + bean.imageUrl);
    Picasso.with(parent.getContext()).load(bean.imageUrl).fit().centerCrop().into(imageView);
    Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
    view.startAnimation(animation);
    lastPosition = position;
    return view;
  }
}
