package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class AccountSettingActivity extends ActionBarActivity
{
  private TextView textEmail;
  private EditText editName;
  private LinearLayout changePasswordContainer, withDrawContainer;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_setting);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_profile));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    textEmail = (TextView) findViewById(R.id.text_email);
    editName = (EditText) findViewById(R.id.edit_name);
    changePasswordContainer = (LinearLayout) findViewById(R.id.container_change_password);
    withDrawContainer = (LinearLayout) findViewById(R.id.container_withdraw);
    
    changePasswordContainer.setOnClickListener(menuClickListener);
    withDrawContainer.setOnClickListener(menuClickListener);
  }
  
  private OnClickListener menuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == changePasswordContainer.getId())
      {
        Intent i = new Intent(AccountSettingActivity.this, FindPasswordActivity.class);
        startActivity(i);
      }
      else
      {
        
      }
    }
  };
  
  
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
      finish();
    return super.onOptionsItemSelected(item);
  }
}
