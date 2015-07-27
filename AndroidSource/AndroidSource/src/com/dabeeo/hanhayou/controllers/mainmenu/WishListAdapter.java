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
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class WishListAdapter extends BaseAdapter
{
  private ArrayList<ProductBean> beans = new ArrayList<>();
  private Activity context;
  private WishListListener listener;
  private ApiClient apiClient;
  
  
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
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
          Toast.makeText(context, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
    });
    btnBuy.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(context))
          new AlertDialogManager(context).showDontNetworkConnectDialog();
        else
          Toast.makeText(context, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
    });
    
    title.setText(bean.name);
    price.setText(context.getString(R.string.term_won) + " " + NumberFormatter.addComma(Integer.parseInt(bean.priceSale)));
    String ch_price = "";
    int calChPrice = (int)(Integer.parseInt(bean.priceSale)/Float.parseFloat(bean.currencyConvert));
    ch_price = "(대략 "+ context.getString(R.string.term_yuan) + ""+ NumberFormatter.addComma(calChPrice) + ")";
    chinaPrice.setText(""+ch_price);
    saleRate.setText(bean.saleRate + "折");
    
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
}
