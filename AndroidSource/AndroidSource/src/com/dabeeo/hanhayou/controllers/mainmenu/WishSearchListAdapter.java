package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class WishSearchListAdapter extends BaseAdapter
{
	private ArrayList<ProductBean> beans = new ArrayList<>();
	private Activity context;
	
	
	public WishSearchListAdapter(Activity context)
	{
		this.context = context;
	}
	
	
	public void add(ProductBean bean)
	{
		this.beans.add(bean);
		notifyDataSetChanged();
	}
	
	
	public void addAll(ArrayList<ProductBean> beans)
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
		final ProductBean bean = (ProductBean) beans.get(position);
		int resId = R.layout.list_item_wishlist_search_product;
		View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView price = (TextView) view.findViewById(R.id.price);
		price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		TextView discountPrice = (TextView) view.findViewById(R.id.discount_price);
		TextView chinaPrice = (TextView) view.findViewById(R.id.discount_china_currency);
		TextView month = (TextView) view.findViewById(R.id.text_discount_month);
		final ImageView btnWishList = (ImageView) view.findViewById(R.id.btn_wishlist);
		btnWishList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btnWishList.setActivated(!btnWishList.isActivated());
			}
		});
		View bottomLine = (View) view.findViewById(R.id.bottom_line);
		if (position == beans.size() - 1)
			bottomLine.setVisibility(View.GONE);
		else
			bottomLine.setVisibility(View.VISIBLE);
		
		title.setText(bean.name);
		discountPrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceSale));
		price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceDiscount));
		month.setText("7월");
		chinaPrice.setText("(500￥)");
		
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!SystemUtil.isConnectNetwork(context))
					new AlertDialogManager(context).showDontNetworkConnectDialog();
				else
					context.startActivity(new Intent(context, TrendProductDetailActivity.class));
			}
		});
		return view;
	}
}
