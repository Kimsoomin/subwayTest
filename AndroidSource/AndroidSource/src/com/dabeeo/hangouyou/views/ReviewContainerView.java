package com.dabeeo.hangouyou.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dabeeo.hangouyou.R;
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
  
  
  private void init()
  {
    apiClient = new ApiClient(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    int resId = R.layout.view_review_container;
    View view = inflater.inflate(resId, null);
    
    container = (LinearLayout) view.findViewById(R.id.content);
    
    addView(view);
  }
  
  
  public void loadMore()
  {
    if (isLoading)
      return;
    page++;
    
    new LoadMoreAsyncTask().execute();
  }
  
  private class LoadMoreAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      isLoading = true;
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
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
      isLoading = false;
      super.onPostExecute(result);
    }
  }
  
  
  private void addReviewView(ReviewBean bean)
  {
    ReviewView reviewView = new ReviewView(context);
    reviewView.setBean(bean);
    container.addView(reviewView);
  }
}
