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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dabeeo.hangouyou.R;

public class JoinActivity extends Activity
{
  private Button btnDateOfbirth;
  private Calendar calendar;
  private Button btnGenderMale, btnGenderFemale;
  private boolean isMale = true;
  
  private CheckBox checkAllowReceiveMail, checkAllowReceivePhone;
  private EditText editEmail, editName, editPassword, editPasswordRe;
  private Button btnCheckDuplicateEmail, btnCheckDuplicateName;
  
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
    
    btnGenderMale = (Button) findViewById(R.id.radio_gender_male);
    btnGenderFemale = (Button) findViewById(R.id.radio_gender_female);
    btnGenderMale.setActivated(true);
    btnGenderMale.setOnClickListener(genderBtnClickListener);
    btnGenderFemale.setOnClickListener(genderBtnClickListener);
    
    checkAllowReceiveMail = (CheckBox) findViewById(R.id.checkbox_allow_receive_ads);
    checkAllowReceivePhone = (CheckBox) findViewById(R.id.checkbox_allow_receive_phone);
    editEmail = (EditText) findViewById(R.id.edit_email);
    editName = (EditText) findViewById(R.id.edit_name);
    editPassword = (EditText) findViewById(R.id.edit_password);
    editPasswordRe = (EditText) findViewById(R.id.edit_re_password);
    btnCheckDuplicateEmail = (Button) findViewById(R.id.btn_check_duplicate_email);
    btnCheckDuplicateName = (Button) findViewById(R.id.btn_check_duplicate_name);
    
    ((Button) findViewById(R.id.btn_join)).setOnClickListener(joinClickListener);
    ((Button) findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
  }
  
  private OnClickListener joinClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      startActivity(new Intent(JoinActivity.this, AuthEmailActivity.class));
    }
  };
  
  private OnClickListener genderBtnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (btnGenderMale.isActivated())
      {
        isMale = false;
        btnGenderMale.setActivated(false);
        btnGenderFemale.setActivated(true);
      }
      else
      {
        isMale = true;
        btnGenderMale.setActivated(true);
        btnGenderFemale.setActivated(false);
      }
    }
  };
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
