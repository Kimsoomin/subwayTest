package com.dabeeo.hangouyou.activities.mainmenu;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hangouyou.beans.PlaceDetailBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.squareup.picasso.Picasso;

public class TravelStrategyDetailActivity extends ActionBarActivity
{
  private TextView textDetail;
  private ViewGroup horizontalImagesView, layoutDetailPlaceInfo;
  
  private ApiClient apiClient;
  private ProgressBar progressBar;
  
  private int placeIdx = -1;
  private PlaceDetailBean bean;
  private ScrollView scrollView;
  private TextView likeCount;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    getSupportActionBar().setElevation(0);
    
    setContentView(R.layout.activity_strategy_seoul_detail);
    
    apiClient = new ApiClient(this);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    placeIdx = getIntent().getIntExtra("place_idx", -1);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    textDetail = (TextView) findViewById(R.id.text_detail);
    horizontalImagesView = (ViewGroup) findViewById(R.id.horizontal_images_view);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    likeCount = (TextView) findViewById(R.id.like_count);
    
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    findViewById(R.id.btn_like).setOnClickListener(clickListener);
    
    scrollView = (ScrollView) findViewById(R.id.scroll_view);
    
    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 45, 189, 182)));
    scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener()
    {
      @Override
      public void onScrollChanged()
      {
        int scrollY = scrollView.getScrollY();
        
        if (scrollY > 255)
          scrollY = 254;
        int opacity = scrollY - 255;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(opacity, 45, 189, 182)));
      }
    });
    loadPlaceDetail();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.map)
    {
      Intent i = new Intent(TravelStrategyDetailActivity.this, BlinkingMap.class);
      i.putExtra("idx", bean.idx);
      startActivity(i);
    }
    else if (id == R.id.close)
    {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void loadPlaceDetail()
  {
    progressBar.setVisibility(View.VISIBLE);
    progressBar.bringToFront();
    new GetPlaceDetailAsyncTask().execute();
  }
  
  private class GetPlaceDetailAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getPlaceDetail(placeIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new PlaceDetailBean();
          bean.setJSONObject(obj.getJSONObject("place"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        displayContentData();
      }
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
    }
  }
  
  
  private void displayContentData()
  {
    if (bean == null)
      return;
    
    setTitle(bean.title);
    ((TextView) findViewById(R.id.text_title)).setText(bean.title);
    ((TextView) findViewById(R.id.text_description)).setText(bean.address);
    textDetail.setText(bean.contents);
    likeCount.setText(Integer.toString(bean.likeCount));
    
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/500/photos/small/Punkt_1.jpg?1426234759")
           .resize(300, 300)
           .centerCrop()
           .into((ImageView) findViewById(R.id.imageview));
    
    int resId = R.layout.list_item_recommend_seoul_photo;
    View parentView = getLayoutInflater().inflate(resId, null);
    ImageView view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/500/photos/small/Punkt_1.jpg?1426234759")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/500/photos/small/Punkt_1.jpg?1426234759");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/498/photos/small/Punkt_6.jpg?1426234741")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/498/photos/small/Punkt_6.jpg?1426234741");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/521/photos/small/mallee_10.jpg?1427043989")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/521/photos/small/mallee_10.jpg?1427043989");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/497/photos/small/Nebbia_09.JPG?1426234581")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/497/photos/small/Nebbia_09.JPG?1426234581");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/495/photos/small/Nebbia_07.JPG?1426234580")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/495/photos/small/Nebbia_07.JPG?1426234580");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/479/photos/small/020_BAGAZIMURI_2.JPG?1425825633")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/479/photos/small/020_BAGAZIMURI_2.JPG?1425825633");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/520/photos/small/mallee_9.jpg?1427043973")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/520/photos/small/mallee_9.jpg?1427043973");
        startActivity(i);
      }
    });
    horizontalImagesView.addView(view);
    
    parentView = getLayoutInflater().inflate(resId, null);
    view = (ImageView) parentView.findViewById(R.id.photo);
    Picasso.with(this)
           .load("https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/504/photos/small/Harman_03.jpg?1426236309")
           .resize(300, 300)
           .centerCrop()
           .into(view);
    view.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
        i.putExtra("imageUrl", "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/aimper/store_photos/504/photos/small/Harman_03.jpg?1426236309");
        startActivity(i);
      }
    });
    
    horizontalImagesView.addView(view);
    
    addDetailInfo(getString(R.string.term_address), bean.address);
    addDetailInfo(getString(R.string.term_phone), bean.contact);
    addDetailInfo(getString(R.string.term_homepage), bean.homepage);
    addDetailInfo(getString(R.string.term_working_time), bean.businessHours);
    addDetailInfo(getString(R.string.term_price_info), bean.priceInfo);
    addDetailInfo(getString(R.string.term_traffic), bean.trafficInfo);
    addDetailInfo(getString(R.string.term_description), bean.tag);
  }
  
  
  private void addDetailInfo(String title, String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(R.id.text_title);
      TextView textView = (TextView) view.findViewById(R.id.text_description);
      titleView.setText(title);
      textView.setText(text);
      layoutDetailPlaceInfo.addView(view);
    }
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_share)
      {
        // 공유하기
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
      }
      else if (v.getId() == R.id.btn_like)
      {
        //좋아요 
      }
    }
  };
}
