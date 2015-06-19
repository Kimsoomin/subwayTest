package com.dabeeo.hanhayou.activities.ticket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TicketBean;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.NumberFormatter;

@SuppressWarnings("deprecation")
public class BoughtTicketDetailActivity extends ActionBarActivity
{
  private ImageView imageView, imgBarcode;
  private TextView textTitle, textPrice, textPriceCn, textOption, textValidityPeriod, textValidityCondition, textRefundCondition, textWhereUseIn;
  private ApiClient apiClient;
  private String orderId;
  private TicketBean ticket;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bought_ticket_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_ticket));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    orderId = getIntent().getStringExtra("order_id");
    
    imageView = (ImageView) findViewById(R.id.imageview);
    imgBarcode = (ImageView) findViewById(R.id.img_barcode);
    textTitle = (TextView) findViewById(R.id.text_title);
    textOption = (TextView) findViewById(R.id.text_option);
    textPrice = (TextView) findViewById(R.id.text_price);
    textPriceCn = (TextView) findViewById(R.id.text_price_cn);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textRefundCondition = (TextView) findViewById(R.id.text_refund_condition);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    
    findViewById(R.id.btn_go_to_bought_list).setOnClickListener(clickListener);
    findViewById(R.id.btn_show_location).setOnClickListener(clickListener);
    
    apiClient = new ApiClient(this);
    
//    new GetAsyncTask().execute();
    ticket = new TicketBean();
    
    ticket.title = "아쿠아리움 입장권";
    ticket.displayPriceWon = 150000;
    ticket.displayPriceYuan = 80;
    ticket.priceWon = 100000;
    ticket.priceYuan = 56;
    ticket.discountRate = "8折";
    ticket.fromValidityDate = "2015.01.01";
    ticket.toValidityDate = "2015.12.31";
    ticket.validityCondition = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    ticket.refundCondition = ticket.validityCondition;
    ticket.whereUseIn = "신사 아쿠아리움";
    
    displayData();
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
    textOption.setText("성인 2명");
    textTitle.setText(ticket.title);
    textPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(ticket.priceWon) + "(" + getString(R.string.term_yuan) + ticket.priceYuan + ")");
    textValidityPeriod.setText(getString(R.string.term_validity_period) + " : " + ticket.fromValidityDate + "~" + ticket.toValidityDate);
    textValidityCondition.setText(ticket.validityCondition);
    textRefundCondition.setText(ticket.refundCondition);
    textWhereUseIn.setText(ticket.whereUseIn);
  }
  
  
  private void displayOnMap()
  {
    Intent i = new Intent(this, BlinkingMap.class);
    i.putExtra("idx", orderId);
    startActivity(i);
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
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_show_location)
        displayOnMap();
      if (v.getId() == R.id.btn_go_to_bought_list)
        goToBoughtList();
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
      return apiClient.getTicketOrderDetail(orderId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
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
}
