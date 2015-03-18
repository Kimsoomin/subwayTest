package com.dabeeo.hangouyou.fragments.mypage;

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
import com.dabeeo.hangouyou.controllers.mypage.MyPlaceListAdapter;

public class MyPlaceListFragment extends Fragment
{
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private ListView listView;
  private MyPlaceListAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_place_list;
    View view = inflater.inflate(resId, null);
    
    progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    
    listView = (ListView) view.findViewById(R.id.listview);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    
    return view;
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    adapter = new MyPlaceListAdapter(getActivity());
    listView.setAdapter(adapter);
    
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
  
  /**************************************************
   * listener
   ***************************************************/
  private OnItemClickListener itemClickListener = new OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      startActivity(new Intent(getActivity(), MyPlaceDetailActivity.class));
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
