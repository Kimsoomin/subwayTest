package com.dabeeo.hangouyou.activities.ticket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.NumberFormatter;
import com.google.android.gms.drive.execution.internal.GetContentTransferInfoRequest;
import com.squareup.picasso.Picasso;

public class TicketCheckoutActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textCode, textDisplayPrice, textPrice, textValidityPeriod, textQuantity, textAge, textValidityCondition, textRefundCondition;
  private TextView textName, textEmail, textTotalPrice, textTotalQuantity, textDiscountByCoupon, textBillPrice, textWhereUseIn;
  private EditText editEnglishName, editPassportNumber, editPhoneNumber;
  private CheckBox checkAgree;
  private RadioGroup radioPayType;
  
  private ApiClient apiClient;
  private TicketBean ticket;
  private String ticketId;
  private Button btnCheckout;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket_checkout);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textCode = (TextView) findViewById(R.id.text_code);
    textDisplayPrice = (TextView) findViewById(R.id.text_display_price);
    textPrice = (TextView) findViewById(R.id.text_price);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textQuantity = (TextView) findViewById(R.id.text_quantity);
    textAge = (TextView) findViewById(R.id.text_age);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textRefundCondition = (TextView) findViewById(R.id.text_refund_condition);
    textName = (TextView) findViewById(R.id.text_name);
    textEmail = (TextView) findViewById(R.id.text_email);
    textTotalPrice = (TextView) findViewById(R.id.text_total_price);
    textTotalQuantity = (TextView) findViewById(R.id.text_total_quantity);
    textDiscountByCoupon = (TextView) findViewById(R.id.text_discount_by_coupon);
    textBillPrice = (TextView) findViewById(R.id.text_bill_price);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    editEnglishName = (EditText) findViewById(R.id.edit_english_name);
    editPassportNumber = (EditText) findViewById(R.id.edit_passport_number);
    editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
    checkAgree = (CheckBox) findViewById(R.id.check_agree);
    radioPayType = (RadioGroup) findViewById(R.id.radio_pay_type);
    btnCheckout = (Button) findViewById(R.id.btn_checkout);
    btnCheckout.setOnClickListener(clickListener);
    
    ticketId = getIntent().getStringExtra("ticket_idx");
    int quantity = getIntent().getIntExtra("quantity", 1);
    int agePosition = getIntent().getIntExtra("age_position", 0);
    String[] ages = getResources().getStringArray(R.array.age_type);
    textQuantity.setText(getString(R.string.term_quantity) + " : " + quantity + getString(R.string.term_quantity_unit));
    textAge.setText(getString(R.string.term_age) + " : " + ages[agePosition]);
    
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
    textTitle.setText(ticket.title);
    textCode.setText(getString(R.string.term_code) + " : " + ticket.code);
    textDisplayPrice.setText(getString(R.string.term_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(ticket.displayPriceWon) + "(" + getString(R.string.term_yuan)
        + ticket.displayPriceYuan + ")");
    textPrice.setText(getString(R.string.term_discount_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(ticket.priceWon) + "(" + getString(R.string.term_yuan)
        + ticket.priceYuan + ")");
    textValidityPeriod.setText(getString(R.string.term_validity_period) + " : " + ticket.fromValidityDate + "~" + ticket.toValidityDate);
    
    textValidityCondition.setText(ticket.validityCondition);
    textRefundCondition.setText(ticket.refundCondition);
    textWhereUseIn.setText(ticket.whereUseIn);
    
    textName.setText("홍길동");
    textEmail.setText("asdf@asdf.com");
    textTotalPrice.setText(getString(R.string.term_total_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(30000));
    textTotalQuantity.setText(getString(R.string.term_total_quantity) + " : " + "2" + getString(R.string.term_quantity_unit));
    textDiscountByCoupon.setText(getString(R.string.term_discount_by_coupon) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(10000));
    textBillPrice.setText(getString(R.string.term_bill_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(20000) + "(" + getString(R.string.term_yuan) + ticket.priceYuan + ")");
  }
  
  
  private void checkout()
  {
    if (TextUtils.isEmpty(editEnglishName.getText().toString()))
    {
      editEnglishName.requestFocus();
      editEnglishName.setError(getString(R.string.error_english_name_is_empty));
      return;
    }
    
    if (TextUtils.isEmpty(editPassportNumber.getText().toString()))
    {
      editPassportNumber.requestFocus();
      editPassportNumber.setError(getString(R.string.error_passport_number_is_empty));
      return;
    }
    
    if (!checkAgree.isChecked())
    {
      checkAgree.requestFocus();
      checkAgree.setError(getString(R.string.error_didnt_agree_order));
      return;
    }
    
    btnCheckout.setEnabled(false);
    new CheckoutAsyncTask().execute();
  }
  
  
  private void onAfterCheckout()
  {
    Toast.makeText(this, "결제 완료", Toast.LENGTH_SHORT).show();
    setResult(RESULT_OK);
    finish();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      checkout();
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
      return apiClient.getTicketDetail(ticketId);
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
  
  private class CheckoutAsyncTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.checkoutTicket(ticketId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
      {
        btnCheckout.setEnabled(true);
        return;
      }
      
      onAfterCheckout();
    }
  }
}
