package com.dabeeo.hangouyou.activities.sub;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.ImageDownloader;

public class ReviewDetailActivity extends ActionBarActivity
{
  private ImageView icon;
  private TextView name;
  private TextView time;
  private TextView content;
  private TextView reviewScore;
  private ImageView btnMore;
  
  private ListPopupWindow listPopupWindow;
  private LinearLayout imageContainer;
  
  private int reviewIdx = -1;
  private ApiClient apiClient;
  private ReviewBean bean;
  
  private ProgressBar progressBar;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_review));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    setContentView(R.layout.activity_review_detail);
    
    apiClient = new ApiClient(this);
    reviewIdx = getIntent().getIntExtra("review_idx", -1);
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    imageContainer = (LinearLayout) findViewById(R.id.image_container);
    icon = (ImageView) findViewById(R.id.icon);
    name = (TextView) findViewById(R.id.name);
    time = (TextView) findViewById(R.id.time);
    content = (TextView) findViewById(R.id.content);
    reviewScore = (TextView) findViewById(R.id.text_review_score);
    btnMore = (ImageView) findViewById(R.id.btn_review_list_more);
    
    loadReviewDetail();
  }
  
  
  private void loadReviewDetail()
  {
    new LoadReviewDetailAsyncTask().execute();
  }
  
  private class LoadReviewDetailAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getReviewDetail(reviewIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new ReviewBean();
          bean.setJSONObject(obj.getJSONObject("review"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
      }
      progressBar.setVisibility(View.GONE);
      displayContent();
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContent()
  {
    
    name.setText(bean.userName);
    time.setText(bean.insertDateString);
    reviewScore.setText(Integer.toString(bean.rate));
    content.setText(bean.content);
    
    for (int i = 0; i < bean.imageUrls.size(); i++)
    {
      ImageView imageView = new ImageView(ReviewDetailActivity.this);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
      params.setMargins(8, 8, 8, 8);
      imageView.setLayoutParams(params);
      final String imageUrl = bean.imageUrls.get(i);
      imageView.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View arg0)
        {
          Intent i = new Intent(ReviewDetailActivity.this, ImagePopUpActivity.class);
          i.putExtra("imageUrls", bean.imageUrls);
          i.putExtra("imageUrl", imageUrl);
          startActivity(i);
        }
      });
      ImageDownloader.displayImage(ReviewDetailActivity.this, bean.imageUrls.get(i), imageView, null);
      imageContainer.addView(imageView);
    }
  }
}
