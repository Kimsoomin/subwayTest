package com.dabeeo.hanhayou.activities.mypage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.sub.FindPasswordActivity;
import com.dabeeo.hanhayou.controllers.NetworkBraodCastReceiver;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.LoginBottomAlertView;

public class LoginActivity extends Activity
{
  private EditText editEmail, editPassword;
  private LoginBottomAlertView alertView;
  
  public CheckBox autoLogin;
  public RelativeLayout progressLayout;
  public ApiClient apiClient;
  public Context mContext;
  public AlertDialogManager alertDialogManager;
  
  public String userSeq = "";
  public String userEmail = "";
  public String userName = "";
  public String gender = "";
  public String profile = "";
  
  private int mainActivityPosition = -1;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    
    mContext = this;
    apiClient = new ApiClient(mContext);
    alertDialogManager = new AlertDialogManager(LoginActivity.this);
    
    mainActivityPosition = getIntent().getIntExtra("mainFragmentPostion", -1);
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    editEmail = (EditText) findViewById(R.id.edit_email);
    editPassword = (EditText) findViewById(R.id.edit_password);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    autoLogin = (CheckBox) findViewById(R.id.checkbox_auto_login);
    autoLogin.setChecked(true);
    
    findViewById(R.id.btn_join).setOnClickListener(clickListener);
    findViewById(R.id.btn_login).setOnClickListener(clickListener);
    findViewById(R.id.btn_find_password).setOnClickListener(clickListener);
    findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
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
        
        if (!validCheckEmail(editEmail.getText().toString()))
        {
          alertView.setAlert(getString(R.string.msg_please_valid_check_email));
          return;
        }
        
        new UserLoginTask().execute();
        
      }
      else if (v.getId() == R.id.btn_find_password)
        startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
      else if (v.getId() == R.id.btn_join)
        startActivity(new Intent(LoginActivity.this, JoinActivity.class));
      else if (v.getId() == R.id.btn_cancel)
        finish();
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
      
      if (status.equals("OK"))
      {
        if (jsonObject.has("userSeq"))
          userSeq = jsonObject.getString("userSeq");
        if (jsonObject.has("userEmail"))
          userEmail = jsonObject.getString("userEmail");
        if (jsonObject.has("userName"))
          userName = jsonObject.getString("userName");
        if (jsonObject.has("gender"))
          gender = jsonObject.getString("gender");
        if (jsonObject.has("profile"))
          profile = jsonObject.getString("profile");
        //autologin checked
        PreferenceManager.getInstance(mContext).setUserSeq(userSeq);
        PreferenceManager.getInstance(mContext).setUserEmail(userEmail);
        PreferenceManager.getInstance(mContext).setUserName(userName);
        PreferenceManager.getInstance(mContext).setUserGender(gender);
        PreferenceManager.getInstance(mContext).setUserProfile(profile);
        PreferenceManager.getInstance(mContext).setIsAutoLogin(autoLogin.isChecked());
        
        new SyncMyPlanAndShceduleTask().execute();
      }
      else
      {
        String alertMessage = "";
        if (status.equals("ERROR_AUTH"))
        {
          if (jsonObject.has("userSeq"))
            userSeq = jsonObject.getString("userSeq");
          //getString(R.string.msg_please_error_auth)
          alertDialogManager.showAlertDialog(getString(R.string.term_alert), jsonObject.getString("message"), getString(R.string.term_ok), null, new AlertListener()
          {
            @Override
            public void onPositiveButtonClickListener()
            {
              Intent i = new Intent(LoginActivity.this, AuthEmailActivity.class);
              i.putExtra("email", editEmail.getText().toString());
              i.putExtra("userSeq", userSeq);
              startActivity(i);
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
              
            }
          });
        }
        else
        {
          if (status.equals("ERROR_ID"))
          {
            alertMessage = getString(R.string.msg_please_error_id);
          }
          
          else if (status.equals("ERROR_OUT"))
          {
            alertMessage = getString(R.string.msg_please_error_out);
          }
          else if (status.equals("ERROR_PW"))
          {
            alertMessage = getString(R.string.msg_please_error_password);
          }
          else
          {
            alertMessage = getString(R.string.msg_please_check_user_info);
          }
          alertDialogManager.showAlertDialog(getString(R.string.term_alert), alertMessage, getString(R.string.term_ok), null, null);
        }
      }
    }
    catch (JSONException e)
    {
      alertDialogManager.showAlertDialog(getString(R.string.term_alert), getString(R.string.msg_dont_connect_network), getString(R.string.term_ok), null, null);
      e.printStackTrace();
    }
  }
  
  private class UserLoginTask extends AsyncTask<Void, Void, NetworkResult>
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
  
  private class SyncMyPlanAndShceduleTask extends AsyncTask<Void, Void, NetworkResult>
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
      apiClient.getMyPlaceList();
      apiClient.getMyTravelSchedules(-1);
      return null;
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      sendBroadcast(new Intent(NetworkBraodCastReceiver.ACTION_LOGIN));
      if (mainActivityPosition != -1)
      {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("position", mainActivityPosition);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      }
      finish();
      super.onPostExecute(result);
    }
  }
}
