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

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hanhayou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.beans.SearchResultBean;
import com.dabeeo.hanhayou.controllers.SearchResultAdapter;
import com.dabeeo.hanhayou.controllers.SearchResultDetailAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
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
  private TextView textSearchResult;
  
  private ListView autoSearchListView;
  private SearchResultDetailAdapter autoSearchAdapter;
  
  private LinearLayout bottomMenuHome, bottomMenuMyPage, bottomMenuPhotolog, bottomMenuWishList, bottomMenuSearch;
  private LinearLayout containerBottomTab;
  
  private TextWatcher watcher;
  private boolean isClickedSearchButton = false;
  
  
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
    
    containerBottomTab = (LinearLayout) findViewById(R.id.container_bottom_tab);
    bottomMenuHome = (LinearLayout) findViewById(R.id.container_menu_home);
    bottomMenuMyPage = (LinearLayout) findViewById(R.id.container_menu_mypage);
    bottomMenuPhotolog = (LinearLayout) findViewById(R.id.container_menu_photolog);
    bottomMenuWishList = (LinearLayout) findViewById(R.id.container_menu_wishlist);
    bottomMenuSearch = (LinearLayout) findViewById(R.id.container_menu_search);
    bottomMenuHome.setOnClickListener(bottomMenuClickListener);
    bottomMenuMyPage.setOnClickListener(bottomMenuClickListener);
    bottomMenuPhotolog.setOnClickListener(bottomMenuClickListener);
    bottomMenuWishList.setOnClickListener(bottomMenuClickListener);
    bottomMenuSearch.setOnClickListener(bottomMenuClickListener);
    
    autoSearchAdapter = new SearchResultDetailAdapter();
    autoSearchListView = (ListView) findViewById(R.id.auto_search_list);
    autoSearchListView.setOnItemClickListener(itemClickListener);
    autoSearchListView.setOnScrollListener(new OnScrollListener()
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
    autoSearchListView.setAdapter(autoSearchAdapter);
    autoSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        new SearchTask().execute(inputWord.getText().toString());
      }
    });
    
    textSearchResult = (TextView) findViewById(R.id.text_search_result);
    textSearchResult.setVisibility(View.GONE);
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
        inputWord.removeTextChangedListener(watcher);
        inputWord.setText("");
        inputWord.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(inputWord, 0);
        inputWord.addTextChangedListener(watcher);
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
    
    watcher = new TextWatcher()
    {
      @Override
      public void afterTextChanged(Editable s)
      {
        if (inputWord.getText().toString().length() > 1)
        {
          isClickedSearchButton = false;
          searchListView.setVisibility(View.VISIBLE);
          emptyContainer.setVisibility(View.GONE);
          imageX.setVisibility(View.VISIBLE);
          search(inputWord.getText().toString());
        }
        else
        {
          imageX.setVisibility(View.GONE);
          autoSearchListView.setVisibility(View.GONE);
//          searchListView.setVisibility(View.GONE);
//          emptyContainer.setVisibility(View.VISIBLE);
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
      new SearchTask().execute(inputWord.getText().toString());
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
      new AutoSearchTask().execute(inputWord.getText().toString());
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
   * listener
   ***************************************************/
  private OnClickListener bottomMenuClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == bottomMenuHome.getId())
      {
        Intent i = new Intent(SearchResultDetailActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_HOME);
        startActivity(i);
        finish();
      }
      else if (v.getId() == bottomMenuMyPage.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(SearchResultDetailActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_MY_PAGE);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(SearchResultDetailActivity.this).showNeedLoginDialog(1);
        }
      }
      else if (v.getId() == bottomMenuWishList.getId())
      {
        if (PreferenceManager.getInstance(getApplicationContext()).isLoggedIn())
        {
          Intent i = new Intent(SearchResultDetailActivity.this, MainActivity.class);
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.putExtra("position", MainActivity.POSITION_WISHLIST);
          startActivity(i);
        }
        else
        {
          new AlertDialogManager(SearchResultDetailActivity.this).showNeedLoginDialog(2);
        }
      }
      else if (v.getId() == bottomMenuSearch.getId())
      {
        Intent i = new Intent(SearchResultDetailActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("position", MainActivity.POSITION_SEARCH);
        startActivity(i);
      }
    }
  };
  
  /**************************************************
   * AsyncTask
   ***************************************************/
  private class AutoSearchTask extends AsyncTask<String, Void, ArrayList<SearchResultBean>>
  {
    @Override
    protected void onPreExecute()
    {
      autoSearchAdapter.clear();
      super.onPreExecute();
    }
    
    
    @Override
    protected ArrayList<SearchResultBean> doInBackground(String... params)
    {
      return apiClient.searchAuto(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<SearchResultBean> result)
    {
      autoSearchAdapter.addAll(result);
      
      if (autoSearchAdapter.getCount() == 0 || TextUtils.isEmpty(inputWord.getText().toString()) || isClickedSearchButton)
        autoSearchListView.setVisibility(View.GONE);
      else
      {
        Log.w("WARN", "Auto listView show!");
        autoSearchListView.setVisibility(View.VISIBLE);
        autoSearchListView.bringToFront();
      }
      super.onPostExecute(result);
    }
  }
  
  private class SearchTask extends AsyncTask<String, Void, ArrayList<SearchResultBean>>
  {
    private int size = 0;
    
    
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
      ArrayList<SearchResultBean> reuslt = apiClient.searchResult(params[0], 3);
      size = apiClient.getSearchResultCount(params[0]);
      return reuslt;
    }
    
    
    @Override
    protected void onPostExecute(ArrayList<SearchResultBean> result)
    {
      if (size == 0)
        textSearchResult.setVisibility(View.GONE);
      else
      {
        textSearchResult.setVisibility(View.VISIBLE);
        textSearchResult.setText(getString(R.string.term_search_result) + " (" + Integer.toString(size) + ")");
      }
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
      
      autoSearchListView.setVisibility(View.GONE);
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
        isClickedSearchButton = true;
        new SearchTask().execute(inputWord.getText().toString());
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
