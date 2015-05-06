package com.dabeeo.hangouyou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.utils.NumberFormatter;
import com.squareup.picasso.Picasso;

public class TicketListAdapter extends BaseAdapter
{
  private ArrayList<TicketBean> items = new ArrayList<>();
  
  
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
    int resId = R.layout.list_item_ticket;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView discountRate = (TextView) view.findViewById(R.id.text_discount_rate);
    TextView priceWon = (TextView) view.findViewById(R.id.text_price_won);
    TextView priceYuan = (TextView) view.findViewById(R.id.text_price_yuan);
    
    Picasso.with(parent.getContext()).load("http://lorempixel.com/400/200/cats").fit().centerCrop().into(imageView);
    title.setText(bean.title);
    discountRate.setText(bean.discountRate);
    priceWon.setText(parent.getContext().getString(R.string.term_won) + NumberFormatter.addComma(bean.priceWon));
    priceYuan.setText("(" + parent.getContext().getString(R.string.term_yuan) + NumberFormatter.addComma(bean.priceYuan) + ")");
    return view;
  }
}
