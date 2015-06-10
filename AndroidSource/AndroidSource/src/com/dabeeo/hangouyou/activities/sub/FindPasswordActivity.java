package com.dabeeo.hangouyou.activities.sub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class FindPasswordActivity extends Activity
{
  private EditText email;
  private Button btnGetTempPassword, btnCancel;
  private LoginBottomAlertView alertView;
  public RelativeLayout progressLayout;
  
  public ApiClient apiClient; 
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_password);
    
    apiClient = new ApiClient(this);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    email = (EditText) findViewById(R.id.email);
    btnGetTempPassword = (Button) findViewById(R.id.btn_get_temp_password);
    btnCancel = (Button) findViewById(R.id.btn_cancel);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    
    btnGetTempPassword.setOnClickListener(tempBtnClickListener);
    btnCancel.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        finish();
      }
    });
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (email.getText().toString().length() > 1)
          btnGetTempPassword.setEnabled(true);
        else
          btnGetTempPassword.setEnabled(false);
      }
    };
    email.addTextChangedListener(watcher);
    btnGetTempPassword.setEnabled(false);
  }
  
  private OnClickListener tempBtnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (validCheckEmail(email.getText().toString()))
      {
        new GetTempPasswordTask().execute();
      }
      else
      {
        alertView.setAlert(getString(R.string.msg_please_vaild_check_email));
      }      
    }
  };
  
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
  
  private class GetTempPasswordTask extends AsyncTask<Void, Void, NetworkResult>
  {
    @Override
    protected void onPreExecute()
    {
      progressLayout.setVisibility(View.VISIBLE);
      progressLayout.bringToFront();
      super.onPreExecute();
    }

    @Override
    protected NetworkResult doInBackground(Void... params)
    {
      return null;
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      //TODO response 처리 필요
      super.onPostExecute(result);
    }    
  }
}
