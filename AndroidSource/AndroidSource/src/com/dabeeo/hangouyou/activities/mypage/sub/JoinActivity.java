package com.dabeeo.hangouyou.activities.mypage.sub;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.sub.AgreementActivity;
import com.dabeeo.hangouyou.views.LoginBottomAlertView;

public class JoinActivity extends Activity
{
	private Button btnDateOfbirth;
	private Calendar calendar;
	private Date birthDay;
	private Button btnGenderMale, btnGenderFemale;
	private boolean isMale = true;
	
	private CheckBox checkAllowReceiveMail, checkAllowReceivePhone;
	private EditText editEmail, editName, editPassword, editPasswordRe;
	private Button btnCheckDuplicateEmail, btnCheckDuplicateName;
	private LoginBottomAlertView alertView;
	private CheckBox checkAgreement, checkPrivateAgreement, checkGPSAgreement;
	private boolean isEmailChecked = false;
	private boolean isNameChecked = false;
	private TextView textAgree, textPrivateAgree, textGpsAgree;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		
		calendar = Calendar.getInstance();
		btnDateOfbirth = (Button) findViewById(R.id.btn_date);
		btnDateOfbirth.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new DatePickerDialog(JoinActivity.this, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		alertView = (LoginBottomAlertView) findViewById(R.id.alert_view);
		btnGenderMale = (Button) findViewById(R.id.radio_gender_male);
		btnGenderFemale = (Button) findViewById(R.id.radio_gender_female);
		btnGenderMale.setActivated(true);
		btnGenderMale.setOnClickListener(genderBtnClickListener);
		btnGenderFemale.setOnClickListener(genderBtnClickListener);
		
		textAgree = (TextView) findViewById(R.id.text_agree_more);
		textPrivateAgree = (TextView) findViewById(R.id.text_private_agree_more);
		textGpsAgree = (TextView) findViewById(R.id.text_gps_agree_more);
		textAgree.setOnClickListener(agreeClickListener);
		textPrivateAgree.setOnClickListener(agreeClickListener);
		textGpsAgree.setOnClickListener(agreeClickListener);
		
		checkAgreement = (CheckBox) findViewById(R.id.chk_agreement);
		checkPrivateAgreement = (CheckBox) findViewById(R.id.chk_private_agreement);
		checkGPSAgreement = (CheckBox) findViewById(R.id.chk_gps_agreement);
		
		checkAllowReceiveMail = (CheckBox) findViewById(R.id.checkbox_allow_receive_ads);
		checkAllowReceivePhone = (CheckBox) findViewById(R.id.checkbox_allow_receive_phone);
		editEmail = (EditText) findViewById(R.id.edit_email);
		editName = (EditText) findViewById(R.id.edit_name);
		editPassword = (EditText) findViewById(R.id.edit_password);
		editPasswordRe = (EditText) findViewById(R.id.edit_re_password);
		btnCheckDuplicateEmail = (Button) findViewById(R.id.btn_check_duplicate_email);
		btnCheckDuplicateName = (Button) findViewById(R.id.btn_check_duplicate_name);
		btnCheckDuplicateEmail.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (TextUtils.isEmpty(editEmail.getText().toString()))
					return;
				
				if (!isEmailChecked)
					alertView.setAlert(getString(R.string.msg_duplicate_email));
				else
					alertView.setAlert(getString(R.string.msg_possibile_email_account));
				
				isEmailChecked = true;
			}
		});
		btnCheckDuplicateName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (TextUtils.isEmpty(editName.getText().toString()))
					return;
				
				if (!isEmailChecked)
					alertView.setAlert(getString(R.string.msg_duplicate_name));
				else
					alertView.setAlert(getString(R.string.msg_possibile_name_account));
				isNameChecked = true;
			}
		});
		
		((Button) findViewById(R.id.btn_join)).setOnClickListener(joinClickListener);
		((Button) findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	private OnClickListener agreeClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == textAgree.getId())
			{
				Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
				i.putExtra("type", AgreementActivity.TYPE_AGREEMENT);
				startActivity(i);
			}
			else if (v.getId() == textPrivateAgree.getId())
			{
				Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
				i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_PRIVATE);
				startActivity(i);
			}
			else
			{
				Intent i = new Intent(JoinActivity.this, AgreementActivity.class);
				i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_GPS);
				startActivity(i);
			}
		}
	};
	private OnClickListener joinClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (TextUtils.isEmpty(editEmail.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_please_write_email));
				return;
			}
			if (TextUtils.isEmpty(editName.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_please_write_name));
				return;
			}
			if (TextUtils.isEmpty(editPassword.getText().toString()) || TextUtils.isEmpty(editPasswordRe.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_please_write_password));
				return;
			}
			if (!editPassword.getText().toString().equals(editPasswordRe.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_please_not_match_password));
				return;
			}
			if (editPassword.getText().toString().length() > 12 || 6 > editPassword.getText().toString().length())
			{
				alertView.setAlert(getString(R.string.msg_warn_password_length));
				return;
			}
			if (editName.getText().toString().equals(editPassword.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_dont_same_name_and_password));
				return;
			}
			if (editName.getText().toString().equals(editPassword.getText().toString()))
			{
				alertView.setAlert(getString(R.string.msg_dont_same_name_and_password));
				return;
			}
			if (!checkAgreement.isChecked())
			{
				alertView.setAlert(getString(R.string.msg_check_agreement));
				return;
			}
			if (!checkPrivateAgreement.isChecked())
			{
				alertView.setAlert(getString(R.string.msg_check_private_agreement));
				return;
			}
			if (!checkGPSAgreement.isChecked())
			{
				alertView.setAlert(getString(R.string.msg_check_gps_agreement));
				return;
			}
			if (!isEmailChecked)
			{
				alertView.setAlert(getString(R.string.msg_check_duplicate_email));
				return;
			}
			if (!isNameChecked)
			{
				alertView.setAlert(getString(R.string.msg_check_duplicate_name));
				return;
			}
			startActivity(new Intent(JoinActivity.this, AuthEmailActivity.class));
		}
	};
	
	private OnClickListener genderBtnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (btnGenderMale.isActivated())
			{
				isMale = false;
				btnGenderMale.setActivated(false);
				btnGenderFemale.setActivated(true);
			}
			else
			{
				isMale = true;
				btnGenderMale.setActivated(true);
				btnGenderFemale.setActivated(false);
			}
		}
	};
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener()
	{
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			updateLabel();
		}
		
	};
	
	
	@SuppressLint("SimpleDateFormat")
	private void updateLabel()
	{
		String myFormat = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
		
		btnDateOfbirth.setText(sdf.format(calendar.getTime()));
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
}
