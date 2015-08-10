package com.dabeeo.hanhayou.fragments.coupon;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.coupon.CouponDetailActivity;
import com.dabeeo.hanhayou.beans.CouponBean;
import com.dabeeo.hanhayou.controllers.coupon.CouponListAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;

public class CouponListFragment extends Fragment
{
  private ProgressBar progressBar;
  private ListView listMyLocation, listPopular;
  private CouponListAdapter listMyLocationAdapter;
  private CouponListAdapter listPopularAdapter;
  private int myLocationListpage = 1;
  private int popularListpage = 1;
  private ApiClient apiClient;
  private LocationManager locationManager;
  private LocationListener locationListener;
  
  private Handler handler = new Handler();
  private static int LCOATION_TIME_OUT_SECOND = 30 * 1000;
  
  private boolean isMyLocationLoadEnded = false;
  private boolean isPopularLoadEnded = false;
  private Button btnMyLocation, btnPopular;
  private Location currentLocation;
  private boolean isResultGPSOn = false;
  
  private LinearLayout emptyContainer;
  private int categoryId = -1;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_coupon;
    View view = inflater.inflate(resId, null);
    btnMyLocation = (Button) view.findViewById(R.id.btn_my_location);
    btnPopular = (Button) view.findViewById(R.id.btn_popular);
    return view;
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    listMyLocationAdapter = new CouponListAdapter(getActivity());
    listMyLocation = (ListView) getView().findViewById(R.id.list_my_location);
    listMyLocation.setOnItemClickListener(itemClickListener);
    listMyLocation.setOnScrollListener(scrollListener);
    listMyLocation.setAdapter(listMyLocationAdapter);
    
    listPopularAdapter = new CouponListAdapter(getActivity());
    listPopular = (ListView) getView().findViewById(R.id.list_popular);
    listPopular.setOnItemClickListener(itemClickListener);
    listPopular.setOnScrollListener(scrollListener);
    listPopular.setAdapter(listPopularAdapter);
    
    btnMyLocation.setOnClickListener(btnClickListener);
    btnPopular.setOnClickListener(btnClickListener);
    btnMyLocation.setActivated(true);
    
    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    locationListener = new SubwayLocationListener();
    
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    selectedListView(true);
  }
  
  
  public void changeFilteringMode(int categoryId)
  {
    this.categoryId = categoryId;
    if (btnMyLocation.isActivated())
      myLocationListpage = 1;
    else
      popularListpage = 1;
    
    if (btnMyLocation.isActivated())
      listMyLocationAdapter.clear();
    else
      listPopularAdapter.clear();
    load();
  }
  
  
  @Override
  public void onResume()
  {
    if (isResultGPSOn)
    {
      selectedListView(true);
      isResultGPSOn = false;
    }
    super.onResume();
  }
  
  private Runnable timeOutFindStationCallBack = new Runnable()
  {
    public void run()
    {
      selectedListView(false);
    }
  };
  
  private OnClickListener btnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnMyLocation.getId())
        selectedListView(true);
      else
        selectedListView(false);
    }
  };
  
  
  private void selectedListView(boolean isMyLocation)
  {
    btnMyLocation.setActivated(false);
    btnPopular.setActivated(false);
    
    if (isMyLocation)
    {
      if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        buildAlertMessageNoGps();
      else
      {
        myLocationListpage = 1;
        listMyLocationAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        
        handler.postDelayed(timeOutFindStationCallBack, LCOATION_TIME_OUT_SECOND);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        btnMyLocation.setActivated(true);
      }
    }
    else
    {
      popularListpage = 1;
      listPopularAdapter.clear();
      btnPopular.setActivated(true);
      listPopular.setVisibility(View.VISIBLE);
      listMyLocation.setVisibility(View.GONE);
      
      load();
    }
  }
  
  
  private void buildAlertMessageNoGps()
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.app_name).setMessage(R.string.msg_please_gps_enable).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        isResultGPSOn = true;
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
      }
    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      public void onClick(final DialogInterface dialog, final int id)
      {
        selectedListView(false);
        dialog.cancel();
      }
    });
    final AlertDialog alert = builder.create();
    alert.show();
  }
  
  
  private void load()
  {
    new GetAsyncTask().execute();
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      try
      {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      if (btnMyLocation.isActivated())
        if (currentLocation != null)
          return apiClient.getCouponWithMyLocation(myLocationListpage, currentLocation.getLatitude(), currentLocation.getLongitude());
        else
          return null;
      else
        return apiClient.getCouponPopular(popularListpage);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result == null && !result.isSuccess)
        return;
      
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (obj.has("coupon"))
        {
          JSONArray arr = obj.getJSONArray("coupon");
          for (int i = 0; i < arr.length(); i++)
          {
            JSONObject objInArr = arr.getJSONObject(i);
            CouponBean bean = new CouponBean();
            bean.setJSONObject(objInArr);
            
            if (categoryId == -1)
            {
              if (btnMyLocation.isActivated())
                listMyLocationAdapter.add(bean);
              else
                listPopularAdapter.add(bean);
            }
            else
            {
              if (btnMyLocation.isActivated())
              {
                if (Integer.parseInt(bean.category) == categoryId)
                  listMyLocationAdapter.add(bean);
              }
              else
              {
                if (Integer.parseInt(bean.category) == categoryId)
                  listPopularAdapter.add(bean);
              }
            }
          }
          
          if (btnMyLocation.isActivated())
            listMyLocationAdapter.notifyDataSetChanged();
          else
            listPopularAdapter.notifyDataSetChanged();
        }
        else
        {
          if (btnMyLocation.isActivated())
            isMyLocationLoadEnded = true;
          if (btnPopular.isActivated())
            isPopularLoadEnded = true;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      if (btnMyLocation.isActivated())
      {
        if (listMyLocationAdapter.getCount() == 0)
        {
          listMyLocation.setVisibility(View.GONE);
          listPopular.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
        }
        else
        {
          listMyLocation.setVisibility(View.VISIBLE);
          listPopular.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.GONE);
        }
      }
      else
      {
        if (listPopularAdapter.getCount() == 0)
        {
          listMyLocation.setVisibility(View.GONE);
          listPopular.setVisibility(View.GONE);
          emptyContainer.setVisibility(View.VISIBLE);
        }
        else
        {
          listMyLocation.setVisibility(View.GONE);
          listPopular.setVisibility(View.VISIBLE);
          emptyContainer.setVisibility(View.GONE);
        }
      }
      
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private class SubwayLocationListener implements LocationListener
  {
    
    @Override
    public void onLocationChanged(Location loc)
    {
      handler.removeCallbacks(timeOutFindStationCallBack);
      Log.w("WARN", "LocationChanged! " + loc.getLatitude() + " / " + loc.getLongitude());
      locationManager.removeUpdates(locationListener);
      
      currentLocation = loc;
      load();
    }
    
    
    @Override
    public void onProviderDisabled(String provider)
    {
    }
    
    
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
  }
  
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      CouponBean bean = null;
      if (btnMyLocation.isActivated())
        bean = (CouponBean) listMyLocationAdapter.getItem(position);
      else
        bean = (CouponBean) listPopularAdapter.getItem(position);
      
      Intent i = new Intent(getActivity(), CouponDetailActivity.class);
      i.putExtra("coupon_idx", bean.couponIdx);
      i.putExtra("branch_idx", bean.branchIdx);
      getActivity().startActivityForResult(i, 1);
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
        if (btnMyLocation.isActivated() && isMyLocationLoadEnded)
          return;
        if (btnPopular.isActivated() && isPopularLoadEnded)
          return;
        
        if (btnMyLocation.isActivated())
          myLocationListpage++;
        else
          popularListpage++;
        
        load();
      }
    }
  };
  
}
