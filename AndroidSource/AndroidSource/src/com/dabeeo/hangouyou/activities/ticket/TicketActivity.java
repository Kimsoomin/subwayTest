package com.dabeeo.hangouyou.activities.ticket;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hangouyou.R;
import com.dabeeo.hangouyou.bases.BaseNavigationTabActivity;
import com.dabeeo.hangouyou.controllers.ticket.TicketViewPagerAdapter;
import com.dabeeo.hangouyou.managers.AlertDialogManager;
import com.dabeeo.hangouyou.utils.SystemUtil;

public class TicketActivity extends BaseNavigationTabActivity
{
  private TicketViewPagerAdapter adapter;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    @SuppressLint("InflateParams")
    View customActionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null);
    TextView title = (TextView) customActionBar.findViewById(R.id.title);
    title.setText(getString(R.string.term_ticket));
    getSupportActionBar().setCustomView(customActionBar);
    getSupportActionBar().setDisplayShowCustomEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    
    adapter = new TicketViewPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    
    displayTitles();
  }
  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_empty, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  
  @SuppressWarnings("deprecation")
  private void displayTitles()
  {
    ArrayList<String> titles = new ArrayList<>();
    titles.add(getString(R.string.term_all_tickets));
    titles.add(getString(R.string.term_bought_ticket));
    
    for (String title : titles)
    {
      adapter.add(title);
      getSupportActionBar().addTab(getSupportActionBar().newTab().setText(title).setTabListener(tabListener));
    }
    
    adapter.notifyDataSetChanged();
    
    if (!SystemUtil.isConnectNetwork(getApplicationContext()))
      viewPager.setCurrentItem(1);
    
    viewPager.setOnPageChangeListener(pageChangeListener);
  }
  
  private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
  {
    @Override
    public void onPageSelected(int position)
    {
      if (!SystemUtil.isConnectNetwork(getApplicationContext()) && position == 0)
      {
        new AlertDialogManager(TicketActivity.this).showDontNetworkConnectDialog();
        getSupportActionBar().setSelectedNavigationItem(1);
      }
      else
      {
        getSupportActionBar().setSelectedNavigationItem(position);
      }
    }
    
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }
    
    
    @Override
    public void onPageScrollStateChanged(int state)
    {
    }
  };
}
