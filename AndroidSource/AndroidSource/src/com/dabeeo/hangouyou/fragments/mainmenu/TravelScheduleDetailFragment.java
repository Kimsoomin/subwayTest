package com.dabeeo.hangouyou.fragments.mainmenu;

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
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.ProductView;
import com.dabeeo.hangouyou.views.ReviewView;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;
import com.dabeeo.hangouyou.views.ScheduleTitleView;
import com.dabeeo.hangouyou.views.ScheduleView;

public class TravelScheduleDetailFragment extends Fragment
{
  private LinearLayout contentContainer, containerReview;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_travel_schedule_detail;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollview);
    contentContainer = (LinearLayout) getView().findViewById(R.id.content_container);
    containerReview = (LinearLayout) getView().findViewById(R.id.container_review);
    
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
    contentContainer.removeAllViews();
    
    ProductView productView = new ProductView(getActivity());
    ProductBean bean = new ProductBean();
    bean.title = "XXX 수분크림";
    bean.originalPrice = 150;
    bean.discountPrice = 93;
    productView.setBean(bean);
    contentContainer.addView(productView);
    
    ScheduleTitleView tView = new ScheduleTitleView(getActivity());
    contentContainer.addView(tView);
    
    ScheduleView view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    view = new ScheduleView(getActivity());
    contentContainer.addView(view);
    
    containerReview.removeAllViews();
    ReviewView reviewView = new ReviewView(getActivity());
    ReviewBean reviewBean = new ReviewBean();
    reviewBean.userName = "planB";
    reviewBean.content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do";
    reviewView.setBean(reviewBean);
    containerReview.addView(reviewView);
    
    reviewView = new ReviewView(getActivity());
    reviewBean = new ReviewBean();
    reviewBean.userName = "planB";
    reviewBean.content = "좋네요!";
    reviewView.setBean(reviewBean);
    containerReview.addView(reviewView);
  }
}
