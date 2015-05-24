package com.dabeeo.hangouyou.activities.sub;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.managers.PreferenceManager;

public class PromotionActivity extends Activity
{
	private ImageView imageView;
	private Button btnShowDetail;
	private CheckBox checkBoxDontShowToday;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		
		imageView = (ImageView) findViewById(R.id.imageview);
		btnShowDetail = (Button) findViewById(R.id.btn_show_detail);
		checkBoxDontShowToday = (CheckBox) findViewById(R.id.checkbox);
		
		checkBoxDontShowToday.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
					PreferenceManager.getInstance(PromotionActivity.this).setDontShowPopupDate(formatter.format(date));
					finish();
				}
			}
		});
	}
}
