package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.controllers.mainmenu.PlaceListAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class PlaceListFragment extends Fragment
{
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private PlaceListAdapter adapter;
  private int page = 1;
  private ApiClient apiClient;
  private int lastVisibleItem = 0;
  
  
  public PlaceListFragment(int categoryId)
  {
    this.categoryId = categoryId;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_travel_strategy_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new PlaceListAdapter(getActivity());
    
    ListView listView = (ListView) getView().findViewById(R.id.listview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
        if (firstVisibleItem > lastVisibleItem)
          ((TravelStrategyActivity) getActivity()).showBottomTab(true);
        else
          ((TravelStrategyActivity) getActivity()).showBottomTab(false);
        
        if (totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
        {
          page++;
          load(page);
        }
      }
    });
    listView.setAdapter(adapter);
    
    load(page);
  }
  
  
  private void load(int offset)
  {
    progressBar.setVisibility(View.VISIBLE);
    new GetStoreAsyncTask().execute();
  }
  
  private class GetStoreAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getPlaceList(page, categoryId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        ArrayList<PlaceBean> places = new ArrayList<PlaceBean>();
        try
        {
          JSONObject obj = new JSONObject(result.response);
          JSONArray arr = obj.getJSONArray("place");
          for (int i = 0; i < arr.length(); i++)
          {
            JSONObject objInArr = arr.getJSONObject(i);
            PlaceBean bean = new PlaceBean();
            bean.setJSONObject(objInArr);
            places.add(bean);
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        adapter.addAll(places);
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
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
      //
      PlaceBean bean = (PlaceBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), PlaceDetailActivity.class);
      i.putExtra("place_idx", bean.idx);
      startActivity(i);
    }
  };
  
}
