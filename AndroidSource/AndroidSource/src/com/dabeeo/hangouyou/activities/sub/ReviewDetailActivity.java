package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ReviewBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.utils.ImageDownloader;
import com.dabeeo.hangouyou.views.DeclareReviewView;

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
  
  private String reviewIdx = "";
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
    reviewIdx = getIntent().getStringExtra("review_idx");
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    imageContainer = (LinearLayout) findViewById(R.id.image_container);
    icon = (ImageView) findViewById(R.id.icon);
    name = (TextView) findViewById(R.id.name);
    time = (TextView) findViewById(R.id.time);
    content = (TextView) findViewById(R.id.content);
    reviewScore = (TextView) findViewById(R.id.text_review_score);
    reviewScore.setText("4");
    btnMore = (ImageView) findViewById(R.id.btn_review_list_more);
    
    btnMore.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Log.w("WARN", "Click!");
        listPopupWindow = new ListPopupWindow(ReviewDetailActivity.this);
        String[] listPopupArray = new String[2];
        listPopupArray[0] = ReviewDetailActivity.this.getString(R.string.term_delete);
        listPopupArray[1] = ReviewDetailActivity.this.getString(R.string.term_declare);
        
        listPopupWindow.setAdapter(new ArrayAdapter<String>(ReviewDetailActivity.this, android.R.layout.simple_list_item_1, listPopupArray));
        listPopupWindow.setAnchorView(btnMore);
        listPopupWindow.setWidth(300);
        listPopupWindow.setOnItemClickListener(new OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
          {
            if (position == 0)
            {
              //삭제
              Builder builder = new AlertDialog.Builder(ReviewDetailActivity.this);
              builder.setTitle(ReviewDetailActivity.this.getString(R.string.app_name));
              builder.setMessage(ReviewDetailActivity.this.getString(R.string.msg_confirm_delete));
              builder.setPositiveButton(android.R.string.ok, null);
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            else
            {
              //신고
              Builder builder = new AlertDialog.Builder(ReviewDetailActivity.this);
              builder.setTitle(ReviewDetailActivity.this.getString(R.string.term_declare_review));
              DeclareReviewView declareView = new DeclareReviewView(ReviewDetailActivity.this);
              declareView.init();
              final EditText editReasonText = (EditText) declareView.findViewById(R.id.edit_review_declare);
              builder.setView(declareView);
              builder.setPositiveButton(android.R.string.ok, null);
              builder.setNegativeButton(android.R.string.cancel, null);
              builder.show();
            }
            listPopupWindow.dismiss();
          }
        });
        listPopupWindow.show();
      }
    });
    loadReviewDetail();
  }
  
  
  private void loadReviewDetail()
  {
    new LoadReviewDetailAsyncTask().execute();
  }
  
  private class LoadReviewDetailAsyncTask extends AsyncTask<String, Integer, ReviewBean>
  {
    @Override
    protected void onPreExecute()
    {
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    
    @Override
    protected ReviewBean doInBackground(String... params)
    {
      return apiClient.getReviewDetail(reviewIdx);
    }
    
    
    @Override
    protected void onPostExecute(ReviewBean result)
    {
      bean = result;
      progressBar.setVisibility(View.GONE);
      displayContent();
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContent()
  {
    if (bean != null)
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
        imageView.setImageResource(R.drawable.default_thumbnail_s);
        ImageDownloader.displayImage(ReviewDetailActivity.this, bean.imageUrls.get(i), imageView, null);
        imageContainer.addView(imageView);
      }
    }
    else
    {
      name.setText("TourPlan B");
      time.setText("2015.01.01 15:00:30");
      content.setText("테스트중입니다");
      ImageView imageView = new ImageView(ReviewDetailActivity.this);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
      params.setMargins(8, 8, 8, 8);
      imageView.setLayoutParams(params);
      final String imageUrl = "http://image.gsshop.com/image/16/65/16656247_O1.jpg";
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
      imageView.setImageResource(R.drawable.default_thumbnail_s);
      ImageDownloader.displayImage(ReviewDetailActivity.this, "http://image.gsshop.com/image/16/65/16656247_O1.jpg", imageView, null);
      imageContainer.addView(imageView);
    }
    
    ImageDownloader.displayProfileImage(ReviewDetailActivity.this, "", icon);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
}
