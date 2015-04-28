package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.SubwayManager;

public class SubwayActivity extends Activity
{
  private AutoCompleteTextView editSearch;
  private ArrayList<String> subwayNames = new ArrayList<String>();
  private ImageView backImage;
  
  
  @SuppressWarnings("static-access")
  @SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway);
    setTitle(getString(R.string.term_subway));
    
    Log.w("WARN", "GetIntent data : " + getIntent().getDoubleExtra("Latitude", -1));
    subwayNames.clear();
    
    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayNames());
    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayCnNames());
    editSearch = (AutoCompleteTextView) findViewById(R.id.edit_search);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, subwayNames);
    editSearch.setAdapter(adapter);
    
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
      {
        destName = getIntent().getStringExtra("dest_name");
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
    
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.replace(R.id.content, MainActivity.subwayFrament);
    ft.commit();
    
    editSearch.setOnItemClickListener(new OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
      {
        String stationName = arg0.getItemAtPosition(position).toString();
        String stationdId = SubwayManager.getInstance(SubwayActivity.this).getStationId(stationName);
        Log.w("WARN", "Select Station Name : " + stationName);
        Log.w("WARN", "Select Station id : " + stationdId);
        MainActivity.subwayFrament.findStation(stationdId, stationName);
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
  public void onBackPressed()
  {
    MainActivity.subwayFrament.viewClear();
    super.onBackPressed();
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
