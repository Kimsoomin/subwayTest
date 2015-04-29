package com.dabeeo.hangouyou.fragments.trends;

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
import android.widget.ProgressBar;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendExhibitionActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.TrendProductBean;
import com.dabeeo.hangouyou.controllers.trend.TrendProductListAdapter;
import com.dabeeo.hangouyou.external.libraries.GridViewWithHeaderAndFooter;

public class TrendCategoryListFragment extends Fragment
{
  private ProgressBar progressBar;
  private TrendProductListAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_trend_product_list;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
    
    adapter = new TrendProductListAdapter(getActivity());
    GridViewWithHeaderAndFooter gridView = (GridViewWithHeaderAndFooter) getView().findViewById(R.id.gridview);
    
    gridView.setOnItemClickListener(itemClickListener);
    gridView.setOnScrollListener(scrollListener);
    gridView.setAdapter(adapter);
    
    gridView.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
      {
        Intent i = new Intent(getActivity(), TrendProductDetailActivity.class);
        startActivity(i);
      }
    });
    loadSchedules();
  }
  
  
  private void loadSchedules()
  {
    progressBar.setVisibility(View.VISIBLE);
    
    TrendProductBean bean = new TrendProductBean();
    bean.title = "[천송이점퍼]오리털점퍼";
    bean.price = 171500;
    bean.discountPrice = 240000;
    adapter.add(bean);
    
    bean = new TrendProductBean();
    bean.title = "[로체]CUBBEBLOCK OUTER";
    bean.price = 17150;
    bean.discountPrice = 3000;
    adapter.add(bean);
    
    bean = new TrendProductBean();
    bean.title = "[빈폴아웃도어] 남성짚업 후드 방수점퍼";
    bean.price = 121300;
    bean.discountPrice = 214400;
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
