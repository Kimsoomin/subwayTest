package com.dabeeo.hangouyou.activities.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.NoticeBean;
import com.dabeeo.hangouyou.controllers.mypage.NoticeAdapter;

public class NoticeActivity extends ActionBarActivity
{
  private ExpandableListView listView;
  private NoticeAdapter adapter;
  
  private ArrayList<String> titles = new ArrayList<String>();
  private HashMap<String, List<String>> contents = new HashMap<String, List<String>>();
  
  private ProgressBar progressBar;
  private int limit = 20, offset = 0;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notice);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    
    listView = (ExpandableListView) findViewById(android.R.id.list);
    adapter = new NoticeAdapter(titles, contents);
    listView.setAdapter(adapter);
    
    listView.setOnScrollListener(scrollListener);
    
    loadNotices();
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void loadNotices()
  {
    progressBar.setVisibility(View.VISIBLE);
    //가데이터
    NoticeBean bean = new NoticeBean();
    bean.title = "Hangouyou가 오픈했습니다.";
    bean.content = "Hangouyou가 오픈했습니다. 축하드립니다!";
    titles.add(bean.title);
    if (contents.get(bean.title) == null)
      contents.put(bean.title, new ArrayList<String>());
    contents.get(bean.title).add(bean.content);
    
    bean.title = "Hangouyou 점검시간 공지입니다.";
    bean.content = "3월 30일 2시부터 4시까지 점검이 있을 예정입니다.";
    titles.add(bean.title);
    if (contents.get(bean.title) == null)
      contents.put(bean.title, new ArrayList<String>());
    contents.get(bean.title).add(bean.content);
    
    progressBar.setVisibility(View.GONE);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnScrollListener scrollListener = new OnScrollListener()
  {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
      if (totalItemCount > 0 && totalItemCount > offset && totalItemCount <= firstVisibleItem + visibleItemCount)
      {
//        offset += limit;
//        load(offset);
      }
    }
  };
}
