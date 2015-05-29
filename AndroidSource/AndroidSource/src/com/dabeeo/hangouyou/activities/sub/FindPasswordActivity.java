package com.dabeeo.hangouyou.activities.sub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class FindPasswordActivity extends Activity
{
  private EditText email;
  private Button btnGetTempPassword, btnCancel;
  private LoginBottomAlertView alertView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_password);
    
    email = (EditText) findViewById(R.id.email);
    btnGetTempPassword = (Button) findViewById(R.id.btn_get_temp_password);
    btnCancel = (Button) findViewById(R.id.btn_cancel);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    
    btnGetTempPassword.setOnClickListener(tempBtnClickListener);
    btnCancel.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        finish();
      }
    });
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
      }
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (email.getText().toString().length() > 1)
          btnGetTempPassword.setEnabled(true);
        else
          btnGetTempPassword.setEnabled(false);
      }
    };
    email.addTextChangedListener(watcher);
    btnGetTempPassword.setEnabled(false);
  }
  
  private OnClickListener tempBtnClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (email.getText().toString().equals("123456"))
      {
        Builder dialog = new AlertDialog.Builder(FindPasswordActivity.this);
        dialog.setTitle(getString(R.string.term_alert));
        dialog.setMessage(getString(R.string.msg_send_email));
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            finish();
          }
        });
        dialog.show();
      }
      else
        alertView.setAlert(getString(R.string.msg_send_email_fail));
      
    }
  };
}
