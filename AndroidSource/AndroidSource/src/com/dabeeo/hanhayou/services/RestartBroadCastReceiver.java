package com.dabeeo.hanhayou.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartBroadCastReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive(final Context context, Intent intent)
  {
    try
    {
      context.startService(new Intent(context, BackService.class));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
}