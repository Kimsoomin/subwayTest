package com.dabeeo.hanhayou.controllers.coupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CouponDetailBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class DownloadedCouponListAdapter extends BaseAdapter
{
  private ArrayList<CouponDetailBean> items = new ArrayList<>();
  private Context context;
  
  
  public DownloadedCouponListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void addAll(ArrayList<CouponDetailBean> beans)
  {
    this.items.addAll(beans);
    notifyDataSetChanged();
  }
  
  
  public void add(CouponDetailBean bean)
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
  
  
  @SuppressLint({ "ViewHolder", "SimpleDateFormat" })
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    CouponDetailBean bean = (CouponDetailBean) items.get(position);
    int resId = R.layout.list_item_coupon;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView description = (TextView) view.findViewById(R.id.text_description);
    TextView validityDate = (TextView) view.findViewById(R.id.text_validity_period);
    RelativeLayout useContainer = (RelativeLayout) view.findViewById(R.id.use_container);
    
    ImageDownloader.displayImage(context, bean.couponImageUrl, imageView, null);
    title.setText(bean.title);
    description.setText(bean.branchName);
    
    try
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
      if (bean.isNotimeLimit)
        validityDate.setText(context.getString(R.string.term_notimelimit));
      if (bean.isExhaustion)
        validityDate.setText(format.format(bean.startDate) + context.getString(R.string.term_exhaustion));
      if (!bean.isExhaustion && !bean.isNotimeLimit)
        validityDate.setText(format.format(bean.startDate) + "~" + format.format(bean.endDate));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (bean.isUse)
    {
      useContainer.setVisibility(View.VISIBLE);
      useContainer.bringToFront();
    }
    return view;
  }
}
