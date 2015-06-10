package com.dabeeo.hangouyou.activities.sub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.external.libraries.CirclePageIndicator;

public class GuideActivity extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);
    
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
    
    CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
    circleIndicator.setViewPager(viewPager);
    circleIndicator.setFillColor(Color.WHITE);
    circleIndicator.setPageColor(Color.argb(50, 255, 255, 255));
    ((Button) findViewById(R.id.btn_skip)).setOnClickListener(new OnClickListener()
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
      }
      else if (position == 1)
      {
        image.setImageResource(R.drawable.intro_tutorial02);
        startHome.setVisibility(View.INVISIBLE);
      }
      else if(position == 2)
      {
        image.setImageResource(R.drawable.intro_tutorial03);
        startHome.setVisibility(View.INVISIBLE);
      }else if(position == 3)
      {
        image.setImageResource(R.drawable.intro_tutorial04);
        startHome.setVisibility(View.INVISIBLE);
      }else if(position == 4)
      {
        image.setImageResource(R.drawable.intro_tutorial05);
        startHome.setVisibility(View.INVISIBLE);
      }else
      {
        image.setImageResource(R.drawable.intro_tutorial06);
        startHome.setVisibility(View.VISIBLE);
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
