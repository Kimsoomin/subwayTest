/******************************************************************************
 * Copyright (C) Cambridge Silicon Radio Limited 2014 This software is provided
 * to the customer for evaluation purposes only and, as such early feedback on
 * performance and operation is anticipated. The software source code is subject
 * to change and not intended for production. Use of developmental release
 * software is at the user's own risk. This software is provided "as is," and
 * CSR cautions users to determine for themselves the suitability of using the
 * beta release version of this software. CSR makes no warranty or
 * representation whatsoever of merchantability or fitness of the product for
 * any particular purpose or use. In no event shall CSR be liable for any
 * consequential, incidental or special damages whatsoever arising out of the
 * use of or inability to use this software, even if the user has advised CSR of
 * the possibility of such damages.
 ******************************************************************************/

package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Activity;
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

/**
 * Fragment that allows controlling the colour of lights using HSV colour wheel.
 */
public class TravelScheduleListFragment extends Fragment
{
  private Activity activity;
  private View view;
  
  private ProgressBar progressBar;
  private ListView listView;
  private TravelScheduleListAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    return view;
  }
  
  
  @Override
  public void onAttach(final Activity activity)
  {
    super.onAttach(activity);
    this.activity = activity;
    if (view == null)
    {
      int resId = R.layout.fragment_travel_schedule_list;
      view = LayoutInflater.from(activity).inflate(resId, null);
    }
    
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    
    listView = (ListView) view.findViewById(R.id.listview);
    adapter = new TravelScheduleListAdapter(activity);
    listView.setAdapter(adapter);
    
    listView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
      {
        startActivity(new Intent(activity, TravelScheduleDetailActivity.class));
      }
    });
    
    listView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
        
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
//        if (!isLoading && !isLoadEnded && (totalItemCount > 0 && (totalItemCount - firstVisibleItem <= visibleItemCount)))
//        {
//          offset = offset + limit;
//          loadSchedules();
//        }
      }
    });
    
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
}
