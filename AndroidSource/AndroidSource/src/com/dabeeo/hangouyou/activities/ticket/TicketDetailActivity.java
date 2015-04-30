package com.dabeeo.hangouyou.activities.ticket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.TicketBean;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.BlinkingMap;
import com.dabeeo.hangouyou.utils.NumberFormatter;

public class TicketDetailActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textPrice, textDiscountPrice, textValidityPeroid, textDicoundRate, textValidityCondition, textRefundCondition, textWhereUseIn;
  private ApiClient apiClient;
  private String ticketId;
  private TicketBean ticket;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    ticketId = getIntent().getStringExtra("ticket_idx");
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textPrice = (TextView) findViewById(R.id.text_display_price);
    textDiscountPrice = (TextView) findViewById(R.id.text_price);
    textValidityPeroid = (TextView) findViewById(R.id.text_validity_peroid);
    textDicoundRate = (TextView) findViewById(R.id.text_discount_rate);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textRefundCondition = (TextView) findViewById(R.id.text_refund_condition);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    
    findViewById(R.id.img_like).setOnClickListener(clickListener);
    findViewById(R.id.img_share).setOnClickListener(clickListener);
    findViewById(R.id.btn_my_cart).setOnClickListener(clickListener);
    findViewById(R.id.btn_checkout).setOnClickListener(clickListener);
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
    textTitle.setText(ticket.title);
    textPrice.setText(getString(R.string.term_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(ticket.displayPriceWon) + "(" + getString(R.string.term_yuan)
        + ticket.displayPriceYuan + ")");
    textDiscountPrice.setText(getString(R.string.term_discount_price) + " : " + getString(R.string.term_won) + NumberFormatter.addComma(ticket.priceWon) + "(" + getString(R.string.term_yuan)
        + ticket.priceYuan + ")");
    textValidityPeroid.setText(getString(R.string.term_validity_period) + ticket.fromValidityDate + "~" + ticket.toValidityDate);
    textDicoundRate.setText(ticket.discountRate);
    textValidityCondition.setText(ticket.validityCondition);
    textRefundCondition.setText(ticket.refundCondition);
    textWhereUseIn.setText(ticket.whereUseIn);
  }
  
  
  private void displayOnMap()
  {
    Intent i = new Intent(this, BlinkingMap.class);
    i.putExtra("idx", ticketId);
    startActivity(i);
  }
  
  
  private void share()
  {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, ticket.title + "\n" + ticket.priceWon + "\n\n" + "https://play.google.com/store/apps/details?id=com.mintshop.aimper");
    intent.setType("text/plain");
    startActivity(intent);
  }
  
  
  private void checkout()
  {
    Log.i("TicketDetailActivity.java | checkout", "|" + "구매하기" + "|");
//    Intent intent = new Intent(this, CheckoutActivity.class);
//    startActivity(intent);
  }
  
  /**************************************************
   * async task
   ***************************************************/
  private class GetAsyncTask extends AsyncTask<Void, Integer, NetworkResult>
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
      Log.i("TicketDetailActivity.java | onPostExecute", "|" + "=============" + "|");
      if (!result.isSuccess)
        return;
      
      try
      {
        ticket = new TicketBean();
//        bean.setJSONObject(new JSONObject(result.response));
        
        ticket.title = "아쿠아리움 입장권";
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
  
  /**************************************************
   * listener
   ***************************************************/
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.img_like)
        apiClient.likeTicket(ticketId);
      else if (v.getId() == R.id.img_share)
        share();
      else if (v.getId() == R.id.btn_my_cart)
        apiClient.addTicketToCart(ticketId);
      else if (v.getId() == R.id.btn_checkout)
        checkout();
      else if (v.getId() == R.id.btn_show_location)
        displayOnMap();
    }
  };
}
