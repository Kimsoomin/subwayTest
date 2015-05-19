package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.controllers.mypage.SubwaySearchListAdapter;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.SubwayManager;

public class SubwayActivity extends Activity
{
  private EditText editSearch;
  private ArrayList<String> subwayNames = new ArrayList<String>();
  private ImageView backImage;
  private ImageView typingCancel;
  private ListView searchListView;
  private SubwaySearchListAdapter adapter;
  private RelativeLayout searchContainer;
  private RelativeLayout emptySearchContainer;
  
  
  @SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway);
    setTitle(getString(R.string.term_subway));
    
    editSearch = (EditText) findViewById(R.id.edit_search);
    searchContainer = (RelativeLayout) findViewById(R.id.search_container);
    emptySearchContainer = (RelativeLayout) findViewById(R.id.empty_search_container);
    typingCancel = (ImageView) findViewById(R.id.search_cancel);
    typingCancel.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        editSearch.setText("");
      }
    });
    
    searchListView = (ListView) findViewById(R.id.search_listview);
    adapter = new SubwaySearchListAdapter(SubwayActivity.this);
    searchListView.setAdapter(adapter);
    
    Log.w("WARN", "GetIntent data : " + getIntent().getDoubleExtra("Latitude", -1));
    subwayNames.clear();
    
//    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayNames());
//    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayCnNames());
    
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
    
    //위경도로 지하철역 찾기
    double[] latLong = null;
    if (getIntent().hasExtra("near_by_station_lat_lon"))
      latLong = getIntent().getDoubleArrayExtra("near_by_station_lat_lon");
    
    double[] destLatLong = null;
    String destName = "";
    if (getIntent().hasExtra("set_dest_station_lat_lon"))
    {
      destLatLong = getIntent().getDoubleArrayExtra("set_dest_station_lat_lon");
      if (getIntent().hasExtra("dest_name"))
        destName = getIntent().getStringExtra("dest_name");
    }
    
    if (MainActivity.subwayFrament == null)
    {
//    MainActivity.subwayFrament.viewClear();
      MainActivity.subwayFrament = new SubwayFragment();
    }
    
    if (latLong != null)
      MainActivity.subwayFrament.findNearByStation(latLong[0], latLong[1]);
    
    if (destLatLong != null)
      MainActivity.subwayFrament.findNearByStation(destLatLong[0], destLatLong[1], destLatLong[2], destName);
    
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.replace(R.id.content, MainActivity.subwayFrament);
    ft.commit();
    
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
        Log.w("WARN", "length: " + editSearch.getText().toString().length());
        if (editSearch.getText().toString().length() > 1)
        {
          searchContainer.setVisibility(View.VISIBLE);
          if (SubwayManager.getInstance(SubwayActivity.this).getSubwayStationsWithTitle(editSearch.getText().toString()).size() > 0)
          {
            searchListView.setVisibility(View.VISIBLE);
            searchListView.bringToFront();
            emptySearchContainer.setVisibility(View.GONE);
          }
          else
          {
            searchListView.setVisibility(View.GONE);
            emptySearchContainer.setVisibility(View.VISIBLE);
          }
        }
        else
        {
          searchContainer.setVisibility(View.GONE);
          searchListView.setVisibility(View.GONE);
        }
        
        adapter.clear();
        adapter.addAll(SubwayManager.getInstance(SubwayActivity.this).getSubwayStationsWithTitle(editSearch.getText().toString()));
      }
    };
    editSearch.addTextChangedListener(watcher);
    editSearch.setOnEditorActionListener(new OnEditorActionListener()
    {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
      {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
        {
          if (SubwayManager.getInstance(SubwayActivity.this).getSubwayStationsWithTitle(editSearch.getText().toString()).size() > 0)
          {
            searchListView.setVisibility(View.VISIBLE);
            searchListView.bringToFront();
            emptySearchContainer.setVisibility(View.GONE);
          }
          else
          {
            searchListView.setVisibility(View.GONE);
            emptySearchContainer.setVisibility(View.VISIBLE);
          }
          adapter.clear();
          adapter.addAll(SubwayManager.getInstance(SubwayActivity.this).getSubwayStationsWithTitle(editSearch.getText().toString()));
        }
        return true;
      }
    });
    
    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
      {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
        
        searchContainer.setVisibility(View.GONE);
        searchListView.setVisibility(View.GONE);
        StationBean bean = (StationBean) adapter.getItem(position);
        MainActivity.subwayFrament.findStation(bean.stationId, bean.nameKo);
      }
    });
    
    backImage = (ImageView) findViewById(R.id.image_back_button);
    backImage.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
  }
  
  
  @Override
  protected void onNewIntent(Intent intent)
  {
    Log.w("WARN", "Subway onNewIntent");
    
    double[] latLong = null;
    if (intent.hasExtra("near_by_station_lat_lon"))
      latLong = intent.getDoubleArrayExtra("near_by_station_lat_lon");
    
    double[] destLatLong = null;
    String destName = "";
    if (intent.hasExtra("set_dest_station_lat_lon"))
    {
      destLatLong = intent.getDoubleArrayExtra("set_dest_station_lat_lon");
      if (intent.hasExtra("dest_name"))
      {
        destName = intent.getStringExtra("dest_name");
      }
    }
    
    if (MainActivity.subwayFrament == null)
    {
//    MainActivity.subwayFrament.viewClear();
      MainActivity.subwayFrament = new SubwayFragment();
    }
    
    if (latLong != null)
      MainActivity.subwayFrament.findNearByStation(latLong[0], latLong[1]);
    
    if (destLatLong != null)
      MainActivity.subwayFrament.findNearByStation(destLatLong[0], destLatLong[1], destLatLong[2], destName);
    
    MainActivity.subwayFrament.checkSVGLoaded();
    super.onNewIntent(intent);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    
    return super.onOptionsItemSelected(item);
  }
}
