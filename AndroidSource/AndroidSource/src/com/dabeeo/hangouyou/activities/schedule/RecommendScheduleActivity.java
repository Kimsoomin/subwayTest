package com.dabeeo.hangouyou.activities.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

@SuppressWarnings("deprecation")
public class RecommendScheduleActivity extends ActionBarActivity
{
  private LinearLayout containerChoiceStartDate;
  
  private Button btnDayUp, btnDayDown;
  private TextView textDay;
  private TextView startDate;
  private int day = 1;
  private String selectTheme = "";
  
  private int year = -1, month = -1, dayOfMonth = -1;
  
  private String type = "";
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
      month = intent.getIntExtra("month", -1);
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
      else if (TextUtils.isEmpty(type))
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
//    new FindAsyncTask().execute();
    Builder builder = new AlertDialog.Builder(RecommendScheduleActivity.this);
    CharacterProgressView pView = new CharacterProgressView(RecommendScheduleActivity.this);
    pView.title.setText(getString(R.string.msg_progress_recommend_schedule));
    builder.setView(pView);
    builder.setCancelable(false);
    final AlertDialog dialog = builder.create();
    
    if (!dialog.isShowing())
      dialog.show();
    
    Log.i("RecommendScheduleActivity.java | findAndShowDialog", );
    
    new Handler().postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        dialog.dismiss();
        Intent i = new Intent(RecommendScheduleActivity.this, RecommendScheduleCompeletedActivity.class);
        startActivity(i);
      }
    }, 5000);
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
        if (day == 6)
          return;
        
        day++;
      }
      else
      {
        if (day == 1)
          return;
        
        day--;
      }
      
      textDay.setText(Integer.toString(day));
    }
  };
  
  private OnClickListener typeClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      type = "test";
      containerShopping.setSelected(false);
      containerCulture.setSelected(false);
      containerTour.setSelected(false);
      containerFood.setSelected(false);
      containerRest.setSelected(false);
      containerRandom.setSelected(false);
      
      if (v.getId() == containerShopping.getId())
      {
        containerShopping.setSelected(true);
      }
      else if (v.getId() == containerCulture.getId())
      {
        containerCulture.setSelected(true);
      }
      else if (v.getId() == containerTour.getId())
      {
        containerTour.setSelected(true);
      }
      else if (v.getId() == containerFood.getId())
      {
        containerFood.setSelected(true);
      }
      else if (v.getId() == containerRest.getId())
      {
        containerRest.setSelected(true);
      }
      else if (v.getId() == containerRandom.getId())
      {
        containerRandom.setSelected(true);
      }
    }
  };
  
  /**************************************************
   * async task
   ***************************************************/
  private class FindAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    AlertDialog dialog;
    
    
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
      
      Log.i("RecommendScheduleActivity.java | onPreExecute", "|" + year + "|" + month + "|" + dayOfMonth + "|" + day + "일 짜리|" + type + "|");
      super.onPreExecute();
    }
    
    
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.getRecommendSchedule(type, day, year, month, dayOfMonth);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      
      dialog.dismiss();
      Intent i = new Intent(RecommendScheduleActivity.this, RecommendScheduleCompeletedActivity.class);
      startActivity(i);
      super.onPostExecute(result);
    }
  }
}
