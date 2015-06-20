package com.dabeeo.hanhayou.fragments.mainmenu;

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
import com.dabeeo.hanhayou.controllers.mainmenu.TravelScheduleListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.utils.SystemUtil;
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
  private int lastVisibleItem = 0;
  
  private LinearLayout emptyContainer;
  private TextView emptyText;
  private LinearLayout recommendContainer;
  private GridViewWithHeaderAndFooter listView;
  
  public int dayCount = 0;
  
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
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    apiClient = new ApiClient(getActivity());
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    emptyContainer = (LinearLayout) getView().findViewById(R.id.empty_container);
    emptyText = (TextView) getView().findViewById(R.id.text_empty);
    recommendContainer = (LinearLayout) getView().findViewById(R.id.recommend_container);
    ((TravelSchedulesActivity) getActivity()).showBottomTab(true);
    
    adapter = new TravelScheduleListAdapter(getActivity());
    listView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    ScheduleListHeaderMallView view = new ScheduleListHeaderMallView(getActivity());
    view.setBean(null);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(getActivity(), TrendExhibitionActivity.class);
        startActivity(i);
      }
    });
    listView.addHeaderView(view);
    
//    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    loadSchedules();
  }
  
  
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
    
    if (type == SCHEDULE_TYPE_POPULAR)
      new LoadScheduleAsyncTask().execute();
    else if (type == SCHEDULE_TYPE_MY)
    {
      progressBar.setVisibility(View.GONE);
      listView.setVisibility(View.GONE);
      emptyContainer.setVisibility(View.VISIBLE);
      emptyText.setText(getString(R.string.msg_empty_my_schedule));
      
      recommendContainer.setVisibility(View.VISIBLE);
      Button btnRecommendSchedule = (Button) getView().findViewById(R.id.recommend_button);
      btnRecommendSchedule.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View arg0)
        {
          if (!SystemUtil.isConnectNetwork(getActivity()))
            new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
          else
          {
            Intent i = new Intent(getActivity(), RecommendScheduleActivity.class);
            startActivity(i);
          }
        }
      });
    }
    else if (type == SCHEDULE_TYPE_BOOKMARK)
    {
      progressBar.setVisibility(View.GONE);
      listView.setVisibility(View.GONE);
      emptyContainer.setVisibility(View.VISIBLE);
      emptyText.setText(getString(R.string.msg_empty_my_bookmark));
      recommendContainer.setVisibility(View.GONE);
    }
  }
  
  private class LoadScheduleAsyncTask extends AsyncTask<String, Integer, ArrayList<ScheduleBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
//      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<ScheduleBean> doInBackground(String... params)
    {
      return apiClient.getTravelSchedules(page, dayCount);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ScheduleBean> result)
    {
      if (result.size() == 0)
        isLoadEnded = true;
      
      adapter.addAll(result);
      
      if (adapter.getCount() == 0)
      {
        listView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
        emptyText.setText(getString(R.string.msg_empty_my_schedule));
      }
      progressBar.setVisibility(View.GONE);
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
        page++;
        loadSchedules();
      }
    }
  };
}