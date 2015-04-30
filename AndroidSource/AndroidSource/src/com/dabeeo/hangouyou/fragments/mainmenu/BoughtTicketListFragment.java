package com.dabeeo.hangouyou.fragments.mainmenu;

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
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.controllers.mainmenu.BoughtTicketListAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class BoughtTicketListFragment extends Fragment
{
  private ProgressBar progressBar;
  private BoughtTicketListAdapter adapter;
  private int page = 1;
  private ApiClient apiClient;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_ticket_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new BoughtTicketListAdapter();
    
    ListView listView = (ListView) getView().findViewById(android.R.id.list);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    
    load(page);
  }
  
  
  private void load(int offset)
  {
    progressBar.setVisibility(View.VISIBLE);
    new GetAsyncTask().execute();
  }
  
  private class GetAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getAllTicket(page, "Place");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (!result.isSuccess)
        return;
      
      try
      {
        JSONObject obj = new JSONObject(result.response);
        JSONArray arr = obj.getJSONArray("travelog");
        for (int i = 0; i < arr.length(); i++)
        {
          JSONObject objInArr = arr.getJSONObject(i);
          TicketBean bean = new TicketBean();
          bean.setJSONObject(objInArr);
          bean.discountRate = "8折";
          bean.priceWon = 10000;
          bean.priceYuan = 57;
          bean.fromUseableDate = "2015.04.11";
          bean.toUseableDate = "2015.09.11";
          adapter.add(bean);
        }
        adapter.notifyDataSetChanged();
      }
      catch (Exception e)
      {
        e.printStackTrace();
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
//      PlaceBean bean = (PlaceBean) adapter.getItem(position);
//      Intent i = new Intent(getActivity(), PlaceDetailActivity.class);
//      i.putExtra("place_idx", bean.idx);
//      startActivity(i);
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
      if (totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
        page++;
        load(page);
      }
    }
  };
}
