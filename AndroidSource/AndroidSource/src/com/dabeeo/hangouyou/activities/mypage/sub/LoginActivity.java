package com.dabeeo.hangouyou.activities.mypage.sub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.FindPasswordActivity;
import com.dabeeo.hangouyou.activities.ticket.TicketActivity;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class LoginActivity extends Activity
{
  private EditText editEmail, editPassword;
  private LoginBottomAlertView alertView;
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
      super.onPostExecute(result);
    }
  }
}
