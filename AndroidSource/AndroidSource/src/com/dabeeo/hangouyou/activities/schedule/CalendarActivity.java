package com.dabeeo.hangouyou.activities.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import com.dabeeo.hangouyou.R;

public class CalendarActivity extends ActionBarActivity
{
  private CalendarView calendar;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    calendar = (CalendarView) findViewById(R.id.calendarview);
    calendar.setOnDateChangeListener(new OnDateChangeListener()
    {
      @Override
      public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
      {
        Log.w("WARN", " " + year + " " + month + " " + dayOfMonth);
        Intent i = new Intent();
        i.putExtra("year", year);
        i.putExtra("month", month);
        i.putExtra("dayOfMonth", dayOfMonth);
        setResult(RESULT_OK, i);
        finish();
      }
    });
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
}
