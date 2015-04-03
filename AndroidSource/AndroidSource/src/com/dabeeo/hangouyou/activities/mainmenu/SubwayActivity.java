package com.dabeeo.hangouyou.activities.mainmenu;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.fragments.mainmenu.SubwayFragment;

public class SubwayActivity extends ActionBarActivity
{
  @SuppressWarnings("static-access")
  @SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subway);
    setTitle(getString(R.string.term_subway));
    
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
