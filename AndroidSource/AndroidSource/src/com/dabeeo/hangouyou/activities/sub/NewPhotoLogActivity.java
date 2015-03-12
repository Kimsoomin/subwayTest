package com.dabeeo.hangouyou.activities.sub;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.dabeeo.hangouyou.R;

public class NewPhotoLogActivity extends ActionBarActivity
{
  private ViewGroup container;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_photo_log);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    displayContents();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_new_photo_log, menu);
    return true;
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == android.R.id.home)
      finish();
    else if (id == R.id.save)
      Log.i("NewPhotoLogActivity.java | onOptionsItemSelected", "|" + "save" + "|");
    return super.onOptionsItemSelected(item);
  }
  
  
  private void displayContents()
  {
  }
}
