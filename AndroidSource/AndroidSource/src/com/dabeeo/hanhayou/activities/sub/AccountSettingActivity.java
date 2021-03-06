package com.dabeeo.hanhayou.activities.sub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.ChangePasswordActivity;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;
import com.dabeeo.hanhayou.views.LoginBottomAlertView;

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
  private ImageView typingCancel;
  
  private ScrollView accountScroll;
  
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
    
    accountScroll = (ScrollView) findViewById(R.id.account_scroll);
    accountScroll.setOnTouchListener(new OnTouchListener()
    {
      
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        typingCancel.setVisibility(View.INVISIBLE);
        return false;
      }
    });
    
    typingCancel = (ImageView) findViewById(R.id.image_name_typing_cancel);
    progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    
    textEmail = (TextView) findViewById(R.id.text_email);
    textEmail.setText(preferenceManager.getUserEmail());
    editName = (EditText) findViewById(R.id.edit_name);
    checkDuplicatedBtn = (Button) findViewById(R.id.btn_check_duplicate_name);
    changePasswordContainer = (LinearLayout) findViewById(R.id.container_change_password);
    withDrawContainer = (LinearLayout) findViewById(R.id.container_withdraw);
    
    typingCancel.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        editName.setText("");
      }
    });
    
    editName.setOnFocusChangeListener(new OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(View v, boolean hasFocus)
      {
        if (!hasFocus)
          typingCancel.setVisibility(View.GONE);
        else
        {
          if (editName.getText().toString().length() > 0)
            typingCancel.setVisibility(View.VISIBLE);
          else
            typingCancel.setVisibility(View.GONE);
        }
      }
    });
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void afterTextChanged(Editable s)
      {
        if (s.length() > 0)
          typingCancel.setVisibility(View.VISIBLE);
        else
          typingCancel.setVisibility(View.GONE);
      }
    };
    editName.addTextChangedListener(watcher);
    editName.setOnEditorActionListener(new OnEditorActionListener()
    {
      
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
      {
        typingCancel.setVisibility(View.VISIBLE);
        return false;
      }
    });
    
    checkDuplicatedBtn.setOnClickListener(duplicatedChcek);
    changePasswordContainer.setOnClickListener(menuClickListener);
    withDrawContainer.setOnClickListener(menuClickListener);
  }
  
  
  @Override
  public void onBackPressed()
  {
    typingCancel.setVisibility(View.INVISIBLE);
    super.onBackPressed();
  }
  
  
  @Override
  protected void onResume()
  {
    super.onResume();
    editName.setText(preferenceManager.getUserName());
    typingCancel.setVisibility(View.GONE);
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
        textQQ.setText(underLineString(textQQ.getText().toString()));
        
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
      if (!SystemUtil.isConnectNetwork(AccountSettingActivity.this))
      {
        new AlertDialogManager(AccountSettingActivity.this).showDontNetworkConnectDialog();
        return;
      }
      
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
  
  
  public SpannableString underLineString(String text)
  {
    SpannableString spanString = new SpannableString(text);
    spanString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
    return spanString;
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
        preferenceManager.setUserName(editName.getText().toString());
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
