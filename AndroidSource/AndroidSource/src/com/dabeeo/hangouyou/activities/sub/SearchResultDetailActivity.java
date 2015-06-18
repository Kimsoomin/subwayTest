package com.dabeeo.hangouyou.activities.sub;

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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.activities.mainmenu.PlaceDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelScheduleDetailActivity;
import com.dabeeo.hangouyou.activities.travel.TravelStrategyDetailActivity;
import com.dabeeo.hangouyou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hangouyou.beans.SearchResultBean;
import com.dabeeo.hangouyou.controllers.SearchResultAdapter;
import com.dabeeo.hangouyou.managers.network.ApiClient;
import com.dabeeo.hangouyou.managers.network.NetworkResult;

public class SearchResultDetailActivity extends ActionBarActivity
{
  private EditText inputWord;
  private SearchResultAdapter adapter;
  private ImageView imageX;
  private ListView searchListView;
  private LinearLayout emptyContainer;
  private ApiClient apiClient;
  private Handler handler = new Handler();
  
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
      intent.putExtra("results", adapter.getJsonStringForParameter(searchResultBean.type));
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
            bean.text = obj.getString("title");
            bean.idx = obj.getString("idx");
            bean.setLogType(obj.getString("logType"));
            
            if (bean.type == SearchResultBean.TYPE_PLACE)
              placeList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_SCHEDULE)
              planList.add(bean);
            else if (bean.type == SearchResultBean.TYPE_RECOMMEND_SEOUL)
              premiumList.add(bean);
          }
          
          SearchResultBean titleBean = new SearchResultBean();
          titleBean.addPlaceTitle(getString(R.string.term_place), jsonObject.getInt("placeCount"));
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
          
          titleBean = new SearchResultBean();
          titleBean.addPlaceTitle(getString(R.string.term_strategy_seoul), jsonObject.getInt("premiumCount"));
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
          
          titleBean = new SearchResultBean();
          titleBean.addPlaceTitle(getString(R.string.term_travel_schedule), jsonObject.getInt("planCount"));
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
