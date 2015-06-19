package com.dabeeo.hanhayou.activities.ticket;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;
import com.dabeeo.hanhayou.bases.BaseNavigationTabActivity;
import com.dabeeo.hanhayou.controllers.ticket.TicketViewPagerAdapter;
import com.dabeeo.hanhayou.managers.AlertDialogManager;
import com.dabeeo.hanhayou.utils.SystemUtil;

public class TicketActivity extends BaseNavigationTabActivity
{
  private TicketViewPagerAdapter adapter;
  private boolean isFirstSelectAllTicketTab = true; // 오프라인일 때 탭을 생성하지마자 오프라인 팝업 뜨는 문제 회피용 
  
  
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
    viewPager.setOnPageChangeListener(pageChangeListener);
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
  }
  
  protected TabListener tabListener = new TabListener()
  {
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
      if (!SystemUtil.isConnectNetwork(getApplicationContext()) && tab.getPosition() == 0 && !isFirstSelectAllTicketTab)
      {
        new AlertDialogManager(TicketActivity.this).showDontNetworkConnectDialog();
        
        Runnable runn = new Runnable()
        {
          @Override
          public void run()
          {
            getSupportActionBar().setSelectedNavigationItem(1);
          }
        };
        
        Handler handler = new Handler();
        handler.post(runn);
      }
      else
      {
        viewPager.setCurrentItem(tab.getPosition());
      }
      
      if (tab.getPosition() == 0)
        isFirstSelectAllTicketTab = false;
    }
    
    
    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
    
    
    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1)
    {
      
    }
  };
}
