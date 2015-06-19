package com.dabeeo.hanhayou.controllers.ticket;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dabeeo.hanhayou.fragments.ticket.BoughtTicketListFragment;
import com.dabeeo.hanhayou.fragments.ticket.TicketListFragment;

public class TicketViewPagerAdapter extends FragmentPagerAdapter
{
  private ArrayList<String> titles = new ArrayList<String>();
  
  
  public TicketViewPagerAdapter(Context context, FragmentManager fm)
  {
    super(fm);
  }
  
  
  public void add(String title)
  {
    titles.add(title);
  }
  
  
  @Override
  public Fragment getItem(int position)
  {
    if (position == 0)
      return new TicketListFragment();
    else
      return new BoughtTicketListFragment();
  }
  
  
  @Override
  public int getCount()
  {
    return titles.size();
  }
}