package com.dabeeo.hanhayou.controllers.ticket;

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
import com.dabeeo.hanhayou.beans.TicketBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class BoughtTicketListAdapter extends BaseAdapter
{
  private ArrayList<TicketBean> items = new ArrayList<>();
  private Context context;
  
  
  public BoughtTicketListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(TicketBean bean)
  {
    this.items.add(bean);
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
    TicketBean bean = (TicketBean) items.get(position);
    int resId = R.layout.list_item_bought_ticket;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView validityDate = (TextView) view.findViewById(R.id.text_validity_period);
    
    ImageDownloader.displayImage(context, "", imageView, null);
    title.setText(bean.title);
    validityDate.setText(bean.fromValidityDate + "~" + bean.toValidityDate);
    return view;
  }
}
