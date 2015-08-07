package com.dabeeo.hanhayou.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ReviewDetailActivity;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.managers.network.ApiClient;

public class ProductReviewContainerView extends LinearLayout
{
  private Activity context;
  private LinearLayout container;
  private ApiClient apiClient;
  
  private String parentType, parentIdx;
  private int page = 0;
  private boolean isLoading = false;
  
  private RelativeLayout progressLayout;
  
  
  public ProductReviewContainerView(Activity context, String parentType, String parentIdx)
  {
    super(context);
    this.context = context;
    this.parentType = parentType;
    this.parentIdx = parentIdx;
    init();
  }
  
  
  public ProductReviewContainerView(Activity context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public void setData(Activity context, String parentType, String parentIdx)
  {
    this.context = context;
    this.parentType = parentType;
    this.parentIdx = parentIdx;
    init();
  }
  
  
  private void init()
  {
    apiClient = new ApiClient(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    int resId = R.layout.view_product_review_container;
    View view = inflater.inflate(resId, null);
    
    container = (LinearLayout) view.findViewById(R.id.content);
    progressLayout = (RelativeLayout) view.findViewById(R.id.progress_layout);
    addView(view);
  }
  
  
  public void testLoadMore()
  {
    ReviewBean bean = new ReviewBean();
    bean.content = "로컬 더미 데이터입니다. 테스트 용입니다.";
    bean.userName = "Tourplan B";
    bean.insertDateString = "2015.01.01 15:30";
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    bean.imageUrls.add("http://image.gsshop.com/planprd/banner_MAINCORNER/25077562_02.jpg");
    addReviewView(bean);
  }
  
  
  public void loadMore()
  {
    if (isLoading)
      return;
    page++;
    
    new LoadMoreAsyncTask().execute();
  }
  
  private class LoadMoreAsyncTask extends AsyncTask<String, Integer, ArrayList<ReviewBean>>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      progressLayout.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<ReviewBean> doInBackground(String... params)
    {
      if (apiClient == null)
        apiClient = new ApiClient(context);
      return apiClient.getReviews(page, parentType, parentIdx);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ReviewBean> result)
    {
      try
      {
        for (int i = 0; i < result.size(); i++)
        {
          addReviewView(result.get(i));
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      progressLayout.setVisibility(View.GONE);
      isLoading = false;
      super.onPostExecute(result);
    }
  }
  
  
  private void addReviewView(final ReviewBean bean)
  {
    ProductReviewView reviewView = new ProductReviewView(context);
    reviewView.setBean(bean);
    reviewView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent i = new Intent(context, ReviewDetailActivity.class);
        i.putExtra("review_idx", bean.idx);
        context.startActivity(i);
      }
    });
    container.addView(reviewView);
    
    View line = new View(context);
    line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3));
    line.setBackgroundColor(Color.parseColor("#f0f0f0"));
    container.addView(line);
  }
}
