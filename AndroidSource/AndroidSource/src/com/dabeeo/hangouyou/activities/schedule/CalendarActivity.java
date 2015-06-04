package com.dabeeo.hangouyou.activities.schedule;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.external.library.calendar.CaldroidFragment;
import com.dabeeo.hangouyou.external.library.calendar.CaldroidListener;

@SuppressWarnings("deprecation")
public class CalendarActivity extends ActionBarActivity
{
	private CaldroidFragment calFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		@SuppressLint("InflateParams")
		View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
		TextView title = (TextView) customActionBar.findViewById(R.id.title);
		title.setText(getString(R.string.term_select_date));
		getSupportActionBar().setCustomView(customActionBar);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		calFragment = new CaldroidFragment();
		calFragment.setMinDate(new Date());
		if (calFragment != null)
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.content, calFragment);
			ft.commit();
		}
		
		final CaldroidListener listener = new CaldroidListener()
		{
			
			@Override
			public void onSelectDate(Date date, View view)
			{
				Calendar calendar = Calendar.getInstance();
				int cYear = calendar.get(Calendar.YEAR);
				int cMonth = calendar.get(Calendar.MONTH);
				int cDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				
				Calendar selectCalendar = Calendar.getInstance();
				selectCalendar.setTime(date);
				
				int year = selectCalendar.get(Calendar.YEAR);
				int month = selectCalendar.get(Calendar.MONTH);
				int dayOfMonth = selectCalendar.get(Calendar.DAY_OF_MONTH);
				
				Log.w("WARN", " " + year + " " + month + " " + dayOfMonth);
				if (year >= cYear && month >= cMonth && dayOfMonth >= cDayOfMonth)
				{
					Intent i = new Intent();
					i.putExtra("year", year);
					i.putExtra("month", month);
					i.putExtra("dayOfMonth", dayOfMonth);
					setResult(RESULT_OK, i);
					finish();
				}
			}
			
			
			@Override
			public void onChangeMonth(int month, int year)
			{
			}
			
			
			@Override
			public void onLongClickDate(Date date, View view)
			{
			}
			
			
			@Override
			public void onCaldroidViewCreated()
			{
				
			}
			
		};
		
		calFragment.setCaldroidListener(listener);
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
