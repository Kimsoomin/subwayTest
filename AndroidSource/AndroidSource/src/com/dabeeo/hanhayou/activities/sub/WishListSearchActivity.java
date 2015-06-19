package com.dabeeo.hanhayou.activities.sub;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.ProductBean;
import com.dabeeo.hanhayou.controllers.mainmenu.WishSearchListAdapter;
import com.dabeeo.hanhayou.external.libraries.GridViewWithHeaderAndFooter;
import com.dabeeo.hanhayou.views.PopularWishListParticleView;

public class WishListSearchActivity extends Activity
{
	private EditText editSearch;
	private ImageView backImage;
	private ImageView typingCancel;
	
	private LinearLayout emptyContainer, popularWishListContainer;
	private GridViewWithHeaderAndFooter listView;
	private WishSearchListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist_search);
		
		emptyContainer = (LinearLayout) findViewById(R.id.empty_container);
		listView = (GridViewWithHeaderAndFooter) findViewById(R.id.listview);
		adapter = new WishSearchListAdapter(this);
		listView.setAdapter(adapter);
		
		popularWishListContainer = (LinearLayout) findViewById(R.id.wish_list_container);
		PopularWishListParticleView pView = new PopularWishListParticleView(this);
		pView.setBean(0, "CELDREAM 화장품", "아쿠아 리움 티켓");
		popularWishListContainer.addView(pView);
		
		pView = new PopularWishListParticleView(this);
		pView.setBean(1, "같은 시간 속의 너", "W DRESSROOM 퍼퓸");
		popularWishListContainer.addView(pView);
		
		pView = new PopularWishListParticleView(this);
		pView.setBean(2, "남성BB크림", "SKII 여성용 스킨세트");
		popularWishListContainer.addView(pView);
		
		editSearch = (EditText) findViewById(R.id.edit_search);
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
					listView.setVisibility(View.VISIBLE);
					emptyContainer.setVisibility(View.GONE);
					
					ProductBean bean = new ProductBean();
					bean.title = "[숨]워터풀 타임리스 워터젤 크림";
					bean.originalPrice = 80000;
					bean.discountPrice = 452000;
					adapter.add(bean);
					
					bean = new ProductBean();
					bean.title = "[SKII]나이트밤";
					bean.originalPrice = 100000;
					bean.discountPrice = 252000;
					adapter.add(bean);
				}
				else
				{
					typingCancel.setVisibility(View.GONE);
					listView.setVisibility(View.GONE);
					emptyContainer.setVisibility(View.VISIBLE);
					
					adapter.clear();
				}
				
			}
		};
		editSearch.addTextChangedListener(watcher);
		
		backImage = (ImageView) findViewById(R.id.image_back_button);
		backImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}
