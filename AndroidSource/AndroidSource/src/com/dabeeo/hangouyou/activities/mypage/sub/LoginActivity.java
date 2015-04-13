package com.dabeeo.hangouyou.activities.mypage.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dabeeo.hangouyou.R;

public class LoginActivity extends ActionBarActivity
{
  private Button btnJoin;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    
    btnJoin = (Button) findViewById(R.id.btn_join);
    btnJoin.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(LoginActivity.this, JoinActivity.class));
      }
    });
  }
}
