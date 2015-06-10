package com.dabeeo.hangouyou.activities.ticket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.NumberFormatter;
import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")
public class TicketOrderCompleteActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textOrderCode, textTitle, textCode, textValidityPeriod, textValidityCondition;
  private TextView textTotalPrice, textTotalQuantity, textDiscountByCoupon, textBillPrice, textWhereUseIn, textPaidBy, textPaidPrice, textPaidDate;
  
  private ApiClient apiClient;
  private TicketBean ticket;
  private String orderId;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket_order_complete);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textOrderCode = (TextView) findViewById(R.id.text_order_code);
    textTitle = (TextView) findViewById(R.id.text_title);
    textCode = (TextView) findViewById(R.id.text_code);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textTotalPrice = (TextView) findViewById(R.id.text_total_price);
    textTotalQuantity = (TextView) findViewById(R.id.text_total_quantity);
    textDiscountByCoupon = (TextView) findViewById(R.id.text_discount_by_coupon);
    textBillPrice = (TextView) findViewById(R.id.text_bill_price);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    textPaidBy = (TextView) findViewById(R.id.text_paid_by);
    textPaidPrice = (TextView) findViewById(R.id.text_paid_price);
    textPaidDate = (TextView) findViewById(R.id.text_paid_date);
    
    findViewById(R.id.btn_go_to_main).setOnClickListener(clickListener);
    findViewById(R.id.btn_go_to_bought_list).setOnClickListener(clickListener);
    
    orderId = getIntent().getStringExtra("order_id");
    
    apiClient = new ApiClient(this);
    
    new GetAsyncTask().execute();
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayData()
  {
    Picasso.with(this).load("http://lorempixel.com/300/300/cats").fit().centerCrop().into(imageView);
    
    textOrderCode.setText(getString(R.string.term_order_code) + " : " + "192370283qwer");
    textTitle.setText(ticket.title);
    textCode.setText(getString(R.string.term_code) + " : " + ticket.code);
    textValidityPeriod.setText(getString(R.string.term_validity_period) + " : " + ticket.fromValidityDate + "~" + ticket.toValidityDate);
    
    textValidityCondition.setText(ticket.validityCondition);
    textWhereUseIn.setText(ticket.whereUseIn);
    
    textTotalPrice.setText(getString(R.string.term_total_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(30000));
    textDiscountByCoupon.setText(getString(R.string.term_discount_by_coupon) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(10000));
    textBillPrice.setText(getString(R.string.term_bill_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(20000) + "(" + getString(R.string.term_yuan) + ticket.priceYuan + ")");
    textTotalQuantity.setText(getString(R.string.term_total_quantity) + " : " + 1 + getString(R.string.term_quantity_unit));
    textPaidBy.setText(String.format(getString(R.string.term_paid_by), "알리페이"));
    textPaidPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(20000) + "(" + getString(R.string.term_yuan) + ticket.priceYuan + ")");
    textPaidDate.setText("2015.05.04");
  }
  
  
  private void goToMain()
  {
    Intent intent = new Intent(this, TicketActivity.class);
    startActivity(intent);
  }
  
  
  private void goToBoughtList()
  {
    // TODO 구매내역 화면으로 보내기
    Intent intent = new Intent(this, TicketActivity.class);
    startActivity(intent);
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_go_to_main)
        goToMain();
      else if (v.getId() == R.id.btn_go_to_bought_list)
        goToBoughtList();
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.getTicketOrderDetail(orderId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      try
      {
        ticket = new TicketBean();
//        bean.setJSONObject(new JSONObject(result.response));
        
        ticket.title = "아쿠아리움 입장권";
        ticket.code = "1249395X";
        ticket.displayPriceWon = 150000;
        ticket.displayPriceYuan = 80;
        ticket.priceWon = 100000;
        ticket.priceYuan = 56;
        ticket.discountRate = "80%";
        ticket.fromValidityDate = "2015.01.01";
        ticket.toValidityDate = "2015.12.31";
        ticket.validityCondition = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        ticket.refundCondition = ticket.validityCondition;
        ticket.whereUseIn = "신사 아쿠아리움";
        
        displayData();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
