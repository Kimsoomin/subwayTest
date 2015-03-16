package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.views.ProductView;

public class SearchFragment extends Fragment
{
  private EditText inputWord;
  private ListView listView;
  private LinearLayout layoutRecommedProduct;
  private SearchResultAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    
    inputWord = (EditText) view.findViewById(R.id.input_word);
    inputWord.setOnEditorActionListener(editorActionListener);
    
    layoutRecommedProduct = (LinearLayout) view.findViewById(R.id.layout_recommend_product);
    
    adapter = new SearchResultAdapter();
    listView = (ListView) view.findViewById(android.R.id.list);
    listView.setOnItemClickListener(itemClickListener);
    listView.setAdapter(adapter);
    
    return view;
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    ProductView productView = new ProductView(getActivity());
    ProductBean productBean = new ProductBean();
    productBean.title = "상품명";
    productBean.originalPrice = 100;
    productBean.discountPrice = 40;
    productView.setBean(productBean);
    layoutRecommedProduct.addView(productView);
    
    SearchResultBean resultBean = new SearchResultBean();
    resultBean.addNormalTitle("최근 검색어", 0);
    adapter.add(resultBean);
    
    ArrayList<String> recentWords = PreferenceManager.getInstance(getActivity()).getRecentSearchWord();
    for (String string : recentWords)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(string);
      adapter.add(bean);
    }
    
    adapter.notifyDataSetChanged();
  }
  
  
  private void search(String text)
  {
    PreferenceManager.getInstance(getActivity()).setRecentSearchWord(text);
    
    adapter.clear();
    
    SearchResultBean resultBean = new SearchResultBean();
    resultBean.addNormalTitle("검색결과", 300);
    adapter.add(resultBean);
    
    SearchResultBean locationBean = new SearchResultBean();
    locationBean.addLocationTitle("장소", 100);
    adapter.add(locationBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " 장소" + i);
      adapter.add(bean);
    }
    
    SearchResultBean productBean = new SearchResultBean();
    productBean.addProductTitle("상품", 3);
    adapter.add(productBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " 상품" + i);
      adapter.add(bean);
    }
    
    SearchResultBean recommendSeoulBean = new SearchResultBean();
    recommendSeoulBean.addRecommendSeoulTitle("추천서울", 100);
    adapter.add(recommendSeoulBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " 추천서울" + i);
      adapter.add(bean);
    }
    
    SearchResultBean scheduleBean = new SearchResultBean();
    scheduleBean.addScheduleTitle("스케줄", 100);
    adapter.add(scheduleBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " 스케줄" + i);
      adapter.add(bean);
    }
    SearchResultBean photoLogBean = new SearchResultBean();
    photoLogBean.addScheduleTitle("포토로그", 100);
    adapter.add(photoLogBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " 포토로그" + i);
      adapter.add(bean);
    }
    
    adapter.notifyDataSetChanged();
  }
  
  
  private void detail(SearchResultBean searchResultBean)
  {
    
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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        
        String text = v.getText().toString();
        if (TextUtils.isEmpty(text))
        {
          layoutRecommedProduct.setVisibility(View.VISIBLE);
          return false;
        }
        else
          layoutRecommedProduct.setVisibility(View.GONE);
        
        search(text);
      }
      
      return false;
    }
  };
  
  private OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      detail((SearchResultBean) adapter.getItem(position));
    }
  };
}
