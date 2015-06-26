package com.dabeeo.hanhayou.activities.sub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class SettingActivity extends ActionBarActivity
{
  private TextView textName;
  private LinearLayout containerEditProfile;
  private Switch switchNotificationOnOff, switchSyncWifiOnly;
  
  private LinearLayout containerVersionInfo, containerRatingApp, containerNotice, containerLogout;
  private ImageView badgeNotice, badgeVersionInfo;
  private PreferenceManager preferenceManager;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    
    preferenceManager = PreferenceManager.getInstance(getApplicationContext());
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_setting));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    textName = (TextView) findViewById(R.id.text_name);
    
    badgeNotice = (ImageView) findViewById(R.id.badge_notice);
    badgeVersionInfo = (ImageView) findViewById(R.id.badge_version_info);
    badgeNotice.setVisibility(View.GONE);
    badgeVersionInfo.setVisibility(View.GONE);
    containerLogout = (LinearLayout) findViewById(R.id.container_logout);
    containerEditProfile = (LinearLayout) findViewById(R.id.container_edit_profile);
    containerEditProfile.setOnClickListener(nameSaveListener);
    containerLogout.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage(getString(R.string.msg_logout));
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which)
          {
            preferenceManager.clearUserInfo();
            Intent i = new Intent(SettingActivity.this, MainActivity.class);
            i.putExtra("position", MainActivity.POSITION_HOME);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();
          }
        });
        dialog.setNegativeButton(android.R.string.cancel, null);
        dialog.show();
      }
    });
    
    switchNotificationOnOff = (Switch) findViewById(R.id.switch_notification_on_off);
    switchSyncWifiOnly = (Switch) findViewById(R.id.switch_sync_only_wifi);
    
    switchNotificationOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        PreferenceManager.getInstance(SettingActivity.this).setAllowPopup(isChecked);
      }
    });
    switchSyncWifiOnly.setOnCheckedChangeListener(new OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked)
      {
        if (isChecked)
          PreferenceManager.getInstance(SettingActivity.this).setSyncOnlyWifi(isChecked);
        else
        {
          Builder builder = new AlertDialog.Builder(SettingActivity.this);
          builder.setTitle(getString(R.string.term_alert));
          builder.setMessage(getString(R.string.msg_only_wifi_is_disable));
          builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
              PreferenceManager.getInstance(SettingActivity.this).setSyncOnlyWifi(false);
              switchSyncWifiOnly.setChecked(PreferenceManager.getInstance(SettingActivity.this).getSyncOnlyWifi());
            }
          });
          builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
              PreferenceManager.getInstance(SettingActivity.this).setSyncOnlyWifi(true);
              switchSyncWifiOnly.setChecked(PreferenceManager.getInstance(SettingActivity.this).getSyncOnlyWifi());
            }
          });
          builder.create().show();
        }
      }
    });
    containerNotice = (LinearLayout) findViewById(R.id.container_notice);
    containerRatingApp = (LinearLayout) findViewById(R.id.container_rating_app);
    containerVersionInfo = (LinearLayout) findViewById(R.id.container_version_info);
    
    containerNotice.setOnClickListener(containerClickListener);
    containerRatingApp.setOnClickListener(containerClickListener);
    containerVersionInfo.setOnClickListener(containerClickListener);
  }
  
  
  @Override
  protected void onResume()
  {
    textName.setText(preferenceManager.getUserName());
    switchNotificationOnOff.setChecked(PreferenceManager.getInstance(SettingActivity.this).getAllowPopup());
    switchSyncWifiOnly.setChecked(PreferenceManager.getInstance(SettingActivity.this).getSyncOnlyWifi());
    super.onResume();
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
  
  private OnClickListener nameSaveListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      Intent i = new Intent(SettingActivity.this, AccountSettingActivity.class);
      startActivity(i);
    }
  };
  
  private OnClickListener containerClickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == containerVersionInfo.getId())
      {
        startActivity(new Intent(SettingActivity.this, VersionActivity.class));
      }
      else if (v.getId() == containerRatingApp.getId())
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(SettingActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        Toast.makeText(SettingActivity.this, "준비중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerNotice.getId())
      {
        
        if (!SystemUtil.isConnectNetwork(SettingActivity.this))
          new AlertDialogManager(SettingActivity.this).showDontNetworkConnectDialog();
        else
        {
          startActivity(new Intent(SettingActivity.this, NoticeActivity.class));
        }
      }
      
    }
  };
}
