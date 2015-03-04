package com.dabeeo.hangouyou.activities.sub;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;

public class VersionActivity extends ActionBarActivity
{
  private TextView textVersion;
  private Button btnUpdate;
  
  private LinearLayout containerAgreement, containerPrivateAgreement, containerGpsInfoAgreement;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_version);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    textVersion = (TextView) findViewById(R.id.text_version);
    btnUpdate = (Button) findViewById(R.id.btn_update);
    containerAgreement = (LinearLayout) findViewById(R.id.container_agreement);
    containerPrivateAgreement = (LinearLayout) findViewById(R.id.container_private_agreement);
    containerGpsInfoAgreement = (LinearLayout) findViewById(R.id.container_gps_agreement);
    
    try
    {
      PackageManager manager = this.getPackageManager();
      PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
      textVersion.setText(getString(R.string.term_current_version) + info.versionName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    btnUpdate.setOnClickListener(clickListener);
    containerAgreement.setOnClickListener(clickListener);
    containerPrivateAgreement.setOnClickListener(clickListener);
    containerGpsInfoAgreement.setOnClickListener(clickListener);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
      finish();
    return super.onOptionsItemSelected(item);
  }
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnUpdate.getId())
      {
        Toast.makeText(VersionActivity.this, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerAgreement.getId())
      {
        Toast.makeText(VersionActivity.this, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerPrivateAgreement.getId())
      {
        Toast.makeText(VersionActivity.this, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerGpsInfoAgreement.getId())
      {
        Toast.makeText(VersionActivity.this, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
    }
  };
}
