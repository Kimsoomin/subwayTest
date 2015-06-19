package com.dabeeo.hanhayou.controllers.coupon;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CouponBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class DownloadedCouponListAdapter extends BaseAdapter
{
  private ArrayList<CouponBean> items = new ArrayList<>();
  private Context context;
  
  
  public DownloadedCouponListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(CouponBean bean)
  {
    this.items.add(bean);
    notifyDataSetChanged();
  }
  
  
  public void clear()
  {
    this.items.clear();
    notifyDataSetChanged();
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
    CouponBean bean = (CouponBean) items.get(position);
    int resId = R.layout.list_item_downloaded_coupon;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView description = (TextView) view.findViewById(R.id.text_description);
    description.setVisibility(View.GONE);
    TextView validityDate = (TextView) view.findViewById(R.id.text_validity_period);
    TextView isUsed = (TextView) view.findViewById(R.id.text_used);
    
//    Picasso.with(parent.getContext()).load("http://lorempixel.com/400/200/cats").fit().centerCrop().into(imageView);
    ImageDownloader.displayImage(context, "", imageView, null);
    
    title.setText(bean.title);
    description.setText(bean.description);
    validityDate.setText(bean.fromValidityDate + "~" + bean.toValidityDate);
    isUsed.setVisibility(bean.isUsed ? View.VISIBLE : View.GONE);
    return view;
  }
}
