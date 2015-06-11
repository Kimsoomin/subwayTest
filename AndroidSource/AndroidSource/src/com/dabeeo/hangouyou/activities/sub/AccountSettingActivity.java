package com.dabeeo.hangouyou.activities.sub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.ChangePasswordActivity;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;
import com.dabeeo.hangouyou.utils.SystemUtil;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

@SuppressWarnings("deprecation")
public class AccountSettingActivity extends ActionBarActivity
{
  private TextView textEmail;
  private EditText editName;
  private Button checkDuplicatedBtn;
  private LinearLayout changePasswordContainer, withDrawContainer;
  private LoginBottomAlertView alertView;
  
  public String userName;
  public RelativeLayout progressLayout;
  public ApiClient apiClient;
  public boolean isValidName = false;
  
  private PreferenceManager preferenceManager;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_setting);
    
    apiClient = new ApiClient(this);
    preferenceManager = PreferenceManager.getInstance(getApplicationContext());
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_profile));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    
    textEmail = (TextView) findViewById(R.id.text_email);
    textEmail.setText(preferenceManager.getUserEmail());
    editName = (EditText) findViewById(R.id.edit_name);
    editName.setText(preferenceManager.getUserName());
    checkDuplicatedBtn = (Button) findViewById(R.id.btn_check_duplicate_name);
    changePasswordContainer = (LinearLayout) findViewById(R.id.container_change_password);
    withDrawContainer = (LinearLayout) findViewById(R.id.container_withdraw);
    
    checkDuplicatedBtn.setOnClickListener(duplicatedChcek);
    changePasswordContainer.setOnClickListener(menuClickListener);
    withDrawContainer.setOnClickListener(menuClickListener);
  }
  
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v)
    {
      if (v.getId() == changePasswordContainer.getId())
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(AccountSettingActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        Intent i = new Intent(AccountSettingActivity.this, ChangePasswordActivity.class);
        startActivity(i);
      }
      else
      {
        Builder dialog = new AlertDialog.Builder(AccountSettingActivity.this);
        dialog.setTitle(getString(R.string.term_alert));
        View view = LayoutInflater.from(AccountSettingActivity.this).inflate(R.layout.view_withdraw_popup, null);
        TextView textQQ = (TextView) view.findViewById(R.id.text_withdraw_qq);
        textQQ.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            if (!isAppInstalled("com.tencent.mobileqq"))
            {
              String url = "https://play.google.com/store/apps/details?id=com.tencent.mobileqq";
              Intent i = new Intent(Intent.ACTION_VIEW);
              i.setData(Uri.parse(url));
              startActivity(i);
            }
            else
            {
              String url = "mqq://";
              Intent i = new Intent(Intent.ACTION_VIEW);
              i.setData(Uri.parse(url));
              startActivity(i);
            }
          }
        });
        dialog.setView(view);
        dialog.setPositiveButton(android.R.string.ok, null);
        dialog.show();
      }
    }
  };
  
  private OnClickListener duplicatedChcek = new OnClickListener()
  {
    
    @Override
    public void onClick(View v)
    {
      userName = editName.getText().toString();
      if (TextUtils.isEmpty(userName))
      {
        alertView.setAlert(getString(R.string.msg_please_write_name));
        return;
      }
      
      if (validCheckName(userName))
      {
        new checkDuplicatedNamelTask().execute();
      }
      else
        alertView.setAlert(getString(R.string.msg_please_valid_check_name));
    }
  };
  
  
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
  
  
  private boolean isAppInstalled(String packageName)
  {
    try
    {
      getPackageManager().getApplicationInfo(packageName, 0);
      return true;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      return false;
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_save, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  public String responseParser(String response)
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
    return status;
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
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    else if (item.getItemId() == R.id.save)
    {
      if (!isValidName)
      {
        alertView.setAlert(getString(R.string.msg_check_duplicate_name));
        return true;
      }
      else
        new UserNameModifyTask().execute();
    }
    return super.onOptionsItemSelected(item);
  }
  
  private class UserNameModifyTask extends AsyncTask<Void, Void, NetworkResult>
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
      return apiClient.userNameModify(preferenceManager.getUserSeq(), userName);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      progressLayout.setVisibility(View.GONE);
      String status = responseParser(result.response);
      if (status.equals("OK"))
      {
        CreateAlert(getString(R.string.msg_compete_modify_name));
      }
      else
      {
        CreateAlert(getString(R.string.msg_please_check_user_info));
      }
      super.onPostExecute(result);
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
      String status = responseParser(result.response);
      if (status.equals("OK"))
      {
        isValidName = true;
        alertView.setAlert(getString(R.string.msg_possibile_name_account));
      }
      else
      {
        alertView.setAlert(getString(R.string.msg_duplicate_name));
      }
    }
  }
}
