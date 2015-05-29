package com.dabeeo.hangouyou.activities.mypage.sub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.FindPasswordActivity;
import com.dabeeo.hangouyou.activities.ticket.TicketActivity;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class LoginActivity extends Activity
{
  private Button btnCancel;
  private EditText editEmail, editPassword;
  private LoginBottomAlertView alertView;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    
    editEmail = (EditText) findViewById(R.id.edit_email);
    editPassword = (EditText) findViewById(R.id.edit_password);
    alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
    
    findViewById(R.id.btn_join).setOnClickListener(clickListener);
    findViewById(R.id.btn_login).setOnClickListener(clickListener);
    findViewById(R.id.btn_find_password).setOnClickListener(clickListener);
    findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
    
    Toast.makeText(this, "화면 확인을 위해 아이디, 비밀번호 입력 후 로그인을 누르면 티켓화면으로 이동합니다.", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(LoginActivity.this, TicketActivity.class)); // TODO 원래꺼로 바꾸기
        finish();
      }
      else if (v.getId() == R.id.btn_find_password)
        startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
      else if (v.getId() == R.id.btn_join)
        startActivity(new Intent(LoginActivity.this, JoinActivity.class));
      else if (v.getId() == R.id.btn_cancel)
        finish();
    }
  };
}
