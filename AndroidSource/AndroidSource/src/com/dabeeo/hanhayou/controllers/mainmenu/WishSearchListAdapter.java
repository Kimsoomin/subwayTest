package com.dabeeo.hanhayou.controllers.mainmenu;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class WishSearchListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<>();
  private Activity context;
  private ApiClient apiClient;
  
  
  public WishSearchListAdapter(Activity context)
  {
    this.context = context;
    apiClient = new ApiClient(context);
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
    Picasso.with(context).load(bean.imageUrl).fit().centerCrop().into(imageView);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView salePrice = (TextView) view.findViewById(R.id.sale_price);
    TextView chinaPrice = (TextView) view.findViewById(R.id.china_price);
    TextView saleRate = (TextView) view.findViewById(R.id.sale_rate);
    final ImageView btnWishList = (ImageView) view.findViewById(R.id.btn_wishlist);
    btnWishList.setActivated(bean.isWished);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new ToggleWishList().execute(bean.id);
      }
    });
    
    View bottomLine = (View) view.findViewById(R.id.bottom_line);
    if (position == beans.size() - 1)
      bottomLine.setVisibility(View.GONE);
    else
      bottomLine.setVisibility(View.VISIBLE);
    
    title.setText(bean.name);
    salePrice.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(bean.priceSale));
    String ch_price = Global.getCurrencyConvert(context, Integer.parseInt(bean.priceSale), Float.parseFloat(bean.currencyConvert));
    chinaPrice.setText(ch_price);
    saleRate.setText(bean.saleRate + context.getString(R.string.term_sale_rate));
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
        {
          Intent i = new Intent(context, TrendProductDetailActivity.class);
          i.putExtra("product_idx", bean.id);
          i.putExtra("product_isWished", bean.isWished);
          i.putExtra("proudct_categoryId", bean.categoryId);
          i.putExtra("product_rate", bean.rate);
          context.startActivity(i);
        }
      }
    });
    return view;
  }
  
  private class ToggleWishList extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.setUsedLog(PreferenceManager.getInstance(context).getUserSeq(), params[0], "product", "W");
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if(obj.getString("result").equals("INS"))
        {
          Toast.makeText(context, context.getString(R.string.msg_add_wishlist), Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
      }
      catch (Exception e)
      {
        BlinkingCommon.smlLibPrintException("WishSearchListAdapter", " e : " + e);
      }
      
    }
  }
}
