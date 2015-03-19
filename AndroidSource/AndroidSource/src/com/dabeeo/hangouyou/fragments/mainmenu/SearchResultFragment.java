package com.dabeeo.hangouyou.fragments.mainmenu;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.mainmenu.RecommendSeoulDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyPhotoLogDetailActivity;
import com.dabeeo.hangouyou.activities.mypage.sub.MyScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.sub.SearchResultDetail;
import com.dabeeo.hangouyou.beans.ProductBean;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;
import com.dabeeo.hangouyou.managers.PreferenceManager;
import com.dabeeo.hangouyou.views.ProductView;

public class SearchResultFragment extends Fragment
{
  private EditText inputWord;
  private ViewGroup layoutRecommedProductParent, layoutRecommedProduct;
  private SearchResultAdapter adapter;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    int resId = R.layout.fragment_search_result;
    return inflater.inflate(resId, null);
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    inputWord = (EditText) getView().findViewById(R.id.input_word);
    inputWord.setOnEditorActionListener(editorActionListener);
    
    layoutRecommedProductParent = (LinearLayout) getView().findViewById(R.id.layout_recommend_product_parent);
    layoutRecommedProduct = (LinearLayout) getView().findViewById(R.id.layout_recommend_product);
    
    adapter = new SearchResultAdapter();
    ListView listView = (ListView) getView().findViewById(android.R.id.list);
    listView.setOnItemClickListener(itemClickListener);
    listView.setAdapter(adapter);
    
    loadRecentSearchWord();
    loadRecommendProduct();
  }
  
  
  private void loadRecommendProduct()
  {
    ProductView productView = new ProductView(getActivity());
    ProductBean productBean = new ProductBean();
    productBean.title = "상품명1";
    productBean.originalPrice = 100;
    productBean.discountPrice = 40;
    productView.setBean(productBean);
    layoutRecommedProduct.addView(productView);
  }
  
  
  private void loadRecentSearchWord()
  {
    SearchResultBean resultBean = new SearchResultBean();
    resultBean.addNormalTitle(getString(R.string.recent_search_word), 0);
    adapter.add(resultBean);
    
    ArrayList<String> recentWords = PreferenceManager.getInstance(getActivity()).getRecentSearchWord();
    for (String string : recentWords)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(string, SearchResultBean.TYPE_NORMAL);
      adapter.add(bean);
    }
    
    adapter.notifyDataSetChanged();
  }
  
  
  private void search(String text)
  {
    adapter.clear();
    
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    
    if (TextUtils.isEmpty(text))
    {
      layoutRecommedProductParent.setVisibility(View.VISIBLE);
      loadRecentSearchWord();
      return;
    }
    else
      layoutRecommedProductParent.setVisibility(View.GONE);
    
    PreferenceManager.getInstance(getActivity()).setRecentSearchWord(text);
    
    SearchResultBean resultBean = new SearchResultBean();
    resultBean.addNormalTitle(getString(R.string.search_result), 300);
    adapter.add(resultBean);
    
    SearchResultBean locationBean = new SearchResultBean();
    locationBean.addPlaceTitle(getString(R.string.place), 100);
    adapter.add(locationBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " " + getString(R.string.place) + i, SearchResultBean.TYPE_PLACE);
      adapter.add(bean);
    }
    
    SearchResultBean productBean = new SearchResultBean();
    productBean.addProductTitle(getString(R.string.product), 3);
    adapter.add(productBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " " + getString(R.string.product) + i, SearchResultBean.TYPE_PRODUCT);
      adapter.add(bean);
    }
    
    SearchResultBean recommendSeoulBean = new SearchResultBean();
    recommendSeoulBean.addRecommendSeoulTitle(getString(R.string.term_recommend_seoul), 100);
    adapter.add(recommendSeoulBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " " + getString(R.string.term_recommend_seoul) + i, SearchResultBean.TYPE_RECOMMEND_SEOUL);
      adapter.add(bean);
    }
    
    SearchResultBean scheduleBean = new SearchResultBean();
    scheduleBean.addScheduleTitle(getString(R.string.term_travel_schedule), 100);
    adapter.add(scheduleBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " " + getString(R.string.term_travel_schedule) + i, SearchResultBean.TYPE_SCHEDULE);
      adapter.add(bean);
    }
    SearchResultBean photoLogBean = new SearchResultBean();
    photoLogBean.addScheduleTitle(getString(R.string.term_photolog), 100);
    adapter.add(photoLogBean);
    
    for (int i = 0; i < 3; i++)
    {
      SearchResultBean bean = new SearchResultBean();
      bean.addText(text + " " + getString(R.string.term_photolog) + i, SearchResultBean.TYPE_PHOTO_LOG);
      adapter.add(bean);
    }
    
    adapter.notifyDataSetChanged();
  }
  
  
  private void detail(SearchResultBean searchResultBean)
  {
    if (searchResultBean.isTitle)
    {
      Intent intent = new Intent(getActivity(), SearchResultDetail.class);
      intent.putExtra("title", searchResultBean.text + " " + getString(R.string.search_result));
      intent.putExtra("results", adapter.getJsonStringForParameter(searchResultBean.type));
      startActivity(intent);
    }
    else
    {
      switch (searchResultBean.type)
      {
        case SearchResultBean.TYPE_PLACE:
          startActivity(new Intent(getActivity(), PlaceDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_PRODUCT:
          Log.i("SearchResultFragment.java | detail", "|" + "상품 상세화면으로 가기" + "|");
          break;
        
        case SearchResultBean.TYPE_RECOMMEND_SEOUL:
          startActivity(new Intent(getActivity(), RecommendSeoulDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_SCHEDULE:
          startActivity(new Intent(getActivity(), MyScheduleDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_PHOTO_LOG:
          startActivity(new Intent(getActivity(), MyPhotoLogDetailActivity.class));
          break;
        
        default:
          inputWord.setText(searchResultBean.text);
          search(searchResultBean.text);
          break;
      }
    }
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
        search(v.getText().toString());
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
