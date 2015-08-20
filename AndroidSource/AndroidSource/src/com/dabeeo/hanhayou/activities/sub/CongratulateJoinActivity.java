package com.dabeeo.hanhayou.activities.sub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mypage.LoginActivity;

public class CongratulateJoinActivity extends Activity
{
	private Button btnLogin, btnHome;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_congratulate_join);
		
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnHome = (Button) findViewById(R.id.btn_home);
		
		btnLogin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		btnHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			  if(LoginActivity.loginActivity != null)
			    LoginActivity.loginActivity.finish();
				finish();
			}
		});
	}
	
}
