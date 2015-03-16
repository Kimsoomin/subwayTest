package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
    resultBean.addNormalTitle("최근 검색어");
    adapter.add(resultBean);
    
    ArrayList<String> recentWords = PreferenceManager.getInstance(getActivity()).getRecentSearchWord();
    for (String string : recentWords)
    {
      resultBean = new SearchResultBean();
      resultBean.addText(string);
      adapter.add(resultBean);
    }
    
    adapter.notifyDataSetChanged();
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
        Log.i("SearchFragment.java | onEditorAction", "|" + text + "|");
        if (TextUtils.isEmpty(text))
          return false;
        
        PreferenceManager.getInstance(getActivity()).setRecentSearchWord(text);
        
        layoutRecommedProduct.setVisibility(View.GONE);
      }
      
      return false;
    }
  };
}
