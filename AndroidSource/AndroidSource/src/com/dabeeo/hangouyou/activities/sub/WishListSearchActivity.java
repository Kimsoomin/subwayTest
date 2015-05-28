package com.dabeeo.hangouyou.activities.sub;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabeeo.hangouyou.R;

public class WishListSearchActivity extends Activity
{
	private EditText editSearch;
	private ImageView backImage;
	private ImageView typingCancel;
	private RelativeLayout searchContainer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist_search);
		
		editSearch = (EditText) findViewById(R.id.edit_search);
		searchContainer = (RelativeLayout) findViewById(R.id.search_container);
		typingCancel = (ImageView) findViewById(R.id.search_cancel);
		typingCancel.setVisibility(View.GONE);
		typingCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				editSearch.setText("");
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
				Log.w("WARN", "length: " + editSearch.getText().toString().length());
				if (editSearch.getText().toString().length() > 1)
				{
					typingCancel.setVisibility(View.VISIBLE);
					searchContainer.setVisibility(View.VISIBLE);
				}
				else
				{
					typingCancel.setVisibility(View.GONE);
					searchContainer.setVisibility(View.GONE);
				}
				
			}
		};
		editSearch.addTextChangedListener(watcher);
	}
}
