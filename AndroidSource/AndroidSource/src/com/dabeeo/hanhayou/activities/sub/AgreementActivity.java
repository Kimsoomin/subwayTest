package com.dabeeo.hanhayou.activities.sub;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.dabeeo.hanhayou.managers.network.ApiClient;
import com.dabeeo.hanhayou.managers.network.NetworkResult;
import com.dabeeo.hanhayou.utils.SystemUtil;

@SuppressWarnings("deprecation")
public class AgreementActivity extends ActionBarActivity
{
  public static final String TYPE_AGREEMENT = "agreement";
  public static final String TYPE_AGREEMENT_PRIVATE = "agreement_private";
  public static final String TYPE_AGREEMENT_GPS = "agreement_gps";
  private String type;
  private WebView webview;
  private ApiClient client;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agreement);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    type = getIntent().getStringExtra("type");
    client = new ApiClient(this);
    
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
    
    webview = (WebView) findViewById(R.id.webview);
    
    if (!SystemUtil.isConnectNetwork(AgreementActivity.this))
      displayFromLocal();
    else
      new GetAgreementAsyncTask().execute();
  }
  
  
  private void displayFromLocal()
  {
    if (type.equals(TYPE_AGREEMENT))
      webview.loadData(PreferenceManager.getInstance(this).getAgreementHtmlString(), "text/html", "UTF-8");
    else if (type.equals(TYPE_AGREEMENT_PRIVATE))
      webview.loadData(PreferenceManager.getInstance(this).getAgreementPrivateHtmlString(), "text/html", "UTF-8");
    else if (type.equals(TYPE_AGREEMENT_GPS))
      webview.loadData(PreferenceManager.getInstance(this).getAgreementGPSInfoHtmlString(), "text/html", "UTF-8");
  }
  
  private class GetAgreementAsyncTask extends AsyncTask<String, Integer, NetworkResult>
  {
    @Override
    protected NetworkResult doInBackground(String... params)
    {
      if (type.equals(TYPE_AGREEMENT))
        return client.getAgreement();
      else if (type.equals(TYPE_AGREEMENT_PRIVATE))
        return client.getAgreementPrivate();
      else
        return client.getAgreementGPS();
    }
    
    
    @Override
    protected void onPostExecute(NetworkResult result)
    {
      try
      {
        String htmlString = result.response;
        if (type.equals(TYPE_AGREEMENT))
          PreferenceManager.getInstance(AgreementActivity.this).setAgreementHtmlString(htmlString);
        else if (type.equals(TYPE_AGREEMENT_PRIVATE))
          PreferenceManager.getInstance(AgreementActivity.this).setAgreementPrivateHtmlString(htmlString);
        else if (type.equals(TYPE_AGREEMENT_GPS))
          PreferenceManager.getInstance(AgreementActivity.this).setAgreementGPSInfoHtmlString(htmlString);
        
        displayFromLocal();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      super.onPostExecute(result);
    }
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
