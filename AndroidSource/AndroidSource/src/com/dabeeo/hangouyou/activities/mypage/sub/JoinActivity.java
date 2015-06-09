package com.dabeeo.hangouyou.activities.mypage.sub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.AgreementActivity;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class JoinActivity extends Activity implements OnFocusChangeListener
{
  private Button btnDateOfbirth;
  private Calendar calendar;
  private Date birthDay = null;
  private Button btnGenderMale, btnGenderFemale;
  private boolean isMale = true;
  
  private CheckBox checkAllowReceiveMail, checkAllowReceivePhone;
  private EditText editEmail, editName, editPassword, editPasswordRe, editPhoneNumber;
  private Button btnCheckDuplicateEmail, btnCheckDuplicateName;
  private LoginBottomAlertView alertView;
  private CheckBox checkAgreement, checkPrivateAgreement, checkGPSAgreement;
  
  private TextView textAgree, textPrivateAgree, textGpsAgree;
  
  private ApiClient apiClient;
  
  public boolean isValidEmail = false;
  public boolean isValidName = false;
  public boolean isValidPassword = false;
  
  public String email;
  public String name;
  public String phoneNum;
  public String passWord;
  public String passWordRe;
  public String mailFemail;
  
  private RelativeLayout progressLayout;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join);
    
    apiClient = new ApiClient(this);
    
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
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    btnGenderMale = (Button) findViewById(R.id.radio_gender_male);
    btnGenderFemale = (Button) findViewById(R.id.radio_gender_female);
    btnGenderMale.setActivated(true);
    btnGenderMale.setOnClickListener(genderBtnClickListener);
    btnGenderFemale.setOnClickListener(genderBtnClickListener);
    
    textAgree = (TextView) findViewById(R.id.text_agree_more);
    textPrivateAgree = (TextView) findViewById(R.id.text_private_agree_more);
    textGpsAgree = (TextView) findViewById(R.id.text_gps_agree_more);
    textAgree.setOnClickListener(agreeClickListener);
    textPrivateAgree.setOnClickListener(agreeClickListener);
    textGpsAgree.setOnClickListener(agreeClickListener);
    
    checkAgreement = (CheckBox) findViewById(R.id.chk_agreement);
    checkPrivateAgreement = (CheckBox) findViewById(R.id.chk_private_agreement);
    checkGPSAgreement = (CheckBox) findViewById(R.id.chk_gps_agreement);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    
    checkAllowReceiveMail = (CheckBox) findViewById(R.id.checkbox_allow_receive_ads);
    checkAllowReceivePhone = (CheckBox) findViewById(R.id.checkbox_allow_receive_phone);
    editEmail = (EditText) findViewById(R.id.edit_email);
    editName = (EditText) findViewById(R.id.edit_name);
    editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
    editPassword = (EditText) findViewById(R.id.edit_password);
    editPasswordRe = (EditText) findViewById(R.id.edit_re_password);
    btnCheckDuplicateEmail = (Button) findViewById(R.id.btn_check_duplicate_email);
    btnCheckDuplicateEmail.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (TextUtils.isEmpty(editEmail.getText().toString()))
        {
          alertView.setAlert(getString(R.string.msg_please_write_email));
          return;
        }
        
        if(validCheckEmail(editEmail.getText().toString()))
        {
          new checkDuplicatedEmailTask().execute();
        }
        else
          alertView.setAlert(getString(R.string.msg_please_vaild_check_email));
      }
    });
    btnCheckDuplicateName = (Button) findViewById(R.id.btn_check_duplicate_name);
    btnCheckDuplicateName.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        if (TextUtils.isEmpty(editName.getText().toString()))
        {
          alertView.setAlert(getString(R.string.msg_please_write_name));
          return;
        }
                
        if (validCheckName(editName.getText().toString()))
        {
          new checkDuplicatedNamelTask().execute();
        }else
          alertView.setAlert(getString(R.string.msg_please_valid_check_name));
      }
    });
    
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
  
  private OnClickListener agreeClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == textAgree.getId())
      {
        Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT);
        startActivity(i);
      }
      else if (v.getId() == textPrivateAgree.getId())
      {
        Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_PRIVATE);
        startActivity(i);
      }
      else
      {
        Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_GPS);
        startActivity(i);
      }
    }
  };
  
  private OnClickListener joinClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      email = editEmail.getText().toString();
      name = editName.getText().toString();
      phoneNum = editPhoneNumber.getText().toString();
      passWord = editPassword.getText().toString();
      passWordRe = editPasswordRe.getText().toString();
      mailFemail = null;
      
      if(TextUtils.isEmpty(email))
      {
        alertView.setAlert(getString(R.string.msg_please_write_email));
        return;
      }else
      {
        if(validCheckEmail(email))
        {
          if(!isValidEmail)
          {
            alertView.setAlert(getString(R.string.msg_check_duplicate_email));
            return;
          }
        }else
        {
          alertView.setAlert(getString(R.string.msg_please_vaild_check_email));
          return;
        }
      }
      
      if(TextUtils.isEmpty(name))
      {
        alertView.setAlert(getString(R.string.msg_please_write_name));
        return;
      }else
      {
        if(validCheckName(name))
        {
          if(!isValidName)
          {
            alertView.setAlert(getString(R.string.msg_check_duplicate_name));
            return;
          }
        }else
        {
          alertView.setAlert(getString(R.string.msg_please_valid_check_name));
          return;
        }
      }
      
      if(TextUtils.isEmpty(phoneNum))
      {
        alertView.setAlert(getString(R.string.msg_please_write_phone_number));
        return;
      }
//      To-do phoneNumber validation check
//      else
//      {
//        
//      }
      
      if(TextUtils.isEmpty(passWord))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return;
      }else
      {
        if(passWord.length()<6 || passWord.length()>16)
        {
          alertView.setAlert(getString(R.string.msg_warn_password_length));
          return;
        }else
        {
          if(!validCheckPassword(passWord))
          {
            alertView.setAlert(getString(R.string.msg_please_valid_check_password));
            return;
          }
        }
      }
      
      if(TextUtils.isEmpty(passWordRe))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return;
      }else
      {
        if(!passWord.equals(passWordRe))
        {
          alertView.setAlert(getString(R.string.msg_warn_password_not_same));
          return;
        }
      }
      
      if(isMale == false)
      {
        mailFemail = "F";
      }else
      {
        mailFemail = "M";
      }
      
      if(birthDay == null)
      {
        alertView.setAlert(getString(R.string.msg_please_write_birthday));
        return;
      }
      
      if(checkAgreement.isChecked())
      {
        alertView.setAlert(getString(R.string.msg_check_agreement));
        return;
      }
      
      if(checkPrivateAgreement.isChecked())
      {
        alertView.setAlert(getString(R.string.msg_check_private_agreement));
        return;
      }
      
      if(checkGPSAgreement.isChecked())
      {
        alertView.setAlert(getString(R.string.msg_check_gps_agreement));
        return;
      }
      
      new joinHanhayouTask().execute();
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
      
      birthDay = calendar.getTime();
      
      updateLabel();
    }
    
  };
  
  @SuppressLint("SimpleDateFormat")
  private void updateLabel()
  {
    String myFormat = "yyyy-MM-dd";
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
  
  
  @Override
  public void onFocusChange(View v, boolean hasFocus)
  {
    // TODO Auto-generated method stub
    
  }
  
  public boolean validCheckEmail(String email)
  {    
    boolean validEmail = false;
    
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    CharSequence inputStr = email;
    
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inputStr);
    if (matcher.matches())
    {
      validEmail = true;
    }
    
    return validEmail;
  }
  
  public boolean validCheckName(String name)
  {
    boolean validName = false;
    
    String expression = "^[0-9a-zA-Z]*$";
    CharSequence inputStr = name;
    
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inputStr);
    if (matcher.matches()) 
    {
      validName = true;
    }
    
    return validName;
  }
  
  public boolean validCheckPassword(String password)
  {
    isValidPassword = false;
    
    String expression = "^[a-z0-9A-Z]*@#";
    CharSequence inputStr = password;
    
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inputStr);
    if (matcher.matches()) 
    {
      isValidName = true;
    }
    
    return isValidPassword;
  }
  
  /*
   * AsyncTask
   */  
  private class checkDuplicatedEmailTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      progressLayout.bringToFront();
      progressLayout.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      // TODO Auto-generated method stub
      return apiClient.userIdDuplicateCheck(editEmail.getText().toString());
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      progressLayout.setVisibility(View.GONE);
      if(!result.isSuccess)
      {
        alertView.setAlert(getString(R.string.msg_duplicate_email));
      }else
      {
        isValidEmail = true;
        alertView.setAlert(getString(R.string.msg_possibile_email_account));
      }
    }
  }
  
  private class checkDuplicatedNamelTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      progressLayout.bringToFront();
      progressLayout.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }
    
    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      // TODO Auto-generated method stub
      return apiClient.userNameDuplicateCheck(editName.getText().toString());
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      progressLayout.setVisibility(View.GONE);
      if(!result.isSuccess)
      {
        alertView.setAlert(getString(R.string.msg_duplicate_name));
      }else
      {
        isValidName = true;
        alertView.setAlert(getString(R.string.msg_possibile_name_account));
      }
    }
  }
  
  private class joinHanhayouTask extends AsyncTask<String, Void, NetworkResult>
  {
    
    @Override
    protected void onPreExecute()
    {
      progressLayout.bringToFront();
      progressLayout.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }

    @Override
    protected NetworkResult doInBackground(String... params)
    {
      String allowReceiveMail = null;
      String allowReceiveSms = null;
      
      if(checkAllowReceiveMail.isChecked())
      {
        allowReceiveMail = "1";
      }else
      {
        allowReceiveMail = "0";
      }
      
      if(checkAllowReceivePhone.isChecked())
      {
        allowReceiveSms = "1";
      }else
      {
        allowReceiveSms = "0";
      }
      
      return apiClient.userJoin(email, passWord, name, phoneNum, mailFemail, btnDateOfbirth.getText().toString(), 
                                allowReceiveMail, allowReceiveSms);
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      String userSeq = null;
      progressLayout.setVisibility(View.GONE);
      if(!result.isSuccess)
      {
        alertView.setAlert(getString(R.string.msg_please_check_user_info));
      }else
      {
        try
        {
          JSONObject resultResponse = new JSONObject(result.response);
          if(resultResponse.has("userSeq"))
          {
            userSeq = resultResponse.getString("userSeq");
          }
        }
        catch (JSONException e)
        {
          e.printStackTrace();
        }
        
        Intent intent = new Intent(JoinActivity.this, AuthEmailActivity.class);
        intent.putExtra("userSeq", userSeq);
        startActivity(intent);
      }
    }
    
  }
}
