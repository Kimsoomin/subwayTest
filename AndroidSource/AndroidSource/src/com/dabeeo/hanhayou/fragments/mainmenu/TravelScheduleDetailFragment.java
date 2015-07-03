package com.dabeeo.hanhayou.fragments.mainmenu;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.WriteReviewActivity;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.beans.ScheduleDayBean;
import com.dabeeo.hanhayou.beans.ScheduleDetailBean;
import com.dabeeo.hanhayou.external.libraries.stikkylistview.StikkyHeaderBuilder;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.CustomScrollView;
import com.dabeeo.hanhayou.views.CustomScrollView.ScrollViewListener;
import com.dabeeo.hanhayou.views.ProductRecommendScheduleView;
import com.dabeeo.hanhayou.views.ReviewContainerView;
import com.dabeeo.hanhayou.views.ScheduleDetailHeaderView;
import com.dabeeo.hanhayou.views.ScheduleDetailTitleView;
import com.dabeeo.hanhayou.views.ScheduleTitleView;
import com.dabeeo.hanhayou.views.ScheduleView;

public class TravelScheduleDetailFragment extends Fragment
{
  private LinearLayout contentContainer;
  
  private RelativeLayout reviewLayout;
  private ReviewContainerView reviewContainerView;
  
  private ScheduleDetailHeaderView headerView;
  public ScheduleDetailTitleView titleView;
  
  private TextView textRate;
  
  private ScheduleDetailBean bean;
  private ScheduleDayBean dayBean;
  
  private Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  private CustomScrollView scrollView;
  private FrameLayout headerContainer;
  @SuppressWarnings("unused")
  private LinearLayout rateLayout, rateTextLayout, rateButtonLayout;
  
  private int position;
  private boolean isMySchedule = false;
  private boolean isRecommendSchedule = false;
  
  int rate = 3;
  
  int spotNum = 0;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_travel_schedule_detail;
    return inflater.inflate(resId, null);
  }
  
  
  @SuppressLint("CutPasteId")
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    scrollView = (CustomScrollView) getView().findViewById(R.id.scrollview);
    contentContainer = (LinearLayout) getView().findViewById(R.id.content_container);
    reviewLayout = (RelativeLayout) getView().findViewById(R.id.review_layout);
    textRate = (TextView) getView().findViewById(R.id.text_rate);
    
    rateLayout = (LinearLayout) getView().findViewById(R.id.rate_Layout);
    rateLayout.setOnClickListener(rateClickListener);
    rateTextLayout = (LinearLayout) getView().findViewById(R.id.rate_text_container);
    rateButtonLayout = (LinearLayout) getView().findViewById(R.id.rate_button_container);
    
    btnReviewBest = (Button) getView().findViewById(R.id.btn_review_best);
    btnReviewSoso = (Button) getView().findViewById(R.id.btn_review_soso);
    btnReviewWorst = (Button) getView().findViewById(R.id.btn_review_worst);
    btnReviewBest.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    btnReviewWorst.setOnClickListener(rateClickListener);
    
    headerContainer = (FrameLayout) getView().findViewById(R.id.header);
    headerView = (ScheduleDetailHeaderView) getView().findViewById(R.id.header_view);
    titleView = (ScheduleDetailTitleView) getView().findViewById(R.id.title_view);
    headerView.init();
//    titleView.init();
    
    FrameLayout header = (FrameLayout) getView().findViewById(R.id.header);
    Resources r = getResources();
    int tempPx = 0;
    if (dayBean == null)
    {
      if (isRecommendSchedule)
        tempPx = 75;
      else
        tempPx = 51;
    }
    else
    {
      if (isRecommendSchedule)
        tempPx = 75;
      else
        tempPx = 91;
    }
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tempPx, r.getDisplayMetrics());
    StikkyHeaderBuilder.stickTo(scrollView).setHeader(header).minHeightHeaderPixel((int) px).build();
    final float density = getResources().getDisplayMetrics().density;
    
    scrollView.setScrollViewListener(new ScrollViewListener()
    {
      @Override
      public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy)
      {
        if (!isRecommendSchedule)
        {
          if (scrollView.getScrollY() > 250 * density)
          {
            if (dayBean == null)
            {
              titleView.title.setVisibility(View.INVISIBLE);
              titleView.infoContainer.setVisibility(View.INVISIBLE);
              titleView.titleDivider.setVisibility(View.VISIBLE);
            }
          }
          else
          {
            if (dayBean == null)
            {
              titleView.title.setVisibility(View.VISIBLE);
              titleView.infoContainer.setVisibility(View.VISIBLE);
              titleView.titleDivider.setVisibility(View.GONE);
            }
          }
        }
        
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        
        if (diff == 0 && (reviewContainerView.getVisibility() == View.VISIBLE))
          reviewContainerView.loadMore();
      }
    });
    displayContentData();
  }
  
  
  @Override
  public void onResume()
  {
    if (reviewContainerView != null)
      reviewContainerView.reload();
    super.onResume();
  }
  
  
  public void setBean(int position, ScheduleDetailBean bean, ScheduleDayBean dayBean, boolean isMySchedule, boolean isRecommendSchedule)
  {
    this.position = position;
    this.bean = bean;
    this.dayBean = dayBean;
    this.isMySchedule = isMySchedule;
    this.isRecommendSchedule = isRecommendSchedule;
  }
  
  
  private void displayContentData()
  {
    contentContainer.removeAllViews();
    
    displayDayView();
    
    textRate.setText(Float.toString(bean.rate));
    headerView.setData(bean.imageUrl, bean.title, bean.days.size(), bean.budgetTotal);
    titleView.setBean(bean);
    
    reviewContainerView = new ReviewContainerView(getActivity(), "plan", bean.idx);
    reviewLayout.addView(reviewContainerView);
    reviewContainerView.loadMore();
    
    if (isMySchedule)
    {
      rateButtonLayout.setVisibility(View.GONE);
    }
    
    float density = getResources().getDisplayMetrics().density;
    if (isRecommendSchedule)
    {
      rateLayout.setVisibility(View.GONE);
      titleView.likeAndBookmarkContainer.setVisibility(View.GONE);
      titleView.userContainer.setVisibility(View.GONE);
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (75 * density));
      params.gravity = Gravity.BOTTOM;
      titleView.setLayoutParams(params);
    }
    
    if (position != 0)
    {
      rateLayout.setVisibility(View.GONE);
    }
    
    if (headerView != null)
    {
      if (dayBean == null)
      {
        //전체
        if (!isRecommendSchedule)
        {
          headerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (300 * density)));
          headerContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (300 * density)));
        }
        else
        {
          headerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (300 * density)));
          headerContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (225 * density)));
        }
        scrollView.setPadding(0, 0, 0, 0);
      }
      else
      {
        //일정일 때 (1일 ..)
        headerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (0 * density)));
        if (!isRecommendSchedule)
          headerContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (100 * density)));
        else
          headerContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (0 * density)));
        scrollView.setPadding(0, 0, 0, 0);
        reviewContainerView.setVisibility(View.GONE);
      }
    }
  }
  
  
  private void displayDayView()
  {
    if (dayBean == null)
    {
      titleView.init();
      Log.w("WARN", "전체에 대한 뷰 " + bean.days.size());
      //전체에 대한 내용
      for (int i = 0; i < bean.days.size(); i++)
      {
        ScheduleTitleView tView = new ScheduleTitleView(getActivity());
        Calendar c = Calendar.getInstance();
        c.setTime(bean.startDate);
        c.add(Calendar.DATE, i);
        if (Locale.getDefault().getLanguage().contains("ko"))
          tView.setData("Day" + Integer.toString(i + 1), new Date(c.getTimeInMillis()));
        else
        {
          String dayString = "第" + Integer.toString(i + 1) + "天";
          tView.setData(dayString, new Date(c.getTimeInMillis()));
        }
        contentContainer.addView(tView);
        
        if (spotNum == 0 && SystemUtil.isConnectNetwork(getActivity()))
        {
          //TEST ADD RECOMMEND PRODUCT VIEW
          ProductBean bean = new ProductBean();
          bean.title = "韩国it's skin/伊思清爽修护套装 香港直邮 正品";
          bean.originalPrice = 109000;
          bean.discountPrice = 90000;
          
          ProductRecommendScheduleView productRecommendView = new ProductRecommendScheduleView(getActivity());
          productRecommendView.setBean(bean);
          productRecommendView.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View v)
            {
              if (!SystemUtil.isConnectNetwork(getActivity()))
                new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
              else
                getActivity().startActivity(new Intent(getActivity(), TrendProductDetailActivity.class));
            }
          });
          contentContainer.addView(productRecommendView);
        }
        
        for (int j = 0; j < bean.days.get(i).spots.size(); j++)
        {
          ScheduleView view = new ScheduleView(getActivity());
          view.setThumbnailInvisible();
          if (bean.days.get(i).spots.get(j).type != 3)
            spotNum += 1;
          view.setData(spotNum, bean.days.get(i).spots.get(j));
          if (j == bean.days.get(i).spots.size() - 1)
          {
            view.setFinalView();
            spotNum = 0;
          }
          contentContainer.addView(view);
        }
      }
    }
    else
    {
      FrameLayout.LayoutParams btnLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      btnLayoutParams.gravity = Gravity.TOP;
      titleView.setLayoutParams(btnLayoutParams);
      titleView.initDayTitle();
      if (spotNum == 0 && SystemUtil.isConnectNetwork(getActivity()))
      {
        //TEST ADD RECOMMEND PRODUCT VIEW
        ProductBean bean = new ProductBean();
        bean.title = "韩国it's skin/伊思清爽修护套装 香港直邮 正品";
        bean.originalPrice = 4000;
        bean.discountPrice = 3000;
        
        ProductRecommendScheduleView productRecommendView = new ProductRecommendScheduleView(getActivity());
        productRecommendView.setBean(bean);
        productRecommendView.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            if (!SystemUtil.isConnectNetwork(getActivity()))
              new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
            else
              getActivity().startActivity(new Intent(getActivity(), TrendProductDetailActivity.class));
          }
        });
        contentContainer.addView(productRecommendView);
      }
      
//      Log.w("WARN", "일정에 대한 뷰");
//      ScheduleTitleView tView = new ScheduleTitleView(getActivity());
//      Calendar c = Calendar.getInstance();
//      c.setTime(bean.startDate);
//      tView.setData("Day" + Integer.toString(position), new Date(c.getTimeInMillis()));
//      contentContainer.addView(tView);
      
      //하루에 대한 내용
      for (int i = 0; i < dayBean.spots.size(); i++)
      {
        ScheduleView view = new ScheduleView(getActivity());
        if (dayBean.spots.get(i).type != 3)
          spotNum += 1;
        view.setData(spotNum, dayBean.spots.get(i));
        
        if (i == dayBean.spots.size() - 1)
        {
          view.setFinalView();
          spotNum = 0;
        }
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
      
      if (!PreferenceManager.getInstance(getActivity()).isLoggedIn())
      {
        new AlertDialogManager(getActivity()).showNeedLoginDialog(-1);
        return;
      }else
      {
        if(!SystemUtil.isConnectNetwork(getActivity()))
        {
          new AlertDialogManager(getActivity()).showDontNetworkConnectDialog();
          return;
        }
      }
      
      if (v.getId() == btnReviewBest.getId())
      {
        btnReviewBest.setSelected(true);
        rate = 5;
      }
      else if (v.getId() == btnReviewSoso.getId())
      {
        btnReviewSoso.setSelected(true);
        rate = 3;
      }
      else if (v.getId() == btnReviewWorst.getId())
      {
        btnReviewWorst.setSelected(true);
        rate = 1;
      }
      
      AlertDialogManager alert = new AlertDialogManager(getActivity());
      alert.showAlertDialog(getString(R.string.term_alert), getString(R.string.msg_write_review), getString(R.string.term_ok), getString(R.string.term_cancel), new AlertListener()
      {
        
        public void onPositiveButtonClickListener()
        {
          Intent i = new Intent(getActivity(), WriteReviewActivity.class);
          i.putExtra("idx", bean.idx);
          i.putExtra("type", "plan");
          i.putExtra("rate", rate);
          startActivity(i);
        }
        
        
        public void onNegativeButtonClickListener()
        {
          //TODO 평점 서버로 전송 - API필요
        }
      });
    }
  };
  
}
