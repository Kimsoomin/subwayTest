package com.dabeeo.hangouyou.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.ReviewDetailActivity;
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class ReviewContainerView extends LinearLayout
{
  private Activity context;
  private LinearLayout container;
  private ApiClient apiClient;
  
  private String parentType, parentIdx;
  private int page = 0;
  private boolean isLoading = false;
  
  private RelativeLayout progressLayout;
  
  
  public ReviewContainerView(Activity context, String parentType, String parentIdx)
  {
    super(context);
    this.context = context;
    this.parentType = parentType;
    this.parentIdx = parentIdx;
    init();
  }
  
  
  public ReviewContainerView(Activity context, AttributeSet attrs)
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
    int resId = R.layout.view_review_container;
    View view = inflater.inflate(resId, null);
    
    container = (LinearLayout) view.findViewById(R.id.content);
    progressLayout = (RelativeLayout) view.findViewById(R.id.progress_layout);
    addView(view);
  }
  
  
  public void loadMore()
  {
//    if (isLoading)
//      return;
//    page++;
//    
//    new LoadMoreAsyncTask().execute();
    
    //Test Loading 
    ReviewBean bean = new ReviewBean();
    bean.content = "테스트 리뷰입니다";
    bean.insertDateString = "2015.01.01 15:30:00";
    bean.userName = "Tourplan B";
    bean.imageUrls.add("http://image.gsshop.com/image/16/65/16656247_O1.jpg");
    addReviewView(bean);
  }
  
  private class LoadMoreAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      progressLayout.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      if (apiClient == null)
        apiClient = new ApiClient(context);
      return apiClient.getReviews(page, parentType, parentIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      try
      {
        ArrayList<ReviewBean> beans = new ArrayList<ReviewBean>();
        JSONObject obj = new JSONObject(result.response);
        JSONArray arr = obj.getJSONArray("review");
        for (int i = 0; i < arr.length(); i++)
        {
          JSONObject reviewObj = arr.getJSONObject(i);
          ReviewBean bean = new ReviewBean();
          bean.setJSONObject(reviewObj);
          beans.add(bean);
        }
        
        for (int i = 0; i < beans.size(); i++)
        {
          addReviewView(beans.get(i));
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
    ReviewView reviewView = new ReviewView(context);
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
    line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
    line.setBackgroundColor(Color.parseColor("#c3c3c3"));
    container.addView(line);
  }
}
