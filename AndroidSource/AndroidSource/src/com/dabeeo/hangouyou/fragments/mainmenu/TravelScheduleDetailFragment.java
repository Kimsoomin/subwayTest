package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.Calendar;
import java.util.Date;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ScheduleDayBean;
import com.dabeeo.hangouyou.beans.ScheduleDetailBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;
import com.dabeeo.hangouyou.views.ScheduleTitleView;
import com.dabeeo.hangouyou.views.ScheduleView;

public class TravelScheduleDetailFragment extends Fragment
{
  private LinearLayout contentContainer, containerReview;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  private ScheduleDetailBean bean;
  private ScheduleDayBean dayBean;
  
  private Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  
  
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
    
    btnReviewBest = (Button) getView().findViewById(R.id.btn_review_best);
    btnReviewSoso = (Button) getView().findViewById(R.id.btn_review_soso);
    btnReviewWorst = (Button) getView().findViewById(R.id.btn_review_worst);
    btnReviewBest.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    btnReviewWorst.setOnClickListener(rateClickListener);
    
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
  
  
  public void setBean(ScheduleDetailBean bean, ScheduleDayBean dayBean)
  {
    this.bean = bean;
    this.dayBean = dayBean;
  }
  
  
  private void displayContentData()
  {
    contentContainer.removeAllViews();
    
//    ProductView productView = new ProductView(getActivity());
//    ProductBean bean = new ProductBean();
//    bean.title = "XXX 수분크림";
//    bean.originalPrice = 150;
//    bean.discountPrice = 93;
//    productView.setBean(bean, bean);
//    contentContainer.addView(productView);
    
    headerView.setData("", bean.title, bean.days.size(), bean.budgetTotal);
    titleView.setData(bean.likeCount, bean.reviewCount);
    
    displayDayView();
    
//    containerReview.removeAllViews();
//    ReviewView reviewView = new ReviewView(getActivity());
//    ReviewBean reviewBean = new ReviewBean();
//    reviewBean.userName = "planB";
//    reviewBean.content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do";
//    reviewView.setBean(reviewBean);
//    containerReview.addView(reviewView);
//    
//    reviewView = new ReviewView(getActivity());
//    reviewBean = new ReviewBean();
//    reviewBean.userName = "planB";
//    reviewBean.content = "좋네요!";
//    reviewView.setBean(reviewBean);
//    containerReview.addView(reviewView);
  }
  
  
  private void displayDayView()
  {
    if (dayBean == null)
    {
      //전체에 대한 내용
      for (int i = 0; i < bean.days.size(); i++)
      {
        ScheduleTitleView tView = new ScheduleTitleView(getActivity());
        Calendar c = Calendar.getInstance();
        c.setTime(bean.startDate);
        c.add(Calendar.DATE, i);
        tView.setData("Day" + Integer.toString(i), new Date(c.getTimeInMillis()));
        contentContainer.addView(tView);
        
        for (int j = 0; j < bean.days.get(i).spots.size(); j++)
        {
          ScheduleView view = new ScheduleView(getActivity());
          view.setData(j, bean.days.get(i).spots.get(j));
          contentContainer.addView(view);
        }
      }
      
    }
    else
    {
      //하루에 대한 내용
    }
    
  }
  
  private OnClickListener rateClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      btnReviewBest.setSelected(false);
      btnReviewSoso.setSelected(false);
      btnReviewWorst.setSelected(false);
      
      if (v.getId() == btnReviewBest.getId())
      {
        btnReviewBest.setSelected(true);
      }
      else if (v.getId() == btnReviewSoso.getId())
      {
        btnReviewSoso.setSelected(true);
      }
      else if (v.getId() == btnReviewWorst.getId())
      {
        btnReviewWorst.setSelected(true);
      }
    }
  };
  
}
