package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
        editSearch.setText("");
      }
    });
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        Log.w("WARN", "length: " + editSearch.getText().toString().length());
        if (editSearch.getText().toString().length() > 1)
        {
          typingCancel.setVisibility(View.VISIBLE);
          listView.setVisibility(View.VISIBLE);
          emptyContainer.setVisibility(View.GONE);
          
          ProductBean bean = new ProductBean();
          bean.name = "[숨]워터풀 타임리스 워터젤 크림";
          bean.priceSale = "80000";
          bean.priceDiscount = "452000";
          adapter.add(bean);
          
          bean = new ProductBean();
          bean.name = "[SKII]나이트밤";
          bean.priceSale = "100000";
          bean.priceDiscount = "252000";
          adapter.add(bean);
        }
        else
        {
          typingCancel.setVisibility(View.GONE);
          listView.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
          
          adapter.clear();
        }
        
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
        PopularWishListParticleView pView = new PopularWishListParticleView(WishListSearchActivity.this, getWindowManager());
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
