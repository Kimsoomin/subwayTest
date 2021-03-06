package com.dabeeo.hanhayou.activities.mypage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.CongratulateJoinActivity;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.map.BlinkingCommon;

public class AuthEmailActivity extends Activity implements OnClickListener
{
  private EditText editText;
  private Button btnReSend;
  private Button btnAuth;
  private Button btnAuthCancel;
  private RelativeLayout progressLayout;
  
  private ApiClient apiClient;
  
  public Context mContext;
  
  private String email;
  private String passWord;
  private String name;
  private String phoneNum;
  private String mailFemail;
  private String dateOfbirth;
  private String allowReceiveMail;
  private String allowReceiveSms;
  private int authKey;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_email);
    
    mContext = this;
    apiClient = new ApiClient(mContext);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    
    btnAuth = (Button) findViewById(R.id.btn_auth);
    btnAuth.setOnClickListener(this);
    btnAuthCancel = (Button) findViewById(R.id.btn_auth_cancel);
    btnAuthCancel.setOnClickListener(this);
    btnReSend = (Button) findViewById(R.id.btn_re_send);
    btnReSend.setOnClickListener(this);
    editText = (EditText) findViewById(R.id.edit_text);
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
        if (editText.getText().toString().length() > 1)
        {
          btnAuth.setEnabled(true);
          btnAuth.setActivated(true);
        }
        else
        {
          btnAuth.setEnabled(false);
          btnAuth.setActivated(false);
        }
      }
    };
    editText.addTextChangedListener(watcher);
    
    getUserInfo();
  }
  
  
  @Override
  public void onClick(View v)
  {
    switch (v.getId())
    {
      case R.id.btn_auth:
        if(editText.getText().toString().equals(""+authKey))
          new UserJoinAsyncTask().execute();
        else
          CreateAlert(getString(R.string.msg_not_correct_email_auth), false);
        break;
        
      case R.id.btn_auth_cancel:
        finish();
        break;
        
      case R.id.btn_re_send:
        new emailKeyResendTask().execute();
        break;
    }
  }
  
  
  public void getUserInfo()
  {
    Intent intent = getIntent();    
    email = intent.getStringExtra("email");
    passWord = intent.getStringExtra("passWord");
    name = intent.getStringExtra("name");
    phoneNum = intent.getStringExtra("phoneNum");
    mailFemail = intent.getStringExtra("mailFemail");
    dateOfbirth = intent.getStringExtra("dateOfbirth");
    allowReceiveMail = intent.getStringExtra("allowReceiveMail");
    allowReceiveSms = intent.getStringExtra("allowReceiveSms");
    authKey = intent.getIntExtra("authKey", 0);
  }
  
  
  public void CreateAlert(String message, final boolean isSuccess)
  {
    AlertDialog.Builder ab = new AlertDialog.Builder(this);
    ab.setTitle(R.string.term_alert);
    ab.setPositiveButton(R.string.term_ok, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
        if (isSuccess)
        {
          finish();
          startActivity(new Intent(mContext, CongratulateJoinActivity.class));
        }
      }
    });
    
    ab.setMessage(message);
    ab.show();
  }
  
  private class emailKeyResendTask extends AsyncTask<Void, Void, NetworkResult>
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
      return apiClient.userEmailKeyResend(email);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      String status = null;
      String message = null;
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
          message = jsonObject.getString("message");
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
      if (status.equals("OK"))
      {
        CreateAlert(getString(R.string.msg_resend_emil_auth), false);
        JSONObject resultResponse;
        try
        {
          resultResponse = new JSONObject(result.response);
          if (resultResponse.has("key"))
          {
            authKey = resultResponse.getInt("key");
          }
        }
        catch (JSONException e)
        {
          BlinkingCommon.smlLibDebug("AuthEmail", " e :" + e);
        }
        
      }
      else
      {
        if (status.equals("ERROR_EMAIL"))
          CreateAlert(getString(R.string.msg_send_email_fail), false);
        else
          CreateAlert(message, false);
      }
      super.onPostExecute(result);
    }
    
  }
  
  private class UserJoinAsyncTask extends AsyncTask<Void, Void, NetworkResult>
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
      return apiClient.userJoin(email, passWord, name, phoneNum, mailFemail, dateOfbirth, allowReceiveMail, allowReceiveSms);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      String status = null;
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
      if (status.equals("OK"))
      {
        CreateAlert(getString(R.string.msg_complete_auth), true);
      }
      else
      {
        CreateAlert(getString(R.string.msg_general_error), false);
      }
      
      super.onPostExecute(result);
    }
  }
}
