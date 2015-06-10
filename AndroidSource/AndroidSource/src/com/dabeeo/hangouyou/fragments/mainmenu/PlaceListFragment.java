package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

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
import com.dabeeo.hangouyou.map.BlinkingCommon;

public class PlaceListFragment extends Fragment
{
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private PlaceListAdapter adapter;
  private int page = 0;
  private boolean isLoadEnded = false;
  private boolean isLoading = false;
  private ApiClient apiClient;
  private ListView listView;
  
  
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
    ((TravelStrategyActivity) getActivity()).showBottomTab(true);
    
    listView = (ListView) getView().findViewById(R.id.listview);
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
        if (!isLoading && !isLoadEnded && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
        {
          page++;
          BlinkingCommon.smlLibDebug("PlaceListFragment", "page : " + page);
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
    new GetStoreAsyncTask().execute(page);
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
      PlaceBean bean = (PlaceBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), PlaceDetailActivity.class);
      i.putExtra("place_idx", bean.idx);
      startActivity(i);
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetStoreAsyncTask extends AsyncTask<Integer, Integer, ArrayList<PlaceBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<PlaceBean> doInBackground(Integer... params)
    {
      return apiClient.getPlaceList(params[0], categoryId);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<PlaceBean> result)
    {
      adapter.addAll(result);
      if (result.size() < 10)
        isLoadEnded = true;
      
      if (adapter.getCount() == 0)
      {
        listView.setVisibility(View.GONE);
      }
      isLoading = false;
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
}
