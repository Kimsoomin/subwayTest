package com.dabeeo.hangouyou.fragments.mypage;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;
import com.dabeeo.hangouyou.views.ScheduleTitleView;
import com.dabeeo.hangouyou.views.ScheduleView;

public class MyScheduleDetailFragment extends Fragment
{
  private ScrollView scrollView;
  private LinearLayout container;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_my_schedule_detail;
    View view = inflater.inflate(resId, null);
    
    scrollView = (ScrollView) view.findViewById(R.id.scrollview);
    container = (LinearLayout) view.findViewById(R.id.content_container);
    
    headerView = (ScheduleDetailHeaderView) view.findViewById(R.id.header_view);
    titleView = (ScheduleDetailTitleView) view.findViewById(R.id.title_view);
    
    FrameLayout header = (FrameLayout) view.findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    return view;
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    headerView.init();
    titleView.init();
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
    productView.setBean(bean);
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
