package com.dabeeo.hanhayou.fragments.mypage;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hanhayou.views.ProductView;
import com.dabeeo.hanhayou.views.ScheduleDetailHeaderView;
import com.dabeeo.hanhayou.views.ScheduleDetailTitleView;
import com.dabeeo.hanhayou.views.ScheduleTitleView;
import com.dabeeo.hanhayou.views.ScheduleView;

public class MyScheduleDetailFragment extends Fragment
{
  private ViewGroup container;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_schedule_detail;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollview);
    container = (ViewGroup) getView().findViewById(R.id.content_container);
    
    headerView = (ScheduleDetailHeaderView) getView().findViewById(R.id.header_view);
    titleView = (ScheduleDetailTitleView) getView().findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    FrameLayout header = (FrameLayout) getView().findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    displayContentData();
  }
  
  
  private void displayContentData()
  {
    container.removeAllViews();
    
    ProductView productView = new ProductView(getActivity());
    ProductBean bean = new ProductBean();
    bean.title = "XXX 수분크림";
    bean.originalPrice = 150;
    bean.discountPrice = 93;
    productView.setBean(bean, bean);
    container.addView(productView);
    
    ScheduleTitleView tView = new ScheduleTitleView(getActivity());
    container.addView(tView);
    
    ScheduleView view = new ScheduleView(getActivity());
    container.addView(view);
    view = new ScheduleView(getActivity());
    container.addView(view);
    view = new ScheduleView(getActivity());
    container.addView(view);
    view = new ScheduleView(getActivity());
    container.addView(view);
    view = new ScheduleView(getActivity());
    container.addView(view);
    view = new ScheduleView(getActivity());
    container.addView(view);
  }
}
