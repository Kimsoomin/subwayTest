package com.dabeeo.hangouyou.fragments.mainmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;
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
    inputWord.addTextChangedListener(textWatcher);
    
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
    
    resultBean = new SearchResultBean();
    resultBean.addText("검색어1");
    adapter.add(resultBean);
    
    resultBean = new SearchResultBean();
    resultBean.addText("검색어2");
    adapter.add(resultBean);
    
    adapter.notifyDataSetChanged();
  }
  
  /**************************************************
   * listener
   ***************************************************/
  private TextWatcher textWatcher = new TextWatcher()
  {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }
    
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }
    
    
    @Override
    public void afterTextChanged(Editable s)
    {
      boolean inputedWord = s.length() > 0;
      
      layoutRecommedProduct.setVisibility(inputedWord ? View.GONE : View.VISIBLE);
    }
  };
}
