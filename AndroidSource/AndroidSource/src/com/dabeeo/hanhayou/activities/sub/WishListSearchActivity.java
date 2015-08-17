package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PopularWishBean;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.mainmenu.WishSearchListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.views.PopularWishListParticleView;

public class WishListSearchActivity extends Activity
{
  private EditText editSearch;
  private ImageView backImage;
  private ImageView typingCancel;
  
  private LinearLayout emptyContainer, popularWishListContainer;
  private GridViewWithHeaderAndFooter listView;
  private WishSearchListAdapter adapter;
  
  private ApiClient apiClient;
  private ArrayList<ProductBean> productList;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wishlist_search);
    
    apiClient = new ApiClient(WishListSearchActivity.this);
    
    emptyContainer = (LinearLayout) findViewById(R.id.empty_container);
    listView = (GridViewWithHeaderAndFooter) findViewById(R.id.listview);
    adapter = new WishSearchListAdapter(this);
    listView.setAdapter(adapter);
    
    popularWishListContainer = (LinearLayout) findViewById(R.id.wish_list_container);
    
    new PopularWishList().execute();
    
    editSearch = (EditText) findViewById(R.id.edit_search);
    typingCancel = (ImageView) findViewById(R.id.search_cancel);
    typingCancel.setVisibility(View.GONE);
    typingCancel.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        hideKeyboard();
        editSearch.setText("");
      }
    });
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
        Log.w("WARN", "length: " + editSearch.getText().toString().length());
        if (editSearch.getText().toString().length() > 1)
        {
          typingCancel.setVisibility(View.VISIBLE);
          listView.setVisibility(View.VISIBLE);
          emptyContainer.setVisibility(View.GONE);
          new GetProductListAsyncTask().execute(editSearch.getText().toString());
        }
        else
        {
          typingCancel.setVisibility(View.GONE);
          listView.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
          adapter.clear();
        }
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
      }
    };
    editSearch.addTextChangedListener(watcher);
    
    backImage = (ImageView) findViewById(R.id.image_back_button);
    backImage.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
  }
  
  private void hideKeyboard()
  {
    editSearch.clearFocus();
    try
    {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void searchResult(int result)
  { 
//    searchContainer.setVisibility(View.VISIBLE);
    
    if(result>0)
    {
//      listView.setVisibility(View.VISIBLE);
//      searchNotExistContainer.setVisibility(View.GONE);
    }else
    {
//      listView.setVisibility(View.GONE);
//      searchNotExistContainer.setVisibility(View.VISIBLE);
    }
  }
  
  private class GetProductListAsyncTask extends AsyncTask<String, Void, ArrayList<ProductBean>>
  {
    
    @Override
    protected ArrayList<ProductBean> doInBackground(String... params) 
    {
      if(!TextUtils.isEmpty(params[0]))
      {
        productList = apiClient.getProductListSearch(params[0]);
      }
      
      return productList;
    }
    
    @Override
    protected void onPostExecute(ArrayList<ProductBean> result) 
    {
      adapter.clear();
      adapter.addAll(result);
      
      searchResult(productList.size());
      
      super.onPostExecute(result);
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
      String leftName = "";
      String leftId = "";
      String rightName = "";
      String rightId = "";
      super.onPostExecute(result);
      
      int position = 0;
      for(int i = 0; i < result.size(); i++)
      {
        PopularWishListParticleView pView = new PopularWishListParticleView(WishListSearchActivity.this, getWindowManager(), null);
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
