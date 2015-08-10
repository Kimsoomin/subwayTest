package com.dabeeo.hanhayou.fragments.coupon;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.coupon.CouponActivity;
import com.dabeeo.hanhayou.activities.coupon.CouponDetailActivity;
import com.dabeeo.hanhayou.activities.coupon.DownloadedCouponDetailActivity;
import com.dabeeo.hanhayou.beans.CouponDetailBean;
import com.dabeeo.hanhayou.controllers.OfflineCouponDatabaseManager;
import com.dabeeo.hanhayou.controllers.coupon.DownloadedCouponListAdapter;
import com.dabeeo.hanhayou.managers.PreferenceManager;

public class DownloadedCouponListFragment extends Fragment
{
  private ProgressBar progressBar;
  private DownloadedCouponListAdapter adapter;
  private ListView listView;
  
  private LinearLayout emptyContainer;
  private OfflineCouponDatabaseManager couponDatabase;
  private int categoryId = -1;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_download_coupon;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    adapter = new DownloadedCouponListAdapter(getActivity());
    
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    ((Button) getView().findViewById(R.id.recommend_button)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        ((CouponActivity) getActivity()).setViewPagerPosition(0);
      }
    });
    listView = (ListView) getView().findViewById(R.id.listview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setAdapter(adapter);
    
    couponDatabase = new OfflineCouponDatabaseManager(getActivity());
  }
  
  
  public void changeFilteringMode(int categoryId)
  {
    this.categoryId = categoryId;
    load();
  }
  
  
  @Override
  public void onResume()
  {
    load();
    super.onResume();
  }
  
  
  private void load()
  {
    if (PreferenceManager.getInstance(getActivity()).isLoggedIn())
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
      CouponDetailBean bean = (CouponDetailBean) adapter.getItem(position);
      if (!bean.isUse)
      {
        Intent i = new Intent(getActivity(), DownloadedCouponDetailActivity.class);
        i.putExtra("coupon_idx", bean.couponIdx);
        i.putExtra("branch_idx", bean.branchIdx);
        startActivity(i);
      }
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<String, Integer, ArrayList<CouponDetailBean>>
  {
    @Override
    protected void onPreExecute()
    {
      try
      {
        progressBar.setVisibility(View.VISIBLE);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      adapter.clear();
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<CouponDetailBean> doInBackground(String... params)
    {
      Log.w("WARN", "Get DownloadCoupons");
      return couponDatabase.getDownloadCoupons();
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<CouponDetailBean> result)
    {
      super.onPostExecute(result);
      if (result.size() == 0)
      {
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
      }
      else
      {
        listView.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
        if (categoryId == -1)
          adapter.addAll(result);
        else
        {
          for (int i = 0; i < result.size(); i++)
          {
            if (Integer.parseInt(result.get(i).category) == categoryId)
              adapter.add(result.get(i));
          }
        }
      }
      
      progressBar.setVisibility(View.GONE);
    }
  }
}
