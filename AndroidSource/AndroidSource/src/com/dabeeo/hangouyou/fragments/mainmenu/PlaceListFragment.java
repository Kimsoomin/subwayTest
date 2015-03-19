package com.dabeeo.hangouyou.fragments.mainmenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.controllers.mypage.MyPlaceListAdapter;
import com.dabeeo.hangouyou.managers.NetworkManager;

public class PlaceListFragment extends Fragment
{
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private MyPlaceListAdapter adapter;
  private int offset = 0, limit = 10;
  
  
  public PlaceListFragment(int categoryId)
  {
    this.categoryId = categoryId;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_place_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new MyPlaceListAdapter(getActivity());
    
    ListView listView = (ListView) getView().findViewById(R.id.listview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    
    load(0);
  }
  
  
  private void load(int offset)
  {
    progressBar.setVisibility(View.VISIBLE);
    String url = getString(R.string.server_address) + "stores.json?limit=" + limit + "&offset=" + offset;
    if (categoryId != -1)
      url += "&sphere_id=" + categoryId;
    
    JsonObjectRequest request = new JsonObjectRequest(url, null, successListener, errorListener);
    NetworkManager.instance(getActivity()).call(request);
  }
  
  
  public void setCategoryId(int categoryId)
  {
    //전체, 명소, 쇼핑 등 
    this.categoryId = categoryId;
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      startActivity(new Intent(getActivity(), PlaceDetailActivity.class));
    }
  };
  
  private OnScrollListener scrollListener = new OnScrollListener()
  {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
      if (totalItemCount > 0 && totalItemCount > offset && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
        offset += limit;
        load(offset);
      }
    }
  };
  
  private Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>()
  {
    @Override
    public void onResponse(JSONObject jsonObject)
    {
      try
      {
        JSONArray stroes = jsonObject.getJSONArray("stores");
        for (int i = 0; i < stroes.length(); i++)
        {
          JSONObject store = stroes.getJSONObject(i);
          
          PlaceBean bean = new PlaceBean();
          bean.title = store.getString("name");
          bean.likeCount = store.getInt("like_count");
          
          JSONObject category = store.getJSONObject("category");
          bean.category = category.getString("title");
          
          bean.reviewCount = 11;
          
          adapter.add(bean);
        }
        adapter.notifyDataSetChanged();
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
      
      progressBar.setVisibility(View.GONE);
    }
  };
  
  private Response.ErrorListener errorListener = new Response.ErrorListener()
  {
    @Override
    public void onErrorResponse(VolleyError e)
    {
      progressBar.setVisibility(View.GONE);
      Log.e("RecommendSeoulActivity.java | onErrorResponse", "|" + e.getLocalizedMessage() + "|" + e.getMessage() + "|");
    }
  };
}
