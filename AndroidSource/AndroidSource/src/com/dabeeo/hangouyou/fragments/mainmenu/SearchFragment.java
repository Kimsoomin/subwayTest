package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.sub.SearchResultDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;
import com.dabeeo.hangouyou.controllers.SearchResultDetailAdapter;
import com.dabeeo.hangouyou.views.PopularKeywordView;
import com.dabeeo.hangouyou.views.ProductView;

public class SearchFragment extends Fragment
{
	private EditText inputWord;
	private ViewGroup layoutRecommedProductParent, layoutRecommedProduct;
	private SearchResultDetailAdapter adapter;
	private ImageView imageX;
	private LinearLayout popularKeywordOuterContainer, popularKeyworkdContainer;
	private ListView searchListView;
	private ScrollView emptyContainer;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		int resId = R.layout.fragment_search;
		return inflater.inflate(resId, null);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		popularKeywordOuterContainer = (LinearLayout) getView().findViewById(R.id.popular_keyword_outer_container);
		popularKeyworkdContainer = (LinearLayout) getView().findViewById(R.id.popular_keyword_container);
		searchListView = (ListView) getView().findViewById(R.id.search_list);
		emptyContainer = (ScrollView) getView().findViewById(R.id.empty_container);
		
		inputWord = (EditText) getView().findViewById(R.id.edit_search);
		inputWord.setOnEditorActionListener(editorActionListener);
		
		imageX = (ImageView) getView().findViewById(R.id.search_cancel);
		imageX.setVisibility(View.GONE);
		imageX.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				inputWord.setText("");
				search("");
			}
		});
		
		layoutRecommedProductParent = (LinearLayout) getView().findViewById(R.id.layout_recommend_product_parent);
		layoutRecommedProduct = (LinearLayout) getView().findViewById(R.id.layout_recommend_product);
		
		adapter = new SearchResultDetailAdapter();
		searchListView = (ListView) getView().findViewById(R.id.search_list);
		searchListView.setOnItemClickListener(itemClickListener);
		searchListView.setAdapter(adapter);
		
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
				if (inputWord.getText().toString().length() > 1)
				{
					searchListView.setVisibility(View.VISIBLE);
					emptyContainer.setVisibility(View.GONE);
					imageX.setVisibility(View.VISIBLE);
					search(inputWord.getText().toString());
				}
				else
				{
					imageX.setVisibility(View.GONE);
					searchListView.setVisibility(View.GONE);
					emptyContainer.setVisibility(View.VISIBLE);
				}
			}
			
		};
		inputWord.addTextChangedListener(watcher);
		
		loadPopularWords();
		loadRecommendProduct();
	}
	
	
	private void loadRecommendProduct()
	{
		layoutRecommedProduct.removeAllViews();
		ProductView productView = new ProductView(getActivity());
		ProductBean productBean = new ProductBean();
		productBean.title = "상품명1";
		productBean.originalPrice = 100;
		productBean.discountPrice = 40;
		productView.setBean(productBean, productBean);
		layoutRecommedProduct.addView(productView);
	}
	
	
	private void loadPopularWords()
	{
		popularKeywordOuterContainer.setVisibility(View.VISIBLE);
		popularKeyworkdContainer.removeAllViews();
		
		PopularKeywordView view = new PopularKeywordView(getActivity());
		view.setData(1, "다비오");
		popularKeyworkdContainer.addView(view);
		
		view = new PopularKeywordView(getActivity());
		view.setData(2, "SKII");
		popularKeyworkdContainer.addView(view);
		
		view = new PopularKeywordView(getActivity());
		view.setData(3, "아쿠아리움");
		popularKeyworkdContainer.addView(view);
	}
	
	
	private void search(String text)
	{
		if (TextUtils.isEmpty(text))
		{
			emptyContainer.setVisibility(View.VISIBLE);
			searchListView.setVisibility(View.GONE);
			loadPopularWords();
			return;
		}
		else
		{
			searchListView.setVisibility(View.VISIBLE);
			emptyContainer.setVisibility(View.GONE);
		}
		
		//TEST Beans
		adapter.clear();
		
		SearchResultBean bean = new SearchResultBean();
		bean.text = "테스트";
		adapter.add(bean);
		bean = new SearchResultBean();
		bean.text = "다비오";
		adapter.add(bean);
	}
	
	/**************************************************
	 * listener
	 ***************************************************/
	private OnEditorActionListener editorActionListener = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				Intent i = new Intent(getActivity(), SearchResultDetailActivity.class);
				i.putExtra("keyword", inputWord.getText().toString());
				startActivity(i);
			}
			return false;
		}
	};
	
	private OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent i = new Intent(getActivity(), SearchResultDetailActivity.class);
			i.putExtra("keyword", ((SearchResultBean) adapter.getItem(position)).text);
			startActivity(i);
		}
	};
}
