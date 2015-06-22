package com.dabeeo.hanhayou.activities.sub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dabeeo.hanhayou.MainActivity;
import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.external.libraries.CirclePageIndicator;

public class GuideActivity extends Activity
{
  public Button skipBtn;
  @SuppressLint("InlinedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);
    
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
    
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    View decorView = getWindow().getDecorView();
    if (Build.VERSION.SDK_INT == 14 || Build.VERSION.SDK_INT == 15)
    {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }
    else if (Build.VERSION.SDK_INT >= 16)
    {
        if (Build.VERSION.SDK_INT >= 19)
        {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        else
        {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    
    CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
    circleIndicator.setViewPager(viewPager);
    circleIndicator.setFillColor(Color.WHITE);
    circleIndicator.setPageColor(Color.argb(50, 255, 255, 255));
    skipBtn = (Button) findViewById(R.id.btn_skip);
    skipBtn.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
      }
    });
  }
  
  private class PagerAdapterClass extends PagerAdapter
  {
    public PagerAdapterClass(Context c)
    {
      super();
    }
    
    
    @Override
    public int getCount()
    {
      return 6;
    }
    
    
    @Override
    public Object instantiateItem(View pager, int position)
    {
      int resId = R.layout.view_guide;
      View v = LayoutInflater.from(GuideActivity.this).inflate(resId, null);
      ImageView image = (ImageView) v.findViewById(R.id.guide_image);
      ImageButton startHome = (ImageButton) v.findViewById(R.id.start_home);
      if (position == 0)
      {
        image.setImageResource(R.drawable.intro_tutorial01);
        startHome.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
      }
      else if (position == 1)
      {
        image.setImageResource(R.drawable.intro_tutorial02);
        startHome.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
      }
      else if(position == 2)
      {
        image.setImageResource(R.drawable.intro_tutorial03);
        startHome.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
      }else if(position == 3)
      {
        image.setImageResource(R.drawable.intro_tutorial04);
        startHome.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
      }else if(position == 4)
      {
        image.setImageResource(R.drawable.intro_tutorial05);
        startHome.setVisibility(View.INVISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
      }else
      {
        skipBtn.setVisibility(View.INVISIBLE);
        image.setImageResource(R.drawable.intro_tutorial06);
        startHome.setVisibility(View.VISIBLE);
        startHome.bringToFront();
        startHome.setOnClickListener(new OnClickListener()
        {
          
          @Override
          public void onClick(View v)
          {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            finish();
          }
        });
      }
      ((ViewPager) pager).addView(v, 0);
      
      return v;
    }
    
    
    @Override
    public void destroyItem(View pager, int position, Object view)
    {
      ((ViewPager) pager).removeView((View) view);
    }
    
    
    @Override
    public boolean isViewFromObject(View pager, Object obj)
    {
      return pager == obj;
    }
    
    
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }
    
    
    @Override
    public Parcelable saveState()
    {
      return null;
    }
    
    
    @Override
    public void startUpdate(View arg0)
    {
    }
    
    
    @Override
    public void finishUpdate(View arg0)
    {
    }
  }
}
