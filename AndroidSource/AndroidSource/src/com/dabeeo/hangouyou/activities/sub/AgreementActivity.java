package com.dabeeo.hangouyou.activities.sub;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    
    type = getIntent().getStringExtra("type");
    
    if (type.equals(TYPE_AGREEMENT))
      setTitle(getString(R.string.term_agreement));
    else if (type.equals(TYPE_AGREEMENT_PRIVATE))
      setTitle(getString(R.string.term_private_agreement));
    else if (type.equals(TYPE_AGREEMENT_GPS))
      setTitle(getString(R.string.term_gps_info_agreement));
    
    content = (TextView) findViewById(R.id.content);
  }
}
