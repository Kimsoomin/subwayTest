package com.dabeeo.hangouyou.activities.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;

public class MyPageSettingActivity extends ActionBarActivity
{
  private EditText editName;
  private Button btnNameSave;
  
  private Switch switchNotificationOnOff, switchSyncWifiOnly;
  
  private LinearLayout containerVersionInfo, containerRatingApp, containerNotice;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    
    editName = (EditText) findViewById(R.id.edit_name);
    btnNameSave = (Button) findViewById(R.id.save_my_name);
    
    btnNameSave.setOnClickListener(nameSaveListener);
    
    switchNotificationOnOff = (Switch) findViewById(R.id.switch_notification_on_off);
    switchSyncWifiOnly = (Switch) findViewById(R.id.switch_sync_only_wifi);
    
    switchNotificationOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        
      }
    });
    switchSyncWifiOnly.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        
      }
    });
    containerNotice = (LinearLayout) findViewById(R.id.container_notice);
    containerRatingApp = (LinearLayout) findViewById(R.id.container_rating_app);
    containerVersionInfo = (LinearLayout) findViewById(R.id.container_version_info);
    
    containerNotice.setOnClickListener(containerClickListener);
    containerRatingApp.setOnClickListener(containerClickListener);
    containerVersionInfo.setOnClickListener(containerClickListener);
  }
  
  private OnClickListener nameSaveListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      
    }
  };
  
  private OnClickListener containerClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == containerVersionInfo.getId())
      {
        startActivity(new Intent(MyPageSettingActivity.this, VersionActivity.class));
      }
      else if (v.getId() == containerRatingApp.getId())
      {
        Toast.makeText(MyPageSettingActivity.this, "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerNotice.getId())
      {
        Toast.makeText(MyPageSettingActivity.this, "준비중입니다", Toast.LENGTH_LONG).show();
      }
      
    }
  };
}
