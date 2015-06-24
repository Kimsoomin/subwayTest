package com.dabeeo.hanhayou.activities.travel;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.ImagePopUpActivity;
import com.dabeeo.hanhayou.beans.ContentBean;
import com.dabeeo.hanhayou.beans.PremiumDetailBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.views.SharePickView;
import com.squareup.picasso.Picasso;

public class TravelStrategyDetailActivity extends ActionBarActivity
{
  private LinearLayout contentContainer;
  private ViewGroup horizontalImagesView, layoutDetailPlaceInfo;
  
  private ApiClient apiClient;
  private ProgressBar progressBar;
  
  private String placeIdx;
  private PremiumDetailBean bean;
  private ScrollView scrollView;
  private TextView likeCount;
  private ImageView btnLike;
  private SharePickView sharePickView;
  
  private boolean isMap = false;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    getSupportActionBar().setElevation(0);
    setTitle("");
    
    setContentView(R.layout.activity_strategy_seoul_detail);
    
    apiClient = new ApiClient(this);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    placeIdx = getIntent().getStringExtra("place_idx");
    isMap = getIntent().getBooleanExtra("is_map", false);
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    contentContainer = (LinearLayout) findViewById(R.id.container_details);
    horizontalImagesView = (ViewGroup) findViewById(R.id.horizontal_images_view);
    layoutDetailPlaceInfo = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    likeCount = (TextView) findViewById(R.id.like_count);
    btnLike = (ImageView) findViewById(R.id.btn_like);
    findViewById(R.id.btn_share).setOnClickListener(clickListener);
    findViewById(R.id.btn_like).setOnClickListener(clickListener);
    sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
    
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
        if (scrollY < 253)
          setTitle("");
        else
          setTitle(bean.title);
      }
    });
    loadPlaceDetail();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    if (!isMap)
      getMenuInflater().inflate(R.menu.menu_my_place_detail, menu);
    else
      getMenuInflater().inflate(R.menu.menu_empty, menu);
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
      MapCheckUtil.checkMapExist(TravelStrategyDetailActivity.this, new Runnable()
      {
        @Override
        public void run()
        {
          Intent i = new Intent(TravelStrategyDetailActivity.this, BlinkingMap.class);
          i.putExtra("premiumIdx", placeIdx);
          i.putExtra("isPremium", true);
          startActivity(i);
        }
      });
    }
    else if (id == R.id.close)
    {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  public void loadPlaceDetail()
  {
    progressBar.setVisibility(View.VISIBLE);
    progressBar.bringToFront();
    new GetPlaceDetailAsyncTask().execute();
  }
  
  
  public void displayContentData()
  {
    if (bean == null)
      return;
    
    setTitle("");
    ((TextView) findViewById(R.id.text_title)).setText(bean.title);
    if(bean.placeDetail != null)
      ((TextView) findViewById(R.id.text_description)).setText(bean.placeDetail.title);
    else
      ((TextView) findViewById(R.id.text_description)).setText("");
    
    //Contents
    if (bean.contents.size() > 0)
    {
      if (!bean.contents.get(0).isText)
        bean.contents.remove(0);
    }
    for (int i = 0; i < bean.contents.size(); i++)
    {
      int detailResId = R.layout.view_premium_seoul_content;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView text = (TextView) view.findViewById(R.id.text_content);
      ImageView image = (ImageView) view.findViewById(R.id.image_content);
      
      ContentBean contentBean = bean.contents.get(i);
      if (contentBean.isText)
      {
        text.setText(Html.fromHtml(contentBean.text));
        text.setVisibility(View.VISIBLE);
      }
      else
      {
        Picasso.with(this).load(contentBean.imageUrl).fit().centerCrop().into(image);
        image.setVisibility(View.VISIBLE);
        final String imageUrl = contentBean.imageUrl;
        image.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent i = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
            i.putExtra("imageUrls", bean.allImages);
            i.putExtra("imageUrl", imageUrl);
            startActivity(i);
          }
        });
      }
      
      contentContainer.addView(view);
    }
    
    likeCount.setText(Integer.toString(bean.likeCount));
    
    //Main Image
    Picasso.with(this).load(bean.mainImageUrl).into((ImageView) findViewById(R.id.imageview));
    Log.w("WARN", "Image Url : " + bean.mainImageUrl);
    
    //Scroll small Images
    horizontalImagesView.removeAllViews();
    
    for (int i = 0; i < bean.smallImages.size(); i++)
    {
      int resId = R.layout.list_item_recommend_seoul_photo;
      View parentView = getLayoutInflater().inflate(resId, null);
      
      final String imageUrl = bean.smallImages.get(i);
      ImageView view = (ImageView) parentView.findViewById(R.id.photo);
      Picasso.with(this).load(imageUrl).resize(150, 150).centerCrop().into(view);
      final String finalImageUrl = imageUrl;
      view.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View arg0)
        {
          Log.w("WARN", "onClick!");
          Intent intent = new Intent(TravelStrategyDetailActivity.this, ImagePopUpActivity.class);
          intent.putExtra("imageUrls", bean.allImages);
          intent.putExtra("imageUrl", finalImageUrl);
          startActivity(intent);
        }
      });
      horizontalImagesView.addView(parentView);
    }
    
    if(bean.placeDetail != null)
    {
      addDetailInfo(getString(R.string.term_place_detail_info), bean.placeDetail.contents);
      addDetailInfo(getString(R.string.term_address), bean.placeDetail.address);
      addDetailInfo(getString(R.string.term_phone), bean.placeDetail.contact);
      addDetailInfo(getString(R.string.term_homepage), bean.placeDetail.homepage);
      addDetailInfo(getString(R.string.term_working_time), bean.placeDetail.businessHours);
      addDetailInfo(getString(R.string.term_price_info), bean.placeDetail.priceInfo);
      addDetailInfo(getString(R.string.term_traffic), bean.placeDetail.trafficInfo);
//    addDetailInfo(getString(R.string.term_description), bean.placeDetail.); //이용 시간
    }
  }
  
  
  public void addDetailInfo(String title, final String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(R.id.text_title);
      TextView textView = (TextView) view.findViewById(R.id.text_description);
      ImageView imageCall = (ImageView) view.findViewById(R.id.btn_call);
      ImageView imageAddress = (ImageView) view.findViewById(R.id.btn_address);
      
      if (title.equals(getString(R.string.term_phone)))
      {
        imageCall.setVisibility(View.VISIBLE);
        imageAddress.setVisibility(View.GONE);
        
        imageCall.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
          }
        });
      }
      else if (title.equals(getString(R.string.term_address)))
      {
        imageCall.setVisibility(View.GONE);
        if (!isMap)
        {
          imageAddress.setVisibility(View.VISIBLE);
          
          imageAddress.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View arg0)
            {
              MapCheckUtil.checkMapExist(TravelStrategyDetailActivity.this, new Runnable()
              {
                @Override
                public void run()
                {
                  Intent i = new Intent(TravelStrategyDetailActivity.this, BlinkingMap.class);
                  i.putExtra("premiumIdx", placeIdx);
                  i.putExtra("isPremium", true);
                  startActivity(i);
                }
              });
            }
          });
        }
      }
      else
      {
        imageCall.setVisibility(View.GONE);
        imageAddress.setVisibility(View.GONE);
      }
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
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "공유테스트");
//        sendIntent.setType("text/plain");
//        startActivity(Intent.createChooser(sendIntent, null));
        sharePickView.setVisibility(View.VISIBLE);
        sharePickView.view.setVisibility(View.VISIBLE);
        sharePickView.bringToFront();
      }
      else if (v.getId() == R.id.btn_like)
      {
        //좋아요 
        new ToggleLikeTask().execute();
      }
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetPlaceDetailAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      BlinkingCommon.smlLibDebug("추천서울", "premiumIdx : " + placeIdx);
      return apiClient.getPremiumDetail(placeIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      if (result.isSuccess)
      {
        try
        {
          JSONObject obj = new JSONObject(result.response);
          bean = new PremiumDetailBean();
          bean.setJSONObject(obj.getJSONObject("premium"));
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
  
  private class ToggleLikeTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      // TODO 아직 api없음 
      return apiClient.setUsedLog(PreferenceManager.getInstance(getApplicationContext()).getUserSeq(), bean.idx, "premium", "L");
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      try
      {
        JSONObject obj = new JSONObject(result.response);
        btnLike.setActivated(obj.getString("result").equals("INS"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
