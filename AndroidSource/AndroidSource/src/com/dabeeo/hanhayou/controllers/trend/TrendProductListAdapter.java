package com.dabeeo.hanhayou.controllers.trend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class TrendProductListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<>();
  private Activity context;
  private ApiClient apiClient;
  private Button btnWishList;
  
  public TrendProductListAdapter(Activity context)
  {
    apiClient = new ApiClient(context);
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
  
  @SuppressLint("ViewHolder")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent)
  {
    final ProductBean bean = (ProductBean) beans.get(position);
    int resId = R.layout.list_item_product;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView price = (TextView) view.findViewById(R.id.price);
    TextView chinesePrice = (TextView) view.findViewById(R.id.chinese_price);
    TextView discountRate = (TextView) view.findViewById(R.id.discount_rate);
    
    btnWishList = (Button) view.findViewById(R.id.btn_wish_list);
    btnWishList.setActivated(bean.isWished);
    btnWishList.setTag(position);
    btnWishList.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if(PreferenceManager.getInstance(context).isLoggedIn())
        {
          int position = (int) v.getTag();
          ProductBean pb = (ProductBean)getItem(position);
          if(pb.isWished == false)
            pb.isWished = true;
          else
            pb.isWished = false;
          new ToggleWishList().execute(pb.id);
        }else
          new AlertDialogManager(context).showNeedLoginDialog(-1);
      }
    });
    
    Picasso.with(context).load(bean.imageUrl).fit().centerCrop().into(imageView);
    title.setText(bean.name);
    price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(bean.priceSale)));
    String ch_price = Global.getCurrencyConvert(context, Integer.parseInt(bean.priceSale), Float.parseFloat(bean.currencyConvert));
    chinesePrice.setText(ch_price);
    discountRate.setText(bean.saleRate + context.getString(R.string.term_sale_rate));
    
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
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
      notifyDataSetChanged();
    }
  }
}
