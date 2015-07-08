package com.dabeeo.hanhayou.views;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ReviewDetailActivity;
import com.dabeeo.hanhayou.beans.ReviewBean;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.ReviewView.DeleteListener;

public class ReviewContainerView extends LinearLayout
{
  private Activity context;
  private LinearLayout container;
  private ApiClient apiClient;
  
  private String parentType, parentIdx;
  private int page = 1;
  private boolean isLoading = false;
  private boolean isLoadEnded = false;
  private RelativeLayout progressLayout;
  private HashMap<String, ReviewView> reviewViews = new HashMap<String, ReviewView>();
  
  
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
    page = 1;
    apiClient = new ApiClient(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    int resId = R.layout.view_review_container;
    View view = inflater.inflate(resId, null);
    
    container = (LinearLayout) view.findViewById(R.id.content);
    progressLayout = (RelativeLayout) view.findViewById(R.id.progress_layout);
    addView(view);
  }
  
  
  public void reload()
  {
    try
    {
      page = 1;
      isLoadEnded = false;
      isLoading = false;
      container.removeAllViews();
      reviewViews.clear();
      loadMore();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void loadMore()
  {
    if (isLoading || isLoadEnded)
      return;
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
      Log.w("WARN", "Review loadMore");
      
      if (apiClient == null)
        apiClient = new ApiClient(context);
      return apiClient.getReviews(page, parentType, parentIdx);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<ReviewBean> result)
    {
      if (result.size() == 0)
        isLoadEnded = true;
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
      page++;
      isLoading = false;
      super.onPostExecute(result);
    }
  }
  
  
  private void addReviewView(final ReviewBean bean)
  {
    ReviewView reviewView = new ReviewView(context);
    reviewView.setBean(bean, new DeleteListener()
    {
      @Override
      public void onDelete(String idx)
      {
        new DeleteAsyncTask().execute(idx);
      }
      
      
      @Override
      public void onDeclare(String idx, String reason)
      {
        new DeclareAsyncTask().execute(idx, reason);
      }
    });
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
    reviewViews.put(bean.idx, reviewView);
    container.addView(reviewView);
    
    View line = new View(context);
    line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
    line.setBackgroundColor(Color.parseColor("#c3c3c3"));
    container.addView(line);
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class DeleteAsyncTask extends AsyncTask<String, Integer, Boolean>
  {
    public String reviewIdx;
    
    
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    
    @Override
    protected Boolean doInBackground(String... params)
    {
      reviewIdx = params[0];
      return apiClient.removeReview(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      if (result)
        if (reviewViews.get(reviewIdx) != null)
          container.removeView(reviewViews.get(reviewIdx));
      
      reload();
      super.onPostExecute(result);
    }
  }
  
  private class DeclareAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.declareReview(params[0], params[1]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      try
      {
        JSONObject object = new JSONObject(result.response);
        if (object.has("status") && object.getString("status").equals("OK"))
          Toast.makeText(context, context.getString(R.string.msg_declare_compelete), Toast.LENGTH_SHORT).show();
        else
          Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      super.onPostExecute(result);
    }
  }
}
