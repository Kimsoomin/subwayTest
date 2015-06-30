package com.dabeeo.hanhayou.activities.schedule;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.external.library.calendar.CaldroidFragment;
import com.dabeeo.hanhayou.external.library.calendar.CaldroidListener;

public class CalendarActivity extends ActionBarActivity
{
  private CaldroidFragment calFragment;
  private Calendar selectedCalendar = Calendar.getInstance();
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_select_date));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    calFragment = new CaldroidFragment();
    calFragment.setMinDate(new Date());
    if (calFragment != null)
    {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.content, calFragment);
      ft.commit();
    }
    
    final CaldroidListener listener = new CaldroidListener()
    {
      @Override
      public void onSelectDate(Date date, View view)
      {
        selectedCalendar.setTime(date);
      }
      
      
      @Override
      public void onChangeMonth(int month, int year)
      {
      }
      
      
      @Override
      public void onLongClickDate(Date date, View view)
      {
      }
      
      
      @Override
      public void onCaldroidViewCreated()
      {
        
      }
      
    };
    
    calFragment.setCaldroidListener(listener);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_confirm, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else
    {
      Calendar calendar = Calendar.getInstance();
      
      if (DateUtils.isToday(selectedCalendar.getTimeInMillis()) || selectedCalendar.getTimeInMillis() > calendar.getTimeInMillis())
      {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        Intent i = new Intent();
        i.putExtra("year", year);
        i.putExtra("month", month);
        i.putExtra("dayOfMonth", dayOfMonth);
        setResult(RESULT_OK, i);
        finish();
      }
    }
    return super.onOptionsItemSelected(item);
  }
}
