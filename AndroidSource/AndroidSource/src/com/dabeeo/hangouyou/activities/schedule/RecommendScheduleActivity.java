package com.dabeeo.hangouyou.activities.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.views.CharacterProgressView;

public class RecommendScheduleActivity extends ActionBarActivity
{
  private LinearLayout containerChoiceStartDate;
  
  private Button btnDayUp, btnDayDown;
  private TextView textDay;
  private TextView startDate;
  private int dayCount = 1;
  private String selectTheme = "";
  
  private int year = -1, month = -1, dayOfMonth = -1;
  
  private int theme = -1;
  private LinearLayout containerShopping, containerCulture, containerTour, containerFood, containerRest, containerRandom;
  private ApiClient apiClient;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_travel_schedule);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_recommend_travel_schedule));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    apiClient = new ApiClient(this);
    containerShopping = (LinearLayout) findViewById(R.id.container_shopping);
    containerCulture = (LinearLayout) findViewById(R.id.container_culture);
    containerTour = (LinearLayout) findViewById(R.id.container_tour);
    containerFood = (LinearLayout) findViewById(R.id.container_food);
    containerRest = (LinearLayout) findViewById(R.id.container_rest);
    containerRandom = (LinearLayout) findViewById(R.id.container_random);
    
    containerShopping.setOnClickListener(typeClickListener);
    containerCulture.setOnClickListener(typeClickListener);
    containerTour.setOnClickListener(typeClickListener);
    containerFood.setOnClickListener(typeClickListener);
    containerRest.setOnClickListener(typeClickListener);
    containerRandom.setOnClickListener(typeClickListener);
    
    containerChoiceStartDate = (LinearLayout) findViewById(R.id.container_choice_start_date);
    containerChoiceStartDate.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent i = new Intent(RecommendScheduleActivity.this, CalendarActivity.class);
        startActivityForResult(i, 101);
      }
    });
    startDate = (TextView) findViewById(R.id.text_start_date);
    btnDayUp = (Button) findViewById(R.id.date_up);
    btnDayDown = (Button) findViewById(R.id.date_down);
    textDay = (TextView) findViewById(R.id.text_day);
    
    btnDayUp.setOnClickListener(dayClickListener);
    btnDayDown.setOnClickListener(dayClickListener);
  }
  
  
  @Override
  protected void onActivityResult(int arg0, int arg1, Intent intent)
  {
    if (arg0 == 101 && arg1 == RESULT_OK)
    {
      year = intent.getIntExtra("year", -1);
      month = intent.getIntExtra("month", -1) + 1;
      dayOfMonth = intent.getIntExtra("dayOfMonth", -1);
      Log.w("WARN", "Activity Result:  " + year + " " + month + " " + dayOfMonth);
      
      if (year != -1)
      {
        startDate.setText(Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(dayOfMonth));
        startDate.setTextColor(Color.parseColor("#ff6262"));
      }
    }
    super.onActivityResult(arg0, arg1, intent);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_next, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (item.getItemId() == R.id.next)
    {
      if (year == -1)
      {
        Toast.makeText(RecommendScheduleActivity.this, getString(R.string.msg_warn_empty_start_date), Toast.LENGTH_LONG).show();
      }
      else if (theme == -1)
      {
        Toast.makeText(RecommendScheduleActivity.this, getString(R.string.msg_warn_select_theme), Toast.LENGTH_LONG).show();
      }
      else
        findAndShowDialog();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void findAndShowDialog()
  {
    new CreateAsyncTask().execute();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private OnClickListener dayClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnDayUp.getId())
      {
        if (dayCount == 7)
          return;
        
        dayCount++;
      }
      else
      {
        if (dayCount == 1)
          return;
        
        dayCount--;
      }
      
      textDay.setText(Integer.toString(dayCount));
    }
  };
  
  private OnClickListener typeClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      containerShopping.setSelected(false);
      containerCulture.setSelected(false);
      containerTour.setSelected(false);
      containerFood.setSelected(false);
      containerRest.setSelected(false);
      containerRandom.setSelected(false);
      
      if (v.getId() == containerShopping.getId())
      {
        containerShopping.setSelected(true);
        theme = 0;
      }
      else if (v.getId() == containerCulture.getId())
      {
        containerCulture.setSelected(true);
        theme = 1;
      }
      else if (v.getId() == containerTour.getId())
      {
        containerTour.setSelected(true);
        theme = 2;
      }
      else if (v.getId() == containerFood.getId())
      {
        containerFood.setSelected(true);
        theme = 3;
      }
      else if (v.getId() == containerRest.getId())
      {
        containerRest.setSelected(true);
        theme = 4;
      }
      else if (v.getId() == containerRandom.getId())
      {
        containerRandom.setSelected(true);
        theme = 5;
      }
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class CreateAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    AlertDialog dialog;
    String startDate;
    
    
    @Override
    protected void onPreExecute()
    {
      Builder builder = new AlertDialog.Builder(RecommendScheduleActivity.this);
      CharacterProgressView pView = new CharacterProgressView(RecommendScheduleActivity.this);
      pView.title.setText(getString(R.string.msg_progress_recommend_schedule));
      builder.setView(pView);
      builder.setCancelable(false);
      dialog = builder.create();
      
      if (!dialog.isShowing())
        dialog.show();
      
      Log.i("RecommendScheduleActivity.java | onPreExecute", "|" + year + "|" + month + "|" + dayOfMonth + "|" + dayCount + "일 짜리|" + theme + "|");
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      startDate = year + "-" + String.format("%02d", month) + "-" + dayOfMonth;
      return apiClient.getCreateRecommendSchedule(startDate, dayCount, theme);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      dialog.dismiss();
      
      if (TextUtils.isEmpty(result.response))
        return;
      
      Intent i = new Intent(RecommendScheduleActivity.this, RecommendScheduleCompeletedActivity.class);
      i.putExtra("json", result.response);
      i.putExtra("startDate", startDate);
      startActivity(i);
      super.onPostExecute(result);
    }
  }
}
