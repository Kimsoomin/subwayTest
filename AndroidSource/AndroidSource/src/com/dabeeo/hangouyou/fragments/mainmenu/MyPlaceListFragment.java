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
import com.dabeeo.hangouyou.activities.mypage.sub.MyPlaceDetailActivity;
import com.dabeeo.hangouyou.beans.PlaceBean;
import com.dabeeo.hangouyou.controllers.MyPlaceListAdapter;

/**
 * Fragment that allows controlling the colour of lights using HSV colour wheel.
 */
public class MyPlaceListFragment extends Fragment
{
  private Activity activity;
  private View view;
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private ListView listView;
  private MyPlaceListAdapter adapter;
  
  
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
      int resId = R.layout.fragment_my_place_list;
      view = LayoutInflater.from(activity).inflate(resId, null);
    }
    
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    
    listView = (ListView) view.findViewById(R.id.listview);
    adapter = new MyPlaceListAdapter(activity);
    listView.setAdapter(adapter);
    
    listView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
      {
        startActivity(new Intent(activity, MyPlaceDetailActivity.class));
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
    
    loadPlaces();
  }
  
  
  private void loadPlaces()
  {
    //Category Id 활용해서 써야 함 
    
    progressBar.setVisibility(View.VISIBLE);
    
    //테스트 가데이터 
    PlaceBean bean = new PlaceBean();
    bean.title = "왓슨스";
    bean.category = "Shopping";
    bean.likeCount = 7;
    bean.reviewCount = 11;
    adapter.add(bean);
    
    bean = new PlaceBean();
    bean.title = "GS편의점";
    bean.category = "Shopping";
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    progressBar.setVisibility(View.GONE);
  }
  
  
  public void setCategoryId(int categoryId)
  {
    //전체, 명소, 쇼핑 등 
    this.categoryId = categoryId;
  }
}
