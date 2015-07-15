package com.dabeeo.hanhayou.fragments.mainmenu;

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

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hanhayou.beans.PlaceBean;
import com.dabeeo.hanhayou.controllers.mainmenu.PlaceListAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.ListFooterProgressView;

public class PlaceListFragment extends Fragment
{
  public static final int FILTERING_MODE_ALL = 0;
  public static final int FILTERING_MODE_PUPOLAR = 1;
  public static final int FILTERING_MODE_BOOKMARKED = 2;
  public static final int FILTERING_MODE_ADDED_BY_ME = 3;
  
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private PlaceListAdapter adapter;
  private int page = 1;
  private boolean isLoadEnded = false;
  private boolean isLoading = false;
  private ApiClient apiClient;
  private ListView listView;
  private ListFooterProgressView footerLoadView;
  
  private int filteringMode = FILTERING_MODE_ALL;
  
  
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
    
    footerLoadView = new ListFooterProgressView(getActivity());
    
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
        if (SystemUtil.isConnectNetwork(getActivity()) && !isLoading && !isLoadEnded && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
        {
          page++;
          load();
        }
      }
    });
    listView.setAdapter(adapter);
    
    load();
  }
  
  
  private void load()
  {
    progressBar.setVisibility(View.VISIBLE);
    new GetAllAsyncTask().execute();
  }
  
  
  public void setCategoryId(int categoryId)
  {
    //전체, 명소, 쇼핑 등 
    this.categoryId = categoryId;
  }
  
  
  public void changeFilteringMode(int mode)
  {
    filteringMode = mode;
    page = 0;
    adapter.clear();
    load();
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
  private class GetAllAsyncTask extends AsyncTask<Void, Integer, ArrayList<PlaceBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      listView.addFooterView(footerLoadView);
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<PlaceBean> doInBackground(Void... params)
    {
      ArrayList<PlaceBean> result = null;
      if (filteringMode == FILTERING_MODE_PUPOLAR)
        result = apiClient.getPlaceListByPopular(page, categoryId);
      else if (filteringMode == FILTERING_MODE_BOOKMARKED)
        result = apiClient.getPlaceListByBookmarked(page, categoryId);
      else if (filteringMode == FILTERING_MODE_ADDED_BY_ME)
        result = apiClient.getPlaceListByAddedByMe(page, categoryId);
      else
        result = apiClient.getPlaceList(page, categoryId);
      return result;
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<PlaceBean> result)
    {
      adapter.addAll(result);
      if (result.size() < 10)
        isLoadEnded = true;
      
      if (adapter.getCount() == 0)
        listView.setVisibility(View.GONE);
      else
        listView.setVisibility(View.VISIBLE);
      isLoading = false;
      progressBar.setVisibility(View.GONE);
      listView.removeFooterView(footerLoadView);
      super.onPostExecute(result);
    }
  }
}
