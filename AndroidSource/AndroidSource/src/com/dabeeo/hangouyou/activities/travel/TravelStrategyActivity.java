package com.dabeeo.hangouyou.activities.travel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.beans.TitleCategoryBean;
import com.dabeeo.hangouyou.controllers.mainmenu.RecommendSeoulViewPagerAdapter;

public class TravelStrategyActivity extends BaseNavigationTabActivity
{
  private RecommendSeoulViewPagerAdapter adapter;
  private ArrayList<TitleCategoryBean> spheres = new ArrayList<>();
  private MenuItem areaItem;
  
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_strategy_seoul));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    
    adapter = new RecommendSeoulViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    adapter.add(new TitleCategoryBean(getString(R.string.term_recommend_seoul), -1));
    adapter.add(new TitleCategoryBean(getString(R.string.term_popular_place), 8));
    adapter.add(new TitleCategoryBean(getString(R.string.term_shopping), 9));
    adapter.add(new TitleCategoryBean(getString(R.string.term_restaurant), 4));
    adapter.notifyDataSetChanged();
    
    for (int i = 0; i < adapter.getCount(); i++)
    {
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(adapter.getPageTitle(i)).setTabListener(tabListener));
    }
    
    viewPager.setOnPageChangeListener(new OnPageChangeListener()
    {
      @Override
      public void onPageSelected(int position)
      {
        getSupportActionBar().setSelectedNavigationItem(position);
      }
      
      
      @Override
      public void onPageScrolled(int position, float arg1, int arg2)
      {
        adapter.currentPosition = position;
        invalidateOptionsMenu();
      }
      
      
      @Override
      public void onPageScrollStateChanged(int arg0)
      {
      }
    });
  }
  
  
  @Override
  public void onBackPressed()
  {
    super.onBackPressed();
    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_recommend_seoul, menu);
    areaItem = menu.findItem(R.id.all);
    if (adapter.currentPosition == 0)
      areaItem.setVisible(true);
    else
      areaItem.setVisible(false);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.all)
      showSphereDialog();
    return super.onOptionsItemSelected(item);
  }
  
  
  private void showSphereDialog()
  {
    
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
    
    for (TitleCategoryBean bean : spheres)
    {
      arrayAdapter.add(bean.title);
    }
    
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.dismiss();
      }
    });
    
    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        String strName = arrayAdapter.getItem(which);
        Toast.makeText(getApplicationContext(), strName, Toast.LENGTH_SHORT).show();
      }
    });
    builderSingle.show();
  }
  
}
