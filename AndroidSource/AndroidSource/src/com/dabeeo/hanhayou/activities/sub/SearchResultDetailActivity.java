package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.dabeeo.hanhayou.managers.network.NetworkResult;

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
  private class SearchTask extends AsyncTask<String, Void, NetworkResult>
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
    protected NetworkResult doInBackground(String... params)
    {
      return apiClient.searchResult(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      adapter.clear();
      String status = "";
      try
      {
        JSONObject jsonObject = new JSONObject(result.response);
        if (jsonObject.has("status"))
        {
          status = jsonObject.getString("status");
        }
        
        if (status.equals("OK"))
        {
          ArrayList<SearchResultBean> placeList = new ArrayList<SearchResultBean>();
          ArrayList<SearchResultBean> planList = new ArrayList<SearchResultBean>();
          ArrayList<SearchResultBean> premiumList = new ArrayList<SearchResultBean>();
          
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          
          for (int i = 0; i < jsonArray.length(); i++)
          {
            SearchResultBean bean = new SearchResultBean();
            JSONObject obj = jsonArray.getJSONObject(i);
            bean.setLogType(obj.getString("logType"));
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            
            if (bean.type == SearchResultBean.TYPE_PLACE)
              placeList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_SCHEDULE)
              planList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_RECOMMEND_SEOUL)
              premiumList.add(bean);
          }
          
          Log.w("WARN","검색결과 장소 사이즈 : "+placeList.size());
          Log.w("WARN","검색결과 일정 사이즈 : "+planList.size());
          Log.w("WARN","검색결과 추천서울 사이즈 : "+premiumList.size());
          
          SearchResultBean titleBean = new SearchResultBean();
          if (placeList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(getString(R.string.term_place), jsonObject.getInt("placeCount"));
            titleBean.type = SearchResultBean.TYPE_PLACE;
            adapter.add(titleBean);
            
            if (placeList.size() > 0)
            {
              for (int i = 0; i < 3; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = placeList.get(i).idx;
                  listBean.addText(placeList.get(i).text, placeList.get(i).type);
                  adapter.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
          
          if (premiumList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(getString(R.string.term_strategy_seoul), jsonObject.getInt("premiumCount"));
            titleBean.type = SearchResultBean.TYPE_RECOMMEND_SEOUL;
            adapter.add(titleBean);
            
            if (premiumList.size() > 0)
            {
              for (int i = 0; i < 3; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = premiumList.get(i).idx;
                  listBean.addText(premiumList.get(i).text, premiumList.get(i).type);
                  adapter.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
          
          if (planList.size() > 0)
          {
            titleBean = new SearchResultBean();
            titleBean.addPlaceTitle(getString(R.string.term_travel_schedule), jsonObject.getInt("planCount"));
            titleBean.type = SearchResultBean.TYPE_SCHEDULE;
            adapter.add(titleBean);
            
            if (planList.size() > 0)
            {
              for (int i = 0; i < 3; i++)
              {
                try
                {
                  SearchResultBean listBean = new SearchResultBean();
                  listBean.idx = planList.get(i).idx;
                  listBean.addText(planList.get(i).text, planList.get(i).type);
                  adapter.add(listBean);
                }
                catch (Exception e)
                {
                }
              }
            }
          }
          
          adapter.notifyDataSetChanged();
        }
      }
      catch (JSONException e)
      {
        status = "";
        e.printStackTrace();
      }
      
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
        search(v.getText().toString());
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
