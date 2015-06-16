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
import com.dabeeo.hangouyou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.beans.PremiumBean;
import com.dabeeo.hangouyou.controllers.mainmenu.RecommendSeoulListAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class TravelStrategyListFragment extends Fragment
{
  @SuppressWarnings("unused")
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private RecommendSeoulListAdapter adapter;
  private int page = 1;
  private boolean isLoadEnded = false;
  private ApiClient apiClient;
  private int area = 0; // 0전체, 1한류, 2홍대, 3압구정, 4명동, 5인사동, 6기타 
  
  
  public TravelStrategyListFragment(int categoryId)
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
    
    adapter = new RecommendSeoulListAdapter(getActivity());
    
    ((TravelStrategyActivity) getActivity()).showBottomTab(true);
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
        if (!isLoadEnded && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
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
    
    new GetStoreAsyncTask().execute();
  }
  
  
  public void filtering(int area)
  {
    adapter.clear();
    
    page = 1;
    this.area = area;
    
    load();
  }
  
  private class GetStoreAsyncTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getPremiumList(page, area);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        ArrayList<PremiumBean> places = new ArrayList<PremiumBean>();
        try
        {
          JSONObject obj = new JSONObject(result.response);
          JSONArray arr = obj.getJSONArray("premium");
          for (int i = 0; i < arr.length(); i++)
          {
            JSONObject objInArr = arr.getJSONObject(i);
            PremiumBean bean = new PremiumBean();
            bean.setJSONObject(objInArr);
            places.add(bean);
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
        if (places.size() == 0)
          isLoadEnded = true;
        adapter.addAll(places);
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      PremiumBean bean = (PremiumBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), TravelStrategyDetailActivity.class);
      i.putExtra("place_idx", bean.idx);
      startActivity(i);
    }
  };
}
