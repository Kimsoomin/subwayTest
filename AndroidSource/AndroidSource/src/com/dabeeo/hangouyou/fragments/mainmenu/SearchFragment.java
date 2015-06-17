package com.dabeeo.hangouyou.fragments.mainmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.SearchResultDetailActivity;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultDetailAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.SystemUtil;
import com.dabeeo.hangouyou.views.PopularKeywordView;
import com.dabeeo.hangouyou.views.ProductView;

public class SearchFragment extends Fragment
{
  private EditText inputWord;
  private ViewGroup layoutRecommedProductParent, layoutRecommedProduct;
  private SearchResultDetailAdapter adapter;
  private ImageView imageX;
  private LinearLayout popularKeywordOuterContainer, popularKeyworkdContainer;
  private ListView searchListView;
  private ScrollView emptyContainer;
  
  public ApiClient apiClient;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_search;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    apiClient = new ApiClient(getActivity());
    
    popularKeywordOuterContainer = (LinearLayout) getView().findViewById(R.id.popular_keyword_outer_container);
    popularKeyworkdContainer = (LinearLayout) getView().findViewById(R.id.popular_keyword_container);
    searchListView = (ListView) getView().findViewById(R.id.search_list);
    emptyContainer = (ScrollView) getView().findViewById(R.id.empty_container);
    
    inputWord = (EditText) getView().findViewById(R.id.edit_search);
    inputWord.setOnEditorActionListener(editorActionListener);
    
    imageX = (ImageView) getView().findViewById(R.id.search_cancel);
    imageX.setVisibility(View.GONE);
    imageX.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        inputWord.setText("");
        search("");
      }
    });
    
    layoutRecommedProductParent = (LinearLayout) getView().findViewById(R.id.layout_recommend_product_parent);
    layoutRecommedProduct = (LinearLayout) getView().findViewById(R.id.layout_recommend_product);
    
    adapter = new SearchResultDetailAdapter();
    searchListView = (ListView) getView().findViewById(R.id.search_list);
    searchListView.setOnItemClickListener(itemClickListener);
    searchListView.setAdapter(adapter);
    
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
        if (s.length() >= 2)
        {
          search(s.toString());
        }
        else if (s.length() > 1)
        {
          searchListView.setVisibility(View.VISIBLE);
          emptyContainer.setVisibility(View.GONE);
          imageX.setVisibility(View.VISIBLE);
        }
        else
        {
          imageX.setVisibility(View.GONE);
          searchListView.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
        }
      }
      
    };
    inputWord.addTextChangedListener(watcher);
    
    loadPopularWords();
    loadRecommendProduct();
  }
  
  
  private void loadRecommendProduct()
  {
    layoutRecommedProduct.removeAllViews();
    ProductView productView = new ProductView(getActivity());
    ProductBean productBean = new ProductBean();
    productBean.title = "상품명1";
    productBean.originalPrice = 100;
    productBean.discountPrice = 40;
    productView.setBean(productBean, productBean);
    layoutRecommedProduct.addView(productView);
  }
  
  
  private void loadPopularWords()
  {
    popularKeywordOuterContainer.setVisibility(View.VISIBLE);
    popularKeyworkdContainer.removeAllViews();
    if (SystemUtil.isConnectNetwork(getActivity()))
    {
      new SearchPopularTask().execute();
    }
    else
    {
      //TODO offline 처리 필요
    }
  }
  
  
  private void search(String text)
  {
    new SearchTask().execute(text);
  }
  
  
  private void responseParser(String response)
  {
    String status = "";
    try
    {
      
      JSONObject jsonObject = new JSONObject(response);
      if (jsonObject.has("status"))
      {
        status = jsonObject.getString("status");
      }
      
      if (status.equals("OK"))
      {
        JSONArray jsonArray = jsonObject.getJSONArray("keyword");
        for (int i = 0; i < jsonArray.length(); i++)
        {
          PopularKeywordView view = new PopularKeywordView(getActivity());
          view.setData(i + 1, jsonArray.getString(i));
          popularKeyworkdContainer.addView(view);
        }
      }
    }
    catch (JSONException e)
    {
      status = "";
      e.printStackTrace();
    }
    
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnEditorActionListener editorActionListener = new OnEditorActionListener()
  {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
      if (actionId == EditorInfo.IME_ACTION_SEARCH)
      {
        Intent i = new Intent(getActivity(), SearchResultDetailActivity.class);
        i.putExtra("keyword", inputWord.getText().toString());
        startActivity(i);
      }
      return false;
    }
  };
  
  private OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      Intent i = new Intent(getActivity(), SearchResultDetailActivity.class);
      i.putExtra("keyword", ((SearchResultBean) adapter.getItem(position)).text);
      startActivity(i);
    }
  };
  
  /**************************************************
   * Search_Popular AsyncTask
   ***************************************************/
  private class SearchPopularTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.searchPopular();
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      responseParser(result.response);
      super.onPostExecute(result);
    }
  }
  
  private class SearchTask extends AsyncTask<String, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      adapter.clear();
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.searchAuto(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      String status = "";
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
        
        if (status.equals("OK"))
        {
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          for (int i = 0; i < jsonArray.length(); i++)
          {
            SearchResultBean bean = new SearchResultBean();
            JSONObject obj = jsonArray.getJSONObject(i);
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            adapter.add(bean);
          }
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
      if (adapter.getCount() == 0)
      {
        emptyContainer.setVisibility(View.VISIBLE);
        searchListView.setVisibility(View.GONE);
        loadPopularWords();
        return;
      }
      else
      {
        searchListView.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
      }
      super.onPostExecute(result);
    }
  }
}
