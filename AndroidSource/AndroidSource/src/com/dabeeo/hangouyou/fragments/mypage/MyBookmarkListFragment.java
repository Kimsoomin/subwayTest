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
import com.dabeeo.hangouyou.beans.BookmarkBean;
import com.dabeeo.hangouyou.controllers.mypage.MyBookmarkListAdapter;

public class MyBookmarkListFragment extends Fragment
{
  private int categoryId = -1;
  
  private ProgressBar progressBar;
  private MyBookmarkListAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    setHasOptionsMenu(true);
    
    int resId = R.layout.fragment_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new MyBookmarkListAdapter(getActivity());
    ListView listView = (ListView) getView().findViewById(android.R.id.list);
    listView.setOnItemClickListener(itemClickListener);
    listView.setOnScrollListener(scrollListener);
    listView.setAdapter(adapter);
    
    loadPlaces();
  }
  
  
  private void loadPlaces()
  {
    //Category Id 활용해서 써야 함 
    
    progressBar.setVisibility(View.VISIBLE);
    
    //테스트 가데이터 
    BookmarkBean bean = new BookmarkBean();
    bean.title = "인사동";
    bean.category = "culture";
    bean.likeCount = 964;
    bean.reviewCount = 11;
    adapter.add(bean);
    
    bean = new BookmarkBean();
    bean.title = "삼청동길";
    bean.category = " culture";
    bean.likeCount = 50;
    bean.reviewCount = 13;
    adapter.add(bean);
    
    bean = new BookmarkBean();
    bean.title = "쌈지길";
    bean.category = " culture";
    bean.likeCount = 1234;
    bean.reviewCount = 12;
    adapter.add(bean);
    
    bean = new BookmarkBean();
    bean.title = "감로당길";
    bean.category = " culture";
    bean.likeCount = 123;
    bean.reviewCount = 12;
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
