package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class AccountSettingActivity extends Activity
{
  private TextView textEmail;
  private EditText editName;
  private LinearLayout changePasswordContainer, withDrawContainer;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_setting);
    
    textEmail = (TextView) findViewById(R.id.text_email);
    editName = (EditText) findViewById(R.id.edit_name);
    changePasswordContainer = (LinearLayout) findViewById(R.id.container_change_password);
    withDrawContainer = (LinearLayout) findViewById(R.id.container_withdraw);
    
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
        Intent i = new Intent(AccountSettingActivity.this, FindPasswordActivity.class);
        startActivity(i);
      }
      else
      {
        Builder dialog = new AlertDialog.Builder(AccountSettingActivity.this);
        dialog.setTitle(getString(R.string.app_name));
        View view = LayoutInflater.from(AccountSettingActivity.this).inflate(R.layout.view_withdraw_popup, null);
        TextView textQQ = (TextView) view.findViewById(R.id.text_withdraw_qq);
        textQQ.setText(Html.fromHtml("<strike>" + getString(R.string.msg_help_withdraw_qq) + "</strike>"));
        TextView textEmail = (TextView) view.findViewById(R.id.text_withdraw_eamil);
        textEmail.setText(Html.fromHtml("<strike>" + getString(R.string.msg_help_withdraw_email) + "</strike>"));
        textQQ.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View arg0)
          {
            String url = "mqq://";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
          }
        });
        dialog.setView(view);
        dialog.setPositiveButton(android.R.string.ok, null);
        dialog.show();
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
