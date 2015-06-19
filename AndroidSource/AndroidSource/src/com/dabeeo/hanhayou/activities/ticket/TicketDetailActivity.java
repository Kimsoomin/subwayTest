package com.dabeeo.hanhayou.activities.ticket;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.TicketBean;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingMap;
import com.dabeeo.hanhayou.utils.ImageDownloader;
import com.dabeeo.hanhayou.utils.MapCheckUtil;
import com.dabeeo.hanhayou.utils.NumberFormatter;
import com.dabeeo.hanhayou.views.SharePickView;
import com.dabeeo.hanhayou.views.TrendOptionAndAmountPickView;

@SuppressWarnings("deprecation")
public class TicketDetailActivity extends ActionBarActivity
{
  private ImageView imageView;
  private TextView textTitle, textDisplayPrice, textDisplayPriceCn, textPrice, textValidityPeriod, textValidityCondition, textRefundCondition, textWhereUseIn, textDiscountMonth;
  private ApiClient apiClient;
  private String ticketId;
  private TicketBean ticket;
  private Button btnAddToCart, btnCheckout;
  private ImageButton btnWishList;
  private TextView textQuantity;
  private TrendOptionAndAmountPickView optionAmountPickerView;
  private SharePickView sharePickView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket_detail);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_ticket));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    ticketId = getIntent().getStringExtra("ticket_idx");
    
    imageView = (ImageView) findViewById(R.id.imageview);
    textTitle = (TextView) findViewById(R.id.text_title);
    textDisplayPrice = (TextView) findViewById(R.id.text_display_price);
    textPrice = (TextView) findViewById(R.id.text_price);
    textValidityPeriod = (TextView) findViewById(R.id.text_validity_period);
    textValidityCondition = (TextView) findViewById(R.id.text_validity_condition);
    textRefundCondition = (TextView) findViewById(R.id.text_refund_condition);
    textWhereUseIn = (TextView) findViewById(R.id.text_where_use_in);
    textDisplayPriceCn = (TextView) findViewById(R.id.text_discount_price_cn);
    btnAddToCart = (Button) findViewById(R.id.btn_my_cart);
    btnWishList = (ImageButton) findViewById(R.id.img_wishlist);
    btnCheckout = (Button) findViewById(R.id.btn_checkout);
    textDiscountMonth = (TextView) findViewById(R.id.text_discount_month);
    optionAmountPickerView = (TrendOptionAndAmountPickView) findViewById(R.id.option_picker_view);
    sharePickView = (SharePickView) findViewById(R.id.view_share_pick);
    
    btnWishList.setOnClickListener(clickListener);
    findViewById(R.id.img_share).setOnClickListener(clickListener);
    btnAddToCart.setOnClickListener(clickListener);
    btnCheckout.setOnClickListener(clickListener);
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
    textTitle.setText(ticket.title);
    textDisplayPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(ticket.displayPriceWon));
    textPrice.setText(getString(R.string.term_won) + NumberFormatter.addComma(ticket.priceWon));
    textPrice.setPaintFlags(textPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    textDisplayPriceCn.setText("(" + getString(R.string.term_yuan) + ticket.displayPriceYuan + ")");
    textValidityPeriod.setText(getString(R.string.term_validity_period) + " : " + ticket.fromValidityDate + "~" + ticket.toValidityDate);
    textValidityCondition.setText(ticket.validityCondition);
    textDiscountMonth.setText(ticket.discountRate);
    textRefundCondition.setText(ticket.refundCondition);
    textWhereUseIn.setText("아쿠아 플라넷");
    
    btnAddToCart.setEnabled(true);
    btnCheckout.setEnabled(true);
  }
  
  
  private void displayOnMap()
  {
    MapCheckUtil.checkMapExist(this, new Runnable()
    {
      @Override
      public void run()
      {
        Intent i = new Intent(TicketDetailActivity.this, BlinkingMap.class);
        i.putExtra("idx", ticketId);
        startActivity(i);
      }
    });
  }
  
  
  private void checkout()
  {
    selectOption();
  }
  
  
  private void selectOption()
  {
    int resId = R.layout.view_buy_ticket_option;
    View view = getLayoutInflater().inflate(resId, null);
    
    ViewGroup layoutQuantity = (ViewGroup) view.findViewById(R.id.layout_quantity);
    textQuantity = (TextView) view.findViewById(R.id.text_quantity);
    final Spinner ageSpinner = (Spinner) view.findViewById(R.id.spinner_age);
    
    layoutQuantity.setVisibility(View.VISIBLE);
    view.findViewById(R.id.btn_decrease).setOnClickListener(clickListener);
    view.findViewById(R.id.btn_increase).setOnClickListener(clickListener);
    
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(ticket.title);
    builder.setView(view);
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        onAfterSelectOption(ageSpinner.getSelectedItemPosition(), Integer.valueOf(textQuantity.getText().toString()));
      }
    });
    builder.create().show();
  }
  
  
  private void changeQuantity(int add)
  {
    int quantity = Integer.valueOf(textQuantity.getText().toString());
    textQuantity.setText(Integer.toString(quantity + add));
  }
  
  
  private void onAfterSelectOption(int agePosition, int quantity)
  {
    Intent intent = new Intent(this, TicketCheckoutActivity.class);
    intent.putExtra("ticket_idx", ticketId);
    intent.putExtra("age_position", agePosition);
    intent.putExtra("quantity", quantity);
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
      if (v.getId() == btnWishList.getId())
      {
//        new likeTicketTask().execute();
        btnWishList.setActivated(!btnWishList.isActivated());
        if (btnWishList.isActivated())
          Toast.makeText(TicketDetailActivity.this, getString(R.string.msg_add_wishlist), Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == R.id.img_share)
      {
        sharePickView.setVisibility(View.VISIBLE);
        sharePickView.view.setVisibility(View.VISIBLE);
        sharePickView.bringToFront();
      }
      else if (v.getId() == btnAddToCart.getId())
      {
//        new addTicketToCartTask().execute();
        optionAmountPickerView.setVisibility(View.VISIBLE);
        optionAmountPickerView.view.setVisibility(View.VISIBLE);
        optionAmountPickerView.setOptions(null);
        optionAmountPickerView.bringToFront();
      }
      else if (v.getId() == R.id.btn_checkout)
      {
//        checkout();
        optionAmountPickerView.setVisibility(View.VISIBLE);
        optionAmountPickerView.view.setVisibility(View.VISIBLE);
        optionAmountPickerView.setOptions(null);
        optionAmountPickerView.bringToFront();
      }
      else if (v.getId() == R.id.btn_show_location)
        displayOnMap();
      else if (v.getId() == R.id.btn_decrease)
        changeQuantity(-1);
      else if (v.getId() == R.id.btn_increase)
        changeQuantity(1);
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
      return apiClient.getTicketDetail(ticketId);
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
        ticket.discountRate = "9折";
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
  
  private class addTicketToCartTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.addTicketToCart(ticketId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
        return;
      
      Log.i("TicketDetailActivity.java | onPostExecute", "|장바구니에 추가|");
      btnAddToCart.setEnabled(false);
    }
  }
  
  private class likeTicketTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return apiClient.likeTicket(ticketId);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      super.onPostExecute(result);
      
      if (!result.isSuccess)
        return;
      
      Log.i("TicketDetailActivity.java | onPostExecute", "|좋아요 추가|");
    }
  }
}
