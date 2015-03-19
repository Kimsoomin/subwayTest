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
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeeo.hangouyou.MainActivity;
import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.external.libraries.CirclePageIndicator;

public class GuideActivity extends Activity
{
  private ViewPager viewPager;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);
    
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
    
    CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
    circleIndicator.setViewPager(viewPager);
    circleIndicator.setFillColor(Color.parseColor("#C0C0C0"));
    circleIndicator.setPageColor(Color.parseColor("#FFA500"));
    
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
      return 3;
    }
    
    
    @Override
    public Object instantiateItem(View pager, int position)
    {
      int resId = R.layout.view_guide;
      View v = LayoutInflater.from(GuideActivity.this).inflate(resId, null);
      ImageView image = (ImageView) v.findViewById(R.id.guide_image);
      TextView text = (TextView) v.findViewById(R.id.guide_text);
      if (position == 0)
      {
        image.setImageResource(R.drawable.ic_guide_1);
        text.setText("Guide 1입니다");
      }
      else if (position == 1)
      {
        image.setImageResource(R.drawable.ic_guide_2);
        text.setText("Guide 2입니다");
      }
      else
      {
        image.setImageResource(R.drawable.ic_guide_3);
        text.setText("Guide 3입니다");
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
