package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ScheduleDayBean;
import com.dabeeo.hangouyou.beans.ScheduleDetailBean;
import com.dabeeo.hangouyou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hangouyou.views.CustomScrollView;
import com.dabeeo.hangouyou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hangouyou.views.ReviewContainerView;
import com.dabeeo.hangouyou.views.ScheduleDetailHeaderView;
import com.dabeeo.hangouyou.views.ScheduleDetailTitleView;
import com.dabeeo.hangouyou.views.ScheduleTitleView;
import com.dabeeo.hangouyou.views.ScheduleView;

public class TravelScheduleDetailFragment extends Fragment
{
  private LinearLayout contentContainer;
  
  private RelativeLayout reviewLayout;
  private ReviewContainerView reviewContainerView;
  
  private ScheduleDetailHeaderView headerView;
  private ScheduleDetailTitleView titleView;
  
  private ScheduleDetailBean bean;
  private ScheduleDayBean dayBean;
  
  private Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  private CustomScrollView scrollView;
  
  private int position;
  private boolean isMySchedule = false;
  
  
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
    
    scrollView = (CustomScrollView) getView().findViewById(R.id.scrollview);
    contentContainer = (LinearLayout) getView().findViewById(R.id.content_container);
    reviewLayout = (RelativeLayout) getView().findViewById(R.id.review_layout);
    
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
    
    scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
    {
      @SuppressLint("NewApi")
      @Override
      public void onScrollChanged()
      {
        int scrollY = scrollView.getScrollY();
        
        if (scrollY > 255)
          scrollY = 254;
        scrollY = scrollY / 10;
        int opacity = 255 - scrollY;
        if (opacity > 255)
          opacity = 255;
        titleView.container.setBackground(new ColorDrawable(Color.argb(255, opacity, opacity, opacity)));
      }
    });
    
    FrameLayout header = (FrameLayout) getView().findViewById(R.id.header);
    Resources r = getResources();
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    
    scrollView.setScrollViewListener(new ScrollViewListener()
    {
      @Override
      public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy)
      {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        
        if (diff == 0)
        {
          reviewContainerView.loadMore();
        }
      }
    });
    
    displayContentData();
  }
  
  
  public void setBean(int position, ScheduleDetailBean bean, ScheduleDayBean dayBean, boolean isMySchedule)
  {
    this.position = position;
    this.bean = bean;
    this.dayBean = dayBean;
    this.isMySchedule = isMySchedule;
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
    
    headerView.setData(bean.imageUrl, bean.title, bean.days.size(), bean.budgetTotal);
    titleView.setData(bean.likeCount, bean.reviewCount);
    
    displayDayView();
    
    reviewContainerView = new ReviewContainerView(getActivity(), "place", bean.idx);
    reviewLayout.addView(reviewContainerView);
    
    reviewContainerView.loadMore();
    
    if (isMySchedule)
    {
      btnReviewBest.setVisibility(View.GONE);
      btnReviewSoso.setVisibility(View.GONE);
      btnReviewWorst.setVisibility(View.GONE);
    }
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
        tView.setData("Day" + Integer.toString(i + 1), new Date(c.getTimeInMillis()));
        contentContainer.addView(tView);
        
        for (int j = 0; j < bean.days.get(i).spots.size(); j++)
        {
          ScheduleView view = new ScheduleView(getActivity());
          view.setData(j, bean.days.get(i).spots.get(j));
          
          String planMemo = "";
          planMemo += bean.days.get(i).dayDist + "\n" + bean.days.get(i).dayTime;
          if (j == bean.days.get(i).spots.size() - 1)
            view.setFinalView(planMemo);
          contentContainer.addView(view);
        }
      }
    }
    else
    {
      ScheduleTitleView tView = new ScheduleTitleView(getActivity());
      Calendar c = Calendar.getInstance();
      c.setTime(bean.startDate);
      tView.setData("Day" + Integer.toString(position), new Date(c.getTimeInMillis()));
      contentContainer.addView(tView);
      
      //하루에 대한 내용
      for (int i = 0; i < dayBean.spots.size(); i++)
      {
        ScheduleView view = new ScheduleView(getActivity());
        view.setData(i, dayBean.spots.get(i));
        
        String planMemo = "";
        planMemo += dayBean.dayDist + "\n" + dayBean.dayTime;
        if (i == dayBean.spots.size() - 1)
          view.setFinalView(planMemo);
        contentContainer.addView(view);
      }
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
