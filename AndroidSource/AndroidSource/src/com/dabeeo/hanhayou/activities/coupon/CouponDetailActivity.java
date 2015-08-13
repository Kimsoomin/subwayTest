package com.dabeeo.hanhayou.activities.coupon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
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
import com.dabeeo.hanhayou.controllers.OfflineCouponDatabaseManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.map.Global;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.MapCheckUtil;

@SuppressWarnings("deprecation")
public class CouponDetailActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textValidityPeriod, textValidityCondition, textInfo;
  private ApiClient apiClient;
  private String couponIdx;
  private String branchIdx;
  private CouponDetailBean bean;
  private Button btnDownload;
  private ViewGroup layoutInfos;
  private OfflineCouponDatabaseManager couponDatabase;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coupon_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_coupon));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    couponDatabase = new OfflineCouponDatabaseManager(this);
    couponIdx = getIntent().getStringExtra("coupon_idx");
    branchIdx = getIntent().getStringExtra("branch_idx");
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textInfo = (TextView) findViewById(R.id.text_coupon_info);
    
    layoutInfos = (ViewGroup) findViewById(R.id.layout_place_detail_info);
    
    btnDownload = (Button) findViewById(R.id.btn_download);
    btnDownload.setOnClickListener(clickListener);
    
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
  
  
  private void displayData()
  {
//    Picasso.with(this).load("http://lorempixel.com/400/200/cats").fit().centerCrop().into(imageView);
    ImageDownloader.displayImage(this, bean.couponImageUrl, imageView, null);
    
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
    btnDownload.setEnabled(true);
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
            MapCheckUtil.checkMapExist(CouponDetailActivity.this, new Runnable()
            {
              @Override
              public void run()
              {
                Intent i = new Intent(CouponDetailActivity.this, BlinkingMap.class);
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
        Intent i = new Intent(CouponDetailActivity.this, BlinkingMap.class);
        i.putExtra("idx", couponIdx);
        startActivity(i);
      }
    });
  }
  
  
  private void onAfterCheckAlreadyHave(boolean alreadyHave)
  {
    if (!alreadyHave)
    {
      new CouponDownloadTask().execute();
      return;
    }
    
    showAlert(getString(R.string.term_already_have_coupon));
  }
  
  
  private void showAlert(String msg)
  {
    new AlertDialog.Builder(this).setTitle(R.string.term_alert).setMessage(msg).setNegativeButton(android.R.string.cancel, null).setPositiveButton(R.string.term_show_coupon_list,
        new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            goToDownloadedCouponList();
          }
        }).create().show();
  }
  
  
  private void goToDownloadedCouponList()
  {
    Intent i = new Intent(CouponDetailActivity.this, CouponActivity.class);
    i.putExtra("is_show_download", true);
    startActivity(i);
    finish();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnDownload.getId())
      {
        if (!PreferenceManager.getInstance(CouponDetailActivity.this).isLoggedIn())
        {
          new AlertDialogManager(CouponDetailActivity.this).showNeedLoginDialog(-1);
          return;
        }
        new CheckAlreadyHaveTask().execute();
      }
      else if (v.getId() == R.id.btn_show_location)
        displayOnMap();
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<Void, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getCouponDetail(couponIdx, branchIdx);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
        return;
      
      try
      {
        bean = new CouponDetailBean();
        JSONObject obj = new JSONObject(result.response);
        JSONObject detailObj = obj.getJSONObject("detail");
        bean.setJSONObject(detailObj);
        
        displayData();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class CheckAlreadyHaveTask extends AsyncTask<Void, Void, Boolean>
  {
    @Override
    protected Boolean doInBackground(Void... params)
    {
      return couponDatabase.isHaveCoupon(bean.couponIdx, bean.branchIdx);
    }
    
    
    @Override
    protected void onPostExecute(Boolean result)
    {
      super.onPostExecute(result);
      onAfterCheckAlreadyHave(result);
    }
  }
  
  private class CouponDownloadTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      try
      {
        apiClient.couponDownload(couponIdx, branchIdx);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      File dir = new File(com.dabeeo.hanhayou.map.Global.GetCouponImageFilePath());
      
      if (!dir.exists())
        dir.mkdir();
      
      try
      {
        URL url = new URL(bean.couponImageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        File file = new File(Global.GetCouponImageFilePath(), bean.couponIdx + ".jpg");
        if (!file.exists())
          file.createNewFile();
        
        bean.couponImageUrl = file.getAbsolutePath();
        
        FileOutputStream stream = new FileOutputStream(file);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
        byte[] byteArray = outstream.toByteArray();
        
        stream.write(byteArray);
        stream.close();
        
        Log.w("WARN", "CouponDownload!");
        
        url = new URL(bean.downloadCouponImage);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        input = connection.getInputStream();
        myBitmap = BitmapFactory.decodeStream(input);
        
        file = new File(Global.GetCouponImageFilePath(), bean.couponIdx + "_thumbnail.jpg");
        if (!file.exists())
          file.createNewFile();
        
        bean.downloadCouponImage = file.getAbsolutePath();
        
        stream = new FileOutputStream(file);
        
        outstream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
        byteArray = outstream.toByteArray();
        
        stream.write(byteArray);
        stream.close();
        
        Log.w("WARN", "Coupon Thumbnail Download!");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      couponDatabase.insert(bean.getJSONObject(CouponDetailActivity.this));
      return null;
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      showAlert(getString(R.string.term_coupon_download_completed));
    }
  }
}
