package com.dabeeo.hangouyou.activities.schedule;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;

public class RecommendScheduleActivity extends ActionBarActivity
{
  private LinearLayout containerChoiceStartDate;
  private RadioGroup containerBottomViews;
  
  private Button btnDayUp, btnDayDown;
  private TextView textDay;
  private TextView startDate;
  private int day = 1;
  private String selectTheme = "";
  
  private ArrayList<View> bottomViews = new ArrayList<View>();
  
  private int year = -1, month = -1, dayOfMonth = -1;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommend_travel_schedule);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
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
    containerBottomViews = (RadioGroup) findViewById(R.id.container_travel_theme);
    btnDayUp = (Button) findViewById(R.id.date_up);
    btnDayDown = (Button) findViewById(R.id.date_down);
    textDay = (TextView) findViewById(R.id.text_day);
    
    btnDayUp.setOnClickListener(dayClickListener);
    btnDayDown.setOnClickListener(dayClickListener);
    
    displayBottomListView();
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
        startDate.setText(Integer.toString(year) + " " + Integer.toString(month) + " " + Integer.toString(dayOfMonth));
    }
    super.onActivityResult(arg0, arg1, intent);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_next, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  private void displayBottomListView()
  {
    RadioButton radioView = new RadioButton(this);
    radioView.setText("한류");
    containerBottomViews.addView(radioView);
    
    radioView = new RadioButton(this);
    radioView.setText("쇼핑");
    containerBottomViews.addView(radioView);
    
    radioView = new RadioButton(this);
    radioView.setText("명소");
    containerBottomViews.addView(radioView);
  }
  
  private OnClickListener bottomViewClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      Log.w("WARN", "Clicked View id : " + v.getId());
      for (int i = 0; i < bottomViews.size(); i++)
      {
        Log.w("WARN", "bottomViews View id : " + bottomViews.get(i).getId());
        if (v.getId() == bottomViews.get(i).getId())
          bottomViews.get(i).setActivated(true);
        else
          bottomViews.get(i).setActivated(false);
      }
    }
  };
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
      else
        findAndShowDialog();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  private void findAndShowDialog()
  {
    final ProgressDialog dialog = new ProgressDialog(RecommendScheduleActivity.this);
    dialog.setTitle(getString(R.string.app_name));
    dialog.setMessage(getString(R.string.msg_progress_recommend_schedule));
    dialog.setCancelable(false);
    
    if (!dialog.isShowing())
      dialog.show();
    
    //TODO : 네트워크를 통해 추천일정 받기 
    
    new Handler().postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        dialog.dismiss();
        Intent i = new Intent(RecommendScheduleActivity.this, RecommendScheduleCompeletedActivity.class);
        startActivity(i);
      }
    }, 1000);
  }
}
