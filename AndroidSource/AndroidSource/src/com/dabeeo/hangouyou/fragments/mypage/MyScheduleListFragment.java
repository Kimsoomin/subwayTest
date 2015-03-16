package com.dabeeo.hangouyou.fragments.mypage;

import android.app.Activity;
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
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;
import com.dabeeo.hangouyou.views.ScheduleTitleView;
import com.dabeeo.hangouyou.views.ScheduleView;

public class MyScheduleListFragment extends Fragment
{
  private Activity activity;
  private View view;
  
  private ScrollView scrollView;
  private LinearLayout container;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  
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
      int resId = R.layout.fragment_my_schedule_list;
      view = LayoutInflater.from(activity).inflate(resId, null);
    }
    
    scrollView = (ScrollView) view.findViewById(R.id.scrollview);
    container = (LinearLayout) view.findViewById(R.id.content_container);
    
    headerView = (ScheduleDetailHeaderView) view.findViewById(R.id.header_view);
    titleView = (ScheduleDetailTitleView) view.findViewById(R.id.title_view);
    headerView.init();
    titleView.init();
    
    FrameLayout header = (FrameLayout) view.findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    displayContentData();
  }
  
  
  private void displayContentData()
  {
    container.removeAllViews();
    
    ProductView productView = new ProductView(activity);
    container.addView(productView);
    
    ScheduleTitleView tView = new ScheduleTitleView(activity);
    container.addView(tView);
    
    ScheduleView view = new ScheduleView(activity);
    container.addView(view);
    view = new ScheduleView(activity);
    container.addView(view);
    view = new ScheduleView(activity);
    container.addView(view);
    view = new ScheduleView(activity);
    container.addView(view);
    view = new ScheduleView(activity);
    container.addView(view);
    view = new ScheduleView(activity);
    container.addView(view);
    
  }
}
