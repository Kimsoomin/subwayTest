package com.dabeeo.hangouyou.fragments.mainmenu;

import android.content.Intent;
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
import com.dabeeo.hangouyou.activities.mainmenu.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.beans.ScheduleBean;
import com.dabeeo.hangouyou.controllers.mainmenu.TravelScheduleListAdapter;

public class TravelScheduleListFragment extends Fragment
{
  private ProgressBar progressBar;
  private TravelScheduleListAdapter adapter;
  
  
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
    
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new TravelScheduleListAdapter(getActivity());
    ListView listView = (ListView) getView().findViewById(android.R.id.list);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    
    loadSchedules();
  }
  
  
  private void loadSchedules()
  {
    progressBar.setVisibility(View.VISIBLE);
    
    //테스트 가데이터 
    ScheduleBean bean = new ScheduleBean();
    bean.title = "쇼핑천국 서울";
    bean.month = 1;
    bean.likeCount = 7;
    bean.reviewCount = 11;
    adapter.add(bean);
    
    bean = new ScheduleBean();
    bean.title = "서울투어1";
    bean.month = 2;
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    bean = new ScheduleBean();
    bean.title = "서울투어2";
    bean.month = 2;
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    bean = new ScheduleBean();
    bean.title = "서울투어3";
    bean.month = 2;
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    bean = new ScheduleBean();
    bean.title = "서울투어4";
    bean.month = 2;
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    progressBar.setVisibility(View.GONE);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      startActivity(new Intent(getActivity(), TravelScheduleDetailActivity.class));
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
//      if (totalItemCount > 0 && totalItemCount > offset && totalItemCount <= firstVisibleItem + visibleItemCount)
//      {
//        offset += limit;
//        load(offset);
//      }
    }
  };
}
