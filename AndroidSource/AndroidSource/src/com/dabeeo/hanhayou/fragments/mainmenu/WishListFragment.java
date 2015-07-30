package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.WishListSearchActivity;
import com.dabeeo.hanhayou.activities.trend.TrendActivity;
import com.dabeeo.hanhayou.beans.PopularWishBean;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.PopularWishListParticleView;

public class WishListFragment extends Fragment
{
  private LinearLayout popularWishListContainer;
  private ScrollView emptyContainer;
  private GridViewWithHeaderAndFooter listView;
  private WishListAdapter adapter;
  private Button btnPopularProduct;
  private WishListListener wishListListener;
  
  private ApiClient apiClient;
  public ArrayList<ProductBean> ProductArray;
  
  
  @SuppressLint("InflateParams")
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_wish_list;
    View view = inflater.inflate(resId, null);
    
    apiClient = new ApiClient(getActivity());
    
    emptyContainer = (ScrollView) view.findViewById(R.id.empty_container);
    btnPopularProduct = (Button) view.findViewById(R.id.btn_go_to_popular_product);
    btnPopularProduct.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
        else
          startActivity(new Intent(getActivity(), TrendActivity.class));
      }
    });
    
    new getWishListTask().execute();
    
    listView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.listview);
    final View v = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_wishlist, null);
    ((Button) v.findViewById(R.id.btn_add_wishlist)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!SystemUtil.isConnectNetwork(getActivity()))
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
        else
          startActivity(new Intent(getActivity(), WishListSearchActivity.class));
      }
    });
    
    wishListListener = new WishListListener()
    {
      
      @Override
      public void onRemove()
      {
        if (adapter.getCount() == 0)
        {
          emptyContainer.setVisibility(View.VISIBLE);
          listView.setVisibility(View.GONE);
          new PopularWishList().execute();
        }
        else
        {
          emptyContainer.setVisibility(View.GONE);
          listView.setVisibility(View.VISIBLE);
        }
      }
    };
    
    adapter = new WishListAdapter(getActivity(), wishListListener);
    
    listView.addFooterView(v);
    listView.setAdapter(adapter);
    
    popularWishListContainer = (LinearLayout) view.findViewById(R.id.wish_list_container);
    
    return view;
  }
  
  private class getWishListTask extends AsyncTask<Void, Void, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getWishList();
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      responseParser(result.response);
    }	  
  }
  
  public void responseParser(String result)
  {
    try
    {
      JSONObject obj = new JSONObject(result);
      if(!obj.getString("status").equals("NO_DATA"))
      {
        ProductArray = new ArrayList<ProductBean>();
        JSONArray productArr = obj.getJSONArray("product");
        for (int i = 0; i < productArr.length(); i++)
        {
          ProductBean product = new ProductBean();
          JSONObject objInArr = productArr.getJSONObject(i);
          product.setJSONObject(objInArr);
          if(!product.productNull)
            ProductArray.add(product);
        }
        adapter.addAll(ProductArray);
      }
      
      wishListListener.onRemove();
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
  
  private class PopularWishList extends AsyncTask<Void, Void, ArrayList<PopularWishBean>>
  {
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    @Override
    protected ArrayList<PopularWishBean> doInBackground(Void... params)
    {
      ArrayList<PopularWishBean> result = null;
      result = apiClient.getPopularWishList();
      return result;
    }
    
    @Override
    protected void onPostExecute(ArrayList<PopularWishBean> result)
    {
      super.onPostExecute(result);
      
      String leftName = "";
      String leftId = "";
      String rightName = "";
      String rightId = "";
      super.onPostExecute(result);
      
      int position = 0;
      for(int i = 0; i < result.size(); i++)
      {
        PopularWishListParticleView pView = new PopularWishListParticleView(getActivity());
        leftName = result.get(i).name;
        leftId = result.get(i).id;
        if(result.size()-1 > i)
        {
          rightName = result.get(i+1).name;
          rightId = result.get(i+1).id;
        }else
        {
          rightName = "";
          rightId = "";
        }
        pView.setBean(position, leftName, rightName, leftId, rightId);
        position = position + 1;
        i = i + 1;
        popularWishListContainer.addView(pView);
      }
    }
  }
}
