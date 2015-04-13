package com.dabeeo.hangouyou.activities.mainmenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.PhotoSelectActivity;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class WriteReviewActivity extends ActionBarActivity
{
  private final static int REQUEST_CODE = 120;
  
  private Button btnBest, btnSoso, btnWorst;
  private ImageView imageView;
  private EditText editReview;
  private Button btnSave;
  
  private int rate = 5;
  private String reviewImageFilePath;
  private ProgressBar progressBar;
  private ApiClient apiClient;
  
  private int parentIdx = -1;
  private String parentType = "";
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_write_review);
    
    apiClient = new ApiClient(this);
    parentIdx = getIntent().getIntExtra("idx", -1);
    parentType = getIntent().getStringExtra("type");
    
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    btnBest = (Button) findViewById(R.id.btn_review_best);
    btnSoso = (Button) findViewById(R.id.btn_review_soso);
    btnWorst = (Button) findViewById(R.id.btn_review_worst);
    imageView = (ImageView) findViewById(R.id.imageview);
    editReview = (EditText) findViewById(R.id.edit_review);
    btnSave = (Button) findViewById(R.id.btn_save);
    imageView.setVisibility(View.GONE);
    
    btnBest.setOnClickListener(rateClickListener);
    btnSoso.setOnClickListener(rateClickListener);
    btnWorst.setOnClickListener(rateClickListener);
    btnBest.setActivated(true);
    
    imageView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(WriteReviewActivity.this, PhotoSelectActivity.class);
        intent.putExtra("can_select_multiple", false);
        startActivityForResult(intent, REQUEST_CODE);
      }
    });
    
    btnSave.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (!TextUtils.isEmpty(editReview.getText().toString()))
        {
          postReview(editReview.getText().toString());
        }
        else
          Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_empty_value), Toast.LENGTH_LONG).show();
      }
    });
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
      return apiClient.postReview(rate, params[0], parentIdx, parentType);
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
          Toast.makeText(WriteReviewActivity.this, getString(R.string.msg_review_success), Toast.LENGTH_LONG).show();
          finish();
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
  
  private OnClickListener rateClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      btnBest.setActivated(false);
      btnSoso.setActivated(false);
      btnWorst.setActivated(false);
      
      if (v.getId() == btnBest.getId())
      {
        btnBest.setActivated(true);
        rate = 5;
      }
      else if (v.getId() == btnSoso.getId())
      {
        btnSoso.setActivated(true);
        rate = 3;
      }
      else if (v.getId() == btnWorst.getId())
      {
        btnWorst.setActivated(true);
        rate = 1;
      }
    }
  };
  
  
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
      
      reviewImageFilePath = photos[0];
      try
      {
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is = null;
        is = new FileInputStream(new File(photos[0]));
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        is = new FileInputStream(new File(photos[0]));
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        imageView.setImageBitmap(bitmap);
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
