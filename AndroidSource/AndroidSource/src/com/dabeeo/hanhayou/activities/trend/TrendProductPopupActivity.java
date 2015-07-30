package com.dabeeo.hanhayou.activities.trend;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PushBean;
import com.dabeeo.hanhayou.utils.ImageDownloader;

public class TrendProductPopupActivity extends Activity
{
  private PushBean bean;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    getWindow().addFlags(
        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trend_product_popup);
    
    String jsonString = getIntent().getStringExtra("json_string");
    try
    {
      bean = new PushBean();
      bean.setJSONObject(new JSONObject(jsonString));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    displayInfo();
    
    ((Button) findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    ((Button) findViewById(R.id.btn_go_intent)).setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent i = new Intent(TrendProductPopupActivity.this, TrendProductDetailActivity.class);
        i.putExtra("idx", bean.productId);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      }
    });
  }
  
  
  private void displayInfo()
  {
    ImageView imageView = (ImageView) findViewById(R.id.imageview);
    ImageDownloader.displayImageWithListener(TrendProductPopupActivity.this, bean.image, imageView, null);
    
    TextView pushTitle = (TextView) findViewById(R.id.push_title);
    pushTitle.setText(bean.title);
  }
}
