package com.dabeeo.hanhayou.fragments.mainmenu;

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

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.activities.sub.SearchResultDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.SearchResultBean;
import com.dabeeo.hanhayou.controllers.SearchResultDetailAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.PopularKeywordView;
import com.dabeeo.hanhayou.views.ProductView;

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
        if (s.length() >= 2)
        {
          search(s.toString());
        }
        else
        {
          imageX.setVisibility(View.GONE);
          searchListView.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
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
    inputWord.addTextChangedListener(watcher);
    
    if (SystemUtil.isConnectNetwork(getActivity()))
    {
      loadPopularWords();
      loadRecommendProduct();
    }
    else
    {
      layoutRecommedProductParent.setVisibility(View.GONE);
      popularKeywordOuterContainer.setVisibility(View.GONE);
    }
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
      Intent i = null;
      SearchResultBean bean = (SearchResultBean) adapter.getItem(position);
      if (bean.type == SearchResultBean.TYPE_PLACE)
      {
        i = new Intent(getActivity(), PlaceDetailActivity.class);
        i.putExtra("place_idx", bean.idx);
      }
      else if (bean.type == SearchResultBean.TYPE_RECOMMEND_SEOUL)
      {
        i = new Intent(getActivity(), TravelStrategyDetailActivity.class);
        i.putExtra("place_idx", bean.idx);
      }
      else if (bean.type == SearchResultBean.TYPE_SCHEDULE)
      {
        i = new Intent(getActivity(), TravelScheduleDetailActivity.class);
        i.putExtra("idx", bean.idx);
      }
      
      if (i != null)
        startActivity(i);
    }
  };
  
  /**************************************************
   * AsyncTask
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
            bean.setLogType(obj.getString("logType"));
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
        searchListView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
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
