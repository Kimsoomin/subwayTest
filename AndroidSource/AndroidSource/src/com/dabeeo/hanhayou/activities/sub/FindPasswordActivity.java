package com.dabeeo.hanhayou.activities.sub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.LoginBottomAlertView;

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
  }
  
  private OnClickListener tempBtnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (TextUtils.isEmpty(email.getText().toString()))
      {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
        builder.setTitle(R.string.term_alert);
        builder.setMessage(R.string.msg_please_write_email);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
        return;
      }
      if (validCheckEmail(email.getText().toString()))
        new GetTempPasswordTask().execute();
      else
        alertView.setAlert(getString(R.string.msg_please_valid_check_email));
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
      return apiClient.userTempPasswordGet(email.getText().toString());
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      //TODO response 처리 필요
      try
      {
        JSONObject obj = new JSONObject(result.response);
        if (obj.getString("status").equals("OK"))
        {
          AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
          builder.setTitle(R.string.term_alert);
          builder.setMessage(R.string.msg_send_temp_password);
          builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
              finish();
            }
          });
          builder.create().show();
        }
        else
        {
          AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
          builder.setTitle(R.string.term_alert);
          builder.setMessage(obj.getString("message"));
          builder.setPositiveButton(android.R.string.ok, null);
          builder.create().show();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      
      super.onPostExecute(result);
    }
  }
}
