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
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CouponBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class CouponListAdapter extends BaseAdapter
{
  private ArrayList<CouponBean> items = new ArrayList<>();
  private Context context;
  
  
  public CouponListAdapter(Context context)
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
  
  
  @SuppressLint({ "ViewHolder", "SimpleDateFormat" })
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    CouponBean bean = (CouponBean) items.get(position);
    int resId = R.layout.list_item_coupon;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView description = (TextView) view.findViewById(R.id.text_description);
    TextView validityDate = (TextView) view.findViewById(R.id.text_validity_period);
    
    ImageDownloader.displayImage(context, bean.couponImageUrl, imageView, null);
    title.setText(bean.title);
    if (bean.distance != -1)
      description.setText(changeMeter(bean.distance));
    else
      description.setText(bean.branchName);
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    if (bean.isNotimeLimit)
      validityDate.setText(context.getString(R.string.term_notimelimit));
    if (bean.isExhaustion)
      validityDate.setText(format.format(bean.startDate) + context.getString(R.string.term_exhaustion));
    if (!bean.isExhaustion && !bean.isNotimeLimit)
      validityDate.setText(format.format(bean.startDate) + "~" + format.format(bean.endDate));
    return view;
  }
  
  
  @SuppressLint("DefaultLocale")
  private String changeMeter(int meter)
  {
//    float km = meter / 1000.0f;
    String kmStr = NumberFormatter.addComma(meter);
    return kmStr + "m";
  }
}
