package com.dabeeo.hangouyou.activities.sub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;

public class AgreementActivity extends ActionBarActivity
{
  public static final String TYPE_AGREEMENT = "agreement";
  public static final String TYPE_AGREEMENT_PRIVATE = "agreement_private";
  public static final String TYPE_AGREEMENT_GPS = "agreement_gps";
  private String type;
  private TextView content;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agreement);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    type = getIntent().getStringExtra("type");
    
    if (type.equals(TYPE_AGREEMENT))
      title.setText(getString(R.string.term_agreement));
    else if (type.equals(TYPE_AGREEMENT_PRIVATE))
      title.setText(getString(R.string.term_private_agreement));
    else if (type.equals(TYPE_AGREEMENT_GPS))
      title.setText(getString(R.string.term_gps_info_agreement));
    
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    content = (TextView) findViewById(R.id.content);
    content.setText("가상의 이용약관입니다");
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
}
