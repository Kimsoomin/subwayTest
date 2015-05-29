package com.dabeeo.hangouyou.activities.sub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mypage.sub.LoginActivity;

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
				Intent i = new Intent(CongratulateJoinActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
		btnHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(CongratulateJoinActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
}
