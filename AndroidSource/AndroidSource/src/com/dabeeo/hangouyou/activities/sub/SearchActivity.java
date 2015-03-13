package com.dabeeo.hangouyou.activities.sub;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dabeeo.hangouyou.R;

public class SearchActivity extends ActionBarActivity
{
  private EditText inputWord;
  private ViewGroup layoutDefalut, layoutSearchResult;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    
    layoutDefalut = (ViewGroup) findViewById(R.id.layout_default);
    layoutSearchResult = (ViewGroup) findViewById(R.id.layout_search_result);
    
    inputWord = (EditText) findViewById(R.id.input_word);
    inputWord.addTextChangedListener(textWatcher);
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
      layoutDefalut.setVisibility(inputedWord ? View.GONE : View.VISIBLE);
      layoutSearchResult.setVisibility(inputedWord ? View.VISIBLE : View.GONE);
    }
  };
}
