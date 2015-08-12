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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.ProductDetailBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class WishListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<>();
  private Activity context;
  private WishListListener listener;
  private ApiClient apiClient;
  
  private ProductDetailBean productDetail;
  
  public WishListAdapter(Activity context, WishListListener listener)
  {
    this.context = context;
    this.listener = listener;
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
  
  public interface WishListListener
  {
    public void onRemove();
    public void onRefresh();
    public void onProgressVisibleSet(boolean visible);
    public void onOptionSelected(ProductDetailBean productDetailInfo);
    public void onOptionClose();
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
    int resId = R.layout.list_item_wishlist_product;
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, null);
    
    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    Picasso.with(context).load(bean.imageUrl).fit().centerCrop().into(imageView);
    TextView title = (TextView) view.findViewById(R.id.title);
    TextView price = (TextView) view.findViewById(R.id.price);
    TextView chinaPrice = (TextView) view.findViewById(R.id.ch_price);
    TextView saleRate = (TextView) view.findViewById(R.id.sale_rate);
    ImageView btnTrash = (ImageView) view.findViewById(R.id.btn_trash);
    btnTrash.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new DelWishList().execute(bean);
      }
    });
    Button btnCart = (Button) view.findViewById(R.id.btn_cart);
    Button btnBuy = (Button) view.findViewById(R.id.btn_buy);
    btnCart.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
//        if (!SystemUtil.isConnectNetwork(context))
//          new AlertDialogManager(context).showDontNetworkConnectDialog();
//        else
//          Toast.makeText(context, "준비 중입니다", Toast.LENGTH_LONG).show();
        new GetProductDetailTask().execute(bean.id);
      }
    });
    btnBuy.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
//        if (!SystemUtil.isConnectNetwork(context))
//          new AlertDialogManager(context).showDontNetworkConnectDialog();
//        else
//          Toast.makeText(context, "준비 중입니다", Toast.LENGTH_LONG).show();
        new GetProductDetailTask().execute(bean.id);
      }
    });
    
    title.setText(bean.name);
    price.setText(context.getString(R.string.term_won) + NumberFormatter.addComma(Integer.parseInt(bean.priceSale)));
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
  
  private class DelWishList extends AsyncTask<ProductBean, Void, NetworkResult>
  {
    ProductBean pb;
    
    @Override
    protected NetworkResult doInBackground(ProductBean... params)
    {
      pb = params[0];
      return apiClient.setUsedLog(PreferenceManager.getInstance(context).getUserSeq(), pb.id, "product", "W");
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if(obj.getString("result").equals("DEL"))
        {
          beans.remove(pb);
          notifyDataSetChanged();
          if (listener != null)
            listener.onRemove();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class GetProductDetailTask extends AsyncTask<String, Void, ProductDetailBean>
  {
    
    @Override
    protected void onPreExecute()
    {
      if(listener != null)
        listener.onProgressVisibleSet(true);
      super.onPreExecute();
    }
    
    @Override
    protected ProductDetailBean doInBackground(String... params)
    {
      return apiClient.getProductDetail(params[0]);
    }
    
    @Override
    protected void onPostExecute(ProductDetailBean result)
    {
      super.onPostExecute(result);
      
      if (listener != null)
        listener.onProgressVisibleSet(false);
      
      productDetail = result;
      if(productDetail != null)
      {
        listener.onOptionSelected(productDetail);
      }else
      {
        new AlertDialogManager(context).showAlertDialog(context.getString(R.string.term_alert), "상품 정보 오류", 
            context.getString(R.string.term_ok), null, new AlertListener()
        {
          @Override
          public void onPositiveButtonClickListener()
          {
          }
          
          @Override
          public void onNegativeButtonClickListener()
          { 
          }
        });
      }
    }
    
  }
}
