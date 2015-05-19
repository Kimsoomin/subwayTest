package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class FindPasswordActivity extends Activity
{
  private EditText currentPassword, newPassword, newPasswordConfirm;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_password);
    
//    currentPassword = (EditText) findViewById(R.id.edit_current_password);
//    newPassword = (EditText) findViewById(R.id.edit_new_password);
//    newPasswordConfirm = (EditText) findViewById(R.id.edit_new_password_confirm);
    
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
      finish();
    return super.onOptionsItemSelected(item);
  }
}
