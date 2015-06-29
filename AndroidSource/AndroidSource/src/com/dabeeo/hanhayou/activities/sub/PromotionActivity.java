package com.dabeeo.hanhayou.activities.sub;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.beans.PromotionBean;
import com.dabeeo.hanhayou.managers.PreferenceManager;
import com.squareup.picasso.Picasso;

public class PromotionActivity extends Activity
{
  private ImageView imageView;
  private Button btnShowDetail;
  private TextView textCloseAday;
  private ImageView btnClose;
  private PromotionBean bean;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_promotion);
    
    String jsonArrayString = getIntent().getStringExtra("jsonArrString");
    
    try
    {
      bean = new PromotionBean();
      bean.setJSONArray(new JSONArray(jsonArrayString));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    imageView = (ImageView) findViewById(R.id.imageview);
    
    btnShowDetail = (Button) findViewById(R.id.btn_show_detail);
    btnShowDetail.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        try
        {
          String url = bean.url;
          Log.w("WARN", "Promotion Url : " + url);
          if (!bean.url.contains("http://"))
            url = "http://" + bean.url;
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(Uri.parse(url));
          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(i);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
    textCloseAday = (TextView) findViewById(R.id.text_close_a_day);
    btnClose = (ImageView) findViewById(R.id.btn_close);
    btnClose.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    
    textCloseAday.setOnClickListener(new OnClickListener()
    {
      @SuppressLint("SimpleDateFormat")
      @Override
      public void onClick(View v)
      {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        PreferenceManager.getInstance(PromotionActivity.this).setDontShowPopupDate(formatter.format(date));
        finish();
      }
    });
    
    Picasso.with(PromotionActivity.this).load(bean.imageUrl).into(imageView);
  }
}
