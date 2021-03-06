package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.schedule.RecommendScheduleActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelSchedulesActivity;
import com.dabeeo.hanhayou.activities.trend.TrendExhibitionActivity;
import com.dabeeo.hanhayou.beans.ScheduleBean;
import com.dabeeo.hanhayou.beans.TrendKoreaBean;
import com.dabeeo.hanhayou.controllers.NetworkBraodCastReceiver;
import com.dabeeo.hanhayou.controllers.mainmenu.TravelScheduleListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.ListFooterProgressView;
import com.dabeeo.hanhayou.views.ScheduleListHeaderMallView;

public class TravelScheduleListFragment extends Fragment
{
  public static final int SCHEDULE_TYPE_POPULAR = 0;
  public static final int SCHEDULE_TYPE_MY = 1;
  public static final int SCHEDULE_TYPE_BOOKMARK = 2;
  
  private ProgressBar progressBar;
  private TravelScheduleListAdapter adapter;
  private ApiClient apiClient;
  private int page = 1;
  private int type = SCHEDULE_TYPE_POPULAR;
  private boolean isLoading = false;
  private boolean isLoadEnded = false;
  @SuppressWarnings("unused")
  private int lastVisibleItem = 0;
  
  private LinearLayout emptyContainer;
  private TextView emptyText;
  private LinearLayout recommendContainer;
  private GridViewWithHeaderAndFooter listView;
  private ListFooterProgressView footerLoadView;
  
  private ScheduleListHeaderMallView headerView;
  private TrendKoreaBean bean;
  
  public int dayCount = -1;
  
  
  public TravelScheduleListFragment(int type)
  {
    this.type = type;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_list;
    return inflater.inflate(resId, null);
  }
  
  
  public int getItemCount()
  {
    int count = 0;
    try
    {
      count = adapter.getCount();
    }
    catch (Exception e)
    {
    }
    return count;
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    footerLoadView = new ListFooterProgressView(getActivity());
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    IntentFilter filter = new IntentFilter(NetworkBraodCastReceiver.ACTION_LOGIN);
    getActivity().registerReceiver(receiver, filter);
    
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    emptyText = (TextView) getView().findViewById(R.id.text_empty);
    recommendContainer = (LinearLayout) getView().findViewById(R.id.recommend_container);
    ((TravelSchedulesActivity) getActivity()).showBottomTab(true);
    
    adapter = new TravelScheduleListAdapter(getActivity());
    adapter.setType(type);
    listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    //TODO: donghyun temp Prodcut Info Hidden
//    if (SystemUtil.isConnectNetwork(getActivity()) && (type == SCHEDULE_TYPE_POPULAR))
//    {
//      DisplayMetrics metrics = new DisplayMetrics();
//      getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//      int imageWidth = metrics.widthPixels;
//      int imageheight = (int) (imageWidth * 0.43);
//      
//      headerView = new ScheduleListHeaderMallView(getActivity(), imageWidth, imageheight);
//      new GetScheduleThemeAsyncTask().execute();
//      headerView.setOnClickListener(new OnClickListener()
//      {
//        @Override
//        public void onClick(View arg0)
//        {
//          Intent i = new Intent(getActivity(), TrendExhibitionActivity.class);
//          i.putExtra("themeIdx", bean.idx);
//          i.putExtra("themeImageUrl", bean.imageUrl);
//          startActivity(i);
//        }
//      });
//      listView.addHeaderView(headerView);
//    }
    listView.addFooterView(footerLoadView);
    listView.setInvisibleFooterView(footerLoadView);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
  }
  
  
  @Override
  public void onResume()
  {
    loadSchedules();
    super.onResume();
  }
  
  
  @Override
  public void onDestroy()
  {
    getActivity().unregisterReceiver(receiver);
    super.onDestroy();
  }
  
  //Login change receiver
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      Log.w("WARN", "TravelScheduleListFragment receive action login");
      isLoadEnded = false;
      adapter.clear();
      page = 1;
      loadSchedules();
    }
  };
  
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  
  public void setDayCount(int daycount)
  {
    adapter.clear();
    this.dayCount = daycount;
    page = 1;
    loadSchedules();
  }
  
  
  private void loadSchedules()
  {
    progressBar.setVisibility(View.VISIBLE);
    new LoadScheduleAsyncTask().execute();
  }
  
  private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ArrayList<ScheduleBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      if (type != SCHEDULE_TYPE_POPULAR)
        adapter.clear();
      if (adapter.getCount() != 0)
        listView.setVisibleFooterView(footerLoadView);
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<ScheduleBean> doInBackground(String... params)
    {
      if (type == SCHEDULE_TYPE_MY)
        return apiClient.getMyTravelSchedules(dayCount);
      else if (type == SCHEDULE_TYPE_BOOKMARK)
        return apiClient.getBookmarkedSchedules(dayCount);
      else
        return apiClient.getTravelSchedules(page, dayCount);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ScheduleBean> result)
    {
      Log.w("WARN", "result getCount : " + result.size());
      if (result.size() == 0)
        isLoadEnded = true;
      
      adapter.addAll(result);
      
      if (adapter.getCount() == 0)
      {
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
        if (type == SCHEDULE_TYPE_POPULAR)
        {
          emptyText.setText(getString(R.string.msg_empty_my_schedule));
        }
        else if (type == SCHEDULE_TYPE_MY)
        {
          emptyText.setText(getString(R.string.msg_empty_my_schedule));
          recommendContainer.setVisibility(View.VISIBLE);
          Button btnRecommendSchedule = (Button) getView().findViewById(R.id.recommend_button);
          btnRecommendSchedule.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View arg0)
            {
              if (!PreferenceManager.getInstance(getActivity()).isLoggedIn())
              {
                new AlertDialogManager(getActivity()).showNeedLoginDialog(-1);
              }
              else
              {
                Intent i = new Intent(getActivity(), RecommendScheduleActivity.class);
                startActivity(i);
              }
            }
          });
        }
        else
        {
          emptyText.setText(getString(R.string.msg_empty_my_bookmark));
          recommendContainer.setVisibility(View.GONE);
        }
      }
      else
      {
        listView.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
      }
      progressBar.setVisibility(View.GONE);
      listView.setInvisibleFooterView(footerLoadView);
      isLoading = false;
      
      try
      {
        ((TravelSchedulesActivity) getActivity()).invalidateSortItem();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      super.onPostExecute(result);
    }
  }
  
  private class GetScheduleThemeAsyncTask extends AsyncTask<Void, Integer, ArrayList<TrendKoreaBean>>
  {
    
    @Override
    protected ArrayList<TrendKoreaBean> doInBackground(Void... params)
    {
      ArrayList<TrendKoreaBean> result = null;
      result = apiClient.getThemeList(1);
      return result;
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<TrendKoreaBean> result)
    {
      super.onPostExecute(result);
      bean = result.get(0);
      headerView.setBean(bean);
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
      ScheduleBean bean = (ScheduleBean) adapter.getItem(position);
      Intent i = new Intent(getActivity(), TravelScheduleDetailActivity.class);
      i.putExtra("idx", bean.idx);
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
      if (!isLoadEnded && !isLoading && totalItemCount > 0 && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
        Log.w("WARN", "isLoad ended:" + isLoadEnded);
        if (type == SCHEDULE_TYPE_POPULAR)
        {
          page++;
          loadSchedules();
        }
      }
    }
  };
}
