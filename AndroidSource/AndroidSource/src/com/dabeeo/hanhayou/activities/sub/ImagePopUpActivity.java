package com.dabeeo.hanhayou.activities.sub;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.controllers.ImagePopupViewPagerAdapter;

public class ImagePopUpActivity extends ActionBarActivity
{
  private ViewPager viewPager;
  private ImagePopupViewPagerAdapter adapter;
  private ImageView imgX;
  private TextView textIndicator;
  private int allCount = 0;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_image_popup);
    
    ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
    Log.w("WARN", "ImageUrls : " + imageUrls);
    allCount = imageUrls.size();
    imgX = (ImageView) findViewById(R.id.img_x);
    imgX.bringToFront();
    imgX.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
    textIndicator = (TextView) findViewById(R.id.text_view_pager_indicator);
    
//    int position = getIntent().getIntExtra("position", 0);
    String currentImageUrl = getIntent().getStringExtra("imageUrl");
    int position = 0;
    for (int i = 0; i < imageUrls.size(); i++)
    {
      if (imageUrls.get(i).equals(currentImageUrl))
        position = i;
    }
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    adapter = new ImagePopupViewPagerAdapter(this);
    viewPager.setOffscreenPageLimit(0);
    viewPager.setAdapter(adapter);
    
    adapter.addItem(imageUrls);
    
    viewPager.setOnPageChangeListener(new OnPageChangeListener()
    {
      @Override
      public void onPageSelected(int arg0)
      {
        
      }
      
      
      @Override
      public void onPageScrolled(int position, float arg1, int arg2)
      {
        textIndicator.setText("(" + Integer.toString(position + 1) + "/" + Integer.toString(allCount) + ")");
      }
      
      
      @Override
      public void onPageScrollStateChanged(int arg0)
      {
        
      }
    });
    
    try
    {
      viewPager.setCurrentItem(position);
      textIndicator.setText("(" + Integer.toString(position + 1) + "/" + Integer.toString(allCount) + ")");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
  }
}
