package com.dabeeo.hangouyou.activities.mypage.sub;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.dabeeo.hangouyou.R;

public class JoinActivity extends Activity
{
  private Button btnDateOfbirth;
  private Calendar calendar;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join);
    
    calendar = Calendar.getInstance();
    btnDateOfbirth = (Button) findViewById(R.id.btn_date);
    btnDateOfbirth.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        new DatePickerDialog(JoinActivity.this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
      }
    });
    
    ((Button) findViewById(R.id.btn_join)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        startActivity(new Intent(JoinActivity.this, AuthEmailActivity.class));
      }
    });
  }
  
  private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener()
  {
    
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, monthOfYear);
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      updateLabel();
    }
    
  };
  
  
  @SuppressLint("SimpleDateFormat")
  private void updateLabel()
  {
    String myFormat = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    
    btnDateOfbirth.setText(sdf.format(calendar.getTime()));
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
