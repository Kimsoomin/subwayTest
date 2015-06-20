package com.dabeeo.hanhayou.activities.sub;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.activities.trend.TrendProductDetailActivity;
import com.dabeeo.hanhayou.activities.trend.TrendProductPopupActivity;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class VersionActivity extends ActionBarActivity
{
  private TextView textVersion;
  private Button btnUpdate;
  
  private LinearLayout containerAgreement, containerPrivateAgreement, containerGpsInfoAgreement;
  private Button btnNotification;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_version);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_version_info));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    textVersion = (TextView) findViewById(R.id.text_version);
    btnUpdate = (Button) findViewById(R.id.btn_update);
    containerAgreement = (LinearLayout) findViewById(R.id.container_agreement);
    containerPrivateAgreement = (LinearLayout) findViewById(R.id.container_private_agreement);
    containerGpsInfoAgreement = (LinearLayout) findViewById(R.id.container_gps_agreement);
    btnNotification = (Button) findViewById(R.id.btn_test_notification);
    btnNotification.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        final Intent emptyIntent = new Intent(VersionActivity.this, TrendProductDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(VersionActivity.this, 101, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(VersionActivity.this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("피테라 에센스 핑크 리미티드 에디션").setContentText(
            "[무료배송] 32,000원").setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, mBuilder.build());
        
        new Handler().postDelayed(new Runnable()
        {
          @Override
          public void run()
          {
            Intent i = new Intent(VersionActivity.this, TrendProductPopupActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
          }
        }, 3000);
      }
    });
    
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
  
  private OnClickListener clickListener = new OnClickListener()
  {
    @Override
    public void onClick(View v)
    {
      if (v.getId() == btnUpdate.getId())
      {
        if (!SystemUtil.isConnectNetwork(getApplicationContext()))
        {
          new AlertDialogManager(VersionActivity.this).showDontNetworkConnectDialog();
          return;
        }
        
        Toast.makeText(VersionActivity.this, "준비 중입니다", Toast.LENGTH_LONG).show();
      }
      else if (v.getId() == containerAgreement.getId())
      {
        Intent i = new Intent(VersionActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT);
        startActivity(i);
      }
      else if (v.getId() == containerPrivateAgreement.getId())
      {
        Intent i = new Intent(VersionActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_PRIVATE);
        startActivity(i);
      }
      else if (v.getId() == containerGpsInfoAgreement.getId())
      {
        Intent i = new Intent(VersionActivity.this, AgreementActivity.class);
        i.putExtra("type", AgreementActivity.TYPE_AGREEMENT_GPS);
        startActivity(i);
      }
    }
  };
}
