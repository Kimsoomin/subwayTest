package com.dabeeo.hangouyou.activities.mypage.sub;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.FindPasswordActivity;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class LoginActivity extends Activity
{
  private EditText editEmail, editPassword;
  private LoginBottomAlertView alertView;
  
  public CheckBox autoLogin;
  public RelativeLayout progressLayout;
  public ApiClient apiClient;
  public Context mContext;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    
    mContext = this;
    apiClient = new ApiClient(mContext);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    editEmail = (EditText) findViewById(R.id.edit_email);
    editPassword = (EditText) findViewById(R.id.edit_password);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    autoLogin = (CheckBox) findViewById(R.id.checkbox_auto_login);
    
    findViewById(R.id.btn_join).setOnClickListener(clickListener);
    findViewById(R.id.btn_login).setOnClickListener(clickListener);
    findViewById(R.id.btn_find_password).setOnClickListener(clickListener);
    findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
    
//    Toast.makeText(this, "화면 확인을 위해 아이디, 비밀번호 입력 후 로그인을 누르면 티켓화면으로 이동합니다.", Toast.LENGTH_LONG).show();
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == R.id.btn_login)
      {
        if (TextUtils.isEmpty(editEmail.getText().toString()))
        {
          alertView.setAlert(getString(R.string.msg_please_write_email));
          return;
        }
        
        if (TextUtils.isEmpty(editPassword.getText().toString()))
        {
          alertView.setAlert(getString(R.string.msg_please_write_password));
          return;
        }
        
        new userLoginTask().execute();
        
      }
      else if (v.getId() == R.id.btn_find_password)
        startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
      else if (v.getId() == R.id.btn_join)
        startActivity(new Intent(LoginActivity.this, JoinActivity.class));
      else if (v.getId() == R.id.btn_cancel)
        finish();
    }
  };
  
  public void responsParser(String response)
  {
    String status = null;
    JSONObject jsonObject;
    try
    {
      jsonObject = new JSONObject(response);
      if (jsonObject.has("status"))
      {
        status = jsonObject.getString("status");
      }
      
      if(status.equals("OK"))
      {
        String userSeq = "";
        String userEmail = "";
        String userName = "";
        String gender = "";
        String profile = "";
        
        if(jsonObject.has("userSeq"))
          userSeq = jsonObject.getString("userSeq");
        if(jsonObject.has("userEmail"))
          userEmail = jsonObject.getString("userEmail");
        if(jsonObject.has("userName"))
          userName = jsonObject.getString("userName");
        if(jsonObject.has("gender"))
          gender = jsonObject.getString("gender");
        if(jsonObject.has("profile"))
          profile = jsonObject.getString("profile");
        //autologin checked
        if(autoLogin.isChecked())
        {
          PreferenceManager.getInstance(mContext).setUserSeq(userSeq);
          PreferenceManager.getInstance(mContext).setUserEmail(userEmail);
          PreferenceManager.getInstance(mContext).setUserName(userName);
          PreferenceManager.getInstance(mContext).setUserGender(gender);
          PreferenceManager.getInstance(mContext).setUserProfile(profile);
        }else //autologin unchecked 상태 시 userSeq 메모리에 저장
        {
          Global.g_strUserSeq = userSeq;
          Global.g_strUserEmail = userEmail;
          Global.g_strUserName = userName;
          Global.g_strGender = gender;
          Global.g_strProfile = profile;
        }
        finish();
      }else 
      {
        String alertMessage = "";
        if(status.equals("ERROR_ID"))
        {
          alertMessage = getString(R.string.msg_please_error_id);
        }else if(status.equals("ERROR_AUTH"))
        {
          alertMessage = getString(R.string.msg_please_error_auth);
        }else if(status.equals("ERROR_OUT"))
        {
          alertMessage = getString(R.string.msg_please_error_out);
        }else if(status.equals("ERROR_PW"))
        {
          alertMessage = getString(R.string.msg_please_error_password);
        }else
        {
          alertMessage = getString(R.string.msg_please_check_user_info);
        }
        CreateAlert(alertMessage);
      }
    }
    catch (JSONException e)
    {
      CreateAlert(getString(R.string.msg_dont_connect_network));
      e.printStackTrace();
    }
  }
  
  public void CreateAlert(String message)
  {
    AlertDialog.Builder ab = new AlertDialog.Builder(this);
    ab.setTitle(R.string.term_alert);
    ab.setPositiveButton(R.string.term_ok,
        new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        dialog.dismiss();
      }
    });
    
    ab.setMessage(message);
    ab.show();
  }
  
  private class userLoginTask extends AsyncTask<Void, Void, NetworkResult>
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
      return apiClient.userLogin(editEmail.getText().toString(), editPassword.getText().toString());
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      responsParser(result.response);
      super.onPostExecute(result);
    }
  }
}
