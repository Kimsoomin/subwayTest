package com.dabeeo.hangouyou.activities.coupon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.CouponBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.utils.ImageDownloader;

@SuppressWarnings("deprecation")
public class DownloadedCouponDetailActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textCouponNumber, textValidityPeriod, textValidityCondition, textWhereUseIn, textHowToUse, textInstruction;
  private ApiClient apiClient;
  private String couponId;
  private CouponBean coupon;
  private Button btnUse;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_downloaded_coupon_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_coupon));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    couponId = getIntent().getStringExtra("coupon_idx");
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textCouponNumber = (TextView) findViewById(R.id.text_coupon_number);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    textHowToUse = (TextView) findViewById(R.id.text_how_to_use);
    textInstruction = (TextView) findViewById(R.id.text_instructions);
    
    btnUse = (Button) findViewById(R.id.btn_use_coupon);
    btnUse.setOnClickListener(clickListener);
    findViewById(R.id.btn_show_location).setOnClickListener(clickListener);
    
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
    ImageDownloader.displayImage(this, "", imageView, null);
    textTitle.setText(coupon.title);
    textCouponNumber.setText(coupon.couponNumber);
    textValidityPeriod.setText(coupon.fromValidityDate + "~" + coupon.toValidityDate);
    textValidityCondition.setText(coupon.validityCondition);
    textWhereUseIn.setText(coupon.whereUseIn);
    textHowToUse.setText(coupon.howToUse);
    textInstruction.setText(coupon.instruction);
    btnUse.setEnabled(true);
  }
  
  
  private void displayOnMap()
  {
    Intent i = new Intent(this, BlinkingMap.class);
    i.putExtra("idx", couponId);
    startActivity(i);
  }
  
  
  private void useCoupon()
  {
    new AlertDialog.Builder(this).setTitle(R.string.term_alert).setMessage(R.string.term_confirm_use_coupon).setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok,
        new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            new UseCouponTask().execute();
          }
        }).create().show();
  }
  
  
  private void onAfterUseCoupon()
  {
    new AlertDialog.Builder(this).setTitle(R.string.term_alert).setMessage(R.string.term_conpun_used).setPositiveButton(android.R.string.ok, null).create().show();
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
//        useCoupon();
      {
        Builder builder = new AlertDialog.Builder(DownloadedCouponDetailActivity.this);
        builder.setTitle(getString(R.string.term_alert));
        builder.setMessage(getString(R.string.msg_use_coupon_only_employee));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface arg0, int arg1)
          {
            Builder builder = new AlertDialog.Builder(DownloadedCouponDetailActivity.this);
            builder.setTitle(getString(R.string.term_alert));
            builder.setMessage(getString(R.string.msg_complete_use_coupon));
            builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface arg0, int arg1)
              {
              }
            });
            builder.create().show();
          }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.create().show();
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
      return apiClient.getCouponDetail(couponId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
        return;
      
      try
      {
        coupon = new CouponBean();
//        bean.setJSONObject(new JSONObject(result.response));
        
        coupon.title = "아쿠아리움 입장권";
        coupon.couponNumber = "2897642937492378492";
        coupon.fromValidityDate = "2015.01.01";
        coupon.toValidityDate = "2015.12.31";
        coupon.validityCondition = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        coupon.whereUseIn = "신사 아쿠아리움";
        coupon.howToUse = "결제 시 매장 직원에게 제시";
        coupon.instruction = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        
        displayData();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private class UseCouponTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.useCoupon(couponId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
        return;
      
      onAfterUseCoupon();
    }
  }
}
