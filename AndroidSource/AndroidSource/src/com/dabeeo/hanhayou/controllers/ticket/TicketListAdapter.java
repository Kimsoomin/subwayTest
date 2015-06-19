package com.dabeeo.hanhayou.controllers.ticket;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TicketBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.NumberFormatter;

public class TicketListAdapter extends BaseAdapter
{
  private ArrayList<TicketBean> items = new ArrayList<>();
  private Context context;
  
  
  public TicketListAdapter(Context context)
  {
    this.context = context;
  }
  
  
  public void add(TicketBean bean)
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
    TicketBean bean = (TicketBean) items.get(position);
    int resId = R.layout.list_item_ticket;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView originalPrice = (TextView) view.findViewById(R.id.text_original_price);
    originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    TextView discountMonth = (TextView) view.findViewById(R.id.text_discount_month);
    TextView discountPrice = (TextView) view.findViewById(R.id.text_discount_price);
    TextView discountPriceCn = (TextView) view.findViewById(R.id.text_discount_price_cn);
    final ImageView btnWishList = (ImageView) view.findViewById(R.id.btn_wishlist);
    
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        btnWishList.setActivated(!btnWishList.isActivated());
        if (btnWishList.isActivated())
          Toast.makeText(context, context.getString(R.string.msg_add_wishlist), Toast.LENGTH_LONG).show();
      }
    });
    title.setText(bean.title);
    discountMonth.setText(bean.discountRate);
    originalPrice.setText(parent.getContext().getString(R.string.term_won) + NumberFormatter.addComma(bean.priceWon));
    discountPrice.setText(parent.getContext().getString(R.string.term_won) + NumberFormatter.addComma(bean.priceWon));
    discountPriceCn.setText("(" + parent.getContext().getString(R.string.term_yuan) + NumberFormatter.addComma(bean.priceYuan) + ")");
    
    ImageDownloader.displayImage(context, "", imageView, null);
    return view;
  }
}
