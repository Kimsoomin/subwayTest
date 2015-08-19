package com.dabeeo.hanhayou.activities.coupon;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.CouponDetailBean;
import com.dabeeo.hanhayou.beans.OfflineBehaviorBean;
import com.dabeeo.hanhayou.controllers.OfflineCouponDatabaseManager;
import com.dabeeo.hanhayou.controllers.OfflineDeleteManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.utils.SystemUtil;

@SuppressWarnings("deprecation")
public class DownloadedCouponDetailActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textValidityPeriod, textValidityCondition, textInfo;
  private ApiClient apiClient;
  private String couponIdx;
  private String branchIdx;
  private CouponDetailBean bean;
  private Button btnUse;
  private ViewGroup layoutInfos;
  private OfflineCouponDatabaseManager couponDatabase;
  private OfflineDeleteManager offlineBehaviorDatabase;
  private ImageView barcodeImageView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_coupon_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_coupon));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    offlineBehaviorDatabase = new OfflineDeleteManager(this);
    couponDatabase = new OfflineCouponDatabaseManager(this);
    couponIdx = getIntent().getStringExtra("coupon_idx");
    branchIdx = getIntent().getStringExtra("branch_idx");
    
    barcodeImageView = (ImageView) findViewById(R.id.bacode_image);
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textInfo = (TextView) findViewById(R.id.text_coupon_info);
    
    layoutInfos = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    btnUse = (Button) findViewById(R.id.btn_use);
    btnUse.setOnClickListener(clickListener);
    
    apiClient = new ApiClient(this);
    
    new GetAsyncTask().execute();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_map, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.map)
      displayOnMap();
    return super.onOptionsItemSelected(item);
  }
  
  
  @SuppressLint("SimpleDateFormat")
  private void displayData()
  {
    ImageDownloader.displayImage(this, "file:///" + bean.couponImageUrl, imageView, null);
    ImageDownloader.displayImageWithListener(this, "file:///" + bean.downloadCouponImage, barcodeImageView, null);
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    if (bean.isNotimeLimit)
      textValidityPeriod.setText(getString(R.string.term_notimelimit));
    if (bean.isExhaustion)
      textValidityPeriod.setText(format.format(bean.startDate) + getString(R.string.term_exhaustion));
    if (!bean.isExhaustion && !bean.isNotimeLimit)
      textValidityPeriod.setText(format.format(bean.startDate) + "~" + format.format(bean.endDate));
    
    textTitle.setText(bean.title);
    textValidityCondition.setText(bean.condition);
    textInfo.setText(bean.info);
    
    addDetailInfo(getString(R.string.term_where_use_in), bean.brandName);
    addDetailInfo(getString(R.string.term_coupon_address), bean.address);
    addDetailInfo(getString(R.string.term_coupon_tel), bean.tel);
    addDetailInfo(getString(R.string.term_coupon_how_to), bean.howto);
    addDetailInfo(getString(R.string.term_coupon_notice), bean.notice);
    
    if (bean.isUse)
    {
      btnUse.setEnabled(false);
      btnUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.dark_gray_transparent));
      btnUse.setTextColor(Color.WHITE);
      btnUse.setText(getString(R.string.term_used));
    }
    else
    {
      btnUse.setEnabled(true);
      btnUse.setBackgroundDrawable(getResources().getDrawable(R.drawable.dark_trans_pink_pressed_dark_pink));
      btnUse.setTextColor(Color.WHITE);
      btnUse.setText(getString(R.string.term_use_coupon));
    }
  }
  
  
  private void addDetailInfo(String title, final String text)
  {
    if (!TextUtils.isEmpty(text))
    {
      int detailResId = R.layout.list_item_strategy_seoul_place_detail_info;
      View view = getLayoutInflater().inflate(detailResId, null);
      TextView titleView = (TextView) view.findViewById(R.id.text_title);
      TextView textView = (TextView) view.findViewById(R.id.text_description);
      titleView.setText(title);
      textView.setText(text);
      
      ImageView btnCall = (ImageView) view.findViewById(R.id.btn_call);
      ImageView btnAddress = (ImageView) view.findViewById(R.id.btn_address);
      if (title.equals(getString(R.string.term_coupon_address)))
      {
        btnAddress.setVisibility(View.VISIBLE);
        btnAddress.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            //주소에서 지도 모양 눌렀을 때 
            MapCheckUtil.checkMapExist(DownloadedCouponDetailActivity.this, new Runnable()
            {
              @Override
              public void run()
              {
                Intent i = new Intent(DownloadedCouponDetailActivity.this, BlinkingMap.class);
                i.putExtra("cuponTitle", bean.title);
                i.putExtra("cuponAddress", bean.address);
                i.putExtra("cuponLat", bean.lat);
                i.putExtra("cuponLng", bean.lng);
                startActivity(i);
              }
            });
          }
        });
      }
      else if (title.equals(getString(R.string.term_coupon_tel)))
      {
        btnCall.setVisibility(View.VISIBLE);
        btnCall.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + text));
            startActivity(callIntent);
          }
        });
      }
      else if (title.equals(getString(R.string.term_homepage)))
      {
        Linkify.addLinks(textView, Linkify.WEB_URLS);
      }
      layoutInfos.addView(view);
    }
  }
  
  
  private void displayOnMap()
  {
    MapCheckUtil.checkMapExist(this, new Runnable()
    {
      @Override
      public void run()
      {
        Intent i = new Intent(DownloadedCouponDetailActivity.this, BlinkingMap.class);
        i.putExtra("cuponTitle", bean.title);
        i.putExtra("cuponAddress", bean.address);
        i.putExtra("cuponLat", bean.lat);
        i.putExtra("cuponLng", bean.lng);
        startActivity(i);
      }
    });
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnUse.getId())
      {
        new AlertDialog.Builder(DownloadedCouponDetailActivity.this).setTitle(R.string.term_alert).setMessage(getString(R.string.msg_use_confirm))
        .setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            new UseAsyncTask().execute();
          }
        }).create().show();
      }
      else if (v.getId() == R.id.btn_show_location)
        displayOnMap();
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<Void, Integer, CouponDetailBean>
  {
    @Override
    protected CouponDetailBean doInBackground(Void... params)
    {
      return couponDatabase.getDownloadCoupon(couponIdx, branchIdx);
    }
    
    
    @Override
    protected void onPostExecute(CouponDetailBean result)
    {
      super.onPostExecute(result);
      
      if (result == null)
        return;
      
      bean = result;
      displayData();
    }
  }
  
  private class UseAsyncTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      if (!SystemUtil.isConnectNetwork(DownloadedCouponDetailActivity.this))
      {
        OfflineBehaviorBean bean = new OfflineBehaviorBean();
        bean.setUseCoupon(DownloadedCouponDetailActivity.this, couponIdx, branchIdx);
        offlineBehaviorDatabase.addBehavior(bean);
      }
      try
      {
        apiClient.useCoupon(couponIdx, branchIdx);
      } catch (Exception e)
      {
        e.printStackTrace();
      }
      couponDatabase.setUseCoupon(couponIdx, branchIdx);
      return null;
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      new AlertDialog.Builder(DownloadedCouponDetailActivity.this).setTitle(R.string.term_alert).setMessage(getString(R.string.msg_use_complete))
      .setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          new GetAsyncTask().execute();
        }
      }).create().show();
    }
  }
}
