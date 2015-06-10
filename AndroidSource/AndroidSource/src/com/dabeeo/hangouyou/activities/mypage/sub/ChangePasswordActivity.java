package com.dabeeo.hangouyou.activities.mypage.sub;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.CongratulateJoinActivity;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.map.Global;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

@SuppressWarnings("deprecation")
public class ChangePasswordActivity extends ActionBarActivity
{
  private EditText editCurrentPassword, editNewPassword, editNewPasswordRe;
  private LoginBottomAlertView alertView;
  public RelativeLayout progressLayout;
  public ApiClient apiClient;
  
  public String currentPassword = "";
  public String newPassword = "";
  public String newPasswordRe = "";
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    
    apiClient = new ApiClient(this);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_change_password));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    editCurrentPassword = (EditText) findViewById(R.id.edit_current_password);
    editNewPassword = (EditText) findViewById(R.id.edit_new_password);
    editNewPasswordRe = (EditText) findViewById(R.id.edit_new_password_re);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_save, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    else if (item.getItemId() == R.id.save)
    {
      currentPassword = editCurrentPassword.getText().toString();
      newPassword = editNewPassword.getText().toString();
      newPasswordRe = editNewPasswordRe.getText().toString();
      
      if (TextUtils.isEmpty(currentPassword))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return true;
      }
      
      if(currentPassword.length() < 6 || currentPassword.length() > 16)
      {
        alertView.setAlert(getString(R.string.msg_warn_password_length));
      }
      
      if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newPasswordRe))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return true;
      }
      
      if (newPassword.length() < 6 || newPassword.length() > 16)
      {
        alertView.setAlert(getString(R.string.msg_warn_password_length));
        return true;
      }
      
      if (!newPassword.equals(newPasswordRe.toString()))
      {
        alertView.setAlert(getString(R.string.msg_warn_password_not_same));
        return true;
      }
      
      new PasswordModifyTask().execute();
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
  
  public boolean validChcekPassword(String password)
  {
    //TODO password Valid Check 필요 (영문/숫자/특문/연속된 문자 3자 이상 사용불가/이름과 동일한 비번 사용불가)
    boolean isValidPassword = false;
    
    return isValidPassword;
  }
  
  public void responseParser(String response)
  {
    String status = null;
    try
    {
      JSONObject jsonObject = new JSONObject(response);
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
    
    if(status.equals("OK"))
    {
      finish();
    }else if(status.equals("ERROR_PW"))
    {
      CreateAlert(getString(R.string.msg_please_error_old_password));
    }else
    {
      CreateAlert(getString(R.string.msg_please_check_user_info));
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
  
  private class PasswordModifyTask extends AsyncTask<Void, Void, NetworkResult>
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
      //TODO userSeq, userName 값 가져오는 위치 확인 필요
      return apiClient.userPasswordModify(Global.g_strUserSeq, Global.g_strUserName, currentPassword, newPassword);
    }
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      responseParser(result.response);
      super.onPostExecute(result);
    }
    
  }
}
