package com.dabeeo.hangouyou.fragments.coupon;

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
import com.dabeeo.hangouyou.activities.coupon.DownloadedCouponDetailActivity;
import com.dabeeo.hangouyou.beans.CouponBean;
import com.dabeeo.hangouyou.controllers.coupon.DownloadedCouponListAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class DownloadedCouponListFragment extends Fragment
{
  private ProgressBar progressBar;
  private DownloadedCouponListAdapter adapter;
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
    
    adapter = new DownloadedCouponListAdapter();
    
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
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
//      PlaceBean bean = (PlaceBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), DownloadedCouponDetailActivity.class);
      i.putExtra("coupon_id", position);
      startActivity(i);
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
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getDownloadedCoupon(page, "Place");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
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
          bean.description = "100,000이상 구매 시";
          bean.fromValidityDate = "2015.04.11";
          bean.toValidityDate = "2015.09.11";
          bean.isUsed = i % 2 == 1;
          adapter.add(bean);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      adapter.notifyDataSetChanged();
      progressBar.setVisibility(View.GONE);
    }
  }
}
