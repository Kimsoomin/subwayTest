package com.dabeeo.hangouyou.fragments.ticket;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.dabeeo.hangouyou.beans.CouponBean;
import com.dabeeo.hangouyou.controllers.ticket.CouponListAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class CouponListFragment extends Fragment
{
  private ProgressBar progressBar;
  private CouponListAdapter adapter;
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
    
    adapter = new CouponListAdapter();
    
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
      return apiClient.getAllCoupon(page, "Place");
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
          CouponBean bean = new CouponBean();
          bean.setJSONObject(objInArr);
          bean.description = "200,000 이상 구매시";
          bean.fromValidityDate = "2015.04.11";
          bean.toValidityDate = "2015.09.11";
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
