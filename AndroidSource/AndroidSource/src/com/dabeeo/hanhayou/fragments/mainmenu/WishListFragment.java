package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.dabeeo.hanhayou.activities.trend.TrendCartActivity;
import com.dabeeo.hanhayou.beans.PopularWishBean;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.ProductDetailBean;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter;
import com.dabeeo.hanhayou.controllers.mainmenu.WishListAdapter.WishListListener;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.PopularWishListParticleView;
import com.dabeeo.hanhayou.views.TrendOptionAndAmountPickView;

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
  public ArrayList<PopularWishBean> popularWishArray;
  
  private MainActiviListener mainLsitener;
  
  private LinearLayout optionButtonLayout;
  private TrendOptionAndAmountPickView optionAmountPickerView;
  public String itemAttribute;
  private Button btnCart;
  private Button btnCheckOut;
  private String productId;
  
  public interface MainActiviListener
  {
    public void ontabBarVisibleSet(boolean visible);
    public void onProgressLayoutVisibleSet(boolean visible);
  }
  
  public WishListFragment(MainActiviListener listener)
  {
    this.mainLsitener = listener;
  }
  
  @SuppressLint("InflateParams")
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_wish_list;
    View view = inflater.inflate(resId, null);
    
    apiClient = new ApiClient(getActivity());
    
    popularWishArray = new ArrayList<PopularWishBean>();
    
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
    
    optionButtonLayout = (LinearLayout) view.findViewById(R.id.option_btn_layout);
    optionAmountPickerView = (TrendOptionAndAmountPickView) view.findViewById(R.id.option_picker_view);
    
    btnCart = (Button) view.findViewById(R.id.btn_my_cart);
    btnCart.setOnClickListener(cartClickListener);
    btnCheckOut = (Button) view.findViewById(R.id.btn_checkout);
    btnCheckOut.setOnClickListener(cartClickListener);
    
    wishListListener = new WishListListener()
    {
      
      @Override
      public void onRemove()
      {
        if (adapter.getCount() == 0)
        {
          emptyContainer.setVisibility(View.VISIBLE);
          listView.setVisibility(View.GONE);
          if(popularWishArray.size() == 0)
            new PopularWishList().execute();
        }
        else
        {
          emptyContainer.setVisibility(View.GONE);
          listView.setVisibility(View.VISIBLE);
        }
      }
      
      @Override
      public void onRefresh()
      {
        loadWishList();        
      }
      
      @Override
      public void onOptionSelected(ProductDetailBean productDetailInfo)
      {
        if(mainLsitener != null)
        {
          mainLsitener.ontabBarVisibleSet(false);
          productId = productDetailInfo.productId;
          optionAmountPickerView.setOptions(productDetailInfo.productOptionArr, productDetailInfo.productOptionList);
          optionAmountPickerView.setWishListener(wishListListener);
          optionButtonLayout.setVisibility(View.VISIBLE);
          optionAmountPickerView.setVisibility(View.VISIBLE);
          optionAmountPickerView.view.setVisibility(View.VISIBLE);
        }
      }
      
      @Override
      public void onOptionClose()
      {
        if(mainLsitener != null)
        {
          mainLsitener.ontabBarVisibleSet(true);
          optionAmountPickerView.initSpinner();
          optionButtonLayout.setVisibility(View.GONE);
          optionAmountPickerView.setVisibility(View.GONE);
          optionButtonLayout.setVisibility(View.GONE);
        }
      }
      
      @Override
      public void onProgressVisibleSet(boolean visible)
      {
        mainLsitener.onProgressLayoutVisibleSet(visible);
      }
    };
    
    adapter = new WishListAdapter(getActivity(), wishListListener);
    
    listView.addFooterView(v);
    listView.setAdapter(adapter);
    
    popularWishListContainer = (LinearLayout) view.findViewById(R.id.wish_list_container);
    
    return view;
  }
  
  
  @Override
  public void onResume()
  {
    super.onResume();
    loadWishList();
  }
  
  public void loadWishList()
  {
    new getWishListTask().execute();
  }
  
  public void setItemAttributes(String optionColor, String optionSize, int amount)
  {
    itemAttribute = "[{itemAttributes={\"" + "color" +"\"" + ":\"" + optionColor + "\"";
    if(optionSize == null)
      itemAttribute = itemAttribute + "},quantity=" + amount+ "}]";
    else
      itemAttribute = itemAttribute + ",\"" + "size" + "\"" + ":\"" + optionSize + "\"},quantity="+amount+"}]";
  }
  
  private OnClickListener cartClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if(optionAmountPickerView.getOptionSelected())
      {
        
        setItemAttributes(optionAmountPickerView.optionColor, optionAmountPickerView.optionSize, optionAmountPickerView.amount);
        if(v.getId() == R.id.btn_my_cart)
        {
          new AddCartTask().execute();
        }else if(v.getId() == R.id.btn_checkout)
        {
          String url = "newOrderNow?_hgy_token="+PreferenceManager.getInstance(getActivity()).getUserSeq()
              + "&product_id=" + productId + "&itemAttributesList=" + itemAttribute;
          Intent i = new Intent(getActivity(), TrendCartActivity.class);
          i.putExtra("cart_parameter", url);
          startActivity(i);
          wishListListener.onOptionClose();
        }
        
      }else
      {
        if(optionAmountPickerView.view.getVisibility() == View.VISIBLE)
        {
          new AlertDialogManager(getActivity()).showAlertDialog(getString(R.string.term_alert), "옵션을 선택해주세요.", 
              getString(R.string.term_ok), null, new AlertListener()
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
  };
  
  public void createAddCartAlert(boolean addCart)
  {
    String title = getString(R.string.term_alert);
    String message = "";
    String okString = "";
    String cancelString = "";
    if(addCart)
    {
      message = getString(R.string.term_product_add_cart);
      okString = getString(R.string.term_product_cart_move);
      cancelString = getString(R.string.term_product_shopping);
      new AlertDialogManager(getActivity()).showAlertDialog(title, message, okString, cancelString, new AlertListener()
      {
        @Override
        public void onPositiveButtonClickListener()
        {
          startActivity(new Intent(getActivity(), TrendCartActivity.class));
        }
        
        @Override
        public void onNegativeButtonClickListener()
        {
        }
      });
    }
    else
    {
      message = getString(R.string.term_product_fail_cart);
      okString = getString(R.string.term_ok);
      new AlertDialogManager(getActivity()).showAlertDialog(title, message, okString, null, null);
    }      
  }
  
  private class AddCartTask extends AsyncTask<String, Void, JSONObject>
  {
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    @Override
    protected JSONObject doInBackground(String... params)
    {
      HashMap<String, String> body = new HashMap<String, String>();
      body.put("product_id", productId);
      body.put("itemAttributesList", itemAttribute);
      body.put("_hgy_token", PreferenceManager.getInstance(getActivity()).getUserSeq());
      return apiClient.addCart(body);
    }
    
    @Override
    protected void onPostExecute(JSONObject result)
    {
      super.onPostExecute(result);
      wishListListener.onOptionClose();
      if(result != null)
      {
        try
        {
          if (result.has("result"))
          {
            JSONObject obj = result.getJSONObject("result");
            if(obj.getInt("status_code") == 0)
              createAddCartAlert(true);
            else
              createAddCartAlert(false);
          }
        }
        catch (Exception e)
        {
          BlinkingCommon.smlLibPrintException("TrendProductDetail", "e : " + e);
        }
      }else
        createAddCartAlert(false);
    }
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
    adapter.clear();
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
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    wishListListener.onRemove();
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
      popularWishArray = apiClient.getPopularWishList();
      return popularWishArray;
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
        PopularWishListParticleView pView = new PopularWishListParticleView(getActivity(), getActivity().getWindowManager(), wishListListener);
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
