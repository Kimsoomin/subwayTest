package com.dabeeo.hangouyou.activities.mainmenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;
import com.dabeeo.hangouyou.managers.SubwayManager;

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
    
    subwayNames.clear();
    subwayNames.addAll(SubwayManager.getInstance(this).getAllSubwayNames());
    editSearch = (AutoCompleteTextView) findViewById(R.id.edit_search);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, subwayNames);
    editSearch.setAdapter(adapter);
    
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    if (MainActivity.subwayFrament != null)
    {
      MainActivity.subwayFrament.viewClear();
      MainActivity.subwayFrament = new SubwayFragment();
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
