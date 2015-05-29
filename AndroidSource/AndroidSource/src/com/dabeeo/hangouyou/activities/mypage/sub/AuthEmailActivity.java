package com.dabeeo.hangouyou.activities.mypage.sub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.CongratulateJoinActivity;

public class AuthEmailActivity extends Activity
{
	private EditText editText;
	@SuppressWarnings("unused")
	private Button btnReSend;
	private Button btnAuth;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_email);
		
		btnAuth = (Button) findViewById(R.id.btn_auth);
		btnReSend = (Button) findViewById(R.id.btn_re_send);
		editText = (EditText) findViewById(R.id.edit_text);
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
				if (editText.getText().toString().length() > 1)
				{
					btnAuth.setEnabled(true);
					btnAuth.setActivated(true);
				}
				else
				{
					btnAuth.setEnabled(false);
					btnAuth.setActivated(false);
				}
			}
		};
		editText.addTextChangedListener(watcher);
		btnAuth.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (editText.getText().toString().length() == 0)
					return;
				
				if (editText.getText().toString().equals("123456"))
				{
					Builder dialog = new AlertDialog.Builder(AuthEmailActivity.this);
					dialog.setTitle(getString(R.string.term_alert));
					dialog.setMessage(getString(R.string.msg_complete_auth));
					dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Intent i = new Intent(AuthEmailActivity.this, CongratulateJoinActivity.class);
							startActivity(i);
							finish();
						}
					});
					dialog.show();
				}
				else
				{
					Builder dialog = new AlertDialog.Builder(AuthEmailActivity.this);
					dialog.setTitle(getString(R.string.term_alert));
					dialog.setMessage(getString(R.string.msg_not_correct_email_auth));
					dialog.setPositiveButton(android.R.string.ok, null);
					dialog.show();
				}
			}
		});
	}
}
