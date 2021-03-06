package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
import com.dabeeo.hanhayou.activities.travel.TravelStrategyActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hanhayou.activities.trend.TrendExhibitionActivity;
import com.dabeeo.hanhayou.beans.PremiumBean;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.dabeeo.hanhayou.controllers.mainmenu.RecommendSeoulListAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.ListFooterProgressView;

public class TravelStrategyListFragment extends Fragment
{
  @SuppressWarnings("unused")
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private RecommendSeoulListAdapter adapter;
  private int page = 1;
  private boolean isLoading = false;
  private boolean isLoadEnded = false;
  private ApiClient apiClient;
  private int area = 0; // 0전체, 1한류, 2홍대, 3압구정, 4명동, 5인사동, 6기타
  private ListView listView;
  private ListFooterProgressView footerLoadView;
  
  private int themePosition = 0;
  private ArrayList<TrendKoreaBean> themeList;
  
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
    
    footerLoadView = new ListFooterProgressView(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    DisplayMetrics metrics = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int imageWidth = metrics.widthPixels;
    int imageheight = (int) (imageWidth * 0.56);
    
    adapter = new RecommendSeoulListAdapter(getActivity(), imageWidth, imageheight);
    
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
          load();
        }
      }
    });
    listView.setAdapter(adapter);
    
//    loadThemeList();
    //TODO: donghyun temp Prodcut Info Hidden
    load();
  }
  
  private void loadThemeList()
  {
    progressBar.setVisibility(View.VISIBLE);
    
    new GetThemeListAsyncTask().execute();
  }
  
  private void load()
  {
    new GetStoreAsyncTask().execute();
  }
  
  
  public void filtering(int area)
  {
    adapter.clear();
    
    page = 1;
    this.area = area;
    
    load();
  }
  
  private class GetThemeListAsyncTask extends AsyncTask<Void, Void, ArrayList<TrendKoreaBean>>
  {
    
    @Override
    protected ArrayList<TrendKoreaBean> doInBackground(Void... params)
    {
      themeList = null;
      themeList = apiClient.getThemeList(0);
      return themeList;
    }
    
    @Override
    protected void onPostExecute(ArrayList<TrendKoreaBean> result)
    {
      super.onPostExecute(result);
      new GetStoreAsyncTask().execute();
    }
  }
  
  private class GetStoreAsyncTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      if (adapter.getCount() != 0)
        listView.addFooterView(footerLoadView);
      super.onPreExecute();
    }
    
    
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
            
          //TODO: donghyun temp Prodcut Info Hidden
//            if(themeList.size()-1 > themePosition)
//            {
//              if((i+1) % 5 == 0)
//              {
//                bean.idx = themeList.get(themePosition).idx;
//                bean.title = null;
//                bean.imageUrl = themeList.get(themePosition).imageUrl;
//                themePosition = themePosition + 1;
//              }else
//                bean.setJSONObject(objInArr);
//            }else
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
      listView.removeFooterView(footerLoadView);
      isLoading = false;
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
      Intent i;
      if(bean.title != null)
      {
        i = new Intent(getActivity(), TravelStrategyDetailActivity.class);
        i.putExtra("place_idx", bean.idx);
      }else
      {
        i = new Intent(getActivity(), TrendExhibitionActivity.class);
        i.putExtra("themeIdx", bean.idx);
        i.putExtra("themeImageUrl", bean.imageUrl);
      }
      startActivity(i);
    }
  };
}
