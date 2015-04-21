package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.beans.StationBean;
import com.dabeeo.hangouyou.controllers.SubwayListAdapter;
import com.dabeeo.hangouyou.managers.SubwayManager;

public class SubwayStationsActivity extends ActionBarActivity
{
  private ListView listView;
  private SubwayListAdapter adapter;
  private TextView infoText;
  private ImageView imageX;
  
  private TextView stationsInfoText;
  private TextView startStationName, endStationName;
  private ImageView startStationImage, endStationImage;
  
  
  @SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway_stations);
    setTitle(getString(R.string.term_subway));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    String infoString = getIntent().getStringExtra("stations_info");
    String jsonStations = getIntent().getStringExtra("stations_json");
    
    startStationName = (TextView) findViewById(R.id.start_station_name);
    endStationName = (TextView) findViewById(R.id.end_station_name);
    startStationImage = (ImageView) findViewById(R.id.start_station_image);
    endStationImage = (ImageView) findViewById(R.id.end_station_image);
    
    imageX = (ImageView) findViewById(R.id.image_x);
    imageX.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        finish();
      }
    });
    
    infoText = (TextView) findViewById(R.id.text_stations_info);
    infoText.setText(infoString);
    
    listView = (ListView) findViewById(R.id.list_view);
    adapter = new SubwayListAdapter(SubwayStationsActivity.this);
    listView.setAdapter(adapter);
    parseStationsJson(jsonStations);
  }
  
  
  private void parseStationsJson(String stationsJSONString)
  {
    JSONArray stationsJsonArray;
    ArrayList<StationBean> stations = new ArrayList<>();
    try
    {
      
      stationsJsonArray = new JSONArray(stationsJSONString);
      for (int i = 0; i < stationsJsonArray.length(); i++)
      {
        StationBean bean = new StationBean();
        bean.setJSONObject(stationsJsonArray.getJSONObject(i));
        stations.add(bean);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    try
    {
      startStationImage.setImageResource(SubwayManager.getInstance(SubwayStationsActivity.this).getSubwayLineResourceId(stations.get(0).line));
      endStationImage.setImageResource(SubwayManager.getInstance(SubwayStationsActivity.this).getSubwayLineResourceId(stations.get(stations.size() - 1).line));
      
      startStationName.setText(stations.get(0).nameKo);
      endStationName.setText(stations.get(stations.size() - 1).nameKo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    adapter.addAll(stations);
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
