package com.dabeeo.hanhayou.activities.mainmenu;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.PhotoSelectActivity;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.ReviewImageUploadView;

public class WriteReviewActivity extends ActionBarActivity
{
  private final static int REQUEST_CODE = 120;
  
  private Button btnReviewBest, btnReviewSoso, btnReviewWorst;
  private EditText editReview;
  
  private int rate = -1;
  private ProgressBar progressBar;
  private ApiClient apiClient;
  
  private String parentIdx = "";
  private String parentType = "";
  
  private ImageView btnAddPic;
  private HorizontalScrollView imageScrollView;
  private LinearLayout imageContainer;
  private LinearLayout addLayout;
  
  private HashMap<String, View> fileViewWithUrl = new HashMap<String, View>();
  private ReviewImageUploadView addImageView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_write_review);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_write_review));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    rate = getIntent().getIntExtra("rate", -1);
    apiClient = new ApiClient(this);
    if (getIntent().hasExtra("idx"))
      parentIdx = getIntent().getStringExtra("idx");
    parentType = getIntent().getStringExtra("type");
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    btnReviewBest = (Button) findViewById(R.id.btn_review_best);
    btnReviewSoso = (Button) findViewById(R.id.btn_review_soso);
    btnReviewWorst = (Button) findViewById(R.id.btn_review_worst);
    editReview = (EditText) findViewById(R.id.edit_review);
    
    addLayout = (LinearLayout) findViewById(R.id.add_layout);
    btnAddPic = (ImageView) findViewById(R.id.add_pic);
    imageScrollView = (HorizontalScrollView) findViewById(R.id.image_scrollview);
    imageContainer = (LinearLayout) findViewById(R.id.image_container);
    
    btnReviewBest.setOnClickListener(rateClickListener);
    btnReviewSoso.setOnClickListener(rateClickListener);
    btnReviewWorst.setOnClickListener(rateClickListener);
    btnReviewBest.setActivated(true);
    
    editReview.setHintTextColor(Color.parseColor("#c3c3c3"));
    if (!TextUtils.isEmpty(parentType) && parentType.equals("plan"))
    {
      ((TextView) findViewById(R.id.text_rate_here)).setText(getString(R.string.msg_rate_this_schedule));
      editReview.setHint(getString(R.string.msg_how_about_this_schedule));
    }
    
    addLayout.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(WriteReviewActivity.this, PhotoSelectActivity.class);
        intent.putExtra("can_select_multiple", true);
        startActivityForResult(intent, REQUEST_CODE);
      }
    });
    
  }
  
  
  @Override
  protected void onResume()
  {
    btnReviewBest.setActivated(false);
    btnReviewSoso.setActivated(false);
    btnReviewWorst.setActivated(false);
    
    if (rate == 5)
      btnReviewBest.setActivated(true);
    else if (rate == 3)
      btnReviewSoso.setActivated(true);
    else if (rate == 1)
      btnReviewWorst.setActivated(true);
    super.onResume();
  }
  
  
  private void postReview(String content)
  {
    progressBar.setVisibility(View.VISIBLE);
    new PostReviewAsyncTask().execute(content);
  }
  
  private class PostReviewAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.postReviewRate(parentType, parentIdx, PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), rate, params[0]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressBar.setVisibility(View.GONE);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (obj.getString("status").equals("OK"))
        {
          if (fileViewWithUrl.keySet().size() == 0)
          {
            //이미지 없이 리뷰만 업로드할 경우
            Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_review_success), Toast.LENGTH_LONG).show();
            finish();
          }
          else
          {
            //업로드할 이미지가 있는 경우
            Iterator<String> iter = fileViewWithUrl.keySet().iterator();
            while (iter.hasNext())
            {
              String key = iter.next();
              Log.w("fureun", "image filePath : " + key);
              Log.w("WARN", "image review idx : " + obj.getString("seqCode"));
              new ImageUploadAsyncTask().execute(obj.getString("seqCode"), key);
            }
            
            Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_review_success), Toast.LENGTH_LONG).show();
            finish();
          }
        }
        else
        {
          Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_review_fail), Toast.LENGTH_LONG).show();
        }
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
      super.onPostExecute(result);
    }
  }
  
  private class ImageUploadAsyncTask extends AsyncTask<String, Integer, NetworkResult>
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
      return apiClient.uploadReviewImage(params[0], params[1]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressBar.setVisibility(View.GONE);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (obj.getString("status").equals("OK"))
          Log.w("WARN", "Image Upload OK!");
      }
      catch (JSONException e)
      {
        e.printStackTrace();
      }
      super.onPostExecute(result);
    }
  }
  
  private OnClickListener rateClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      btnReviewBest.setActivated(false);
      btnReviewSoso.setActivated(false);
      btnReviewWorst.setActivated(false);
      
      if (v.getId() == btnReviewBest.getId())
      {
        btnReviewBest.setActivated(true);
        rate = 5;
      }
      else if (v.getId() == btnReviewSoso.getId())
      {
        btnReviewSoso.setActivated(true);
        rate = 3;
      }
      else if (v.getId() == btnReviewWorst.getId())
      {
        btnReviewWorst.setActivated(true);
        rate = 1;
      }
    }
  };
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_save, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    if (item.getItemId() == R.id.save)
    {
      if (editReview.getText().toString().length() >= 6)
      {
        if (rate == -1)
          Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_empty_rate), Toast.LENGTH_LONG).show();
        else
          postReview(editReview.getText().toString());
      }
      else
        Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_string_length_less_six), Toast.LENGTH_LONG).show();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK)
      return;
    Log.w("WARN", "ActivityResult");
    if (data != null && data.hasExtra("photos"))
    {
      String[] photos = data.getStringArrayExtra("photos");
      if (photos.length == 0)
        return;
      
      for (int i = 0; i < photos.length; i++)
      {
        if (addImageView != null)
        {
          imageContainer.removeView(addImageView);
          addImageView = null;
        }
        
        ReviewImageUploadView view = new ReviewImageUploadView(WriteReviewActivity.this);
        view.setData(photos[i]);
        final String finalPhotoUrl = photos[i];
        view.imageX.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            ReviewImageUploadView view = (ReviewImageUploadView) fileViewWithUrl.get(finalPhotoUrl);
            imageContainer.removeView(view);
            fileViewWithUrl.remove(finalPhotoUrl);
            
            if (fileViewWithUrl.size() == 0)
            {
              btnAddPic.setVisibility(View.VISIBLE);
              imageScrollView.setVisibility(View.GONE);
              imageContainer.setVisibility(View.GONE);
            }
          }
        });
        
        fileViewWithUrl.put(photos[i], view);
        imageContainer.addView(view);
        
        if (addImageView == null && i == photos.length - 1)
        {
          addImageView = new ReviewImageUploadView(WriteReviewActivity.this);
          addImageView.setData("");
          addImageView.addImage.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View arg0)
            {
              Intent intent = new Intent(WriteReviewActivity.this, PhotoSelectActivity.class);
              intent.putExtra("can_select_multiple", true);
              startActivityForResult(intent, REQUEST_CODE);
            }
          });
          imageContainer.addView(addImageView);
        }
        
        imageScrollView.postDelayed(new Runnable()
        {
          public void run()
          {
            imageScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
          }
        }, 10);
      }
      
      btnAddPic.setVisibility(View.GONE);
      imageScrollView.setVisibility(View.VISIBLE);
      imageContainer.setVisibility(View.VISIBLE);
    }
  }
}
