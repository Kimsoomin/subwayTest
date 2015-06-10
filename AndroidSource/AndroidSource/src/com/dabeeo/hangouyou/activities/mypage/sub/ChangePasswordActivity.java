package com.dabeeo.hangouyou.activities.mypage.sub;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

@SuppressWarnings("deprecation")
public class ChangePasswordActivity extends ActionBarActivity
{
  private EditText editCurrentPassword, editNewPassword, editNewPasswordRe;
  private LoginBottomAlertView alertView;
  public RelativeLayout progressLayout;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    
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
      if (TextUtils.isEmpty(editCurrentPassword.getText().toString()))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return true;
      }
      
      if (TextUtils.isEmpty(editNewPassword.getText().toString()) || TextUtils.isEmpty(editNewPasswordRe.getText().toString()))
      {
        alertView.setAlert(getString(R.string.msg_please_write_password));
        return true;
      }
      
      if (editNewPassword.length() > 12 || 6 > editNewPassword.length())
      {
        alertView.setAlert(getString(R.string.msg_warn_password_length));
        return true;
      }
      
      if (!editNewPassword.getText().toString().equals(editNewPasswordRe.getText().toString()))
      {
        Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_warn_password_not_same), Toast.LENGTH_LONG).show();
        return true;
      }
      
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
