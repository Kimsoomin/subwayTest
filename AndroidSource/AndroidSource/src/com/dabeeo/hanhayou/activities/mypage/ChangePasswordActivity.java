package com.dabeeo.hanhayou.activities.mypage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.AlertDialogManager.AlertListener;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.views.LoginBottomAlertView;

public class ChangePasswordActivity extends ActionBarActivity
{
  private EditText editCurrentPassword, editNewPassword, editNewPasswordRe;
  private LoginBottomAlertView alertView;
  private RelativeLayout progressLayout;
  private ApiClient apiClient;
  
  private PreferenceManager preferenceManager;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    
    apiClient = new ApiClient(this);
    preferenceManager = PreferenceManager.getInstance(getApplicationContext());
    
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
      if (isValidPassword())
        new PasswordModifyTask().execute();
    }
    return super.onOptionsItemSelected(item);
  }
  
  
  public boolean isValidPassword()
  {
    String currentPassword = editCurrentPassword.getText().toString();
    String newPassword = editNewPassword.getText().toString();
    String newPasswordRe = editNewPasswordRe.getText().toString();
    
    if (TextUtils.isEmpty(currentPassword))
    {
      alertView.setAlert(getString(R.string.msg_please_write_password));
      return false;
    }
    
    if (TextUtils.isEmpty(newPassword))
    {
      alertView.setAlert(getString(R.string.msg_please_write_new_password));
      return false;
    }
    else
    {
      if (currentPassword.equals(newPassword))
      {
        alertView.setAlert(getString(R.string.msg_warn_password_dont_same_original_password));
        return false;
      }
      
      if (newPassword.length() < 6 || newPassword.length() > 16)
      {
        alertView.setAlert(getString(R.string.msg_warn_password_length));
        return false;
      }
      else
      {
        // 알파벳, 숫자, 몇몇의 특수문자만 가능
        boolean chk = Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$", newPassword);
        if (!chk)
        {
          alertView.setAlert(getString(R.string.msg_warn_password_only_english_and_number));
          return false;
        }
        
        //영문과 숫자 혼용되어야 함
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(newPassword);
        boolean isContainNumber = matcher.find();
        
        pattern = Pattern.compile("[a-zA-Z]");
        matcher = pattern.matcher(newPassword);
        boolean isContainEnglish = matcher.find();
        
        if (!isContainNumber || !isContainEnglish)
        {
          alertView.setAlert(getString(R.string.msg_valid_password));
          return false;
        }
        
        //연속된 3개의 문자 숫자 
        if (checkForAscendingOrDescendingPart(newPassword, 3))
        {
          alertView.setAlert(getString(R.string.msg_now_allow_consecutive_three_letters));
          return false;
        }
        
        // 붙어있는 3개의 동일한 문자/숫자 불가 
        for (int i = 0; i < newPassword.length(); i++)
        {
          String character = newPassword.charAt(i) + "";
          character = character + character + character;
          if (newPassword.indexOf(character) >= 0)
          {
            alertView.setAlert(getString(R.string.msg_now_allow_consecutive_three_letters));
            return false;
          }
        }
        
        if (newPassword.contains(" "))
        {
          Log.w("WARN", "Containe escape");
          alertView.setAlert(getString(R.string.msg_please_delete_escape));
          return false;
        }
        
        if (preferenceManager.getUserName().equals(newPassword))
        {
          alertView.setAlert(getString(R.string.msg_dont_same_name_and_password));
          return false;
        }
      }
    }
    
    if (TextUtils.isEmpty(newPasswordRe))
    {
      alertView.setAlert(getString(R.string.msg_please_write_new_password));
      return false;
    }
    else
    {
      if (!newPassword.equals(newPasswordRe.toString()))
      {
        alertView.setAlert(getString(R.string.msg_warn_password_not_same));
        return false;
      }
    }
    
    return true;
  }
  
  
  public boolean checkForAscendingOrDescendingPart(String txt, int l)
  {
    for (int i = 0; i <= txt.length() - l; ++i)
    {
      boolean success = true;
      char c = txt.charAt(i);
      for (int j = 1; j < l; ++j)
      {
        if (((char) c + j) != txt.charAt(i + j))
        {
          success = false;
          break;
        }
      }
      if (success)
        return true;
      
      success = true;
      
      for (int j = 1; j < l; ++j)
      {
        if (((char) c - j) != txt.charAt(i + j))
        {
          success = false;
          break;
        }
      }
      if (success)
        return true;
    }
    return false;
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
    
    if (status.equals("OK"))
    {
      new AlertDialogManager(ChangePasswordActivity.this).showAlertDialog(getString(R.string.term_alert), getString(R.string.msg_success_password_change), getString(R.string.term_ok), null,
          new AlertListener()
          {
            
            @Override
            public void onPositiveButtonClickListener()
            {
              finish();
            }
            
            
            @Override
            public void onNegativeButtonClickListener()
            {
              // TODO Auto-generated method stub
              
            }
          });
    }
    else if (status.equals("ERROR_PW"))
    {
      CreateAlert(getString(R.string.msg_please_error_old_password));
    }
    else
    {
      CreateAlert(getString(R.string.msg_fail_password_change));
    }
  }
  
  
  public void CreateAlert(String message)
  {
    AlertDialog.Builder ab = new AlertDialog.Builder(this);
    ab.setTitle(R.string.term_alert);
    ab.setPositiveButton(R.string.term_ok, new DialogInterface.OnClickListener()
    {
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
      return apiClient.userPasswordModify(preferenceManager.getUserSeq(), preferenceManager.getUserName(), editCurrentPassword.getText().toString(), editNewPassword.getText().toString());
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
