package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.SubwayManager;
import com.dabeeo.hangouyou.map.BlinkingMap;

public class SubwayActivity extends ActionBarActivity
{
  private AutoCompleteTextView editSearch;
  private ArrayList<String> subwayNames = new ArrayList<String>();
  
  
  
  
  
  @SuppressWarnings("static-access")
  @SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway);
    setTitle(getString(R.string.term_subway));
    
    Log.w("WARN", "GetIntent data : " + getIntent().getDoubleExtra("Latitude", -1));
    subwayNames.clear();
    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayNames());
    editSearch = (AutoCompleteTextView) findViewById(R.id.edit_search);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, subwayNames);
    editSearch.setAdapter(adapter);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    //위경도로 지하철역 찾기
    double[] latLong = null;
    if (getIntent().hasExtra("near_by_station_lat_lon"))
      latLong = getIntent().getDoubleArrayExtra("near_by_station_lat_lon");
    
    double[] destLatLong = null;
    if (getIntent().hasExtra("set_dest_station_lat_lon"))
      destLatLong = getIntent().getDoubleArrayExtra("set_dest_station_lat_lon");
    
    if (MainActivity.subwayFrament != null)
    {
      MainActivity.subwayFrament.viewClear();
      MainActivity.subwayFrament = new SubwayFragment();
      
      if (latLong != null)
        MainActivity.subwayFrament.findNearByStation(latLong[0], latLong[1]);
      
      if (destLatLong != null)
        MainActivity.subwayFrament.findNearByStation(destLatLong[0], destLatLong[1], destLatLong[2]);
    }
    
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
