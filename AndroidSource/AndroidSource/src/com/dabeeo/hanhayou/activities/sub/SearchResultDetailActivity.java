package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.SearchResultBean;
import com.dabeeo.hanhayou.controllers.SearchResultAdapter;
import com.dabeeo.hanhayou.managers.network.ApiClient;

public class SearchResultDetailActivity extends ActionBarActivity
{
  private EditText inputWord;
  private SearchResultAdapter adapter;
  private ImageView imageX;
  private ListView searchListView;
  private LinearLayout emptyContainer;
  private ApiClient apiClient;
  private Handler handler = new Handler();
  private ProgressBar progressBar;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_search_result));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    progressBar = (ProgressBar) findViewById(R.id.progressbar);
    apiClient = new ApiClient(this);
    searchListView = (ListView) findViewById(R.id.search_list);
    emptyContainer = (LinearLayout) findViewById(R.id.empty_container);
    
    inputWord = (EditText) findViewById(R.id.edit_search);
    inputWord.setOnEditorActionListener(editorActionListener);
    inputWord.setText(getIntent().getStringExtra("keyword"));
    
    imageX = (ImageView) findViewById(R.id.search_cancel);
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
    
    adapter = new SearchResultAdapter();
    searchListView = (ListView) findViewById(R.id.search_list);
    searchListView.setOnItemClickListener(itemClickListener);
    searchListView.setOnScrollListener(new OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState)
      {
        hideKeyboard();
      }
      
      
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
      }
    });
    searchListView.setAdapter(adapter);
    
    TextWatcher watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
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
      
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
      }
      
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
      }
      
    };
    inputWord.addTextChangedListener(watcher);
    
    if (!TextUtils.isEmpty(inputWord.getText().toString()))
    {
      imageX.setVisibility(View.VISIBLE);
      inputWord.setSelection(inputWord.getText().toString().length());
      search(inputWord.getText().toString());
    }
    
  }
  
  
  private void hideKeyboard()
  {
    try
    {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  private void search(String text)
  {
    if (TextUtils.isEmpty(text))
    {
      emptyContainer.setVisibility(View.VISIBLE);
      searchListView.setVisibility(View.GONE);
      return;
    }
    else
    {
      searchListView.setVisibility(View.VISIBLE);
      emptyContainer.setVisibility(View.GONE);
    }
    
    handler.removeCallbacks(searchRunnable);
    handler.postDelayed(searchRunnable, 500);
  }
  
  Runnable searchRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      new SearchTask().execute(inputWord.getText().toString());
    }
  };
  
  
  private void detail(SearchResultBean searchResultBean)
  {
    if (searchResultBean.isTitle)
    {
      //more 인 경우 
      Intent intent = new Intent(SearchResultDetailActivity.this, SearchResultJustOneCategoryListActivity.class);
      intent.putExtra("title", searchResultBean.text + " " + getString(R.string.term_search_result));
      intent.putExtra("more_count", searchResultBean.moreCount);
      Log.w("WARN", "more 의 Search Type : " + searchResultBean.type);
      intent.putExtra("search_type", searchResultBean.type);
      intent.putExtra("search_text", inputWord.getText().toString());
      startActivity(intent);
    }
    else
    {
      Intent i = new Intent(SearchResultDetailActivity.this, PlaceDetailActivity.class);
      switch (searchResultBean.type)
      {
        case SearchResultBean.TYPE_PLACE:
          i = new Intent(SearchResultDetailActivity.this, PlaceDetailActivity.class);
          i.putExtra("place_idx", searchResultBean.idx);
          startActivity(i);
          break;
        
        case SearchResultBean.TYPE_PRODUCT:
          startActivity(new Intent(SearchResultDetailActivity.this, TrendProductDetailActivity.class));
          break;
        
        case SearchResultBean.TYPE_RECOMMEND_SEOUL:
          i = new Intent(SearchResultDetailActivity.this, TravelStrategyDetailActivity.class);
          i.putExtra("place_idx", searchResultBean.idx);
          startActivity(i);
          break;
        
        case SearchResultBean.TYPE_SCHEDULE:
          i = new Intent(SearchResultDetailActivity.this, TravelScheduleDetailActivity.class);
          i.putExtra("idx", searchResultBean.idx);
          startActivity(i);
          break;
        
        default:
          inputWord.setText(searchResultBean.text);
          search(searchResultBean.text);
          break;
      }
    }
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  /**************************************************
   * AsyncTask
   ***************************************************/
  private class SearchTask extends AsyncTask<String, Void, ArrayList<SearchResultBean>>
  {
    @Override
    protected void onPreExecute()
    {
      adapter.clear();
      progressBar.setVisibility(View.VISIBLE);
      progressBar.bringToFront();
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<SearchResultBean> doInBackground(String... params)
    {
      return apiClient.searchResult(params[0], 3);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<SearchResultBean> result)
    {
      adapter.clear();
      adapter.add(result);
      
      if (adapter.getCount() == 0)
      {
        searchListView.setVisibility(View.GONE);
        emptyContainer.setVisibility(View.VISIBLE);
      }
      else
      {
        searchListView.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
      }
      
      progressBar.setVisibility(View.GONE);
      super.onPostExecute(result);
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
        hideKeyboard();
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
